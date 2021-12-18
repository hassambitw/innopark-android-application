package com.autobots.innopark.data.models;

import java.util.Date;

public class Session {

    Date due_datetime;
    Date end_datetime;
    boolean is_paid;
    String parking_id;
    double rate_per_hour;
    Date start_datetime;
    double tariff_amount;
    String vehicle;
    String avenue_name;
    String payment_link;

    public Session() {}

    public Session(String venue) {
        this.avenue_name = avenue_name;
    }



//    public Session(String name) {
//        this.name = name;
//    }

//    public Session(Timestamp due_datetime, Timestamp end_datetime, boolean is_paid, int parking_id, double rate_per_hour, Timestamp start_datetime, double tariff_amount, String vehicle) {
//        this.due_datetime = due_datetime;
//        this.end_datetime = end_datetime;
//        this.is_paid = is_paid;
//        this.parking_id = parking_id;
//        this.rate_per_hour = rate_per_hour;
//        this.start_datetime = start_datetime;
//        this.tariff_amount = tariff_amount;
//        this.vehicle = vehicle;
//    }


    public String getPayment_link() {
        return payment_link;
    }

    public void setPayment_link(String payment_link) {
        this.payment_link = payment_link;
    }

    public String getAvenue_name() {
        return avenue_name;
    }

    public void setAvenue_name(String avenue_name) {
        this.avenue_name = avenue_name;
    }

    public Date getDue_datetime() {
        return due_datetime;
    }

    public void setDue_datetime(Date due_datetime) {
        this.due_datetime = due_datetime;
    }

    public Date getEnd_datetime() {
        return end_datetime;
    }

    public void setEnd_datetime(Date end_datetime) {
        this.end_datetime = end_datetime;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public String getParking_id() {
        return parking_id;
    }

    public void setParking_id(String parking_id) {
        this.parking_id = parking_id;
    }

    public double getRate_per_hour() {
        return rate_per_hour;
    }

    public void setRate_per_hour(double rate_per_hour) {
        this.rate_per_hour = rate_per_hour;
    }

    public Date getStart_datetime() {
        return start_datetime;
    }

    public void setStart_datetime(Date start_datetime) {
        this.start_datetime = start_datetime;
    }

    public double getTariff_amount() {
        return tariff_amount;
    }

    public void setTariff_amount(double tariff_amount) {
        this.tariff_amount = tariff_amount;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
}
