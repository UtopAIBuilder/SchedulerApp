package com.example.influential.schedulerapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.influential.schedulerapp.Adapters.ScheduleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class NotificationView extends AppCompatActivity {
    private String [] timeperiod;
    private String [] roomNo;
    private String [] subjects;
    private int tempSelect[];
    Button submit;
    int k;
    Calendar cur_cal;
    ScheduleAdapter adapter;
    private ListView timeTableList;
    SharedPreferences sharedPreferences;
    String jsonResponseTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_view);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Dismiss Notification
        notificationmanager.cancel(0);

        timeTableList= (ListView) findViewById(R.id.attendance_marking_list);
        timeperiod=new String[8];
        roomNo=new String[8];
        subjects=new String[8];
        tempSelect=new int[8];
        submit= (Button) findViewById(R.id.submit_attendance);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences=getSharedPreferences("SubmittedFile",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                int currentDate=cur_cal.get(Calendar.DATE);
                editor.putInt("Date",currentDate);
                editor.apply();
                for (int p=0;p<8;p++)
                {

                    if (tempSelect[p]==1)
                    {
                        editor.putBoolean(""+p,true);
                    }
                }

                editor.commit();
                finish();
            }
        });

         cur_cal=Calendar.getInstance();
        cur_cal.setTimeInMillis(System.currentTimeMillis());
        int i=cur_cal.get(Calendar.DAY_OF_WEEK)-1;
        Log.d("Bz","value of i that defines current Day: "+i);
        sharedPreferences=getSharedPreferences("JsonDownloadFile", Context.MODE_PRIVATE);
        jsonResponseTemp=sharedPreferences.getString("JsonDownload",null);
        setAdapterValues(jsonResponseTemp,i);
        timeTableList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences sharedPreferences=getSharedPreferences("SubmittedFile",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();


                int currentDate=cur_cal.get(Calendar.DATE);
                int tempDate=sharedPreferences.getInt("Date",currentDate);

                    Log.d("Bz","tempDate: "+tempDate);
                    Log.d("Bz","currentDate: "+currentDate);
                if (currentDate!=tempDate)
                {
                    Log.d("Bz","making all values false due to date change");
                    for (int j=0;j<k;j++)
                    {
                        editor.putBoolean(""+j,false);

                    }


                    editor.commit();
                }

                Boolean temp=sharedPreferences.getBoolean(""+i,false);

                if (!temp){
                    if (tempSelect[i]==0)
                    {
                        view.setBackgroundColor(Color.parseColor("#FF4081"));
                        tempSelect[i]=1;

                    }
                    else
                    {
                        view.setBackgroundColor(Color.TRANSPARENT);
                        tempSelect[i]=0;
                    }


                }
                else
                {
                    Log.d("Bz","already submitted");
                }


                Log.d("Bz","clicked an item");
            }
        });

    }


    public void setAdapterValues(String jsonResponse,int i) {
        try {
           k=0;


            JSONArray rows;
            JSONObject table = new JSONObject(jsonResponse);
            rows = table.getJSONArray("rows");
            Log.d("Bz", "array length:" + rows.length());

            JSONObject row = rows.getJSONObject(i);
            JSONArray columns = row.getJSONArray("c");

            int startTime = 9;
            String timePeriodtemp;
            String subjectstemp;
            String roomNotemp;
            String tempSubject;
            for (int j = 1; j < columns.length(); j++) {
                tempSubject = columns.getJSONObject(j).optString("v");
                if (tempSubject.contains("::")) {
                    subjectstemp = tempSubject.substring(0, tempSubject.indexOf("::"));
                    timePeriodtemp = startTime + " - " + (startTime + 2);
                    roomNotemp = tempSubject.substring(tempSubject.lastIndexOf(":") + 1);
                    timeperiod[k]=timePeriodtemp;
                    subjects[k]=subjectstemp;
                    roomNo[k]=roomNotemp;
                    k++;
                    Log.d("Bz","k:"+k);
                    // publishProgress(timePeriodtemp,subjectstemp,roomNotemp);
                    startTime += 2;
                    j++;
                } else if (tempSubject.contains("(L)")) {
                    subjectstemp = tempSubject.substring(0, tempSubject.indexOf(":"));
                    timePeriodtemp = startTime + " - " + (startTime + 2);
                    roomNotemp = tempSubject.substring(tempSubject.indexOf(":") + 1);

                    timeperiod[k]=timePeriodtemp;
                    subjects[k]=subjectstemp;
                    roomNo[k]=roomNotemp;
                    k++;
                    Log.d("Bz","k:"+k);
                    // publishProgress(timePeriodtemp,subjectstemp,roomNotemp);
                    startTime += 2;
                    j++;
                } else if (tempSubject.equals("-")) {
                    startTime += 1;
                } else {
                    subjectstemp = tempSubject.substring(0, tempSubject.indexOf(":"));
                    timePeriodtemp = startTime + " - " + (startTime + 1);
                    roomNotemp = tempSubject.substring(tempSubject.indexOf(":") + 1);
                    timeperiod[k]=timePeriodtemp;
                    subjects[k]=subjectstemp;
                    roomNo[k]=roomNotemp;
                    k++;
                    Log.d("Bz","k:"+k);
                    // publishProgress(timePeriodtemp,subjectstemp,roomNotemp);
                    startTime += 1;

                }

            }


            adapter=new ScheduleAdapter(this,timeperiod,subjects,roomNo,k);
            timeTableList.setAdapter(adapter);



        }
        catch (JSONException e) {
            Log.d("Bz", "" + e);

        } catch (Exception e) {
            Log.d("Bz", "Exception:" + e);

        }

    }

}
