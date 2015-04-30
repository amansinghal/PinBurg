package com.aman.seeker;

import android.annotation.SuppressLint;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

@SuppressLint("NewApi")
public class NotiicationListener extends
    NotificationListenerService 
    {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) 
    {
        //---show current notification---
        Log.i("","---Current Notification---");
        Log.i("","ID :" + sbn.getId() + "\t" +sbn.getNotification().tickerText + "\t" +
            sbn.getPackageName());
        Log.i("","--------------------------");

        //---show all active notifications---
        Log.i("","===All Notifications===");
        for (StatusBarNotification notif :this.getActiveNotifications()) 
        {           
            Log.i("","ID :" + notif.getId() + "\t" +
                notif.getNotification().tickerText + "\t" +
                notif.getPackageName());
        }
        Log.i("","=======================");       
    }

    @Override
    public void onNotificationRemoved(
        StatusBarNotification sbn) {
        Log.i("","---Notification Removed---");
        Log.i("","ID :" + sbn.getId() + "\t" +
            sbn.getNotification().tickerText + "\t" +
            sbn.getPackageName());
        Log.i("","--------------------------");
       
    }   
}
