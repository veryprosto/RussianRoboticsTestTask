package ru.russianRobotics.testTask.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import ru.russianRobotics.testTask.model.PriceItem;

import java.sql.SQLException;

public class PriceItemDao extends BaseDaoImpl<PriceItem, Integer> {

    public PriceItemDao(ConnectionSource connectionSource, Class<PriceItem> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
