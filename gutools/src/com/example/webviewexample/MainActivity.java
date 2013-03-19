package com.example.webviewexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
 
public class MainActivity extends Activity {
 
	private Button button;
 
	public void onCreate(Bundle savedInstanceState) {
		final Context context = this;
 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			
		  @Override
		  public void onClick(View arg0) {
			EditText ur = (EditText)findViewById(R.id.editText1);
			EditText ps = (EditText)findViewById(R.id.editText2);
		    Intent intent = new Intent(context, WebViewActivity.class);
		    intent.putExtra("username", ur.getText().toString());
		    intent.putExtra("password", ps.getText().toString());
		    startActivity(intent);
		  }
		  
 
		});
 
	}
 
}