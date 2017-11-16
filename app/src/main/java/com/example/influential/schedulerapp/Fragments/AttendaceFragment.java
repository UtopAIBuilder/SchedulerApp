package com.example.influential.schedulerapp.Fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.influential.schedulerapp.Adapters.ScheduleAdapter;
import com.example.influential.schedulerapp.MainActivity;
import com.example.influential.schedulerapp.R;

public class AttendaceFragment extends Fragment{

    private ListView listview;
    MainActivity mainActivity;
    private String productCategory;
    private ScheduleAdapter adapter;
    private String [] mProfList;
    private String[] mSubjects;
    private  String[] mPercentage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mProfList = getResources().getStringArray(R.array.Proffs);
        mSubjects = getResources().getStringArray(R.array.Subjects);
        mPercentage=getResources().getStringArray(R.array.Attendance);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.activity_attendace_fragment,container,false);

        listview= (ListView) rootView.findViewById(R.id.attendance_list);
         adapter=new ScheduleAdapter(mainActivity,mProfList,mSubjects,mPercentage,6);
        listview.setAdapter(adapter);
        return rootView;
    }
}
