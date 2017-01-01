package com.alexmochalov.menu;

public class MenuChild {
	public MenuChild(String childName) {
		title = childName;
	}

	public String getTense()
	{
		return tense;
	}

	public String getTitleStr()
	{
		return title.
			replace("<", "&lt;").
			replace(">", "&gt;");
	}

	public int getHelpIndex()
	{
		return mHelpIndex;
	}


	public void setHelpIndex(String attributeValue) {
		mHelpIndex = Integer.parseInt(attributeValue); 
	}

	String title;
	String type;
	String note = "";
	int mHelpIndex = -1;
	String tense = "";
	int neg = 0; // 0 нет, 1 половина, 2 всегда
	
	public String getTitle() {
		return title; 
	}
	
	public String getType() {
		return type; 
	}
	
	public String getNote() {
		return note; 
	}
	
	public String getNeg() {
		return ""+neg;
	}
}
