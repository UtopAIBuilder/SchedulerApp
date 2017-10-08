package com.example.influential.schedulerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.influential.schedulerapp.com.example.influential.Adapters.HomepageListAdapter;

public class MainActivity extends AppCompatActivity {

   private String [] timeperiod;
    private String [] roomNo;
    private String [] subjects;
    private ListView timeTableList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeperiod=getResources().getStringArray(R.array.TimePeriod);
        roomNo=getResources().getStringArray(R.array.RoomNumber);
        subjects=getResources().getStringArray(R.array.Subjects);
        timeTableList= (ListView) findViewById(R.id.timetable_list);
        HomepageListAdapter adapter=new HomepageListAdapter(this,timeperiod,subjects,roomNo);
        timeTableList.setAdapter(adapter);



    }
}
