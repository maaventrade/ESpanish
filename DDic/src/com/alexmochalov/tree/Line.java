package com.alexmochalov.tree;

public class Line{

	private String mName1;
	private String mName2;
	private String mTranslation;
	
	public Line(String name, String translation) {
		mName1 = name;
		mTranslation = translation;
	}
	
	public Line(Line line) {
		mName1 = line.mName1;
		mTranslation = line.mTranslation;
	}
	
	public Line(String name) {
		mName1 = name;
	}
	

	public String getText()
	{
		return mName1;
	}
	
	String getName() {
		return mName1;
	}
	
	String getName1() {
		return mName1;
	}
	
	String getName2() {
		return mName2;
	}

	String getTranslation() {
		return mTranslation;
	}
	
	public void setName(String name) {
		mName1 = name;		
	}
	
	public void setName2(String name) {
		mName2 = name;		
	}
	
	public void setTranslation(String translation) {
		mTranslation = translation;		
	}
}



/*
 package com.alexmochalov.tree;

public class Line{

	private String mNameEng;
	private String mNameIt;
	private String mTranslation;
	
	public Line(String nameEng, String nameIt, String translation) {
		mNameEng = nameEng;
		mNameIt = nameIt;
		mTranslation = translation;
	}
	
	public Line(Line line) {
		mNameEng = line.mNameEng;
		mNameIt = line.mNameIt;
		mTranslation = line.mTranslation;
	}
	
	//public Line(String name) {
	//	mName = name;
	//}
	

	String getNameEng() {
		return mNameEng;
	}
	
	String getNameIt() {
		return mNameIt;
	}

	String getTranslation() {
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
	
	
}

 */