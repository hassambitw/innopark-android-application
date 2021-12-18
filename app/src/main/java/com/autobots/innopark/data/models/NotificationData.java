package com.autobots.innopark.data.models;

public class NotificationData
{

    int notification_id;
    String notification_info;

    public NotificationData(int notification_id, String notification_info)
    {
        this.notification_id = notification_id;
        this.notification_info = notification_info;
    }

    public int getNotification_id()
    {
        return notification_id;
    }

    public void setNotification_id(int notification_id)
    {
        this.notification_id = notification_id;
    }

    public String getNotification_info()
    {
        return notification_info;
    }

    public void setNotification_info(String notification_info)
    {
        this.notification_info = notification_info;
    }
}
