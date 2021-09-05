package com.autobots.innopark.data;

import java.time.LocalDate;
import java.util.Date;

public class Tariff
{
    int tariffID;
    double tariffRate;
    String startTime;
    String endTime;
    double minimumTariffRate;

    public Tariff(int tariffID, double tariffRate, String startTime, String endTime, double minimumTariffRate)
    {
        this.tariffID = tariffID;
        this.tariffRate = tariffRate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.minimumTariffRate = minimumTariffRate;
    }

    public int getTariffID() {
        return tariffID;
    }

    public void setTariffID(int tariffID) {
        this.tariffID = tariffID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getMinimumTariffRate() {
        return minimumTariffRate;
    }

    public void setMinimumTariffRate(double minimumTariffRate) {
        this.minimumTariffRate = minimumTariffRate;
    }

    public double getTariffRate() {
        return tariffRate;
    }

    public void setTariffRate(double tariffRate) {
        this.tariffRate = tariffRate;
    }
}
