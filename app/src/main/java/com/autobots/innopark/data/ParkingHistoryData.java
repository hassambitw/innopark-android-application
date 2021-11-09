package com.autobots.innopark.data;

import java.util.Date;

public class ParkingHistoryData
{
    String location;
    Date date;
    String parkingLevel;
    String[] drivers;
    String carLicense;
    String carName;
    String duration;
    boolean tariffPaidStatus;
    boolean finePaidStatus;
    String paidMessage;
    int tariffPrice;
    int finePrice;

    public ParkingHistoryData(String location, Date date, String parkingLevel, String[] drivers, String carLicense, String carName, String duration, boolean paidStatus, String paidMessage, int tariffPrice, int finePrice, boolean finePaidStatus)
    {
        this.location = location;
        this.date = date;
        this.parkingLevel = parkingLevel;
        this.drivers = drivers;
        this.carLicense = carLicense;
        this.carName = carName;
        this.duration = duration;
        this.tariffPaidStatus = paidStatus;
        this.paidMessage = paidMessage;
        this.tariffPrice = tariffPrice;
        this.finePaidStatus = finePaidStatus;
        this.finePrice = finePrice;

    }

    public ParkingHistoryData(String location, Date date, String parkingLevel, String[] drivers, String carLicense, String carName, boolean finePaidStatus, String paidMessage, int finePrice) {
        this.location = location;
        this.date = date;
        this.parkingLevel = parkingLevel;
        this.drivers = drivers;
        this.carLicense = carLicense;
        this.carName = carName;
        this.finePaidStatus = finePaidStatus;
        this.paidMessage = paidMessage;
        this.finePrice = finePrice;
    }




    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isTariffPaidStatus() {
        return tariffPaidStatus;
    }

    public void setTariffPaidStatus(boolean tariffPaidStatus) {
        this.tariffPaidStatus = tariffPaidStatus;
    }

    public boolean isFinePaidStatus() {
        return finePaidStatus;
    }

    public void setFinePaidStatus(boolean finePaidStatus) {
        this.finePaidStatus = finePaidStatus;
    }

    public int getFinePrice() {
        return finePrice;
    }

    public void setFinePrice(int finePrice) {
        this.finePrice = finePrice;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getParkingLevel() {
        return parkingLevel;
    }

    public void setParkingLevel(String parkingLevel) {
        this.parkingLevel = parkingLevel;
    }

    public String[] getDrivers() {
        return drivers;
    }

    public void setDrivers(String[] drivers) {
        this.drivers = drivers;
    }

    public String getCarLicense() {
        return carLicense;
    }

    public void setCarLicense(String carLicense) {
        this.carLicense = carLicense;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }


    public void setPaidStatus(boolean paidStatus) {
        this.tariffPaidStatus = paidStatus;
    }

    public String getPaidMessage() {
        return paidMessage;
    }

    public void setPaidMessage(String paidMessage) {
        this.paidMessage = paidMessage;
    }

    public int getTariffPrice() {
        return tariffPrice;
    }

    public void setTariffPrice(int tariffPrice) {
        this.tariffPrice = tariffPrice;
    }
}
