package com.autobots.innopark.data.models;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;
import java.util.Map;

public class Avenue
{
    GeoPoint gps_coordinate;
    String name;
    Map<String, Integer> parking_types;

    Avenue() {}

    public GeoPoint getGps_coordinate() {
        return gps_coordinate;
    }

    public void setGps_coordinate(GeoPoint gps_coordinate) {
        this.gps_coordinate = gps_coordinate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getParking_types() {
        return parking_types;
    }

    public void setParking_types(Map<String, Integer> parking_types) {
        this.parking_types = parking_types;
    }
}
