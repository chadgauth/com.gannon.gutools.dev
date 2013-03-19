package com.example.webviewexample;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;
 
public class WebViewActivity extends Activity {

	private WebView webView;
	private ExtendedWebViewClient eWebViewC;
	private Handler mHandler = new Handler();
	//= new WebView(this);
	private String guXpress = "https://guxpress.gannon.edu/";
	
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_webview);
		final ProgressBar pb = (ProgressBar) findViewById(R.id.loadingBar);
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setVisibility(View.INVISIBLE);
		pb.setMax(5);
		class MyJavaScriptInterface
		{
		    @SuppressWarnings("unused")
		    public void processHTML(String loaded)
		    {
		    	//Toast.makeText(getApplicationContext(), "Made it to process html! var:" + loaded, Toast.LENGTH_LONG).show();
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
		    	            	Toast.makeText(getApplicationContext(), "user is:" + getIntent().getStringExtra("username"), Toast.LENGTH_LONG).show();
		    	            	Toast.makeText(getApplicationContext(), "pass is:" + getIntent().getStringExtra("password"), Toast.LENGTH_LONG).show();
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

		// Register a new JavaScript interface called HTMLOUT
		webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		eWebViewC = new ExtendedWebViewClient();
		eWebViewC.setUrl("https://guxpress.gannon.edu/GUXpress/colleague?TYPE=M&PID=CORE-WBMAIN&TOKENIDX=");
		webView.setWebViewClient(eWebViewC);

		webView.loadUrl(guXpress);
		
		mHandler.postDelayed(new Runnable() {
            public void run() {
            	login();
            }
        }, 1000);
		

		

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
	        }, 1000);
		}
		//webView.loadUrl("javascript:document.getElementById('USER_NAME').value=\"gauthier001\"");
		//webView.loadUrl("javascript:document.getElementById('CURR_PWD').value=\"t3so7sMgnn\"");
        //webView.loadUrl("javascript:document.getElementsByClassName('shortButton')[0].click()");
        //Toast.makeText(this.getApplicationContext(), "Ran: " + count++, Toast.LENGTH_LONG).show();
    }
}
    
