package com.gannon.gutools.fragments;

import android.os.Bundle;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.app.ListFragment;
import org.holoeverywhere.widget.ListView;

import com.gannon.gutools.dev.R;

import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gannon.gutools.activities.HomeActivity;

public class NavigationFragment extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.navlist, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this.getActivity(), 
				R.layout.list_item, 
				this.getResources().getStringArray(R.array.nav_list));
		this.setListAdapter(aa);
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		switch (position) {
		case 0:
			newContent = new HomeFragment();
			break;
		case 1:
			newContent = new EventFragment();
			break;
		case 2:
			newContent = new ScheduleFragment();
			break;
		case 3:
			newContent = new AssignmentFragment();
			break;
		case 4:
			newContent = new WERGFragment();
			break;
		}
		if (newContent != null)
			switchFragment(newContent);
	}
	
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof HomeActivity) {
			HomeActivity ha = (HomeActivity) getActivity();
			ha.replaceFragment(fragment);
		}
	}
}