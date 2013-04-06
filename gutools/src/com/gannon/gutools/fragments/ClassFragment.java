package com.gannon.gutools.fragments;

import org.holoeverywhere.app.Fragment;

import com.gannon.gutools.dev.R;

import android.os.Bundle;
import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ClassFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// construct the RelativeLayout
		return inflater.inflate(R.layout.events);
	}
}