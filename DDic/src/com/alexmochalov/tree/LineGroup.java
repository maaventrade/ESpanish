package com.alexmochalov.tree;

public class LineGroup extends Line
{
	private String mName;

	private int mComplited;
	
	public LineGroup(String name) {
		mName = name;
		
	}

	public void setCompleted(int complited)
	{
		mComplited = complited;
	}

	public int getCompleted()
	{
		return mComplited;
	}
	
	public void setName(String name)
	{
	mName = name;
	}

	public String getName()
	{
		return mName;
	}

	public String getItemsCount() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
