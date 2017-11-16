package com.example.influential.schedulerapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.Calendar;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.influential.schedulerapp.Adapters.ScheduleAdapter;
import com.example.influential.schedulerapp.ExtraJavaClasses.Alarmservices;
import com.example.influential.schedulerapp.ExtraJavaClasses.NetworkUtil;
import com.example.influential.schedulerapp.MainActivity;
import com.example.influential.schedulerapp.NoInternetConnection;
import com.example.influential.schedulerapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ScheduleFragment extends Fragment implements View.OnClickListener {


    MainActivity mainActivity;
    ScheduleAdapter adapter;
    Button btn;
    int i=1;
    private String url="https://spreadsheets.google.com/tq?key=1DhIQBD9sqe5NAnVP83s_8wUxvqIegST09tle1uXDS8Q";
    private String [] timeperiod;
    private String [] roomNo;
    private String [] subjects;
    private int k;
    boolean semaphore=false;
    private ListView timeTableList;
    private int status;
    ProgressBar pb1;
    SharedPreferences sharedPreferences;
    String jsonResponseTemp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        timeperiod=new String[8];
        roomNo=new String[8];
        subjects=new String[8];



        status = NetworkUtil.getConnectivityStatusString(mainActivity);
        Log.d("Bz","status:"+status);
        sharedPreferences =mainActivity.getSharedPreferences("JsonDownloadFile",Context.MODE_PRIVATE);
        jsonResponseTemp= sharedPreferences.getString("JsonDownload",null);

        if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {



            if(jsonResponseTemp==null)
            {
                Intent intent =new Intent(mainActivity,NoInternetConnection.class);
                startActivity(intent);
            }



        }





    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Bz","K:"+k);
        Log.d("Bz","Semaphore:"+semaphore);

        View rootView= inflater.inflate(R.layout.activity_schedule_actvity,container,false);
        Log.d("Bz","In OnCreate View of schedule fragment");
        btn= (Button) rootView.findViewById(R.id.NaviGatorBtn);
        pb1= (ProgressBar) rootView.findViewById(R.id.progresso);
        btn.setOnClickListener(this);
        timeTableList= (ListView) rootView.findViewById(R.id.timetable_list);
        status = NetworkUtil.getConnectivityStatusString(mainActivity);

        if(jsonResponseTemp!=null && status!=NetworkUtil.NETWORK_STAUS_WIFI)
        {SharedPreferences sharedPreferences=mainActivity.getSharedPreferences("DaysFile",Context.MODE_PRIVATE);
            i=sharedPreferences.getInt("dayValue",1);
            new Alarmservices(mainActivity).alarmCal(jsonResponseTemp);
            setAdapterValues(jsonResponseTemp,i);
            Log.d("Bz","visited first elseIf block without wifi");
        }
        else if (status!=NetworkUtil.NETWORK_STATUS_NOT_CONNECTED){
            Log.d("Bz","visited second else if block");
            pb1.setVisibility(View.VISIBLE);
            new MyAsyncTask().execute(url);// when internet connection is available
            }


        return rootView;
    }

    @Override
    public void onClick(View view) {

        mainActivity.displayView(1);
    }



    private class MyAsyncTask  extends AsyncTask<String,String,String> {
        private String jsonResponse=null;





        @Override
        protected String doInBackground(String... urls) {
            // ensuring download of json first then sending mail is executed
            try {



                String result = DownloadUrl(urls[0]);
                int start = result.indexOf("{", result.indexOf("{") + 1);
                int end = result.lastIndexOf("}");
                jsonResponse = result.substring(start, end);

                // Mail m=new

            } catch (IOException e) {
                Log.d("Bz", "IoException:" + e);
            }
           catch (Exception e) {
                Log.d("Bz", "Exception:" + e);

            }
            return jsonResponse;
        }




        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            super.onPostExecute(jsonResponse);
           if(jsonResponse!=null)
           {
              SharedPreferences sharedPreferences=mainActivity.getSharedPreferences("DaysFile",Context.MODE_PRIVATE);
               i=sharedPreferences.getInt("dayValue",1);
               new Alarmservices(mainActivity).alarmCal(jsonResponse);
               setAdapterValues(jsonResponse,i);

              sharedPreferences=
                       mainActivity.getSharedPreferences("JsonDownloadFile", Context.MODE_PRIVATE);
               SharedPreferences.Editor editor=sharedPreferences.edit();
               editor.putString("JsonDownload",jsonResponse);
               editor.apply();
           }
           else if(jsonResponseTemp!=null)
           {
               SharedPreferences sharedPreferences=mainActivity.getSharedPreferences("DaysFile",Context.MODE_PRIVATE);
               i=sharedPreferences.getInt("dayValue",1);
               new Alarmservices(mainActivity).alarmCal(jsonResponseTemp);
               setAdapterValues(jsonResponseTemp,i);
               Log.d("Bz","visited first elseIf block without wifi");
           }
         else
           {
               pb1.setVisibility(View.GONE);
               Toast.makeText(mainActivity,"check connection and try again",Toast.LENGTH_LONG).show();
           }


        }










        private String DownloadUrl(String s) throws IOException {
            InputStream inputStream=null;

            String contentAsString=null;
            try {
                URL url = new URL(s);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000 /* milliseconds */);
                connection.setConnectTimeout(15000 /* milliseconds */);
                connection.setRequestMethod("GET");

                connection.setDoInput(true);
                connection.connect();
                int responsecode = connection.getResponseCode();
                if (responsecode == 200) {
                    inputStream = connection.getInputStream();

                    contentAsString = convertStreamToString(inputStream);

                }
                return contentAsString;
            }
            finally {
                if (inputStream!=null)
                {
                    inputStream.close();
                }
            }

        }
        private String convertStreamToString(InputStream inputStream)
        {
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb=new StringBuilder();
            String line;
            try {
                while((line=reader.readLine())!=null)
                {
                    sb.append(line+"\n");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
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

            Log.d("Bz","semaphore true now");
            semaphore=true;
            adapter=new ScheduleAdapter(mainActivity,timeperiod,subjects,roomNo,k);
            timeTableList.setAdapter(adapter);
            pb1.setVisibility(View.GONE);


        }
        catch (JSONException e) {
            Log.d("Bz", "" + e);

        } catch (Exception e) {
            Log.d("Bz", "Exception:" + e);

        }

    } // filling up of arrays required for adapter and setting it



}
