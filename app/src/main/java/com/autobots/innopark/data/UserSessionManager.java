package com.autobots.innopark.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.autobots.innopark.Config;

import java.util.HashMap;

public class UserSessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor_userSession;
    Context context;

    public static final String login_activity_shared_key = "LoginActivity";
    public static final String email_shared_key = "email_address";
    public static final String token_shared_key = "token";
    public static final String first_name_shared_key = "first_name";
    public static final String last_name_shared_key = "last_name";

    public UserSessionManager(Context context){
        userSession = context.getSharedPreferences(login_activity_shared_key, Context.MODE_PRIVATE);
        editor_userSession = userSession.edit();
    }

    public void createLoginSession(String email_address){
//        editor_userSession.putString(token_shared_key, token);
        editor_userSession.putString(email_shared_key, email_address);
        editor_userSession.commit();
    }

    public String getUserEmail(){
        return userSession.getString(email_shared_key, null);
    }

    public boolean checkLoginStatus(){
        if (userSession.contains(email_shared_key))
            return true;

        return false;
    }

    public void logoutFromSession(){
        editor_userSession.clear();
        editor_userSession.commit();
    }

    //    public HashMap<String, String> getUserDetailsFromSession(){
//        HashMap<String, String> userDetails = new HashMap<>();
//
//        userDetails.put(email_shared_key, userSession.getString(email_shared_key, null));
//
//        return userDetails;
//    }
//
//    public void setUserName(String first_name, String last_name){
//        editor_userSession.putString(first_name_shared_key, first_name);
//        editor_userSession.putString(last_name_shared_key, last_name);
//    }
}
