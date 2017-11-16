package com.example.influential.schedulerapp.ExtraJavaClasses;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by bazil on 24/10/17.
 */

public class Alarmservices {

    private Context context;

    private int k=0;
    private String subjectstemp[];
    private String roomNotemp[];
    private int startHour[];
     Calendar cal;
    private int day = 2,date,hour,i;

    public Alarmservices(Context context) {
        this.context = context;

    }

    private void setAlarmMan(long i) {
        //set the current time and date for this calendar

        Calendar cur_cal = Calendar.getInstance();
        cur_cal.setTimeInMillis(System.currentTimeMillis()
                + i);

        Log.d("Bz", "DayOfWeek:" + cur_cal.get(Calendar.DAY_OF_WEEK));
        Log.d("Bz", "DayOfMonth:" + cur_cal.get(Calendar.DAY_OF_MONTH));
        Log.d("Bz", "HOUR_OF_DAY:" + cur_cal.get(Calendar.HOUR_OF_DAY));
        Log.d("Bz", "Minute:" + cur_cal.get(Calendar.MINUTE));
        Log.d("Bz", "Date:" + cur_cal.get(Calendar.DATE));

        // setting broadcast for creating notification in the background
        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), 234324243, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //setting an alarm for i mills later trigger of broadcastReceiver
        alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + i, pendingIntent);
        Log.d("Bz", "Alarm Set for:" + i);


   }

  public void alarmCal(String jsonresponse) {


Log.d("Bz","Entered alarmCal");


      // creating cur_cal calender instance to detect the current time and date
        Calendar cur_cal = Calendar.getInstance();
        cur_cal.setTimeInMillis(System.currentTimeMillis());

        date = cur_cal.get(Calendar.DATE);
        day = cur_cal.get(Calendar.DAY_OF_WEEK);
      //creating cal instance to set it for the next trigger of notification
        cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, cur_cal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
        cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));
        cal.set(Calendar.YEAR, cur_cal.get(Calendar.YEAR));
        // cal.getTimeInMillis();

        if (day == 7) {
            //if it's saturday today then post a alarm notification for 2 days later
         cal.set(Calendar.DATE, date + 2);
        } else if (day == 1) {
            // if it's sunday then post a notification for next day hence date+1
            cal.set(Calendar.DATE, date + 1);
        }

           i = cal.get(Calendar.DAY_OF_WEEK) - 1; // day 1 is sunday while in database day 1 is monday


             Log.d("Bz","I:"+i);
          rowEvaluation(jsonresponse);
          int forReturnValue=startHourAssignmentForNextDay();

        if (forReturnValue == k) {
            // return value== k means there is no class for today at present hour
            if (i== 6) {
                //if it's friday post for monday. i.e., 3 days later
                cal.set(Calendar.DATE, date + 3);
                //i== row to be invoked from database
              i = cal.get(Calendar.DAY_OF_WEEK) - 1;

                Log.d("Bz","I:"+i);
             rowEvaluation(jsonresponse);
                startHourAssignmentForNextDay();


            } else {
                // otherwise post for next day
                cal.set(Calendar.DATE, date + 1);
                i = cal.get(Calendar.DAY_OF_WEEK) - 1;

                Log.d("Bz","I:"+i);
               rowEvaluation(jsonresponse);
              startHourAssignmentForNextDay();
            }
        }



      Log.d("Bz","return value:"+forReturnValue);

    }

// initializes startHour array with the working day schedule
  private void rowEvaluation(String jsonRespo) {
        JSONArray rows;
         k=0;
        JSONObject table ;
        try {
            table = new JSONObject(jsonRespo);


            rows = table.getJSONArray("rows");
            Log.d("Bz", "array length:" + rows.length());

            JSONObject row = rows.getJSONObject(i);
            JSONArray columns = row.getJSONArray("c");

            int startTime = 9;

            subjectstemp = new String[columns.length() - 1];
            roomNotemp  = new String[columns.length() - 1];
            startHour = new int[columns.length() - 1];
            String tempSubject;
            for (int j = 1; j < columns.length(); j++) {
                tempSubject = columns.getJSONObject(j).optString("v");
                if (tempSubject.contains("::")) {
                    subjectstemp[k] = tempSubject.substring(0, tempSubject.indexOf("::"));

                    roomNotemp[k] = tempSubject.substring(tempSubject.lastIndexOf(":") + 1);
                    startHour[k] = startTime;
                    k++;

                    Log.d("Bz", "k:" + k);

                    startTime += 2;
                    j++;
                } else if (tempSubject.contains("(L)")) {
                    subjectstemp[k] = tempSubject.substring(0, tempSubject.indexOf(":"));

                    roomNotemp[k] = tempSubject.substring(tempSubject.indexOf(":") + 1);
                    startHour[k] = startTime;
                    k++;
                    Log.d("Bz", "k:" + k);

                    startTime += 2;
                    j++;
                } else if (tempSubject.equals("-")) {
                    startTime += 1;
                } else {
                    subjectstemp[k] = tempSubject.substring(0, tempSubject.indexOf(":"));

                    roomNotemp[k] = tempSubject.substring(tempSubject.indexOf(":") + 1);
                    startHour[k] = startTime;
                    k++;
                    Log.d("Bz", "k:" + k);

                    startTime += 1;

                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int startHourAssignmentForNextDay()
    {
        int j;
        Log.d("Bz","Visiting for startHourAss");
        for (j = 0; j < k; j++) {
            cal.set(Calendar.HOUR_OF_DAY, startHour[j]);

            long temp = cal.getTimeInMillis();
            temp = temp - System.currentTimeMillis();
            if (temp>0) {
                temp=temp-600000;
                Log.d("Bz", "startHour: " + startHour[j]);
                SharedPreferences sharedPreferences = context.getSharedPreferences("NotificationFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Subject", subjectstemp[j]);
                editor.putString("RoomNo", roomNotemp[j]);
                editor.commit();
                setAlarmMan(temp);
                break;
            }

        }


        return j;
    }
}
