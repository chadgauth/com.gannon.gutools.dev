package com.gannon.gutools.fragments;
import java.util.ArrayList;
import java.util.HashMap;

import com.gannon.gutools.dev.R;
 
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScheduleAdapter extends BaseAdapter {
 
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
 
    public ScheduleAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.schedule_row, null);
 
        TextView name = (TextView)vi.findViewById(R.id.name); // name
        TextView info = (TextView)vi.findViewById(R.id.info); // class information
        TextView prof = (TextView)vi.findViewById(R.id.prof); // professor
        TextView time = (TextView)vi.findViewById(R.id.time);
        TextView mon = (TextView)vi.findViewById(R.id.monday);
        TextView tue = (TextView)vi.findViewById(R.id.tuesday);
        TextView wed = (TextView)vi.findViewById(R.id.wednesday);
        TextView thu = (TextView)vi.findViewById(R.id.thursday);
        TextView fri = (TextView)vi.findViewById(R.id.friday);
        HashMap<String, String> course = new HashMap<String, String>();
        course = data.get(position);
 
        // Setting all values in listview
        name.setText(course.get(ScheduleFragment.KEY_NAME));
        info.setText(course.get(ScheduleFragment.KEY_INFO));
        prof.setText(course.get(ScheduleFragment.KEY_PROF));
        time.setText(course.get(ScheduleFragment.KEY_TIME));
        if(course.get(ScheduleFragment.KEY_MON).equals("1"))
        	mon.setTextColor(0xFF343434);
        else
        	mon.setTextColor(0xFFE5E5E5);
        if(course.get(ScheduleFragment.KEY_TUE).equals("1"))
        	tue.setTextColor(0xFF343434);
        else
        	tue.setTextColor(0xFFE5E5E5);
        if(course.get(ScheduleFragment.KEY_WED).equals("1"))
        	wed.setTextColor(0xFF343434);
        else
        	wed.setTextColor(0xFFE5E5E5);
        if(course.get(ScheduleFragment.KEY_THU).equals("1"))
        	thu.setTextColor(0xFF343434);
        else
        	thu.setTextColor(0xFFE5E5E5);
        if(course.get(ScheduleFragment.KEY_FRI).equals("1"))
        	fri.setTextColor(0xFF343434);
        else
        	fri.setTextColor(0xFFE5E5E5);
        return vi;
    }
}