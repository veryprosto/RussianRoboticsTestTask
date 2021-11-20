package ru.russianRobotics.testTask;

import ru.russianRobotics.testTask.controller.MainController;
import ru.russianRobotics.testTask.model.PriceItem;
import ru.russianRobotics.testTask.model.SupplierConfig;
import ru.russianRobotics.testTask.servise.Init;
import ru.russianRobotics.testTask.servise.PriceReader;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class App {
    private static MainController controller;
    private static BufferedReader bufferedReader;

    public static void main(String[] args) throws SQLException, IOException, MessagingException {
        Init.init();
        //controller.createSupplerConfig(new SupplierConfig("ООО \"Доставим в срок\"", 1, 10, 3, 6, 8, 1)); //раскоментировать для добавления мокового поставщика из примера
        controller = new MainController();
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String userСhoiсe = "";

        do {
            System.out.println("Выберите действие (наберите соответствующее число и нажмите ввод):");
            System.out.println();
            System.out.println("1.\tСконфигурировать почтовый клиент");
            System.out.println("2.\tВыбрать поставщика для загрузки прайслиста");
            System.out.println("3.\tДобавить поставщика для загрузки прайслиста");
            System.out.println("4.\tВыйти из программы");
            userСhoiсe = bufferedReader.readLine();
            switch (userСhoiсe) {
                case ("1"):
                    System.out.println("Вы выбрали - Сконфигурировать почтовый клиент");
                    break;
                case ("2"):
                    SupplierConfig config = null;
                    while (config == null) {
                        System.out.println();
                        System.out.println("Выберите поставщика для загрузки прайслиста (наберите соответствующее число и нажмите ввод):");
                        controller.getSupplierConfigs().forEach(supplierConfig -> {
                            System.out.println(supplierConfig.getId() + ". " + supplierConfig.getName());
                        });
                        try {
                            int id = Integer.parseInt(bufferedReader.readLine());
                            config = controller.getSupplierConfigById(id);
                            if (config == null) {
                                System.out.println("Вы ввели некорректное число - введите число из списка.");
                            }
                        } catch (NumberFormatException numberFormatException) {
                            System.out.println("Что то пошло не так, попробуйте повторить - введите число.");
                        }
                    }
                    downloadPriceList(config);
                    break;
                case ("3"):
                    addNewSupplier();
                    break;
                case ("4"):
                    System.out.println("Завершение программы");
                    break;
                default:
                    System.out.println("Что то пошло не так, попробуйте повторить.");
                    System.out.println();
            }
        } while (!userСhoiсe.equals("4"));
        bufferedReader.close();



    }

    private static void addNewSupplier() throws IOException {
        System.out.println("Добавление нового поставщика \n");
        System.out.println("Введите название");
        String name = bufferedReader.readLine();
        System.out.println("Введите порядковый номер колонки с наименованием бренда(нумерация начинается с 0)");
        int vendor = Integer.parseInt(bufferedReader.readLine());
        System.out.println("Введите порядковый номер колонки с каталожным номером(нумерация начинается с 0)");
        int number = Integer.parseInt(bufferedReader.readLine());
        System.out.println("Введите порядковый номер колонки с описанием(нумерация начинается с 0)");
        int description = Integer.parseInt(bufferedReader.readLine());
        System.out.println("Введите порядковый номер колонки с ценой(нумерация начинается с 0)");
        int price = Integer.parseInt(bufferedReader.readLine());
        System.out.println("Введите порядковый номер колонки с наличием(нумерация начинается с 0)");
        int count = Integer.parseInt(bufferedReader.readLine());
        System.out.println("Введите количество строк шапки таблицы");
        int skipRows = Integer.parseInt(bufferedReader.readLine());
        controller.createSupplerConfig(new SupplierConfig(name, vendor, number, description, price, count, skipRows));
        System.out.println("Добавлена конфигурация нового поставщика");
    }

    public static void configureMailClient() {
        System.out.println("Введите почтовый ящик");
    }

    public static void downloadPriceList(SupplierConfig supplierConfig) throws IOException, MessagingException {
        PriceReader priceReader = new PriceReader();
        String userСhoiсe = "";
        do {
            System.out.println("Выберите действие:");
            System.out.println();
            System.out.println("1.\tЗагрузить прайслист от поставщика " + supplierConfig.getName());
            System.out.println("2.\tВернуться в предыдущее меню");

            userСhoiсe = bufferedReader.readLine();
            switch (userСhoiсe) {
                case ("1"):
                    System.out.println("Попытка загрузить прайс от " + supplierConfig.getName());
                    priceReader.readPrice(supplierConfig);
                    List<PriceItem> priceItemList = priceReader.getPriceItemList();

                    String mess = priceItemList.isEmpty() ? "Неудачно, возможно нового прайслиста нет" : "прайс загружен";
                    priceReader.getPriceItemList().forEach(controller::createPriceItem);
                    System.out.println(mess + "\n");

                    break;
            }
        } while (!userСhoiсe.equals("2"));

    }
}
