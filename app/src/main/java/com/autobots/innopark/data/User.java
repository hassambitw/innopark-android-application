package com.autobots.innopark.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private String user_uid;
    private String email_address;
    private String password;
    private String first_name;
    private String last_name;
    private String user_id_card_number;
    private String phone_number;
    private List<String> vehicles_owned;
    private List<String> vehicles_driven;
    private boolean is_banned;

    private String user_uid_field = "user_uid";
    private String email_address_field = "email_address";
    private String password_field = "password";
    private String first_name_field =  "first_name";
    private String last_name_field = "last_name";
    private String user_id_card_number_field = "user_id_card_number";
    private String phone_number_field = "phone_number";
    private String vehicles_owned_field = "vehicles_owned";
    private String vehicles_driven_field = "vehicles_driven";
    private String is_banned_field = "is_banned";

    public static String user_uid_field1 = "user_uid";
    public static String email_address_field1 = "email_address";
    public static String password_field1 = "password";
    public static String first_name_field1 =  "first_name";
    public static String last_name_field1 = "last_name";
    public static String user_id_card_number_field1 = "user_id_card_number";
    public static String phone_number_field1 = "phone_number";
    public static String vehicles_owned_field1 = "vehicles_owned";
    public static String vehicles_driven_field1 = "vehicles_driven";
    public static String is_banned_field1 = "is_banned";

    private String collection = "users";
    public static String col = "users";

    //shouldnt be adding more than one vehicle at registration?
    // could ask the user how many vehicles they own first then show number of fields accordingly

    public User(String user_uid, String email_address, String password, String first_name, String last_name,
                String user_id_card_number, String phone_number, String vehicles_owned){
        Log.w("EMAIL_ADDRESS1", email_address);
        this.user_uid = user_uid;
        this.email_address = email_address;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_id_card_number = user_id_card_number;
        this.phone_number = phone_number;
        this.vehicles_owned.add(vehicles_owned);
        this.vehicles_driven = null;
        this.is_banned = false;
    }

    
//    // Create a new user with a first and last name
//    public static void addUser(String user_uid, Object u) {
//
//        DatabaseUtils.addData(col, user_uid, u, new Listeners.DbListenerCallback(){
//            public void getResult(String result){
//                if(result.equals(Tags.SUCCESS.name())){
//                    Log.w("Success", "ADDED DATA");
//                }
//            }
//        });
//    }

    // wouldnt need a parameter when class is used for initialization of user
    public static void getEmail(String user_id){
        DatabaseUtils.db.collection(col).document(user_id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            Log.d("SUCCESS", document.getString(email_address_field1)); //Print the name
                        } else {
                            Log.d("SUCCESS", "No such document");
                        }
                    } else {
                        Log.d("SUCCESS", "get failed with ", task.getException());
                    }
                }
        });
    }

    //change to private
    public static void getUserUsingEmail(String email_address){
        DatabaseUtils.db.collection(col)
                .whereEqualTo(email_address_field1, email_address)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(Tags.SUCCESS.name(), document.getId() + " => " + document.getData());
                                }
                            }else Log.d(Tags.FAILURE.name(), "No such email found: ");
                        } else {
                            Log.d(Tags.FAILURE.name(), "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


//    public void addUser(){
//        Map<String, Object> user = new HashMap<>();
//            user.put(email_address_field, this.email_address);
//            user.put(first_name_field, this.first_name);
//            user.put(user_id_card_number_field, this.user_id_card_number);
//            user.put(is_banned_field, this.is_banned);
//            user.put(last_name_field, this.last_name);
//            user.put(password_field, this.password);
//            user.put(phone_number_field, this.phone_number);
//            user.put(vehicles_driven_field, this.vehicles_driven);
//            user.put(vehicles_owned_field, this.vehicles_owned);
//
//        DatabaseUtils.addData(collection, user_uid, user, new Listeners.DbListenerCallback(){
//            public void getResult(String result){
//                if(result.equals(Tags.SUCCESS.name())){
//                    Log.w("Success", "ADDED DATA");
//                }
//            }
//        });
//    }


}
