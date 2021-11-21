package ru.russianRobotics.testTask.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable
public class SupplierConfig {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(unique = true, canBeNull = false)
    private String name;

    @DatabaseField(unique = true, canBeNull = false)
    private String email;

    @DatabaseField(canBeNull = false)
    private int vendor; //номер столбца в таблице (нумерация начинается с 0)

    @DatabaseField(canBeNull = false)
    private int number; //номер столбца в таблице (нумерация начинается с 0)

    @DatabaseField(canBeNull = false)
    private int description; //номер столбца в таблице (нумерация начинается с 0)

    @DatabaseField(canBeNull = false)
    private int price; //номер столбца в таблице (нумерация начинается с 0)

    @DatabaseField(canBeNull = false)
    private int count; //номер столбца в таблице (нумерация начинается с 0)

    @DatabaseField(canBeNull = false)
    private int skipRows; //Количество строк в шапке таблицы которые нужно пропустить при парсинге.

    public SupplierConfig() {
    }

    public SupplierConfig(String name, String email, int vendor, int number, int description, int price, int count, int skipRows) {
        this.name = name;
        this.email = email;
        this.vendor = vendor;
        this.number = number;
        this.description = description;
        this.price = price;
        this.count = count;
        this.skipRows = skipRows;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getVendor() {
        return vendor;
    }

    public void setVendor(int vendor) {
        this.vendor = vendor;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSkipRows() {
        return skipRows;
    }

    public void setSkipRows(int skipRows) {
        this.skipRows = skipRows;
    }
}
