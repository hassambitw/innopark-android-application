package com.autobots.innopark.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.autobots.innopark.Config;
import com.autobots.innopark.data.Callbacks.HashmapCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;

public class Vehicle
{

    String licensePlateNum;
    String[] driverName;
    String vehicleMake;

    public static String collection = Config.VEHICLES_TABLE;

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
