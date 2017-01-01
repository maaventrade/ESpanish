package com.alexmochalov.menu;

import java.util.*;

public class MenuGroup
 {
	String title;
	ArrayList<MenuChild> mChildren;
	int mHelpIndex = 0;

	MenuGroup(String name, ArrayList<MenuChild> children) {
		title = name;
		mChildren = children;
	}

	MenuGroup() {
		mChildren = new ArrayList();
	}

	public ArrayList<MenuChild> getMChildren(){
		return mChildren;
	}
	
	public void setTitle(String t) {
		title = t;
	}


	public String getTitle() {
		return title;
	}
	
	public void setTense(String attributeValue) {
		//Log.d("my","tense "+attributeValue);
		mChildren.get(mChildren.size()-1).tense = attributeValue;
	}

	public void setNeg(String attributeValue) {
		mChildren.get(mChildren.size()-1).neg = Integer.parseInt(attributeValue);
	}

	public void addItem(String childName) {
		mChildren.add(new MenuChild(childName));
	}

	public void setChildType(String type) {
		mChildren.get(mChildren.size() - 1).type = type;

	}

	public void setChildNote(String note) {
		mChildren.get(mChildren.size() - 1).note = note;
	}

	public void setHelpIndex(String attributeValue) {
		mHelpIndex = Integer.parseInt(attributeValue); 
	}

	public void setChildHelpIndex(String attributeValue) {
		mChildren.get(mChildren.size() - 1).mHelpIndex = Integer.parseInt(attributeValue); 
	}
}
