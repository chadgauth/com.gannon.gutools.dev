package com.gannon.gutools.activities;

import org.holoeverywhere.widget.ProgressBar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;

public class Navigator {
	private WebView wbv;
	private Context ctx;
	private ProgressBar pb;
	private String usr;
	private String pass;
	private int currentSemester;
	private int step=0;
	public Navigator(Context ctx, WebView wbv, ProgressBar pb, String usr, String pass) {
		this.wbv = wbv;
		this.ctx = ctx;
		this.pb = pb;
		this.usr = usr;
		this.pass = pass;
		currentSemester=1;
	}
	public void navigate(){
		switch (step){
		case 1:
			wbv.loadUrl("javascript:document.getElementById('USER_NAME').value=\"" + usr + "\"");
        	wbv.loadUrl("javascript:document.getElementById('CURR_PWD').value=\""+ pass +"\"");
        	wbv.loadUrl("javascript:document.getElementsByClassName('shortButton')[0].click()");
        	pb.setProgress(2);
        	break;
		case 2:
			wbv.loadUrl("javascript:function test(){ var link = document.getElementsByClassName('WBST_Bars')[0].href; window.location.href=link;}");
        	wbv.loadUrl("javascript:test();");
        	pb.setProgress(3);
        	break;
		case 3:
			wbv.loadUrl("javascript:function test(){ var link = document.getElementsByClassName('left')[0].getElementsByTagName('li')[5].getElementsByTagName('a')[0].href; window.location.href=link;}");
        	wbv.loadUrl("javascript:test();");
        	pb.setProgress(4);
        	break;
		case 4:
			wbv.loadUrl("javascript:window.HTMLOUT.processSemester(document.getElementById(\"VAR4\").innerHTML)");
			break;
		case 5:
        	wbv.loadUrl("javascript:document.getElementById('VAR4').selectedIndex=" + Integer.toString(currentSemester));
        	wbv.loadUrl("javascript:document.getElementsByClassName('shortButton')[0].click()");
        	pb.setProgress(5);
        	break;
		case 6:
			wbv.loadUrl("javascript:window.HTMLOUT.processSchedule(document.getElementsByClassName('envisionWindow')[1].childNodes[4].innerHTML);");
		    pb.setProgress(6);
			break;
		case 7:
		  	Intent intent = new Intent(ctx, HomeActivity.class);
	    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    ctx.startActivity(intent);
		    ctx.deleteDatabase("webview.db");
		    ctx.deleteDatabase("webviewCache.db");
		    ((Activity) ctx).finish();
		    break;
		}
		step++;
	}
	public void currentSemester(int s){
		currentSemester = s;
	}
}
