package com.example.influential.schedulerapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.influential.schedulerapp.R;

/**
 * Created by bazil on 8/10/17.
 */

public class ScheduleAdapter extends BaseAdapter {
    Context context;
    private String[] timePeriod;
    private String[] subjects;
    private String[] roomNo;
    private int count=0;

    public ScheduleAdapter(Context context, String[] timePeriod, String[] subjects, String[] roomNo,int count) {
        this.context = context;
        this.timePeriod = timePeriod;
        this.subjects = subjects;
        this.roomNo = roomNo;
        this.count=count;
    }

    @Override
    public int getCount() {

        return count;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View row;
        LayoutInflater inflater;

        if (convertView==null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.homepage_list_row,parent,false);
            row=convertView;

        }
        else
        {
            row=convertView;
        }

        TextView timeTxt= (TextView) row.findViewById(R.id.timeperiodId);
        TextView subjectTxt= (TextView) row.findViewById(R.id.subjectId);
        TextView roomTxt= (TextView) row.findViewById(R.id.roomNoId);

        timeTxt.setText(timePeriod[i]);
        subjectTxt.setText(subjects[i]);
        roomTxt.setText(roomNo[i]);

        return row;
    }
}
