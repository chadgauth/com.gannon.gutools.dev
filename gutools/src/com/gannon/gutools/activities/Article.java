package com.gannon.gutools.activities;

import java.io.Serializable;
import java.net.URL;

import android.util.Log;

public class Article implements Serializable {
	
	public static String KEY;

	private static final long serialVersionUID = 1L;
	private String guid;
	private String title;
	private String description;
	private String pubDate;
	private String author;
	private URL url;
	private String encodedContent;
	private boolean read;
	private boolean offline;


	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getPubDate() {
		return pubDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setEncodedContent(String encodedContent) {	
		this.encodedContent = encodedContent;
		Log.e("encoded content", encodedContent);
	}

	public String getEncodedContent() {
		return encodedContent;
	}
	
	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isOffline() {
		return offline;
	}

	private String extractCData(String data){
		data = data.trim();
	    if (data.startsWith("\\<!\\[CDATA\\[")) {
	    	data = data.substring(9);
	      int i = data.indexOf("\\]\\]\\>;");
	      if (i == -1) {
	        throw new IllegalStateException(
	            "argument starts with <![CDATA[ but cannot find pairing ]]&gt;");
	      }
	      data = data.substring(0, i);
	    }
	    return data;
	}

}