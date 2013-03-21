package com.example.webviewexample;

import android.view.View;
import android.widget.Toast;

public class MyJavaScriptInterface {
	@SuppressWarnings("unused")
    public void processHTML(String loaded)
    {
    	 if(!loaded.equals("true")){
    		 Toast.makeText(getApplicationContext(), "Page returned " + loaded , Toast.LENGTH_SHORT).show();
    		 mHandler.postDelayed(new Runnable() {
    	            public void run() {
    	            	login();
    	            	pb.setProgress(1);
    	            }
    	        }, 1000);
    	 }
    	 else{
    		//All these are sequential once logged in processHTML is used to pass variables back to Android's interface 
    		 webView.loadUrl("https://guxpress.gannon.edu/GUXpress/colleague?TOKENIDX=5787208423&SS=LGRQ");
    		 mHandler.postDelayed(new Runnable() {
    	            public void run() {
    	            	//LOG IN
    	            	webView.loadUrl("javascript:document.getElementById('USER_NAME').value=\"" + getIntent().getStringExtra("username") + "\"");
    	            	webView.loadUrl("javascript:document.getElementById('CURR_PWD').value=\""+ getIntent().getStringExtra("password") +"\"");
    	            	webView.loadUrl("javascript:document.getElementsByClassName('shortButton')[0].click()");
    	            	pb.setProgress(2);
    	            }
    	        }, 3000);
    		 mHandler.postDelayed(new Runnable() {
    	            public void run() {
    	            	//A function is passed to click on the next
    	            	webView.loadUrl("javascript:function test(){ var link = document.getElementsByClassName('WBST_Bars')[0].href; window.location.href=link;}");
    	            	webView.loadUrl("javascript:test();");
    	            	pb.setProgress(3);
    	            }
    	        }, 6000);
    		 mHandler.postDelayed(new Runnable() {
    	            public void run() {
    	            	webView.loadUrl("javascript:function test(){ var link = document.getElementsByClassName('left')[0].getElementsByTagName('li')[5].getElementsByTagName('a')[0].href; window.location.href=link;}");
    	            	webView.loadUrl("javascript:test();");
    	            	pb.setProgress(4);
    	            	
    	            }
    	        }, 9000);
    		 mHandler.postDelayed(new Runnable() {
    	            public void run() {
    	            	webView.loadUrl("javascript:document.getElementById('VAR4').selectedIndex=2");
    	            	webView.loadUrl("javascript:document.getElementsByClassName('shortButton')[0].click()");
    	            	pb.setProgress(5);
    	            }
    	        }, 11000);
    		 mHandler.postDelayed(new Runnable() {
    	            public void run() {
    	            	webView.setVisibility(View.VISIBLE);
    	            	pb.setVisibility(View.INVISIBLE);
    	            }
    	        }, 13000);
    		 //webView.loadUrl("https://guxpress.gannon.edu/GUXpress/colleague?TOKENIDX=180218265&SS=LGRQ&URL");
    		// Toast.makeText(getApplicationContext(), "Page is " + loaded, Toast.LENGTH_LONG).show();
    	 }
    }
}
