package com.gannon.gutools.activities;

import org.holoeverywhere.widget.ProgressBar;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;

public class Navigator {
	private WebView wbv;
	private Context ctx;
	private ProgressBar pb;
	private String usr;
	private String pass;
	private int step=0;
	public Navigator(Context ctx, WebView wbv, ProgressBar pb, String usr, String pass) {
		// TODO Auto-generated constructor stub
		this.wbv = wbv;
		this.ctx = ctx;
		this.pb = pb;
		this.usr = usr;
		this.pass = pass;
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
        	wbv.loadUrl("javascript:document.getElementById('VAR4').selectedIndex=1");
        	wbv.loadUrl("javascript:document.getElementsByClassName('shortButton')[0].click()");
        	pb.setIndeterminate(true);
        	break;
		case 5:
			wbv.loadUrl("javascript:window.HTMLOUT.processSchedule(document.getElementsByClassName('envisionWindow')[1].childNodes[4].innerHTML);");
		    break;
		case 6:
		  	Intent intent = new Intent(ctx, HomeActivity.class);
	    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    ctx.startActivity(intent);
		    break;
		}
		step++;
	}
}
