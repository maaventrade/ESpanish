package com.alexmochalov.tree;

public class Header{
	public Header(String name) {
		mName = name;
	}
	private String mName;
	
	String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;		
	}

}
