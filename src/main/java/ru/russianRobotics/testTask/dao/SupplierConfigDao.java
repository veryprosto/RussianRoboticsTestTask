package ru.russianRobotics.testTask.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import ru.russianRobotics.testTask.model.SupplierConfig;

import java.sql.SQLException;

public class SupplierConfigDao extends BaseDaoImpl<SupplierConfig, Integer> {

    public SupplierConfigDao(ConnectionSource connectionSource, Class<SupplierConfig> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
