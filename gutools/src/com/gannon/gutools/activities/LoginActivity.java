package com.gannon.gutools.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.Toast;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.app.Activity;

import com.gannon.gutools.dev.R;
 
public class LoginActivity extends Activity {
	private SQLiteDatabase database;
	private Button button;
	private EditText ur;
	private EditText ps;
	private int backpress;
	private boolean mVisible;
	TextWatcher tt = null;
	
	public void onCreate(Bundle savedInstanceState) {
		mVisible=true;		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		backpress=0;
		final Context context = this;
		SQLiteDatabase.loadLibs(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		button = (Button) findViewById(R.id.button1);
		ur = (EditText)findViewById(R.id.editText1);
		if(savedInstanceState != null){
			ur.setText(savedInstanceState.getString("ur"));
		}
		//TODO: Fix the login lag
		tt = new TextWatcher() {
	           public void afterTextChanged(Editable s){
	                ur.setSelection(s.length());
	           }
	           public void beforeTextChanged(CharSequence s,int start,int count, int after){} 
	           public void onTextChanged(CharSequence s, int start, int before, int count) {
	               ur.removeTextChangedListener(tt);
	               if(ur.getText().toString().contains("@")){
		               Toast.makeText(getApplicationContext(), "We only need your username. Ex: smith001", Toast.LENGTH_SHORT).show();
		               ps.requestFocus();
	               }    
		           ur.setText(ur.getText().toString().replace("@", " "));
	               ur.addTextChangedListener(tt);	      
	           }
	       };
	       ur.addTextChangedListener(tt);
		ps = (EditText)findViewById(R.id.editText2);
		button.setVisibility(View.INVISIBLE);
		ur.setVisibility(View.INVISIBLE);
		ps.setVisibility(View.INVISIBLE);
		if(isLoggedIn()){
			Intent intent = new Intent(context, HomeActivity.class);
	        startActivity(intent);
		}else{
			button.setVisibility(View.VISIBLE);
			ur.setVisibility(View.VISIBLE);
			ps.setVisibility(View.VISIBLE);
			button.setOnClickListener(new OnClickListener() {
				
			  @Override
			  public void onClick(View arg0) {
				  postLoginData();		
				  toggleInputs();
			  }
	 
			});
		}
	}
	public void toggleInputs(){
		if(mVisible){
			button.setEnabled(false);
			ur.setEnabled(false);
			ps.setEnabled(false);
			button.setVisibility(View.GONE);
			ur.setVisibility(View.GONE);
			ps.setVisibility(View.GONE);
		} 
		else{
			button.setEnabled(true);
			ur.setEnabled(true);
			ps.setEnabled(true);
			button.setVisibility(View.VISIBLE);
			ur.setVisibility(View.VISIBLE);
			ps.setVisibility(View.VISIBLE);
		}
		mVisible = !mVisible;
		
	}
	public void onBackPressed() {
		   // if (!getSupportFragmentManager().popBackStackImmediate()) {
		   //     stopService();
		  //      finish();
		  //  }
		    backpress = (backpress + 1);
		    


		    if (backpress>1) {
		    	Intent startMain = new Intent(Intent.ACTION_MAIN);
		        startMain.addCategory(Intent.CATEGORY_HOME);
		        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(startMain);
		        this.finish();
		    }
		    else
		    	Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

		}
	private boolean isLoggedIn() {
		File databaseFile = getDatabasePath("preferences.db");
		if(!databaseFile.exists()){
			return false;
		}
		DeviceUuidFactory uuid = new DeviceUuidFactory(this.getApplicationContext());
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "gannon" + uuid.getDeviceUuid().toString(), null);
        Cursor cr = database.query("person", null, null, null, null, null, null);
        cr.moveToFirst();
        Boolean result = cr.getInt(2)>0;
        cr.close();
        database.close();
        return result;
	}
	private void InitializeSQLCipher() {
        
        File databaseFile = getDatabasePath("preferences.db");
        databaseFile.mkdirs();
        databaseFile.delete();
        DeviceUuidFactory uuid = new DeviceUuidFactory(this.getApplicationContext());
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "gannon" + uuid.getDeviceUuid().toString(), null);
        database.execSQL("create table person(user, pass, log)");
        database.execSQL("insert into person(user, pass, log) values(?, ?, ?)", new Object[]{ur.getText().toString().trim(),
        		ps.getText().toString().trim(), true});
        database.close();
    }
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
    @Override
    protected void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	outState.putString("ur", ur.getText().toString());
    }
	@Override
	protected void onResume() {
		super.onResume();
	}
    private void postLoginData() {
    	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // Create a new HttpClient and Post Header
    	//InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    	//if(imm.isAcceptingText())
    	//	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        HttpClient httpclient = new DefaultHttpClient();
        
        /* login.php returns true if username and password is equal to saranga */
        HttpPost httppost = new HttpPost("http://angel.gannon.edu/signon/authenticate.asp");

        try {
        	
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", ur.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("password", ps.getText().toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            
            String str = inputStreamToString(response.getEntity().getContent()).toString();
            
            if(str.toLowerCase().contains("invalid"))
            {
            	Toast.makeText(getApplicationContext(), " Invalid Username or Password ", Toast.LENGTH_SHORT).show();  
          	  	toggleInputs();
				ps.setText("");
			//	if(!imm.isAcceptingText())
			//		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				
            }else
            {
            	InitializeSQLCipher();
            	Handler mHandler = new Handler();
            	mHandler.postDelayed(new Runnable(){
					@Override
					public void run() {
						Intent intent = new Intent(getApplicationContext(), LoadingScreenActivity.class);
					    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					    startActivity(intent);
					}
            	}, 2000);
			    
            }

        } catch (ClientProtocolException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    } 
  
    private StringBuilder inputStreamToString(InputStream is) {
    	String line = "";
    	StringBuilder total = new StringBuilder();
    	// Wrap a BufferedReader around the InputStream
    	BufferedReader rd = new BufferedReader(new InputStreamReader(is));
    	// Read response until the end
    	try {
			while ((line = rd.readLine()) != null) { 
				total.append(line); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	// Return full string
    	return total;
    }
}