package com.wht.addekh.Notification;

/**
 * Created by YoYoNituSingh on 7/14/2016.
 */
public class Notification
{
    private String notification_id;
    private String notification_title;
    private String notification_message;
    private String notification_image;
    private String notification_date_time;


    public Notification() {}

    public Notification(String notification_id, String notification_title, String notification_message, String notification_image, String notification_date_time) {
        this.notification_id = notification_id;
        this.notification_title = notification_title;
        this.notification_message = notification_message;
        this.notification_image = notification_image;
        this.notification_date_time = notification_date_time;
    }



    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getNotification_message() {
        return notification_message;
    }

    public void setNotification_message(String notification_message) {
        this.notification_message = notification_message;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public String getNotification_image() {
        return notification_image;
    }

    /*public String getNotification_image() {
        return notification_image;
    }

    public void setNotification_image(String notification_image) {
        this.notification_image = notification_image;
    }*/

    public String getNotification_date_time() {
        return notification_date_time;
    }

    public void setNotification_date_time(String notification_date_time) {
        this.notification_date_time = notification_date_time;
    }


}
