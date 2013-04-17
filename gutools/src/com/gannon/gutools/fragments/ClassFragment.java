package com.gannon.gutools.fragments;

import org.holoeverywhere.app.Fragment;

import com.gannon.gutools.activities.MyCard;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.gannon.gutools.dev.R;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import org.holoeverywhere.LayoutInflater;

public class ClassFragment extends Fragment {

	private CardUI mCardView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View V = inflater.inflate(R.layout.events);
		mCardView = (CardUI) V.findViewById(R.id.cardsview);
		mCardView.setSwipeable(false);

		// add AndroidViews Cards
		mCardView.addCard(new MyCard("Get the CardsUI view"));
		mCardView.addCardToLastStack(new MyCard("for Android at"));
		MyCard androidViewsCard = new MyCard("www.androidviews.net");
		mCardView.addCardToLastStack(androidViewsCard);

		// add one card, and then add another one to the last stack.
		mCardView.addCard(new MyCard("2 cards"));
		mCardView.addCardToLastStack(new MyCard("2 cards"));


		// create a stack
		CardStack stack = new CardStack();
		stack.setTitle("title test");

		// add 3 cards to stack
		stack.add(new MyCard("3 cards"));
		stack.add(new MyCard("3 cards"));
		stack.add(new MyCard("3 cards"));

		// add stack to cardView
		mCardView.addStack(stack);

		// draw cards
		mCardView.refresh();
		return V;
	}
}