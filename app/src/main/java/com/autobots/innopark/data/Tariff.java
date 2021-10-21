package com.autobots.innopark.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.autobots.innopark.Config;
import com.autobots.innopark.data.Callbacks.HashmapCallback;
import com.autobots.innopark.data.Callbacks.StringCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Tariff
{
    int tariffID;
    double tariffRate;
    String startTime;
    String endTime;
    double minimumTariffRate;

    public static String collection = Config.AVENUES_TABLE + "/" +Config.avenue_id+"/"+Config.SESSIONS_TABLE;

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

    public static void getSession(String doc, HashmapCallback callback){
        //gets the session back as type HashMap<String, Object>

        DatabaseUtils.db.collection(collection).document(doc)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document != null && document.exists()) {
                        callback.passHashmapResult((HashMap<String, Object>) document.getData());
                    } else {
                        callback.passHashmapResult(null);
                        Log.d(Tags.FAILURE.name(), "DOCUMENT NOT FOUND IN DB");
                    }
                } else {
                    Log.d(Tags.FAILURE.name(), "ERROR: COULDN'T FETCH DOCUMENT", task.getException());
                }
            }
        });
    }

    public static void addSession(String parking_id, String start_datetime){
        // if you want start_datetime to be initialized automatically, initialize it as null, aka start_datetime = "0"

        if(start_datetime.equals("0")){
            LocalDateTime today_datetime = LocalDateTime.now();
            String formatted_today_datetime = today_datetime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS"));
            start_datetime = formatted_today_datetime;
        }

        String end_datetime = "";
        int tariff_amount=0;
        String vehicle="J71612";
        boolean is_paid=false;

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("parking_id", parking_id);
        data.put("start_datetime", start_datetime);
        data.put("end_datetime", end_datetime);
        data.put("tariff_amount", tariff_amount);
        data.put("vehicle", vehicle);
        data.put("is_paid", is_paid);

        DatabaseUtils.addData(collection, data, new StringCallback(){
            @Override
            public void passStringResult(String result) {
                if(result.equals(Tags.SUCCESS.name())){
                    Log.w(Tags.SUCCESS.name(), "SUCCESSFULLY ADDED SESSION");
                }else {
                    Log.w(Tags.FAILURE.name(), "ERROR: FAILED TO ADD SESSION");
                }
            }
        });

    }

    // this function should be deleted afterwards as it should only be on server side
    // should be inside a transaction
    public static void setEndDateTime(String end_datetime, String session_id, float ratePerHour){
        // if you want end_datetime to be initialized automatically, initialize it as null, aka end_datetime = "0"
        if(end_datetime.equals("0")){
            LocalDateTime today_datetime = LocalDateTime.now();
            String formatted_today_datetime = today_datetime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS"));
            end_datetime = formatted_today_datetime;
        }

        DatabaseUtils.updateData(collection, session_id, "end_datetime", end_datetime);
        calculateTariffAmount(end_datetime, session_id, ratePerHour);

    }

    public static void calculateTariffAmount(String end_datetime, String session_id, float ratePerHour){
        DatabaseUtils.getFieldValue(collection, session_id, "start_datetime", new StringCallback() {
            @Override
            public void passStringResult(String start_datetime) {
                if(!start_datetime.isEmpty()){
                    // end date formatting to get time and day
                    String end_timeString = LocalDateTime.parse(end_datetime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS"))
                            .format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

                    LocalTime end_timeLocalTime = LocalTime.parse(end_timeString, DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

                    String end_dayString = LocalDateTime.parse(end_datetime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS"))
                            .format(DateTimeFormatter.ofPattern("dd"));

                    int end_dayInteger = Integer.valueOf(end_dayString);

                    Log.w("PARSED END_DATETIME", String.valueOf(end_timeLocalTime));
                    Log.w("PARSED END_DAY", String.valueOf(end_dayInteger));

                    String start_timeString = LocalDateTime.parse(start_datetime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS"))
                            .format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

                    LocalTime start_timeLocalTime = LocalTime.parse(start_timeString, DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

                    String start_dayString = LocalDateTime.parse(start_datetime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS"))
                            .format(DateTimeFormatter.ofPattern("dd"));

                    int start_dayInteger = Integer.valueOf(start_dayString);

                    Log.w("PARSED START_DATETIME", String.valueOf(start_timeLocalTime));
                    Log.w("PARSED START_DAY", String.valueOf(start_dayInteger));

                    LocalTime subtracted_time = end_timeLocalTime.minusHours(start_timeLocalTime.getHour()).minusMinutes(start_timeLocalTime.getMinute())
                            .minusSeconds(start_timeLocalTime.getSecond());

                    int subtracted_day = end_dayInteger - start_dayInteger;

                    Log.w("TIME SUBTRACTED", String.valueOf(subtracted_time));
                    Log.w("DAY SUBTRACTED", String.valueOf(subtracted_day));

                    float tariff_amount = subtracted_time.getHour()*ratePerHour;
                    if (subtracted_day>0){
                        tariff_amount += 24*ratePerHour;
                    }

                    DatabaseUtils.updateData(collection, session_id, "tariff_amount", String.valueOf(tariff_amount));

                }else Log.w(Tags.FAILURE.name(), "ERROR: COULDNT CALCULATE TARIFF AMOUNT");
            }
        });

    }
}
