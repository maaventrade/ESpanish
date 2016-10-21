package com.alexmochalov.espanish;

public class Entry {
	static String mText;
	public static String translation;
	static String root;
	static int fit_type;
	
	public void setTranslation(String text) {
		translation = text;
	}

	public void setRoot(String attributeValue) {
		root = attributeValue;
	}

	public void setFitType(String attributeValue) {
		fit_type = Integer.parseInt(attributeValue);
	}
}
