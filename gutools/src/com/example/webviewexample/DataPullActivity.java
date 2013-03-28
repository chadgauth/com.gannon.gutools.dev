package com.example.webviewexample;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.holoeverywhere.widget.ProgressBar;
import org.holoeverywhere.widget.Toast;
import org.holoeverywhere.app.Activity;

public class DataPullActivity extends Activity {

	private WebView webView;
	private ExtendedWebViewClient eWebViewC;
	private Handler mHandler = new Handler();
	//How to output a message:
	//Toast.makeText(getApplicationContext(), "Message", Toast.LENGTH_LONG).show();
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
		    	 if(!loaded.equals("true")){
		    		 Toast.makeText(getApplicationContext(), "Page returned " + loaded , Toast.LENGTH_SHORT).show();
		    		 mHandler.postDelayed(new Runnable() {
		    	            public void run() {
		    	            	login();
		    	            }
		    	        }, 500);
		    	 }
		    	 else{
		    		 pb.setProgress(1);
		    		//All these are sequential once logged in processHTML is used to pass variables back to Android's interface 
		    		 webView.loadUrl("https://guxpress.gannon.edu/GUXpress/colleague?TOKENIDX=5787208423&SS=LGRQ");
		    		 webView.setWebViewClient(new WebViewClient() {
		    			    public void onPageFinished(WebView view, String url) {
		    			    	webView.loadUrl("javascript:document.getElementById('USER_NAME').value=\"" + getIntent().getStringExtra("username") + "\"");
		    	            	webView.loadUrl("javascript:document.getElementById('CURR_PWD').value=\""+ getIntent().getStringExtra("password") +"\"");
		    	            	webView.loadUrl("javascript:document.getElementsByClassName('shortButton')[0].click()");
		    	            	pb.setProgress(2);
		    	            	webView.setWebViewClient(new WebViewClient() {
				    			    public void onPageFinished(WebView view, String url) {
				    			    	webView.loadUrl("javascript:function test(){ var link = document.getElementsByClassName('WBST_Bars')[0].href; window.location.href=link;}");
				    	            	webView.loadUrl("javascript:test();");
				    	            	pb.setProgress(3);
				    	            	webView.setWebViewClient(new WebViewClient() {
						    			    public void onPageFinished(WebView view, String url) {
						    	            	webView.loadUrl("javascript:function test(){ var link = document.getElementsByClassName('left')[0].getElementsByTagName('li')[5].getElementsByTagName('a')[0].href; window.location.href=link;}");
						    	            	webView.loadUrl("javascript:test();");
						    	            	pb.setProgress(4);
						   		    		 	webView.setWebViewClient(new WebViewClient() {
								    			    public void onPageFinished(WebView view, String url) {
								    			    	pb.setProgress(5);
								    	            	webView.loadUrl("javascript:document.getElementById('VAR4').selectedIndex=1");
								    	            	webView.loadUrl("javascript:document.getElementsByClassName('shortButton')[0].click()");
								    	            	webView.setWebViewClient(new WebViewClient() {
										    			    public void onPageFinished(WebView view, String url) {
										    	            	webView.setVisibility(View.VISIBLE);
										    	            	pb.setVisibility(View.INVISIBLE);
										    			    }
										    			});
								    			    }
								    			});
						    			    }
						    			});
				    			    }
				    			});
		    			    }
		    			});
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
        }, 500);
		

		

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
	        }, 500);
		}
		//webView.loadUrl("javascript:document.getElementById('USER_NAME').value=\"gauthier001\"");
		//webView.loadUrl("javascript:document.getElementById('CURR_PWD').value=\"t3so7sMgnn\"");
        //webView.loadUrl("javascript:document.getElementsByClassName('shortButton')[0].click()");
        //Toast.makeText(this.getApplicationContext(), "Ran: " + count++, Toast.LENGTH_LONG).show();
    }
}
    
