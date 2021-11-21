package ru.russianRobotics.testTask.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class EmailConfig {

    @DatabaseField(canBeNull = false)
    String user;

    @DatabaseField(canBeNull = false)
    String password;

    @DatabaseField(canBeNull = false)
    int port;

    @DatabaseField(canBeNull = false)
    String host;

    public EmailConfig() {
    }

    public EmailConfig(String user, String password, int port, String host) {
        this.user = user;
        this.password = password;
        this.port = port;
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
