package com.autobots.innopark.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.autobots.innopark.Config;
import com.autobots.innopark.data.Callbacks.ArraylistCallback;
import com.autobots.innopark.data.Callbacks.HashmapCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class Driver {

    String driverName;
    String driverAge;
    String driverDOB;
    String driverNationality;

    public static String drivers_in_vehicles_collection_field = "driven_by";

    public static String collection = Config.VEHICLES_TABLE;

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

//    public static void getDriversDb(String vehicle_license_plate){
//        DatabaseUtils.db.collection(collection).document(vehicle_license_plate)
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//
//                    if (document != null && document.exists()) {
//                        for (String key: document.getData().keySet()){
//                                    if(key.equals(drivers_in_vehicles_collection_field)){
//                                        ArrayList<String> drivers = (ArrayList<String>) document.getData().get(key);
//                                        //getDriversHandler(drivers);
//
//                                        Log.w("value", String.valueOf((drivers).get(0)));
//                                    }
//                        }
//                    } else {
//                        Log.d("SUCCESS", "No such document");
//                    }
//                } else {
//                    Log.d("SUCCESS", "get failed with ", task.getException());
//                }
//            }
//        });
//    }

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
