package com.example.influential.schedulerapp.ExtraJavaClasses;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.example.influential.schedulerapp.R;

import java.util.Calendar;

/**
 * Created by bazil on 23/10/17.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    private Context context;

    String subject="";
    String roomNO="";
    private String jsonResponseTemp;
    @Override
    public void onReceive(Context context, Intent intent) {
        // this is called by alarm Manager to create notification.

        Log.d("Bz","in OnReceive");
        this.context=context;
        SharedPreferences  sharedPreferences =context.getSharedPreferences("JsonDownloadFile",Context.MODE_PRIVATE);
        jsonResponseTemp= sharedPreferences.getString("JsonDownload",null);
       createNotification();

    }

    public void createNotification()
    {

        // this pushes a notification immediately after being called
        // also sets the next alarm by calling alarmCal()

        int notifCount=0;
        NotificationManager NM= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notified= new Notification.Builder
                (context).setContentTitle(getSubjectNotif()).setContentText("will you plz get to this room: "+getRoomNotif())
                .setSmallIcon(R.drawable.schecon).build();
        NM.notify(notifCount,notified);
        new Alarmservices(context).alarmCal(jsonResponseTemp);
    }


    public String getSubjectNotif()
    {
        // gets subject from shared preferences to be shown in notification

        SharedPreferences sharedPreferences=context.getSharedPreferences("notifFile",Context.MODE_PRIVATE);
        subject=sharedPreferences.getString("Subject","NA");
        return subject;
    }
    public String getRoomNotif()
    {
       // the room No from shared preferences to be shown in notification

        SharedPreferences sharedPreferences=context.getSharedPreferences("notifFile",Context.MODE_PRIVATE);
        roomNO=sharedPreferences.getString("RoomNo","NA");
        return roomNO;
    }


}
