package com.gannon.gutools.fragments;

import java.io.File;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.fima.cardsui.objects.Card;
import com.fima.cardsui.views.CardUI;
import com.gannon.gutools.activities.MyCard;
import com.gannon.gutools.dev.R;

public class ClassFragment extends Fragment {
	private SQLiteDatabase database;
	private CardUI mCardView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View V = inflater.inflate(R.layout.events);
		mCardView = (CardUI) V.findViewById(R.id.cardsview);
		mCardView.setSwipeable(false);
		Cursor cr = getSchedule();
		cr.moveToFirst();
		Card c;
		// add AndroidViews Cards
		mCardView.addCard(new MyCard(cr.getString(0)));
		for(int i=0; i<cr.getCount(); i++){
			c = new MyCard(cr.getString(2));
			((MyCard) c).setDesc(this.getActivity(), cr.getString(3));
			mCardView.addCard(c);
			cr.moveToNext();
		}
		// add one card, and then add another one to the last stack.
		mCardView.addCard(new MyCard(cr.getString(1)));
		cr.close();
		database.close();
		// draw cards
		mCardView.refresh();
		return V;
	}
	private Cursor getSchedule() {
		 File databaseFile = this.getActivity().getDatabasePath("Schedule.db");
	        databaseFile.mkdirs();
	        databaseFile.delete();
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile, null);
        Cursor cr = database.query("courses", null, null, null, null, null, null);
        return cr;
    }
}