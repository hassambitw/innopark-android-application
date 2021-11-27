package com.autobots.innopark.data;

public class EmailUtils
{
    static UserApi userApi = UserApi.getInstance();
    static String fromEmail = userApi.getUserEmail();
    public static String EMAIL = fromEmail;
    public static final String PASSWORD = "";
}
