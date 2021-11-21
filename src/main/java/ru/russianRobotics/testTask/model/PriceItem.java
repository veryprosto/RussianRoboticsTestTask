package ru.russianRobotics.testTask.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "priceitems")
public class PriceItem {

    @DatabaseField(columnDefinition = "VARCHAR(64)")
    private String vendor;

    @DatabaseField(columnDefinition = "VARCHAR(64)")
    private String number;

    @DatabaseField(columnDefinition = "VARCHAR(64)")
    private String search_vendor;

    @DatabaseField(columnDefinition = "VARCHAR(64)")
    private String search_number;

    @DatabaseField(columnDefinition = "VARCHAR(512)")
    private String description;

    @DatabaseField(columnDefinition = "DECIMAL(18,2)")
    private Double price;

    @DatabaseField
    private int count;

    public PriceItem() {
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSearch_vendor() {
        return search_vendor;
    }

    public void setSearch_vendor(String search_vendor) {
        this.search_vendor = search_vendor;
    }

    public String getSearch_number() {
        return search_number;
    }

    public void setSearch_number(String search_number) {
        this.search_number = search_number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
