package com.alexmochalov.main;

import android.app.ActionBar;
import android.content.*;
import android.os.*;
import android.util.*;
import android.webkit.WebView;

import java.io.*;
import java.util.ArrayList;

import com.alexmochalov.alang.R;
import com.alexmochalov.fragments.PronounEdited;
import com.alexmochalov.rules.Pronoun;

public class Utils {

	static final String PROGRAMM_FOLDER = "mnogoyaz";
	static final String EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().getPath();
	public final static String APP_FOLDER = EXTERNAL_STORAGE_DIRECTORY+"/xolosoft/"+PROGRAMM_FOLDER;
	static String REC_FOLDER = APP_FOLDER+"/rec";
	
	private static String language = "fra";
	
	private static boolean randomize = false;
	private static int scale = 110;
	public static int recDuration = 5000;
	
	public static String getRecFolder(){
		return REC_FOLDER;
	}

	public static String firstLetterToUpperCase(String text) {
		if (text == null || text.length() == 0) return "";
		else return text.substring(0,1).toUpperCase() + text.substring(1);
	}
	
	public static String getLanguage(){
		return language;
	}
	
	public static void incScale() {
		scale = scale + 10;
	}

	public static void decScale() {
		scale = scale - 10;
	}

	public static int getScale() {
		return scale;
	}

	public static void setRandomize(boolean boolean1) {
		randomize = boolean1;
	}

	public static void setScale(int int1) {
		scale = int1;
	}

	public static boolean getRandomize() {
		return randomize;
	}
	
	// je aimer -> j'aimer
	public static String setPronounsToVowel(String pronoun, String text) {

		if ( pronoun.equals("JE") && Utils.isVowel(text.substring(0 ,1)))
				return "J'";
			
		return pronoun;
		
	}


	
/*
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
					//Log.d("", "--->"+str);
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
					//Log.d("", "---->"+str);
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
*/
	
	public static void loadHTML(String word, String translation, WebView webView) {
		if (! word.equals(""))
			translation = "<b>" + Utils.firstLetterToUpperCase(word) + "</b>"
				+ translation;

		translation = translation.replace("\n", "<br>");
		translation = translation.replace("<abr>", "<font color = #00aa00>");
		translation = translation.replace("</abr>", "</font>");

		translation = translation.replace("<ex>", "<font color = #aa7777>");
		translation = translation.replace("</ex>", "</font>");

		int start = translation.indexOf("<kref>");
		int end = translation.indexOf("</kref>");
		while (start >= 0 && end >= 0) {
			String text = translation.substring(start + 6, end);

			translation = translation.substring(0, start) + "<a href =\""
					+ "http://" + text + "\">" + text + "</a>"
					+ translation.substring(end + 7);

			start = translation.indexOf("<kref>");
			end = translation.indexOf("</kref>");
		}

		webView.loadData(translation, "text/html; charset=utf-8", "UTF-8");
	}

	public static void setLanguage(String l, ActionBar actionBar) {
		language = l;
		
		if (language.equals("ita"))
			actionBar.setIcon(R.drawable.ic_launcher_ita);
		else if (language.equals("spa"))
			actionBar.setIcon(R.drawable.ic_launcher_spa);
		else if (language.equals("fra"))
			actionBar.setIcon(R.drawable.ic_launcher_fra);

	}

	public static boolean isVowel(CharSequence c){
	    String vowels = "aeiouéAEIOUÉ";
	    return vowels.contains(c);
	}

	public static String getFileName(String fileName) {

		if (fileName.lastIndexOf("/") >= 0)
			return fileName.substring( fileName.lastIndexOf("/")+1 );
		else
			return fileName;
		
	}
	public static ArrayList<Pronoun> toPronoun(ArrayList<PronounEdited> objects) {
		
		ArrayList<Pronoun> result = new ArrayList<Pronoun>();
		
		for (PronounEdited p: objects)
			result.add(p.getPronoun());
		
		return result;
	}	
}
