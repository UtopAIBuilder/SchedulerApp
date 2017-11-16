package com.example.influential.schedulerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import java.util.Calendar;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.influential.schedulerapp.Fragments.AttendaceFragment;
import com.example.influential.schedulerapp.Fragments.ScheduleFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerListener;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView= (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setDayForDisplay();
        displayView(0);


     //   new Alarmservices(this).setAlarmMan(60);


        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout.setStatusBarBackground(Color.TRANSPARENT);

        drawerListener = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar,
                // nav menu toggle icon
                R.string.drawer_open, // nav drawer open - description for
                // accessibility
                R.string.drawer_close // nav drawer close - description for
                // accessibility
        )
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //getSupportActionBar().setTitle(R.string.app_name);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        mDrawerLayout.addDrawerListener(drawerListener);

    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
        Log.d("Bz","set title has been called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (drawerListener!=null)
        {drawerListener.syncState();}
    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (drawerListener!=null)
        {drawerListener.onConfigurationChanged(newConfig);}
    }


    public void displayView(int position)   // display different fragments in main activity
    {
        Fragment fragment=null;

        switch (position){
            case 0:
                fragment=new ScheduleFragment();
                break;
            case 1:
                fragment=new AttendaceFragment();
                // fragment=new Loginfragment();
                break;

            default:
                break;
        }
        if (fragment!=null)
        {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            if (position==0) {
                transaction.replace(R.id.main_frame_container, fragment, null);
                transaction.commitAllowingStateLoss();
            }
            else
            {
                transaction.addToBackStack(null);
                transaction.replace(R.id.main_frame_container, fragment, null);
                transaction.commitAllowingStateLoss();
            }
            // mDrawerList.setItemChecked(position,true);
            // mDrawerLayout.closeDrawer(GravityCompat.START);
            //  setTitle(navDrawerTitles[position]);
        }
        else
        {
            Log.d("Bz","error in retrieving fragment in main due to:");
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.isChecked())item.setChecked(false);
        else item.setChecked(true);
        mDrawerLayout.closeDrawers();
        if (item.getItemId()==R.id.welcome)
        {
            item.setChecked(false);
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
          SharedPreferences sharedPreferences=getSharedPreferences("DaysFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= sharedPreferences.edit();



            switch (item.getItemId()) {
                case R.id.mon:
                    editor.putInt("dayValue",1);
                    editor.commit();
                    displayView(0);
                    break;
                case R.id.tue:
                    editor.putInt("dayValue",2);
                    editor.commit();
                    displayView(0);
                    break;
                case R.id.wed:
                    editor.putInt("dayValue",3);
                    editor.commit();
                    displayView(0);
                    break;
                case R.id.thu:
                    editor.putInt("dayValue",4);
                    editor.commit();
                    displayView(0);

                    break;
                case R.id.fri:
                    editor.putInt("dayValue",5);
                    editor.commit();
                    displayView(0);

                    break;
                case R.id.notif:

                    break;

                default:
                    break;
            }






        return true;
    }


    public void setDayForDisplay()
    {
        int day,hour,min,i,date;
        Calendar cur_cal = Calendar.getInstance();
        cur_cal.setTimeInMillis(System.currentTimeMillis());
        hour=cur_cal.get(Calendar.HOUR_OF_DAY);
        min=cur_cal.get(Calendar.MINUTE);
        date=cur_cal.get(Calendar.DATE);
        day=cur_cal.get(Calendar.DAY_OF_WEEK);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
        cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));
        cal.set(Calendar.YEAR, cur_cal.get(Calendar.YEAR));
        // cal.getTimeInMillis();

        if (day==7)
        {
            cal.set(Calendar.DATE,date+2);
        }
        else if (day==1)
        {
            cal.set(Calendar.DATE,date+1);
        }

        else if (hour*60+min>16*60)
        {
            if (day==6)
            {
                cal.set(Calendar.DATE,date+3);
            }
            else
            {
                cal.set(Calendar.DATE,date+1);
            }
        }


        Log.d("Bz","HourOfDay:"+cur_cal.get(Calendar.HOUR_OF_DAY)+" hour:"+cur_cal.get(Calendar.HOUR));

        i=cal.get(Calendar.DAY_OF_WEEK)-1;
        Log.d("Bz","I:"+i);

        SharedPreferences sharedPreferences=getSharedPreferences("DaysFile",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("dayValue",i);
        editor.commit();

      Menu menu=navigationView.getMenu();

        MenuItem item;
        switch (i) {
            case 1:
               item =menu.findItem(R.id.mon);
                break;
            case 2:
                item =menu.findItem(R.id.tue);
                break;
            case 3:
                item=menu.findItem(R.id.wed);
                break;
            case 4:
                item =menu.findItem(R.id.thu);
                break;
            case 5:
                item =menu.findItem(R.id.fri);

                break;

            default:
                item =menu.findItem(R.id.mon);
                break;
        }
        item.setTitle("The Working Day");


    }









}
