package com.autobots.innopark;

import androidx.appcompat.widget.DrawableUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Config {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


}
