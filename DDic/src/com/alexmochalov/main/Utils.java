package com.alexmochalov.main;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.alexmochalov.ddic.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.view.*;

public final class Utils {
	private static String mInformation = "";
	
	
	private static String mDictionaryName;
	
	private static TextView textViewInformation;
	private static ActionBar actionBar;
	private static Activity activity;

	private static boolean textLoading = false; 
	private static String textLoadingStr = ""; 

	// Gaps
	public static final int XGAP = 5; // left and right 
	public static final int PARAG = 25; // 

	public static final char CHAR_BM =  1;
	public static final char CHAR_TMP_SEL_START = 2;
	public static final char CHAR_TMP_SEL_END = 3;
	public static final char CHAR_SEL_START = 4;
	public static final char CHAR_SEL_END = 5;

	public static final char CHAR_BOLD = 6;
	public static final char CHAR_BOLD_END = 7;

	public static final char  CHAR_ITALIC = 18;
	public static final char  CHAR_ITALIC_END = 19;

	public static final char CHAR_TAB =  9;

	static String PROGRAMM_FOLDER = "xolosoft";
	private static String APP_FOLDER = Environment.getExternalStorageDirectory().getPath()+"/"+PROGRAMM_FOLDER+"/EnRu";
	String RESULTS_FOLDER = "results";
	static String EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().getPath();
	public static String fileName = "";


	private static int internalDictionaryID = 0;
	//private static String internalDictionary = "";

	private static boolean mModified = false;

	private static int firstLine = -1;
	private static int firstLinePixelShift = -1;
	private static int firstPosition = -1;

	public static boolean instant_translation;

	public static String info = "";
	
	public static boolean refreshTranslatiinRemitted = false;

	private static ArrayList<String> alExpressions = new ArrayList<String>();

	public static String firstLetterToUpperCase(String text) {
		if (text == null || text.length() == 0) return "";
		else return text.substring(0,1).toUpperCase() + text.substring(1);
	}
	
	public static void setRefreshTranslatiinRemitted()
	{
		refreshTranslatiinRemitted = true;
	}

	public static void setTitle(MenuItem maSwitch, Activity context)
	{
		maSwitch.setTitle(mDictionaryName.substring(0, mDictionaryName.indexOf(".")));
		//context.invalidateOptionsMenu();
	}
	
	public static boolean isInvertedDic()
	{
		return mDictionaryName.startsWith("ru");
	}

	public static void setDictionaryName(String dictionaryName)
	{
		mDictionaryName = dictionaryName;
		setInternalDictionary();
	}

	public static String getAppDirectory() {
		File file = new File(EXTERNAL_STORAGE_DIRECTORY, PROGRAMM_FOLDER);
		if(!file.exists()){                          
			file.mkdirs();                  
		}
		return file.getAbsolutePath() + "/";
	}

	public static String getAppFolder() {
		File file = new File(APP_FOLDER);
		if(!file.exists()){                          
			file.mkdirs();                  
		}
		return file.getAbsolutePath() + "/";
	}

	public static String extractFileName() {
		if (fileName.length() == 0) return "No text";
		int i = fileName.lastIndexOf("/");
		if (i > 0)
			return fileName.substring(i+1);
		else
			return fileName;
	}

	public static CharSequence getFilePath() {
		if (fileName.length() == 0)
			return "No file opened";
		else
			return fileName;
	}

	public static String getLanguageNoRus() {
		if (mDictionaryName.equals("en_ru.xdxf")){
			return "eng";
		}
		else if (mDictionaryName.equals("sp_eng.xdxf")){
			return  "spa";
		}
		else if (mDictionaryName.equals("it_ru.xdxf")){
			return  "ita";
		}
		else if (mDictionaryName.equals("ru_it.xdxf")){
			return  "ita";
		}
		else if (mDictionaryName.equals("ru_en.xdxf")){
			return  "eng";
		}
		else
			return  "";
	}


