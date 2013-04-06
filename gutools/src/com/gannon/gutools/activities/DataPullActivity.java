package com.gannon.gutools.activities;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.ProgressBar;
import org.holoeverywhere.widget.Toast;

import com.gannon.gutools.dev.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DataPullActivity extends Activity {
	final Context context = this;
	private WebView webView;
	private Navigator navigator;
	private ProgressBar pb;
	private ExtendedWebViewClient eWebViewC;
	private Handler mHandler = new Handler();
	//How to output a message:
	//Toast.makeText(getApplicationContext(), "Message", Toast.LENGTH_LONG).show();
	private String guXpress = "https://guxpress.gannon.edu/";
	
	
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_webview);
		pb = (ProgressBar) findViewById(R.id.loadingBar);
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setVisibility(View.INVISIBLE);
		pb.setMax(4);
		navigator = new Navigator(this.getApplicationContext(), webView, pb, getIntent().getStringExtra("username"), getIntent().getStringExtra("password"));
		class MyJavaScriptInterface
		{
			@SuppressWarnings("unused")
			public void processSchedule(String schedule){
				//This will parse the schedule and send it to this case the Toast display
				Toast.makeText(getApplicationContext(), schedule, Toast.LENGTH_LONG).show();
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
		//webView.loadUrl("javascript:document.getElementById('CURR_PWD').value=\"t3so7sMgnn\"");
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
}
    
