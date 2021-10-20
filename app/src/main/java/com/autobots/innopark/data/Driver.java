package com.autobots.innopark.data;

import android.util.Log;

import androidx.annotation.NonNull;

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

    public static String collection = "vehicles";

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

    public static void getDriversDOB(String vehicle_license_plate){
        DatabaseUtils.db.collection(collection).document(vehicle_license_plate)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document != null && document.exists()) {
                        for (String key: document.getData().keySet()){
                                    if(key.equals("driven_by")){
                                        ArrayList<String> drivers = (ArrayList<String>) document.getData().get(key);
                                        //getDriversHandler(drivers);
                                        //Log.w("value", String.valueOf(document.getData().get(key).getClass()));
                                        Log.w("value", String.valueOf((drivers).get(0)));
                                    }
                        }
                    } else {
                        Log.d("SUCCESS", "No such document");
                    }
                } else {
                    Log.d("SUCCESS", "get failed with ", task.getException());
                }
            }
        });
    }

    public static void getDriversListDOB(String license_plate_number) {

        Vehicle.getVehicleInfo(license_plate_number, new HashmapCallback() {
            @Override
            public void passHashmapResult(HashMap<String, Object> drivers) {
                if (!drivers.isEmpty()) {
                    for (String key : drivers.keySet()) {
                        if (key.equals("driven_by")) {
                            ArrayList<String> driven_by = (ArrayList<String>) drivers.get(key);
                            //getDriversHandler(drivers);
                            //Log.w("value", String.valueOf(document.getData().get(key).getClass()));
                            Log.w("value", String.valueOf((driven_by))); //PRINTS DATA BUENO
                        }
                    }
                }else Log.w(Tags.FAILURE.name(), "ERROR: NO DRIVERS FOUND FOR THE VEHICLE. THIS EXCLUDES OWNER.");
            }
        });
//        Log.w("value", String.valueOf((driven_by[0])));
//        return driven_by; // NOTHING IS PRINTED AS THE DATA WAS NOT YET FETCHED
    }

  //  public static ArrayList<String> getDriversListDOB1(String license_plate_number) {

//        DbEvent e = new DbEvent();
//        e.setOnDbEvent(new onDbEventListener() {
//            @Override
//            public void onEvent(String collection, String doc, Object data) {
//                DatabaseUtils.db.collection(collection).document(doc)
//                        .set(data)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.d(Tags.SUCCESS.name(), "Successfully added data");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w(Tags.FAILURE.name(), "ERROR: Failed to add data with the error - ", e);
//                            }
//                        })
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful()){
//                                    callback.passStringResult(Tags.SUCCESS.name());
//                                }else callback.passStringResult(Tags.FAILURE.name());
//                            }
//                        });
//            }
//        });
//        e.doEvent();


//        final ArrayList<String>[] driven_by = new ArrayList[]{new ArrayList<>()};
//        Vehicle.getVehicleInfo(license_plate_number, new HashmapCallback() {
//            @Override
//            public void passHashmapResult(HashMap<String, Object> drivers) {
//                if (!drivers.isEmpty()) {
//                    for (String key : drivers.keySet()) {
//                        if (key.equals("driven_by")) {
//                            driven_by[0] = (ArrayList<String>) drivers.get(key);
//                            //getDriversHandler(drivers);
//                            //Log.w("value", String.valueOf(document.getData().get(key).getClass()));
//                            Log.w("value", String.valueOf((driven_by[0]).get(0)));
//                        }
//                    }
//                }else Log.w(Tags.FAILURE.name(), "ERROR: NO DRIVERS FOUND FOR THE VEHICLE. THIS EXCLUDES OWNER.");
//            }
//        });
//        Log.w("value", String.valueOf((driven_by[0])));
//        return driven_by[0];
//    }


    private static void getDriversHandler(ArrayList<String> drivers){
        Log.w("value", String.valueOf((drivers).get(0)));
        //Do your handling here
    }
}
