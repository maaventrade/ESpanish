package com.alexmochalov.fragments;

import android.view.View;
import android.widget.EditText;

import com.alexmochalov.dictionary.Pronoun;

public class PronounEdited {

		int mIndex;
		Pronoun mPronoun = null;
		View mLayout = null;
		
		public int getIndex() {
			return mIndex;
		}

		public PronounEdited(Pronoun pronoun, View layout, int index) {
			mPronoun = pronoun;
			mLayout = layout;
			mIndex = index;
		}

		public void swap(PronounEdited p2, int id1, int id2) {
			int index = mIndex;
			Pronoun pronoun = mPronoun;
			View layout = mLayout;
			EditText editText = (EditText) mLayout
					.findViewById(id1);
			String text = editText.getText().toString();

			
			mIndex = p2.mIndex;
			mPronoun = p2.mPronoun;
		
			EditText editText2 = (EditText) p2.mLayout
					.findViewById(id2);
			String text2 = editText2.getText().toString();
			
			editText.setText(text2);

			
			p2.mIndex = index;
			p2.mPronoun = pronoun;
		
			
			editText2.setText(text);
		}

	};
