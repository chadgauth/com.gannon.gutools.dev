package com.gannon.gutools.activities;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.TextView;

import android.content.Context;
import android.view.View;

import com.fima.cardsui.objects.Card;
import com.gannon.gutools.dev.R;

public class MyCard extends Card {
	private String desc;
	public MyCard(String title){
		super(title);
		desc="No data";
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_ex, null);
		((TextView) view.findViewById(R.id.title)).setText(title);
		((TextView) view.findViewById(R.id.description)).setText(desc);
		
		return view;
	}
	
	public void setDesc(String text){
		desc = text;
	}

	
	
	
}
