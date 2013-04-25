package com.gannon.gutools.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.Button;

import com.gannon.gutools.activities.HomeActivity;
import com.gannon.gutools.dev.R;

import org.holoeverywhere.LayoutInflater;

public class WERGFragment extends Fragment implements View.OnClickListener{
    private Button btnMMSWMA;    
    private HomeActivity ha;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        if (getActivity() instanceof HomeActivity){
        	ha = (HomeActivity) getActivity();
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
		View WERGfrag = inflater.inflate(R.layout.werg);
		btnMMSWMA = (Button) WERGfrag.findViewById( R.id.view_main_button_mmswma );
		btnMMSWMA.setOnClickListener(this);
    	if (ha.mIsPlaying){
    		btnMMSWMA.setText(R.string.text_playing);
    	}
    	btnMMSWMA.setEnabled( true );
		return WERGfrag;
	}
    
    @Override
	public void onClick(View v) {
    	if (!ha.mIsPlaying){
    		btnMMSWMA.setText(R.string.text_playing);
    		ha.serviceBinder.startStream();
    		ha.mIsPlaying=true;
    	}else{
    		btnMMSWMA.setText(R.string.button_mmswma);
    		ha.serviceBinder.stopStream();
    		ha.mIsPlaying=false;
    	}
	}
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
   
}
