package com.gannon.gutools.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.holoeverywhere.ArrayAdapter;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.ListFragment;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.Toast;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.gannon.gutools.activities.Assignment;
import com.gannon.gutools.activities.AssignmentsDataSource;
import com.gannon.gutools.dev.R;

public class AssignmentFragment extends ListFragment implements View.OnClickListener{
	private View v;
	private ImageButton addButton; 
	private ImageButton micButton; 
	private static final int REQUEST_CODE = 1234;
	public EditText assignText;
	private AssignmentsDataSource datasource;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		datasource = new AssignmentsDataSource(getActivity());
		datasource.open();
		
		v = inflater.inflate(R.layout.assignment_list);
		
		List<Assignment> values = datasource.getAllAssignments();

	    // Use the SimpleCursorAdapter to show the
	    // elements in a ListView
	    ArrayAdapter<Assignment> adapter = new ArrayAdapter<Assignment>(this.getActivity(),
	        R.layout.simple_list_item_1, values);
	    setListAdapter(adapter);
	    addButton = (ImageButton) v.findViewById(R.id.addButton);
	    micButton = (ImageButton) v.findViewById(R.id.micButton);
	    assignText = (EditText) v.findViewById(R.id.assignmentText);
	    addButton.setOnClickListener(this);
	    micButton.setOnClickListener(this);
		v.findViewById(R.id.floatingFrame).setVisibility(View.GONE);
		return v;
	}
	
	public void setVisibility(boolean vis){
		if(vis){
			v.findViewById(R.id.floatingFrame).setVisibility(View.VISIBLE);
			assignText.setText("");
		}else
			v.findViewById(R.id.floatingFrame).setVisibility(View.GONE);
	}
	@Override
	public void onClick(View v) {
		
		ArrayAdapter<Assignment> adapter = (ArrayAdapter<Assignment>) getListAdapter();
		Assignment comment = null;
		Log.i("test", Integer.toString(v.getId()));
		switch (v.getId()){
			case R.id.addButton:
				if(!assignText.getText().toString().isEmpty()){
					comment = datasource.createAssignment(assignText.getText().toString());
					adapter.add(comment);
					setVisibility(false);
				}else
					Toast.makeText(getActivity(), " Assignment can't be blank ", Toast.LENGTH_SHORT);
		      // Save the new comment to the database
		      
		      break;
		    default:
		    	startVoiceRecognitionActivity();
		     // if (getListAdapter().getCount() > 0) {
		     //   comment = (Assignment) getListAdapter().getItem(0);
		     //   datasource.deleteComment(comment);
		     //   adapter.remove(comment);
		     // }
		      break;
		}
	      
	}
	private void startVoiceRecognitionActivity()
	{
	    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Assignment: ");
	    startActivityForResult(intent, REQUEST_CODE);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	    if (requestCode == REQUEST_CODE)
	    {
	        // Populate the wordsList with the String values the recognition engine thought it heard
	        ArrayList<String> matches = data.getStringArrayListExtra(
	                RecognizerIntent.EXTRA_RESULTS);
	        assignText.setText(matches.get(0));
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}
	
}