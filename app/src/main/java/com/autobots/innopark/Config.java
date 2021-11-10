package com.autobots.innopark;

import androidx.appcompat.widget.DrawableUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Config {

    public static String AVENUES_TABLE = "avenues";
    public static String USERS_TABLE = "users";
    public static String VEHICLES_TABLE = "vehicles";
    public static String SESSIONS_TABLE = "sessions_info";

    public static String avenue_id = "O8483qKcEoQc6SPTDp5e";

    public static final String channel_id = "innopark_notifications";

    public static String current_user_id = null;
    public static String current_user_email = null;

    public static String current_user_token = null;
    public static boolean new_token_status = false;

}
