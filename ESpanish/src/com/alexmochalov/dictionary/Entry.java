package com.alexmochalov.dictionary;

public class Entry {
	String mText;
	String translation;
	String root;
	int fit_type;
	
	public void setTranslation(String text) {
		translation = text;
	}

	public void setRoot(String attributeValue) {
		root = attributeValue;
	}

	public void setFitType(String attributeValue) {
		fit_type = Integer.parseInt(attributeValue);
	}
	
	public String getTranslation(){
		return translation;
	}
	
	public String getText(){
		return mText;
	}
}
