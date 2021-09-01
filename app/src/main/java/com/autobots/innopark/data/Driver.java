package com.autobots.innopark.data;

public class Driver {

    String driverName;
    String driverAge;
    String driverDOB;
    String driverNationality;

    public Driver (String driverName, String driverAge, String driverDOB, String driverNationality)
    {
        this.driverName = driverName;
        this.driverAge = driverAge;
        this.driverDOB = driverDOB;
        this.driverNationality = driverNationality;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverAge() {
        return driverAge;
    }

    public void setDriverAge(String driverAge) {
        this.driverAge = driverAge;
    }

    public String getDriverDOB() {
        return driverDOB;
    }

    public void setDriverDOB(String driverDOB) {
        this.driverDOB = driverDOB;
    }

    public String getDriverNationality() {
        return driverNationality;
    }

    public void setDriverNationality(String driverNationality) {
        this.driverNationality = driverNationality;
    }
}
