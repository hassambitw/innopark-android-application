package com.autobots.innopark.data;

import java.util.ArrayList;

public class Vehicle2 {

    String city_of_registration;
    ArrayList<String> driven_by;
    String manufacturer;
    String model;
    String owned_by;
    String year;
    String documentId;
    String driverName;

    public Vehicle2() {}

    public Vehicle2(String city_of_registration, ArrayList<String> driven_by, String manufacturer, String model, String owned_by, String year, String documentId, String driverName) {
        this.city_of_registration = city_of_registration;
        this.driven_by = driven_by;
        this.manufacturer = manufacturer;
        this.model = model;
        this.owned_by = owned_by;
        this.year = year;
        this.documentId = documentId;
        this.driverName = driverName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getCity_of_registration() {
        return city_of_registration;
    }

    public void setCity_of_registration(String city_of_registration) {
        this.city_of_registration = city_of_registration;
    }

    public ArrayList<String> getDriven_by() {
        return driven_by;
    }

    public void setDriven_by(ArrayList<String> driven_by) {
        this.driven_by = driven_by;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOwned_by() {
        return owned_by;
    }

    public void setOwned_by(String owned_by) {
        this.owned_by = owned_by;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
