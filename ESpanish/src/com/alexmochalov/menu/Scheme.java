package com.alexmochalov.menu;
import java.util.*;

public class Scheme
{
	String title = "";
	String text = "";
	String[] strings;


	public String getTitle()
	{
		return title;
	}
	public String getText()
	{
		return text.
		replace("<","&lt;").
		replace(">","&gt;");
	}
	
	public String[] getStrings()
	{
		return strings;
	}
	
	}
