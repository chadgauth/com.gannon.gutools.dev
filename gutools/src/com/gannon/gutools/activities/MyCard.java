package com.gannon.gutools.activities;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.TextView;

import android.content.Context;
import android.view.View;

import com.fima.cardsui.objects.Card;
import com.gannon.gutools.dev.R;

public class MyCard extends Card {

	public MyCard(String title){
		super(title);
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_ex, null);
		((TextView) view.findViewById(R.id.title)).setText(title);

		
		return view;
	}
	
	public void setDesc(Context context, String text){
		View view = LayoutInflater.from(context).inflate(R.layout.card_ex, null);
		((TextView) view.findViewById(R.id.description)).setText(text);
		
	}

	
	
	
}
