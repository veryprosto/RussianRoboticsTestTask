package ru.russianRobotics.testTask;

import ru.russianRobotics.testTask.controller.MainController;
import ru.russianRobotics.testTask.servise.Init;
import ru.russianRobotics.testTask.servise.PriceReader;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {
        Init.init();
        MainController controller = new MainController();

        try {
            PriceReader priceReader = new PriceReader();
            priceReader.readPrice();

            controller.clearPriceItemTable();

            priceReader.getPriceItemList().forEach(controller::createPriceItem);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }
}
