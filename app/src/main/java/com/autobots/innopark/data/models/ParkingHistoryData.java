package com.autobots.innopark.data.models;

import java.util.Date;

public class ParkingHistoryData
{
    String avenue_name;
    String parking_id;
//    String[] drivers;
    String first_name;
    String last_name;
    String vehicle;
    String manufacturer;
    String model;
    Date start_datetime;
    Date end_datetime;
    Date due_datetime;
    boolean is_paid;
    double tariff;
//    int finePrice;


    public ParkingHistoryData() {}

    public ParkingHistoryData(String avenue_name, String parking_id, String first_name, String last_name, String vehicle, String manufacturer, String model, Date start_datetime, Date end_datetime, Date due_datetime, boolean is_paid, double tariff) {
        this.avenue_name = avenue_name;
        this.parking_id = parking_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.vehicle = vehicle;
        this.manufacturer = manufacturer;
        this.model = model;
        this.start_datetime = start_datetime;
        this.end_datetime = end_datetime;
        this.due_datetime = due_datetime;
        this.is_paid = is_paid;
        this.tariff = tariff;
    }

    public String getAvenue_name() {
        return avenue_name;
    }

    public void setAvenue_name(String location) {
        this.avenue_name = location;
    }


    public String getParking_id() {
        return parking_id;
    }

    public void setParking_id(String parking_id) {
        this.parking_id = parking_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
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

    public Date getStart_datetime() {
        return start_datetime;
    }

    public void setStart_datetime(Date start_datetime) {
        this.start_datetime = start_datetime;
    }

    public Date getEnd_datetime() {
        return end_datetime;
    }

    public void setEnd_datetime(Date end_datetime) {
        this.end_datetime = end_datetime;
    }

    public Date getDue_datetime() {
        return due_datetime;
    }

    public void setDue_datetime(Date due_datetime) {
        this.due_datetime = due_datetime;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public double getTariff() {
        return tariff;
    }

    public void setTariff(double tariff) {
        this.tariff = tariff;
    }
}
