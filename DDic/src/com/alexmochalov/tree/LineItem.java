package com.alexmochalov.tree;

import com.alexmochalov.main.Utils;

public class LineItem extends Line{

	private String mNameEng;
	private String mNameIt;
	private String mTranslation;
	
	public LineItem(String nameEng, String nameIt, String transl) {
		mNameEng = nameEng;
		mNameIt = nameIt;
		mTranslation = transl;
	}
	
	public LineItem(LineItem line) {
		mNameEng = line.mNameEng;
		mNameIt = line.mNameIt;
		mTranslation = line.mTranslation;
	}
	
	public LineItem(String name) {
		mNameEng = name;
	}

	public void setName1(String name)
	{
		if (Utils.isEnglish())
			mNameEng = name;
		else 
			mNameIt = name;
		
	}
	
	String getNameEng() {
		return mNameEng;
	}
	
	String getNameIt() {
		return mNameIt;
	}

	public String getTranslation() {
		return mTranslation;
	}
	
	public void setNameEng(String nameEng) {
		mNameEng = nameEng;
	}
	
	public void setNameIt(String nameIt) {
		mNameEng = nameIt;
	}
	
	public void setTranslation(String translation) {
		mTranslation = translation;		
	}
	
	
	public String getText() {
		if (Utils.isEnglish())
			return mNameEng;
		else 
			return mNameIt;
	}

	String getName2() {
		if (! Utils.isEnglish())
			return mNameEng;
		else 
			return mNameIt;
	}
	
	
}

 
