package com.alexmochalov.espanish;

import android.os.Environment;

public class Utils {

	static String PROGRAMM_FOLDER = "mnogoyaz";
	static String EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().getPath();
	static String APP_FOLDER = EXTERNAL_STORAGE_DIRECTORY+"/xolosoft/"+PROGRAMM_FOLDER;
	static String REC_FOLDER = APP_FOLDER+"/rec";
	
	public static String getRecFolder(){
		return REC_FOLDER;
	}

}
