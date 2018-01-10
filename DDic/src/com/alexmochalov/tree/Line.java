package com.alexmochalov.tree;

public class Line{
	
	public Line(String name, String translation) {
		mName = name;
		mTranslation = translation;
	}
	
	public Line(Line line) {
		mName = line.mName;
		mTranslation = line.mTranslation;
	}
	
	public Line(String name) {
		mName = name;
	}
	
	
	private String mName;
	private String mTranslation;

	public String getText()
	{
		return mName;
	}
	
	String getName() {
		return mName;
	}

	String getTranslation() {
		return mTranslation;
	}
	
	public void setName(String name) {
		mName = name;		
	}
	
	public void setTranslation(String translation) {
		mTranslation = translation;		
	}
	
	
}
