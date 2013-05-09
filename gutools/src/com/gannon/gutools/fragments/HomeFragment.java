package com.gannon.gutools.fragments;

import java.util.List;

import android.os.Bundle;
import org.holoeverywhere.app.Fragment;

import com.fima.cardsui.views.CardUI;
import com.gannon.gutools.activities.Course;
import com.gannon.gutools.activities.CoursesDataSource;
import com.gannon.gutools.activities.MyCard;
import com.gannon.gutools.dev.R;

import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {
	private CardUI mCardView;
	private CoursesDataSource datasource;
	   @Override
	   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	     View V = inflater.inflate(R.layout.home);
	     mCardView = (CardUI) V.findViewById(R.id.cardsview);
	     mCardView.setSwipeable(false);
	     datasource = new CoursesDataSource(this.getActivity());
	     datasource.open();
	     List<Course> courses = datasource.getTodaysCourses();
	     MyCard c;
	     for(int i=0; i<courses.size(); i++){
	         c = new MyCard(courses.get(i).getName());
	    	 c.setDesc(courses.get(i).getInfo() + "\n" + courses.get(i).getTime());
	    	 mCardView.addCard(c);
	     }
	     datasource.close();
	     // draw cards
	     mCardView.refresh();
	     return V;
	   }
}