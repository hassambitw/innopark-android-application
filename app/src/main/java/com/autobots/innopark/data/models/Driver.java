package com.autobots.innopark.data.models;

import android.util.Log;

import com.autobots.innopark.util.Config;
import com.autobots.innopark.data.Callbacks.ArraylistCallback;
import com.autobots.innopark.data.Callbacks.HashmapCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class Driver {

    String email_address;
    String first_name;
    String id_card_number;
    boolean is_banned;
    String last_name;
    String phone_number;
    ArrayList<String> vehicles_driven;
    ArrayList<String> vehicles_owned;

    public static String drivers_in_vehicles_collection_field = "driven_by";

    public static String collection = Config.VEHICLES_TABLE;

    public Driver() {}

    public Driver(String email_address, String first_name, String id_card_number, boolean is_banned, String last_name, String phone_number, ArrayList<String> vehicles_driven, ArrayList<String> vehicles_owned) {
        this.email_address = email_address;
        this.first_name = first_name;
        this.id_card_number = id_card_number;
        this.is_banned = is_banned;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.vehicles_driven = vehicles_driven;
        this.vehicles_owned = vehicles_owned;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getId_card_number() {
        return id_card_number;
    }

    public void setId_card_number(String id_card_number) {
        this.id_card_number = id_card_number;
    }

    public boolean getIs_banned() {
        return is_banned;
    }

    public void setIs_banned(boolean is_banned) {
        this.is_banned = is_banned;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public ArrayList<String> getVehicles_driven() {
        return vehicles_driven;
    }

    public void setVehicles_driven(ArrayList<String> vehicles_driven) {
        this.vehicles_driven = vehicles_driven;
    }

    public ArrayList<String> getVehicles_owned() {
        return vehicles_owned;
    }

    public void setVehicles_owned(ArrayList<String> vehicles_owned) {
        this.vehicles_owned = vehicles_owned;
    }

    public static void getDriversList(String license_plate_number, ArraylistCallback callback) {
        Vehicle.getVehicleInfo(license_plate_number, new HashmapCallback() {
            @Override
            public void passHashmapResult(HashMap<String, Object> drivers) {
                if (!drivers.isEmpty()) {
                    for (String key : drivers.keySet()) {
                        if (key.equals(drivers_in_vehicles_collection_field)) {
                            ArrayList<String> driven_by = (ArrayList<String>) drivers.get(key);

                            callback.passArraylistResult(driven_by);
                        }
                    }
                }else {
                    Log.w(Tags.FAILURE.name(), "ERROR: NO DRIVERS FOUND FOR THE VEHICLE. THIS EXCLUDES OWNER.");
                    callback.passArraylistResult(null);
                }
            }
        });
    }

    private static void getDriversHandler(ArrayList<String> drivers){
        Log.w("value", String.valueOf((drivers).get(0)));
        //Do your handling here
    }
}
