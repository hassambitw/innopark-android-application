package com.autobots.innopark.data.models;

import java.util.ArrayList;
import java.util.Map;

public class UserToken {

    String email_address;
    ArrayList<Map<String, Object>> notif;
//    ArrayList<String> notif_body;
//    ArrayList<String> notif_title;
    String token_id;

    public UserToken(){}

    public String getEmail_address() {
        return email_address;
    }

    public ArrayList<Map<String, Object>> getNotif(){
        return notif;
    }

//    public ArrayList<String> getNotif_body(){
//        return notif_body;
//    }
//
//    public ArrayList<String> getNotif_title(){
//        return notif_title;
//    }

    public String getToken_id(){
        return token_id;
    }
}
