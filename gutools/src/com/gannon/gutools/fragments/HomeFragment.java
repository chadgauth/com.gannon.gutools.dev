package com.gannon.gutools.fragments;

import android.os.Bundle;
import org.holoeverywhere.app.Fragment;

import com.gannon.gutools.dev.R;

import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// construct the RelativeLayout
		return inflater.inflate(R.layout.assignment_list);
	}
}