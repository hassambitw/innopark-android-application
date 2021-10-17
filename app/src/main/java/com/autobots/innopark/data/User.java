package com.autobots.innopark.data;

import androidx.annotation.NonNull;

import com.autobots.innopark.Config;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private String user_uid;
    private String email;
    private String password;
    private String first_name;
    private String last_name;
    private String user_id_card_number;
    private String phone_number;
    private List<String> vehicles_owned;

    private boolean is_banned = false;

    //shouldnt be adding more than one vehicle at registration?
    // could ask the user how many vehicles they own first then show number of fields accordingly
    public User(String user_uid, String email, String password, String first_name, String last_name,
                String user_id_card_number, String phone_number, String vehicles_owned){
        this.user_uid = user_uid;
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_id_card_number = user_id_card_number;
        this.phone_number = phone_number;
        this.vehicles_owned.add(vehicles_owned);
    }

    
    // Create a new user with a first and last name
    public void addUser(){
        Map<String, Object> user = new HashMap<>();
            user.put("first", "Ada");
            user.put("last", "Lovelace");
            user.put("born", 1815);

        // Add a new document with a generated ID
        Config.db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    System.out.println("DocumentSnapshot added with ID: " + documentReference.getId());
                }
            })
            .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            System.out.println("Error adding document");
        }
        });
    }
}
