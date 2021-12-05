package com.autobots.innopark.data;

import android.app.Application;

import java.util.List;

public class UserApi extends Application
{
    private String userId;
    private String username;
    private String userEmail;
    private String tcNum;
    private List<String> vehiclesOwned;
    private List<String> vehiclesDriven;
    private List<String> vehiclesCombined;
    private String individualLicense;
    private static UserApi instance;

    public static UserApi getInstance() {
        if (instance == null)
            instance = new UserApi();
        return instance;
    }


    //empty constructor
    public UserApi() {}

    public String getIndividualLicense() {
        return individualLicense;
    }

    public void setIndividualLicense(String individualLicense) {
        this.individualLicense = individualLicense;
    }

    public String getTcNum() {
        return tcNum;
    }

    public void setTcNum(String tcNum) {
        this.tcNum = tcNum;
    }

    public List<String> getVehiclesCombined() {
        return vehiclesCombined;
    }

    public void setVehiclesCombined(List<String> vehiclesCombined) {
        this.vehiclesCombined = vehiclesCombined;
    }

    public List<String> getVehiclesOwned() {
        return vehiclesOwned;
    }

    public void setVehiclesOwned(List<String> vehiclesOwned) {
        this.vehiclesOwned = vehiclesOwned;
    }

    public List<String> getVehiclesDriven() {
        return vehiclesDriven;
    }

    public void setVehiclesDriven(List<String> vehiclesDriven) {
        this.vehiclesDriven = vehiclesDriven;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}
