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
	
	
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_webview);
		pb = (ProgressBar) findViewById(R.id.loadingBar);
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setVisibility(View.INVISIBLE);
		pb.setMax(4);
		Cursor cr = getLogin();
		cr.moveToFirst();
		navigator = new Navigator(this.getApplicationContext(), webView, pb, cr.getString(0), cr.getString(1));
		cr.close();
		database.close();
		class MyJavaScriptInterface
		{
			@SuppressWarnings("unused")
			public void processSchedule(String schedule){
				Document doc = Jsoup.parse(schedule);	
				HashMap<String, String> queryValues =  new  HashMap<String, String>();
				Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_LONG).show();
				//Get number of courses
				int courseCount = doc.select("span#stuimg").size();
				Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
				String[] courseProf = new String[Integer.valueOf(courseCount)];
				String[] courseName = new String[Integer.valueOf(courseCount)];
				String[] courseInfo = new String[Integer.valueOf(courseCount)];
				String[] courseCred = new String[Integer.valueOf(courseCount)];
				Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();
				for(int currCourse = 0; currCourse < courseCount; currCourse++) {
					queryValues.put("courseProf", courseProf[currCourse] = doc.getElementById("LIST_VAR13_" + Integer.toString(currCourse)).text().toString());
					controller.insertCourseProf(queryValues);
					queryValues.put("courseName", courseName[currCourse] = doc.getElementById("LIST_VAR6_" + Integer.toString(currCourse)).text().toString());
					controller.insertCourseName(queryValues);
					queryValues.put("courseInfo", courseInfo[currCourse] = doc.getElementById("LIST_VAR12_" + Integer.toString(currCourse)).text().toString());
					controller.insertCourseInfo(queryValues);
					queryValues.put("courseCred", courseCred[currCourse] = doc.getElementById("LIST_VAR8_" + Integer.toString(currCourse)).text().toString());
					controller.insertCourseCred(queryValues);
					
					Toast.makeText(getApplicationContext(), courseProf[currCourse-1], Toast.LENGTH_LONG).show();
				}
				Toast.makeText(getApplicationContext(), "finished", Toast.LENGTH_LONG).show();
				controller.close();
				Toast.makeText(getApplicationContext(), "closed", Toast.LENGTH_LONG).show();
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
	}
	@Override
	protected void onPause() {
		super.onPause();
		webView.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
		webView.onResume();
	}
	private Cursor getLogin() {
        SQLiteDatabase.loadLibs(this);
        File databaseFile = getDatabasePath("preferences.db");
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "gannon123", null);
        Cursor cr = database.query("person", null, null, null, null, null, null);
        return cr;
    }
}
    
