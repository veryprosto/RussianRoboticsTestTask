package ru.russianRobotics.testTask.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import ru.russianRobotics.testTask.model.EmailConfig;

import java.sql.SQLException;

public class EmailConfigDao extends BaseDaoImpl<EmailConfig, Integer> {

    public EmailConfigDao(ConnectionSource connectionSource, Class<EmailConfig> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