	/*
	 public static CharSequence getDictionaryPath() {
	 String name = "";
	 if (internalDictionaryID != 0)
	 name = "Internal: ";
	 if (mDictionary_file_name.length() == 0)
	 return "No dictionary";
	 else
	 return name+mDictionary_file_name;
	 }

	 public static String getIndexPath() {
	 if (mIndex_file_name.length() == 0)
	 return "No index opened";
	 else
	 return mIndex_file_name;
	 }
	 */

	public static void clearInfo() {
		/*
		 textViewInformation.setText("");
		 textViewInformation.setVisibility(View.INVISIBLE);
		 */
	}

	public int findBlank0(char[] chars, int prevPos, int nextPos){
		if (prevPos + nextPos >= chars.length) return nextPos;

		char c = chars[prevPos + nextPos];
		if (c == ',' || c == '.' || c == '?' || c == '!' || c == ';'  || c == ' ' ) return nextPos;

		char c1 = chars[prevPos + nextPos-1];
		if (c1 == ',' || c1 == '.' || c1 == '?' || c1 == '!' || c1 == ';'  || c1 == ' ' ) return nextPos;

		for (int i = prevPos + nextPos-1; i > prevPos+1; i--)
			if (chars[i] == ',' || chars[i] == '.' || chars[i] == '?' || chars[i] == '!' || chars[i] == ';'  || chars[i] == ' ' ){
				return i-prevPos;
			}

		return nextPos;
	}


	public static int findBlank(Paint paint, int width, int start, float x, String S){
		int prevBlank = S.length();
		int index = start;
		while (index < S.length()){
			char c = S.charAt(index);

			if (c == ' '){
				prevBlank = index;
			}
			else if (c < 20){
				if (c == 3)
					paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
				else if (c == 4)
					paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
				index++;
				continue;
			} 
			x = x + paint.measureText(""+c);
			if (x >= width - XGAP- XGAP){
				return prevBlank;
			}

			index++;
		}	
		//Log.d("", "prevBlank "+prevBlank);
		return S.length();
	}

	@SuppressLint("NewApi")
	public static void setActionBar(Activity activity_) {
		actionBar = activity_.getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		//actionBar.setIcon(new ColorDrawable(activity_.getResources().getColor(android.R.color.transparent)));
		//actionBar.setDisplayShowTitleEnabled(false);

		activity = activity_;
	}

	public static void hideActionBar() {
		actionBar.hide();
	}

	public static void setActionbarTitle(String title, String subtitle, boolean displayHome){
		actionBar.setDisplayHomeAsUpEnabled(displayHome);
		actionBar.setDisplayShowHomeEnabled(!displayHome);
		actionBar.setTitle(title);
		actionBar.setSubtitle(subtitle);
	}

	public static void setActionbarSubTitle(String subtitle){
		actionBar.setSubtitle(subtitle);
	}

	public static void setViewInformation() {
		/*
		 Utils.textViewInformation = (TextView)activity.findViewById(R.id.textViewInformation);
		 Utils.textViewInformation.setMovementMethod(new ScrollingMovementMethod());
		 Utils.textViewInformation.setOnLongClickListener(new OnLongClickListener(){
		 @Override
		 public boolean onLongClick(View v) {
		 DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		 @Override
		 public void onClick(DialogInterface dialog, int which) {
		 switch (which){
		 case DialogInterface.BUTTON_POSITIVE:
		 Utils.clearInfo();
		 break;

		 case DialogInterface.BUTTON_NEGATIVE:
		 //No button clicked
		 break;
		 }
		 }
		 };

		 AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		 builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
		 .setNegativeButton("No", dialogClickListener).show();				return false;
		 }});
		 */
	}

	public static void saveViewParams(int int1, int int2) {
		firstLine = int1;
		firstLinePixelShift = int2;
	}

	public static void setInternalDictionary() {
		if (mDictionaryName.equals("en_ru.xdxf")){
			internalDictionaryID = R.raw.en_ru;
		}
		else if (mDictionaryName.equals("sp_eng.xdxf")){
			//mLanguage = "spa";
			//internalDictionaryID = R.raw.span_eng;
		}
		else if (mDictionaryName.equals("it_ru.xdxf")){
			internalDictionaryID = R.raw.it_ru;
		}
		else if (mDictionaryName.equals("ru_it.xdxf")){
			internalDictionaryID = R.raw.ru_it;
		}
		else if (mDictionaryName.equals("ru_en.xdxf")){
			internalDictionaryID = R.raw.ru_en;
		}
		else
			internalDictionaryID = 0;

	}

