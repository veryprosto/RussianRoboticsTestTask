package ru.russianRobotics.testTask.servise;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import ru.russianRobotics.testTask.model.PriceItem;
import ru.russianRobotics.testTask.model.SupplierConfig;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class PriceReader {
    private List<PriceItem> priceItemList;
    private SupplierConfig supplierConfig;

    public void readPrice(SupplierConfig supplierConfig) throws IOException, MessagingException {
        this.supplierConfig = supplierConfig;
        FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\config.properties");

        Properties properties = new Properties();
        properties.load(fileInputStream);

        String user = properties.getProperty("mail.user");
        String password = properties.getProperty("mail.password");
        int port = Integer.parseInt(properties.getProperty("mail.port"));

        Properties emailProperties = new Properties();
        emailProperties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getDefaultInstance(emailProperties);
        Store store = null;

        File tmpFile = File.createTempFile("tempFile", null, new File("C:/tmp"));

        try {
            store = session.getStore("imap");
            store.connect("imap.yandex.ru", port, user, password);
            Folder inbox = null;
            try {
                inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);

                Message[] unreadMessages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

                List<Message> unreadMessagesFromSupplier = Arrays.stream(unreadMessages).filter(message -> {
                    try {
                        return "grigproject@yandex.ru".equals(((InternetAddress) message.getFrom()[0]).getAddress());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        return false;
                    }
                }).collect(Collectors.toList());

                Collections.reverse(unreadMessagesFromSupplier);

                for (Message message : unreadMessagesFromSupplier) {
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

    public void readCsvAndPutToDB(File file) {
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

                for (String[] strings : csvReader) {
                    String vendor = strings[supplierConfig.getVendor()].toUpperCase();
                    String number = strings[supplierConfig.getNumber()].toUpperCase();
                    String searchVendor = strings[supplierConfig.getVendor()].replaceAll("[^A-Za-zА-Яа-я0-9]", "");
                    String searchNumber = strings[supplierConfig.getNumber()].replaceAll("[^A-Za-zА-Яа-я0-9]", "");
                    String description = strings[supplierConfig.getDescription()].length() > 512 ? strings[3].substring(0, 512) : strings[3];
                    BigDecimal price = BigDecimal.valueOf(Long.parseLong(strings[supplierConfig.getPrice()]));
                    int count = Integer.parseInt(strings[supplierConfig.getCount()]);

                    PriceItem priceItem = new PriceItem();

                    priceItem.setVendor(vendor);
                    priceItem.setNumber(number);
                    priceItem.setSearch_vendor(searchVendor);
                    priceItem.setSearch_number(searchNumber);
                    priceItem.setDescription(description);
                    priceItem.setPrice(price);
                    priceItem.setCount(count);

                    priceItemList.add(priceItem);
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

