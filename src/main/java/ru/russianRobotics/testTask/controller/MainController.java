package ru.russianRobotics.testTask.controller;

import ru.russianRobotics.testTask.dao.EmailConfigDao;
import ru.russianRobotics.testTask.dao.SupplierConfigDao;
import ru.russianRobotics.testTask.model.EmailConfig;
import ru.russianRobotics.testTask.model.SupplierConfig;
import ru.russianRobotics.testTask.servise.Init;
import ru.russianRobotics.testTask.model.PriceItem;
import ru.russianRobotics.testTask.dao.PriceItemDao;

import java.sql.SQLException;
import java.util.List;

public class MainController {
    private final PriceItemDao priceItemDao = new PriceItemDao(Init.getConnectionSource(), PriceItem.class);
    private final SupplierConfigDao supplierConfigDao = new SupplierConfigDao(Init.getConnectionSource(), SupplierConfig.class);
    private final EmailConfigDao emailConfigDao = new EmailConfigDao(Init.getConnectionSource(), EmailConfig.class);

    public MainController() throws SQLException {
    }

    public void createPriceItem(PriceItem priceItem) {
        try {
            priceItemDao.create(priceItem);

        } catch (SQLException e) {
            System.out.println("Ошибка при запросе в БД");
        }
    }

    public List<SupplierConfig> getSupplierConfigs() {
        try {
            return supplierConfigDao.queryBuilder().query();
        } catch (SQLException e) {
            System.out.println("Ошибка при запросе в БД");
            return null;
        }
    }

    public void createSupplerConfig(SupplierConfig supplierConfig) {
        try {
            supplierConfigDao.create(supplierConfig);
        } catch (SQLException e) {
            System.out.println("Ошибка при запросе в БД");
        }
    }

    public SupplierConfig getSupplierConfigById(int id) {
        try {
            return supplierConfigDao.queryBuilder().where().eq("id", id).queryForFirst();
        } catch (SQLException e) {
            System.out.println("Ошибка при запросе в БД");
            return null;
        }
    }

    public EmailConfig getEmailConfig() {
        try {
            return emailConfigDao.queryBuilder().queryForFirst();
        } catch (SQLException e) {
            System.out.println("Ошибка при запросе в БД");
            return null;
        }
    }

    public void changeEmailConfig(EmailConfig emailConfig) {
        try {
            emailConfigDao.deleteBuilder().delete();
            emailConfigDao.create(emailConfig);
        } catch (SQLException e) {
            System.out.println("Ошибка при запросе в БД");
        }
    }
}
