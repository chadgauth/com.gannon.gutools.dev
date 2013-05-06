package com.gannon.gutools.fragments;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.ListView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.gannon.gutools.activities.Course;
import com.gannon.gutools.activities.CoursesDataSource;
import com.gannon.gutools.dev.R;

public class ScheduleFragment extends Fragment {
	private CoursesDataSource datasource;
	static final String KEY_COURSE = "course"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_NAME = "name";
    static final String KEY_INFO = "info";
    static final String KEY_PROF = "prof";
    static final String KEY_MON = "monday";
    static final String KEY_TUE = "tuesday";
    static final String KEY_WED = "wednesday";
    static final String KEY_THU = "thursday";
    static final String KEY_FRI = "friday";
    static final String KEY_TIME = "time";
    ListView list;
    ScheduleAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View V = inflater.inflate(R.layout.schedule_list);
		 
        ArrayList<HashMap<String, String>> coursesList = new ArrayList<HashMap<String, String>>();

		datasource = new CoursesDataSource(this.getActivity());
		datasource.open();
		List<Course> courses = datasource.getAllCourses();
		//loop to add view
		for(int i=0; i<courses.size(); i++){
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            // adding each child node to HashMap key =&gt; value
            map.put(KEY_ID, String.valueOf(courses.get(i).getId()));
            map.put(KEY_NAME, courses.get(i).getName());
            map.put(KEY_INFO, courses.get(i).getInfo());
            map.put(KEY_PROF, courses.get(i).getProfessor());
            map.put(KEY_MON, String.valueOf(courses.get(i).isM()));
            map.put(KEY_TUE, String.valueOf(courses.get(i).isT()));
            map.put(KEY_WED, String.valueOf(courses.get(i).isW()));
            map.put(KEY_THU, String.valueOf(courses.get(i).isTH()));
            map.put(KEY_FRI, String.valueOf(courses.get(i).isF()));
            map.put(KEY_TIME, courses.get(i).getTime());
 
            // adding HashList to ArrayList
            coursesList.add(map);
        }
 
        list=(ListView) V.findViewById(R.id.list);
 
        // Getting adapter by passing xml data ArrayList
        adapter = new ScheduleAdapter(this.getActivity(), coursesList);
        list.setAdapter(adapter);
		return V;
	}

}