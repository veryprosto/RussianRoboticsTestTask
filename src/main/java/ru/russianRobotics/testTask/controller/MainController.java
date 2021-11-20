package ru.russianRobotics.testTask.controller;

import ru.russianRobotics.testTask.servise.Init;
import ru.russianRobotics.testTask.model.PriceItem;
import ru.russianRobotics.testTask.dao.PriceItemDao;

import java.sql.SQLException;

public class MainController {
    private final PriceItemDao priceItemDao = new PriceItemDao(Init.getConnectionSource(), PriceItem.class);

    public MainController() throws SQLException {
    }

    public PriceItem getPriceItemByDescription(String description) {
        PriceItem priceItem = null;
        try {
            priceItem = priceItemDao.queryBuilder().where().eq("description", description).queryForFirst();
        } catch (SQLException e) {
            System.out.println("Ошибка при запросе в БД");
        }
        return priceItem;
    }

    public void createPriceItem(PriceItem priceItem) {
        try {
            priceItemDao.create(priceItem);

        } catch (SQLException e) {
            System.out.println("Ошибка при запросе в БД");
        }
    }

    public void clearPriceItemTable(){
        try {
            priceItemDao.deleteBuilder().delete();
        } catch (SQLException e) {
            System.out.println("Ошибка при запросе в БД");
        }
    }
}
