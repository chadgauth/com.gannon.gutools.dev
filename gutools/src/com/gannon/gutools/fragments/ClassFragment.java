package com.gannon.gutools.fragments;

import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.fima.cardsui.views.CardUI;
import com.gannon.gutools.activities.Course;
import com.gannon.gutools.activities.CoursesDataSource;
import com.gannon.gutools.activities.MyCard;
import com.gannon.gutools.dev.R;

public class ClassFragment extends Fragment {
	private CardUI mCardView;
	private CoursesDataSource datasource;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View V = inflater.inflate(R.layout.events);
		mCardView = (CardUI) V.findViewById(R.id.cardsview);
		mCardView.setSwipeable(true);
		datasource = new CoursesDataSource(this.getActivity());
		datasource.open();
		List<Course> courses = datasource.getAllCourses();
		MyCard c;
		// add AndroidViews Cards
		for(int i=0; i<courses.size(); i++){
			c = new MyCard(courses.get(i).getName());
			c.setDesc(courses.get(i).getInfo());
			mCardView.addCard(c);
		}
		// add one card, and then add another one to the last stack.
		datasource.close();
		// draw cards
		mCardView.refresh();
		return V;
	}
	//private Cursor getSchedule() {
	//	 File databaseFile = this.getActivity().getDatabasePath("Schedule.db");
	//        databaseFile.delete();
   //     database = SQLiteDatabase.openOrCreateDatabase(databaseFile, null);
   //     Cursor cr = database.query("courses", null, null, null, null, null, null);
   //     return cr;
  //  }
}