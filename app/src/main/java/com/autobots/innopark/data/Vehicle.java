package com.autobots.innopark.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.autobots.innopark.Config;
import com.autobots.innopark.data.Callbacks.HashmapCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class Vehicle
{

    ArrayList<String> vehicles_owned;
    ArrayList<String> vehicles_driven;
    String first_name;
    String last_name;


    public static String collection = Config.VEHICLES_TABLE;

    public Vehicle() {}

    public Vehicle(ArrayList<String> vehicles_owned, ArrayList<String> vehicles_driven, String first_name, String last_name) {
        this.vehicles_owned = vehicles_owned;
        this.vehicles_driven = vehicles_driven;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public ArrayList<String> getVehicles_owned() {
        return vehicles_owned;
    }

    public void setVehicles_owned(ArrayList<String> vehicles_owned) {
        this.vehicles_owned = vehicles_owned;
    }

    public ArrayList<String> getVehicles_driven() {
        return vehicles_driven;
    }

    public void setVehicles_driven(ArrayList<String> vehicles_driven) {
        this.vehicles_driven = vehicles_driven;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public static void getVehicleInfo(String licensePlateNum, HashmapCallback callback){
        DatabaseUtils.db.collection(collection).document(licensePlateNum)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot vehicles_document = task.getResult();

                    if (vehicles_document != null && vehicles_document.exists()) {
                        callback.passHashmapResult((HashMap<String, Object>) vehicles_document.getData());
                    } else {
                        callback.passHashmapResult(null);
                        Log.d(Tags.FAILURE.name(), "VEHICLE NOT FOUND IN DB");
                    }
                } else {
                    Log.d(Tags.FAILURE.name(), "ERROR: COULDN'T FETCH VEHICLE INFO ", task.getException());
                }
            }
        });
    }
}