	public static boolean isInternalDictionary() {
		return internalDictionaryID != 0;
	}

	public static int getInternalDictionaryID() {
		return internalDictionaryID;
	}

	//public static String getInternalDictionary() {
	//	return internalDictionary;
	//}

	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}

	public static void setTextLoading(boolean param){
		textLoading = param;
	}

	public static void setTextLoadingStr(String param){
		textLoadingStr = param;
	}

	public static boolean getTextLoading(){
		return textLoading;
	}

	public static String getTextLoadingStr(){
		return textLoadingStr;
	}


	public static String[] getStringForms(String string){
		if (getLanguageNoRus().equals("ita")){
			String[] strings = {string};
			if (string.endsWith("mi"))
				string = string.substring(0,string.length()-2);

			string = string.replace("ò", "o");

			if (string.endsWith("amo"))
				strings[0] = string.substring(0,string.length()-3) + "are";
			else if (string.endsWith("ate"))
				strings[0] = string.substring(0,string.length()-3) + "are";
			else if (string.endsWith("ano"))
				strings[0] = string.substring(0,string.length()-3) + "are";
			else if (string.endsWith("o"))
				strings[0] = string.substring(0,string.length()-1) + "are";
			else if (string.endsWith("a"))
				strings[0] = string.substring(0,string.length()-1) + "are";
			else if (string.endsWith("e"))
				strings[0] = string.substring(0,string.length()-1) + "are";
			return strings;
		} else {
			String[] strings = {string, string, string};
			if (string.endsWith("ied"))
				strings[1] = string.replace("ied","y");
			else
			if (strings[0].endsWith("d"))
				strings[0] = strings[0].substring(0, string.length()-1);
			else 
			if (string.endsWith("ing") && string.length() > 3)
				strings[2] = string.substring(0, string.length()-3)+"e";
			return strings;
		}



	}

	public static boolean getModified(){
		return mModified;
	}

	public static void setModified(boolean modified){
		mModified = modified;
	}

	public static String getDictionaryName() {
		return mDictionaryName;
	}

	public static String getIndexName() {
		return mDictionaryName.replace(".xdxf", ".index");
	}

	public static String getIndexPath() {
		return APP_FOLDER+"/"+mDictionaryName.replace(".xdxf", ".index");
	}
	
	public static void addInformation(String info){
		mInformation = mInformation + info + "\n";
	}

	
	static class ViewDialog {

	    public void showDialog(Context activity, String msg){
	        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
	        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        //dialog.requestWindowFeature(Window.);
	        dialog.setCancelable(true);
	        dialog.setContentView(R.layout.information);

	        TextView text = (TextView) dialog.findViewById(R.id.tvInformation);
	        text.setText(mInformation);

	        Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
	        btnClose.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                dialog.dismiss();
	            }
	        });

	        dialog.show();

	    }
	}
	
	public static void showInfo(Context mContext) {
		ViewDialog alert = new ViewDialog();
		alert.showDialog(mContext, "Error de conexión al servidor");
		}

	public static void switchDictionary() {
		if (mDictionaryName.equals("en_ru.xdxf")){
			setDictionaryName("ru_en.xdxf");
		}
		else if (mDictionaryName.equals("it_ru.xdxf")){
			setDictionaryName("ru_it.xdxf");
		}
		else if (mDictionaryName.equals("ru_it.xdxf")){
			setDictionaryName("it_ru.xdxf");
		}
		else if (mDictionaryName.equals("ru_en.xdxf")){
			setDictionaryName("en_ru.xdxf");
		}
	}

	public static ArrayList<String> getExpressions() {
		return alExpressions;
	}

	public static void addExpression(String s) {
		if (alExpressions.indexOf(s) < 0)
			alExpressions.add(s);
	}

	public static boolean isEnglish() {
		if ( mDictionaryName.indexOf("en") >= 0 )
			return true;
		else
			return false;
	}
	
	
}

