package com.alexmochalov.fragments;

import org.xmlpull.v1.XmlPullParser;

import com.alexmochalov.alang.R;

public class RemEntry {
	private String mText;
	private String mRus;
	
	public RemEntry(String text, String rus) {
		mText = text;
		mRus = rus;
	}

	public int getLayoutResId() {
		return R.layout.fragment_remember_page;
	}

	public String getText() {
		return mText;
	}
	
	public String getRus() {
		return mRus;
	}

}
