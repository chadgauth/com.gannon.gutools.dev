package com.gannon.gutools.fragments;

import java.util.List;

import com.gannon.gutools.activities.Article;
import com.gannon.gutools.dev.R;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ArticleListAdapter extends ArrayAdapter<Article> {

	public ArticleListAdapter(Activity activity, List<Article> articles) {
		super(activity, 0, articles);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Activity activity = (Activity) getContext();
		LayoutInflater inflater = activity.getLayoutInflater();

		View rowView = inflater.inflate(R.layout.news, null);
		Article article = getItem(position);
		

		TextView textView = (TextView) rowView.findViewById(R.id.article_title_text);
		textView.setText(article.getTitle());
		
		TextView dateView = (TextView) rowView.findViewById(R.id.article_listing_smallprint);
		String pubDate = article.getPubDate();
		//SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss Z", Locale.ENGLISH);
		//Date pDate;
		//	pDate = (Date) df.parse(pubDate);
			pubDate = "published " + "by " + article.getAuthor();
		//pubDate = article.getGuid();
			//+ DateUtils.getDateDifference(pDate) +
		dateView.setText(pubDate);
		
		if (!article.isRead()){
			LinearLayout row = (LinearLayout) rowView.findViewById(R.id.article_row_layout);
			row.setBackgroundColor(Color.WHITE);
			textView.setTypeface(Typeface.DEFAULT_BOLD);
		}
		return rowView;

	} 
}