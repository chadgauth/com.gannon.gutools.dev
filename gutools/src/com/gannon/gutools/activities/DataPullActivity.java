package com.gannon.gutools.activities;
import java.io.File;
import java.util.HashMap;

import net.sqlcipher.database.SQLiteDatabase;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.ProgressBar;
import org.holoeverywhere.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gannon.gutools.dev.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;

public class DataPullActivity extends Activity {
	final Context context = this;
	private WebView webView;
	private Navigator navigator;
	private ProgressBar pb;
	private ExtendedWebViewClient eWebViewC;
	private Handler mHandler = new Handler();
	private SQLiteDatabase database;
	//How to output a message:
	//Toast.makeText(getApplicationContext(), "Message", Toast.LENGTH_LONG).show();
	private String guXpress = "https://guxpress.gannon.edu/";
	DBController controller = new DBController(this);
	private CoursesDataSource datasource;
	
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_webview);
		//Following code instantiates the database to be ready to add courses
		datasource = new CoursesDataSource(this.getApplicationContext());
		datasource.open();
		
		//Following deals with the controls that are displayed and hidden on this activity
		pb = (ProgressBar) findViewById(R.id.loadingBar);
		webView = (WebView) findViewById(R.id.webView1);
		
		//Sets the javascript so GUXpress will work appropriately and hides the webview from user
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setVisibility(View.INVISIBLE);
		
		//Set max function below sets the progress bar to 4 different states of progress
		pb.setMax(4);
		
		//Gets the login information from the secure database and sends to the navigator class that uses
		//the username and password to interface with the webview.
		Cursor cr = getLogin();
		cr.moveToFirst();
		navigator = new Navigator(this.getApplicationContext(), webView, pb, cr.getString(0), cr.getString(1));
		cr.close();
		database.close();
		//The above segment destroys the cursor and the database connection immediately to prevent information
		//being dropped
		
		class MyJavaScriptInterface
		{
			@SuppressWarnings("unused")
			public void processSchedule(String schedule){
				Document doc = Jsoup.parse(schedule);	
				//Get number of courses
				int courseCount = doc.select("span#stuimg").size();
				//String[] courseProf = new String[Integer.valueOf(courseCount)];
				//String[] courseName = new String[Integer.valueOf(courseCount)];
				//String[] courseInfo = new String[Integer.valueOf(courseCount)];
				//String[] courseCred = new String[Integer.valueOf(courseCount)];
				//Toast.makeText(getApplicationContext(), "reached", Toast.LENGTH_LONG).show();
				//Toast.makeText(getApplicationContext(), String.valueOf(courseCount), Toast.LENGTH_LONG).show();
				for(int currCourse = 1; currCourse <= courseCount; currCourse++) {
					datasource.createCourse(doc.getElementById("LIST_VAR6_" + Integer.toString(currCourse)).text().toString(), 
							doc.getElementById("LIST_VAR12_" + Integer.toString(currCourse)).text().toString(), 
							doc.getElementById("LIST_VAR13_" + Integer.toString(currCourse)).text().toString(), 
							doc.getElementById("LIST_VAR8_" + Integer.toString(currCourse)).text().toString());					
				}
				//Toast.makeText(getApplicationContext(), "finished", Toast.LENGTH_LONG).show();
				controller.close();
				//Toast.makeText(getApplicationContext(), "closed", Toast.LENGTH_LONG).show();
				navigator.navigate();
				
				
			}
			@SuppressWarnings("unused")
		    public void processHTML(String loaded){
		    	 if(!loaded.equals("true")){
		    		 Toast.makeText(getApplicationContext(), "Page returned " + loaded , Toast.LENGTH_SHORT).show();
		    		 //mHandler.postDelayed(new Runnable() {
		    	     //       public void run() {
		    	     //       	login();
		    	     //       }
		    	     //   }, 500);
		    	 }
		    	 else{
		    		 eWebViewC.normal = true;
		    		 pb.setProgress(1);
			    	 webView.loadUrl("https://guxpress.gannon.edu/GUXpress/colleague?TOKENIDX=5787208423&SS=LGRQ");
			    	 navigator.navigate();
		    	 }
			}
		}
		// Register a new JavaScript interface called HTMLOUT
		webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		eWebViewC = new ExtendedWebViewClient(navigator);
		eWebViewC.setUrl("https://guxpress.gannon.edu/GUXpress/colleague?TYPE=M&PID=CORE-WBMAIN&TOKENIDX=");
		webView.setWebViewClient(eWebViewC);
		webView.loadUrl(guXpress);
		mHandler.postDelayed(new Runnable() {
            public void run() {
            	login();
            }
        }, 200);

}
	public void onBackPressed() {
		Toast.makeText(getApplicationContext(), " Application Loading... ", Toast.LENGTH_SHORT).show();
	}
	private void login() {
		if (eWebViewC.isLoaded()){
			webView.loadUrl("javascript:function isLoaded(){ if(document.getElementsByClassName('label')[2].innerHTML=\"Log In \"){ return \"true\";}else{return \"false\";}};");
			webView.loadUrl("javascript:window.HTMLOUT.processHTML(isLoaded());");
		}else{
			//Toast.makeText(getApplicationContext(), "Page is not loaded, wait 1 seconds", Toast.LENGTH_LONG).show();
			mHandler.postDelayed(new Runnable() {
	            public void run() {
	            	login();
	            }
	        }, 200);
		}
		//webView.loadUrl("javascript:document.getElementById('USER_NAME').value=\"gauthier001\"");
        //webView.loadUrl("javascript:document.getElementsByClassName('shortButton')[0].click()");
        //Toast.makeText(this.getApplicationContext(), "Ran: " + count++, Toast.LENGTH_LONG).show();
    }
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    webView.destroy();
	    datasource.close();
	}
	@Override
	protected void onPause() {
		super.onPause();
		webView.onPause();
	    datasource.close();
	}
	@Override
	protected void onResume() {
		super.onResume();
		webView.onResume();
		datasource.open();
	}
	private Cursor getLogin() {
        SQLiteDatabase.loadLibs(this);
        File databaseFile = getDatabasePath("preferences.db");
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "gannon123", null);
        Cursor cr = database.query("person", null, null, null, null, null, null);
        return cr;
    }
}
    
