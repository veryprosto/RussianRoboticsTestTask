package ru.russianRobotics.testTask.servise;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import ru.russianRobotics.testTask.model.EmailConfig;
import ru.russianRobotics.testTask.model.PriceItem;
import ru.russianRobotics.testTask.model.SupplierConfig;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class PriceReader {
    private List<PriceItem> priceItemList;
    private SupplierConfig supplierConfig;

    public void readPrice(EmailConfig emailConfig, SupplierConfig supplierConfig) throws IOException, MessagingException, CsvValidationException {
        this.supplierConfig = supplierConfig;

        String user = emailConfig.getUser();
        String password = emailConfig.getPassword();
        int port = emailConfig.getPort();
        String host = emailConfig.getHost();

        Properties emailProperties = new Properties();
        emailProperties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getDefaultInstance(emailProperties);
        Store store = null;

        File folder = new File("C:/tmp");
        if (!folder.exists()) {
            folder.mkdir();
        }

        File tmpFile = File.createTempFile("tempFile", null, folder);

        try {
            store = session.getStore("imap");
            store.connect(host, port, user, password);
            Folder inbox = null;
            try {
                inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_WRITE);

                Message[] unreadMessages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

                List<Message> unreadMessagesFromSupplier = Arrays.stream(unreadMessages).filter(message -> {
                    try {
                        return (((InternetAddress) message.getFrom()[0]).getAddress()).equals(supplierConfig.getEmail());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        return false;
                    }
                }).collect(Collectors.toList());

                Collections.reverse(unreadMessagesFromSupplier);

                for (Message message : unreadMessagesFromSupplier) {
                    message.setFlag(Flags.Flag.SEEN, true);//пометить прочитанным
                    try {
                        if (message.getContentType().contains("multipart")) {
                            Multipart multiPart = (Multipart) message.getContent();

                            for (int i = 0; i < multiPart.getCount(); i++) {
                                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);

                                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) && part.getFileName().endsWith(".csv")) {
                                    FileOutputStream output = new FileOutputStream(tmpFile);
                                    InputStream input = part.getInputStream();
                                    byte[] buffer = new byte[4096];
                                    int byteRead;
                                    while ((byteRead = input.read(buffer)) != -1) {
                                        output.write(buffer, 0, byteRead);
                                    }
                                    output.close();
                                    break;
                                }
                            }
                        }
                    } catch (MessagingException | IOException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                if (inbox != null) {
                    inbox.close(false);
                }
            }
        } finally {
            if (store != null) {
                store.close();
            }
        }
        readCsvAndPutToDB(tmpFile);

        tmpFile.deleteOnExit();
    }

    public void readCsvAndPutToDB(File file) throws CsvValidationException {
        priceItemList = new ArrayList<>();

        if (file != null) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
                CSVParser parser = new CSVParserBuilder()
                        .withSeparator(';')
                        .build();
                CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                        .withCSVParser(parser)
                        .withSkipLines(1)
                        .build();
                String [] strings = csvReader.readNext();
                while(strings!=null){
                    try {
                        String vendor = strings[supplierConfig.getVendor()].toUpperCase();
                        String number = strings[supplierConfig.getNumber()].toUpperCase();
                        String searchVendor = strings[supplierConfig.getVendor()].replaceAll("[^A-Za-zА-Яа-я0-9]", "");
                        String searchNumber = strings[supplierConfig.getNumber()].replaceAll("[^A-Za-zА-Яа-я0-9]", "");
                        String description = strings[supplierConfig.getDescription()].length() > 512 ? strings[3].substring(0, 512) : strings[3];
                        Double price = Double.parseDouble(strings[supplierConfig.getPrice()].replace(",", "."));
                        String[] countStringSplit = strings[supplierConfig.getCount()].replace("<", "").replace(">", "").split("-");
                        int count = Integer.parseInt(countStringSplit[countStringSplit.length - 1]);

                        PriceItem priceItem = new PriceItem();

                        priceItem.setVendor(vendor);
                        priceItem.setNumber(number);
                        priceItem.setSearch_vendor(searchVendor);
                        priceItem.setSearch_number(searchNumber);
                        priceItem.setDescription(description);
                        priceItem.setPrice(price);
                        priceItem.setCount(count);

                        priceItemList.add(priceItem);
                    } catch (NumberFormatException numberFormatException) {
                        System.out.println("неудачная попытка парсинга позиции "+(priceItemList.size()+1));
                    }

                    try {
                        strings = csvReader.readNext();
                    } catch (IOException | CsvValidationException e) {
                        System.out.println("неудачная попытка парсинга позиции "+(priceItemList.size()+1));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<PriceItem> getPriceItemList() {
        return priceItemList;
    }
}

