package com.autobots.innopark.data;

public class ParkingHistoryData
{
    String location;
    String date;
    String parkingLevel;
    String[] drivers;
    String carLicense;
    String carName;
    boolean paidStatus;
    String paidMessage;
    int tariffPrice;

    public ParkingHistoryData(String location, String date, String parkingLevel, String[] drivers, String carLicense, String carName, boolean paidStatus, String paidMessage, int tariffPrice)
    {
        this.location = location;
        this.date = date;
        this.parkingLevel = parkingLevel;
        this.drivers = drivers;
        this.carLicense = carLicense;
        this.carName = carName;
        this.paidStatus = paidStatus;
        this.paidMessage = paidMessage;
        this.tariffPrice = tariffPrice;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public boolean isPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(boolean paidStatus) {
        this.paidStatus = paidStatus;
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
