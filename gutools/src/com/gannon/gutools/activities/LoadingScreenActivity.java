package com.gannon.gutools.activities;
import java.io.File;

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
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

public class LoadingScreenActivity extends Activity {
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
		setContentView(R.layout.loading_screen);
		//Following code instantiates the database to be ready to add courses
		datasource = new CoursesDataSource(context.getApplicationContext());
		datasource.open();
		
		//Following deals with the controls that are displayed and hidden on this activity
		pb = (ProgressBar) findViewById(R.id.loadingBar);
		pb.setVisibility(View.GONE);
		webView = (WebView) findViewById(R.id.webView1);
		
		//Sets the javascript so GUXpress will work appropriately and hides the webview from user
		webView.getSettings().setJavaScriptEnabled(true);
		
		//Set max function below sets the progress bar to 4 different states of progress
		pb.setMax(4);
		
		//Gets the login information from the secure database and sends to the navigator class that uses
		//the username and password to interface with the webview.
		Cursor cr = getLogin(context);
		cr.moveToFirst();
		navigator = new Navigator(context.getApplicationContext(), webView, pb, cr.getString(0), cr.getString(1));
		cr.close();
		database.close();
		//The above segment destroys the cursor and the database connection immediately to prevent information
		//being dropped
		
		//The MyJavaScriptInterface completely handles the XSS injection that is used to parse data from a web
		//view. The below code is run when DOM triggers the HTMLOUT javascript code.
		class MyJavaScriptInterface
		{
			@SuppressWarnings("unused")
			public void processSchedule(String schedule){
				Document doc = Jsoup.parse(schedule);	
				int courseCount = doc.select("span#stuimg").size();
				String name;
				String mInfo;
				String info;
				String prof;
				String cred;
				String time;
				for(int currCourse = 1; currCourse <= courseCount; currCourse++) {
					name = doc.getElementById("LIST_VAR6_" + Integer.toString(currCourse)).text().toString();
					info = doc.getElementById("LIST_VAR12_" + Integer.toString(currCourse)).text().toString();
					prof = doc.getElementById("LIST_VAR13_" + Integer.toString(currCourse)).text().toString();
					cred = doc.getElementById("LIST_VAR8_" + Integer.toString(currCourse)).text().toString();
					name = name.substring(name.indexOf(") ") + 1).trim();
					mInfo = info.replaceAll("Lecture ", "").trim();
					if(!name.contains("Science"))
						name = name.replaceAll("Scienc", "Science");
					if(!name.contains("Business"))
						name = name.replaceAll("Bus", "Business");
					name = name.replaceAll("Prin ", "Principles ");
					name = name.replaceAll("Envirn", "Environment");
					name = name.replaceFirst("HC-", "Honors ");
					if(!name.contains("Culture"))
						name = name.replaceAll("Cult", "Culture");
					mInfo = mInfo.replaceAll("Monday", "");
					mInfo = mInfo.replaceAll("Tuesday", "");
					mInfo = mInfo.replaceAll("Wednesday", "");
					mInfo = mInfo.replaceAll("Thursday", "");
					mInfo = mInfo.replaceAll("Friday", "");
					mInfo = mInfo.replaceAll(" - ", "-");
					mInfo = mInfo.replaceAll("[0-9][0-9].[0-9][0-9].[0-9][0-9][0-9]" +
							"[0-9]-[0-9][0-9].[0-9][0-9]." +
							"[0-9][0-9][0-9][0-9]", "");
					mInfo = mInfo.replaceAll(",", "");
					mInfo = mInfo.trim();
					mInfo = mInfo.replaceAll("  ", "\n");
					time="";
					while (mInfo.contains("-")){
						time = time + mInfo.substring(mInfo.indexOf("-")-7, mInfo.indexOf("-")+8);
						mInfo = mInfo.replaceFirst("..:....-..:....", "");
						time = time +"\n";
					}
					time.replaceAll("^0", "");
					time.replaceAll(":0", ":");
					mInfo = mInfo.trim();
					
					datasource.createCourse(name, mInfo, prof, cred, time, (info.contains("Monday"))?1:0,
							(info.contains("Tuesday"))?1:0, (info.contains("Wednesday"))?1:0,
							(info.contains("Thursday"))?1:0, (info.contains("Friday"))?1:0);
			
				}
				controller.close();
				navigator.navigate();
			}
			@SuppressWarnings("unused")
		    public void processHTML(String loaded){
		    	 if(loaded.equals("true")){
		    		 eWebViewC.normal = true;
		    		 pb.setProgress(1);
			    	 webView.loadUrl("https://guxpress.gannon.edu/GUXpress/colleague?TOKENIDX=5787208423&SS=LGRQ");
			    	 navigator.navigate();
		    	 }else{
		    		 login();
		    	 }
			}
		}
		// Register a new JavaScript interface called HTMLOUT
		webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		eWebViewC = new ExtendedWebViewClient(navigator);
		eWebViewC.setUrl("https://guxpress.gannon.edu/GUXpress/colleague?TYPE=M&PID=CORE-WBMAIN&TOKENIDX=");
		webView.setWebViewClient(eWebViewC);
		webView.loadUrl(guXpress);
		mHandler.postDelayed(new Runnable(){
			public void run(){
				pb.setVisibility(View.VISIBLE);
			}
		}, 200);
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
			mHandler.postDelayed(new Runnable() {
	            public void run() {
	            	login();
	            }
	        }, 200);
		}
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
	private Cursor getLogin(Context context) {
        SQLiteDatabase.loadLibs(this);
        File databaseFile = getDatabasePath("preferences.db");
        DeviceUuidFactory uuid = new DeviceUuidFactory(context.getApplicationContext());
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "gannon" + uuid.getDeviceUuid().toString(), null);
        Cursor cr = database.query("person", null, null, null, null, null, null);
        return cr;
    }
}
    
