package ru.russianRobotics.testTask.servise;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import ru.russianRobotics.testTask.model.PriceItem;
import ru.russianRobotics.testTask.model.SupplierConfig;

import java.sql.SQLException;
import java.util.List;

public class Init {

    private static ConnectionSource connectionSource;

    static {
        try {
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:rrtesttask.db");
        } catch (SQLException throwables) {
            System.out.println("Ошибка при создании БД");
        }
    }

    public static void init() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, PriceItem.class);
            TableUtils.createTableIfNotExists(connectionSource, SupplierConfig.class);
        } catch (SQLException throwables) {
            System.out.println("Ошибка при создании таблиц");
        }
    }

    public static ConnectionSource getConnectionSource() {
        return connectionSource;
    }
}

