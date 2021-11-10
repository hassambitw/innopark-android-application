package com.autobots.innopark.data;

import java.util.ArrayList;

public class UsersTokens {

    String email_address;
    ArrayList<String> notif_body;
    ArrayList<String> notif_title;
    String token_id;

    public UsersTokens(){}

    public String getEmail_address() {
        return email_address;
    }

    public ArrayList<String> getNotif_body(){
        return notif_body;
    }

    public ArrayList<String> getNotif_title(){
        return notif_title;
    }

    public String getToken_id(){
        return token_id;
    }
}
