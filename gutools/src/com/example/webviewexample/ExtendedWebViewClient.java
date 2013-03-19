package com.example.webviewexample;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

class ExtendedWebViewClient extends WebViewClient {
    private int webViewPreviousState;
    private final int PAGE_STARTED = 0x1;
    private final int PAGE_REDIRECTED = 0x2;
    private String url;
    private boolean loaded=true;
	private String tokenid;
    public void onPageStarted(WebView paramWebView, String paramString,
            Bitmap paramBitmap) {
        super.onPageStarted(paramWebView, paramString, paramBitmap);
        loaded=false;
       // Toast.makeText(paramWebView.getContext(), "Param String: " + paramString + " and URL: " + url, Toast.LENGTH_LONG).show();
        if(paramString.length() >= url.length() && !paramString.substring(paramString.length()-4, paramString.length()).contentEquals("NULL") && paramString.substring(0, url.length()).contentEquals(url)) {
            webViewPreviousState = PAGE_STARTED;
        } else {
            webViewPreviousState = PAGE_REDIRECTED;
           // Toast.makeText(paramWebView.getContext(), "Param String: " + paramString.substring(paramString.length()-4, paramString.length()), Toast.LENGTH_SHORT).show();
        }
	// DO YOU STUFF IF NEEDED
	}
    public void onPageFinished(WebView paramWebView, String paramString) {
        if (webViewPreviousState == PAGE_STARTED) {
        	loaded=true;
		}
}
    public String getUrl(){
    	return url;
    }
    public void setUrl(String url){
    	this.url=url;
    } 
    public boolean isLoaded(){
    	return loaded;
    }
    public void setToken(String tokenid){
    	this.tokenid = tokenid;
    }
}
    
