package com.alexmochalov.root;

import android.content.*;
import android.os.*;
import android.util.*;

import java.io.*;

public class Utils {

	static final String PROGRAMM_FOLDER = "mnogoyaz";
	static final String EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().getPath();
	public final static String APP_FOLDER = EXTERNAL_STORAGE_DIRECTORY+"/xolosoft/"+PROGRAMM_FOLDER;
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

	public static boolean resetFile(){
		/*try {
			File file = new File(APP_FOLDER+"/params.txt");
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
			
			outputStreamWriter.write("");
			outputStreamWriter.close();
		}
		catch (IOException e) {
			Log.e("Exception", "File write failed: " + e.toString());
			return false;
		} */
		return true;
	}
	
	public static boolean writeFile(String name, String text){
		/*try {
			File file = new File(APP_FOLDER+"/params.txt");
			FileOutputStream fos = new FileOutputStream(file, true);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
			
			outputStreamWriter.write(name + " "+ text.replace("\n", ";")+"\n");
			outputStreamWriter.close();
		}
		catch (IOException e) {
			Log.e("Exception", "File write failed: " + e.toString());
			return false;
		} */
		return true;
	}

	public static boolean readBoolean(String name, boolean def) {
		try {
			File file = new File(APP_FOLDER+"/params.txt");
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fis);
			
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = "";
				while ( (str = bufferedReader.readLine()) != null ) {
					Log.d("", "-->"+str);
					if (str.startsWith(name)){
						inputStreamReader.close();
						return Boolean.parseBoolean(str.substring(name.length()+1));
					}
				}
            inputStreamReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return def;
	}

	public static int readInt(String name, int def) {
		try {
			File file = new File(APP_FOLDER+"/params.txt");
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fis);
			
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = "";

				while ( (str = bufferedReader.readLine()) != null ) {
					Log.d("", "--->"+str);
					if (str.startsWith(name)){
						inputStreamReader.close();
						return Integer.parseInt(str.substring(name.length()+1));
					}
				}
            inputStreamReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return def;
	}

	public static String readString(String name, String def) {
		try {
			File file = new File(APP_FOLDER+"/params.txt");
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fis);
			
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = "";

				while ( (str = bufferedReader.readLine()) != null ) {
					Log.d("", "---->"+str);
					if (str.startsWith(name)){
						inputStreamReader.close();
						return str.substring(name.length()+1).replace(";", "\n");
					}
				}
            inputStreamReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return def;
	}

}
