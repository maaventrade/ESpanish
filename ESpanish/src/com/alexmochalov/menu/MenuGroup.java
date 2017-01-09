package com.alexmochalov.menu;

import java.util.*;

public class MenuGroup
 {
	String title;
	public ArrayList<MenuChild> menuChild;
	int mHelpIndex = 0;

	MenuGroup(String name, ArrayList<MenuChild> children) {
		title = name;
		menuChild = children;
	}

	MenuGroup() {
		menuChild = new ArrayList();
	}

	public ArrayList<MenuChild> getMChildren(){
		return menuChild;
	}
	
	public void setTitle(String t) {
		title = t;
	}


	public String getTitle() {
		return title;
	}
	
	public void setTense(String attributeValue) {
		//Log.d("my","tense "+attributeValue);
		menuChild.get(menuChild.size()-1).tense = attributeValue;
	}

	public void setNeg(String attributeValue) {
		menuChild.get(menuChild.size()-1).neg = Integer.parseInt(attributeValue);
	}

	public void addItem(String childName) {
		menuChild.add(new MenuChild(childName));
	}

	public void setChildType(String type) {
		menuChild.get(menuChild.size() - 1).type = type;

	}

	public void setChildNote(String note) {
		menuChild.get(menuChild.size() - 1).note = note;
	}

	public void setHelpIndex(String attributeValue) {
		mHelpIndex = Integer.parseInt(attributeValue); 
	}

	public void setChildHelpIndex(String attributeValue) {
		menuChild.get(menuChild.size() - 1).mHelpIndex = Integer.parseInt(attributeValue); 
	}
}
