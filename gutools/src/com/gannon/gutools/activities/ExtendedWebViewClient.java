package com.gannon.gutools.activities;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class ExtendedWebViewClient extends WebViewClient {
    private int webViewPreviousState;
    private final int PAGE_STARTED = 0x1;
    private final int PAGE_REDIRECTED = 0x2;
    private String url;
    private boolean loaded=true;
    public void onPageStarted(WebView paramWebView, String paramString,
            Bitmap paramBitmap) {
        super.onPageStarted(paramWebView, paramString, paramBitmap);
        loaded=false;
        if(paramString.length() >= url.length() && !paramString.substring(paramString.length()-4, paramString.length()).contentEquals("NULL") && paramString.substring(0, url.length()).contentEquals(url)) {
            webViewPreviousState = PAGE_STARTED;
        } else {
            webViewPreviousState = PAGE_REDIRECTED;
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
}
    