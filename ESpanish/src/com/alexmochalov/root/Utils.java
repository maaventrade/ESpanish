package com.alexmochalov.root;

import android.os.Environment;

public class Utils {

	static String PROGRAMM_FOLDER = "mnogoyaz";
	static String EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().getPath();
	static String APP_FOLDER = EXTERNAL_STORAGE_DIRECTORY+"/xolosoft/"+PROGRAMM_FOLDER;
	static String REC_FOLDER = APP_FOLDER+"/rec";
	
	private static String language = "ita";
	private static String neg = "non";
	
	public static String getRecFolder(){
		return REC_FOLDER;
	}

	public static String firstLetterToUpperCase(String text) {
		return text.substring(0,1).toUpperCase() + text.substring(1);
	}
	
	public static String getLanguage(){
		return language;
	}
	
	public static String getNeg(){
		return neg;
	}
}
