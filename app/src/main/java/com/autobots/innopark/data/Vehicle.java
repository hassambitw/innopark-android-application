package com.autobots.innopark.data;

public class Vehicle
{

    String licensePlateNum;
    String[] driverName;
    String vehicleMake;

    public Vehicle(String licensePlateNum, String[] driverName, String vehicleMake)
    {
        this.licensePlateNum = licensePlateNum;
        this.driverName = driverName;
        this.vehicleMake = vehicleMake;
    }

    public String getLicensePlateNum() {
        return licensePlateNum;
    }

    public void setLicensePlateNum(String licensePlateNum) {
        this.licensePlateNum = licensePlateNum;
    }

    public CharSequence[] getDriverName() {
        return driverName;
    }

    public void setDriverName(String[] driverName) {
        this.driverName = driverName;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }
}
