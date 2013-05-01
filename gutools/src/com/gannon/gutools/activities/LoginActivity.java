package com.gannon.gutools.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.app.Activity;

import com.gannon.gutools.dev.R;
 
public class LoginActivity extends Activity {
	private SQLiteDatabase database;
	private Button button;
	private EditText ur;
	private EditText ps;
	private Handler mHandler = new Handler();
	private int backpress;
	public void onCreate(Bundle savedInstanceState) {
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		backpress=0;
		final Context context = this;
		SQLiteDatabase.loadLibs(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		button = (Button) findViewById(R.id.button1);
		ur = (EditText)findViewById(R.id.editText1);
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
		button.setEnabled(!button.isEnabled());
		ur.setEnabled(!ur.isEnabled());
		ps.setEnabled(!ps.isEnabled());
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
		database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "gannon123", null);
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
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "gannon123", null);
        database.execSQL("create table person(user, pass, log)");
        database.execSQL("insert into person(user, pass, log) values(?, ?, ?)", new Object[]{ur.getText().toString(),
        		ps.getText().toString(), true});
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
	protected void onResume() {
		super.onResume();
	}
    private void postLoginData() {
        // Create a new HttpClient and Post Header
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
				
            }else
            {
            	InitializeSQLCipher();
			    Intent intent = new Intent(getApplicationContext(), LoadingScreenActivity.class);
			    startActivity(intent);
			    overridePendingTransition(R.anim.fade_in, 0);
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