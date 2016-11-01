package com.alexmochalov.espanish;

import android.content.*;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.util.*;
import android.widget.*;

import java.util.*;

import org.xmlpull.v1.*;

import com.alex_mochalov.navdraw.R;
import com.alexmochalov.dictionary.Dictionary;
import com.alexmochalov.dictionary.Entry;

import java.io.*;

import android.preference.PreferenceManager;
import android.text.*;

public class MenuData {
	static Context mContext;
	static String LOG_TAG = "";

	private final static String MENU_GROUP_POSITION = "MENU_GROUP_POSITION";
	private final static String MENU_CHILD_POSITION = "MENU_CHILD_POSITION";
	private final static String DIRECTION = "DIRECTION";
	private final static String MTEXT = "TEXT";
	private final static String MENU_INDEX = "MENU_INDEX";
	
	private static int mGroupPosition;
	private static int mChildPosition;
	private static int mIndex = 0;
	private static String mText;
	private static int direction;
	private static String translation;
	
	// меню
	static ArrayList<MenuGroup> menuData;
	static ArrayList<ArrayList<ArrayList<MarkedString>>> textData = new ArrayList<ArrayList<ArrayList<MarkedString>>>();
	static ArrayList<Scheme> schemes;
	
	private static String language = "ita";
	
	static class MarkedString {
		public MarkedString(String text) {
			mText = text;
			mFlag = 0;
		}
		
		public MarkedString(String text, int sub, boolean neg, String verb) {
			
			String negStr = " ";
			String pronoun = "";
		
			if (neg)
				if (Math.random() > 0.5)
					negStr = " no ";
				
			if (sub == 3) 
				mText = "¿"+text+negStr+ Dictionary.conj(2, verb)+"?";
			else {
				pronoun = "tú";
				mText = "¿"+text+" tú"+ negStr +Dictionary.conj(1, verb)+"?";
			}	
			
			if (mText.contains("Cómo tú llamas")){
				mRusText = "Как тебя зовут?";
				mText = "¿Cómo té llamas?";
			} else {
			
			
			
			//Log.d("",negStr);
			Entry e = Dictionary.getTranslation(negStr);
			if ( e != null)
				negStr = e.getTranslation();
				
			if (negStr.length() > 1)
				negStr = negStr + " ";
			else negStr = "";
			
			e = Dictionary.getTranslation(pronoun);
			if ( e != null)
				pronoun = e.getTranslation().trim();
			
			if (pronoun.length() > 0)
				pronoun = pronoun + " ";
				
			e = Dictionary.getTranslation(verb);			verb = Dictionary.fit(e, sub, "present").trim();
			
			//Log.d("", text);
			//Log.d("", Dictionary.getTranslation(text).translation);
			mRusText = firstLetterToUpperCase(Dictionary.getTranslation(text).getTranslation())+" "
					+ pronoun
					+ negStr
					+ verb + "?";
				}	
			mFlag = 0;
		}
		
		String mText;
		String mRusText = "";

		int mFlag;

		// 001 spa->ru completed
		// 010 ru->spa completed
		// 100 audio completed ?????????

		public void setFlag(String flag) {
			mFlag = Integer.parseInt(flag);
		}
	}

	static class MenuChild {
		public MenuChild(String childName) {
			title = childName;
		}

		String title;
		String type;
		String note;
		int mHelpIndex = -1;
	}

	static class MenuGroup {
		String title;
		ArrayList<MenuChild> mChilren;
		int mHelpIndex = 0;

		MenuGroup(String name, ArrayList<MenuChild> children) {
			title = name;
			mChilren = children;
		}

		MenuGroup() {
			mChilren = new ArrayList();
		}

		public void setName(String name) {
			title = name;
		}

		public void addItem(String childName) {
			mChilren.add(new MenuChild(childName));
		}

		public void setChildType(String type) {
			mChilren.get(mChilren.size() - 1).type = type;

		}

		public void setChildNote(String note) {
			mChilren.get(mChilren.size() - 1).note = note;
		}
	}

	static class Mark {
		private int menuGroupPosition;
		private int menuChildPosition;
		private int index;
		int mFlag;

		public void setGroup(String attributeValue) {
			menuGroupPosition = Integer.parseInt(attributeValue);
		}

		public void setChild(String attributeValue) {
			menuChildPosition = Integer.parseInt(attributeValue);
		}

		public void setIndex(String attributeValue) {
			index = Integer.parseInt(attributeValue);
		}

		public void setValue(String attributeValue) {
			mFlag = Integer.parseInt(attributeValue);
		}
	}

	private static void loadData(ArrayList<MenuGroup> groupData) {

		String mode = "";
		MenuGroup menuGroup = null;

		class Record {
			String text;
			int sub; // subject: 2 - you, 3 this
			boolean neg;
			String verbs;
		}
		Record rec = new Record();

		try {
			XmlPullParser xpp = null;

			if (language.equals("ita")) 
				xpp = mContext.getResources().getXml(R.xml.menu_it);
			else if (language.equals("spa"))
				xpp = mContext.getResources().getXml(R.xml.menu_spa);

			groupData.clear();

			while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
				switch (xpp.getEventType()) { // начало документа
				case XmlPullParser.START_DOCUMENT:
					break; // начало тэга
				case XmlPullParser.START_TAG:
					if (xpp.getName().equals("level0")) {
						textData.add(new ArrayList<ArrayList<MarkedString>>());

						// Добавляем группу в меню
						menuGroup = new MenuGroup();
						groupData.add(menuGroup);

						for (int i = 0; i < xpp.getAttributeCount(); i++) {
							if (xpp.getAttributeName(i).equals("title")) {
								menuGroup.setName(xpp.getAttributeValue(i));
							}
						}

					} else if (xpp.getName().equals("level1")) {
						// Child element

						for (int i = 0; i < xpp.getAttributeCount(); i++) {
							if (xpp.getAttributeName(i).equals("title")) {
								menuGroup.addItem(xpp.getAttributeValue(i));
							} else if (xpp.getAttributeName(i).equals("type")) {
								if (xpp.getAttributeValue(i)
										.equals("Выражения")) {
									mode = "Выражения";
									menuGroup.setChildType(mode);
								} else if (xpp.getAttributeValue(i).equals(
										"Спряжения")) {
									mode = "Спряжения";
									menuGroup.setChildType(mode);
								} else if (xpp.getAttributeValue(i).equals(
										"Комбинации")) {
									mode = "Комбинации";
									menuGroup.setChildType(mode);
								}
							} else if (xpp.getAttributeName(i).equals("note")) {
								menuGroup
										.setChildNote(xpp.getAttributeValue(i));
							}
						}
						// Level1
						textData.get(textData.size() - 1).add(
								new ArrayList<MarkedString>());

					} else if (xpp.getName().equals("entry")) {
						//

						for (int i = 0; i < xpp.getAttributeCount(); i++) {
							if (xpp.getAttributeName(i).equals("text") && mode.equals("Комбинации")) {
								rec.text = xpp
										.getAttributeValue(i);
							} else if (xpp.getAttributeName(i).equals("text")) {
								textData.get(textData.size() - 1)
								.get(textData.get(
										textData.size() - 1).size() - 1)
								.add(new MarkedString(xpp
										.getAttributeValue(i)));
							} else if (xpp.getAttributeName(i).equals("flag")) {

								textData.get(textData.size() - 1)
										.get(textData.get(textData.size() - 1)
												.size() - 1)
										.get(textData
												.get(textData.size() - 1)
												.get(textData.get(
														textData.size() - 1)
														.size() - 1).size() - 1)
										.setFlag(xpp.getAttributeValue(i));
							} else if (xpp.getAttributeName(i).equals("sub")) {
								rec.sub = Integer.parseInt(xpp
										.getAttributeValue(i));
							} else if (xpp.getAttributeName(i).equals("neg")) {
								rec.neg = Boolean.parseBoolean(xpp
										.getAttributeValue(i));
							} else if (xpp.getAttributeName(i).equals("verbs")) {
								rec.verbs = xpp
										.getAttributeValue(i);
							}
						}
					} else if (xpp.getName().equals("schemes")) {
							mode = "schemes";
							schemes = new ArrayList<Scheme>();
					} else if (xpp.getName().equals("scheme")) {
						schemes.add(new Scheme());
						for (int i = 0; i < xpp.getAttributeCount(); i++) {
							if (xpp.getAttributeName(i).equals("title")) 
								schemes.get(schemes.size()-1).title = xpp.getAttributeValue(i);
							else if (xpp.getAttributeName(i).equals("text")) 
								schemes.get(schemes.size()-1).text = xpp.getAttributeValue(i);
						}	
						
					}
					break; // конец тэга
				case XmlPullParser.END_TAG:
					if (xpp.getName().equals("entry") && mode.equals("Комбинации")){
						ArrayList<MarkedString> textDataItem = 
								textData.get(textData.size() - 1)
									.get(textData.get(
											textData.size() - 1).size() - 1);
						
						String verbs[] = rec.verbs.split(",");
						//Log.d("","textDataItem "+textDataItem.toString());
						//Log.d("",""+verbs.toString());
						for (String verb : verbs){
							//Log.d("",rec.toString());
							//Log.d("",verb);
							textDataItem.add(new MarkedString(rec.text, rec.sub, rec.neg, verb.trim()));
						}
						
					}
					// Log.d(LOG_TAG, "END_TAG: name = " + xpp.getName());
					break; // содержимое тэга
				case XmlPullParser.TEXT:
					if (mode.equals("Выражения") || mode.equals("Спряжения")) {

					}else if (mode.equals("schemes")) {
						schemes.get(schemes.size()-1).strings =
								xpp.getText().split("\n");
					}

					break;
				default:
					break;
				} // следующий элемент
				xpp.next();
			}

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String firstLetterToUpperCase(String translation) {
		//Log.d("", "translation " + translation);
		return translation.substring(0,1).toUpperCase() + translation.substring(1);
	}

	private static void loadMarks(ArrayList<Mark> marks, String marksString) {

		try {
			XmlPullParser xpp = null;

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);
			xpp = factory.newPullParser();
			StringReader sw = new StringReader(marksString);
			xpp.setInput(sw);

			while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
				switch (xpp.getEventType()) { // начало документа
				case XmlPullParser.START_DOCUMENT:
					break; // начало тэга
				case XmlPullParser.START_TAG:
					if (xpp.getName().equals("mark")) {
						marks.add(new Mark());

						for (int i = 0; i < xpp.getAttributeCount(); i++) {
							if (xpp.getAttributeName(i).equals("group")) {
								marks.get(marks.size() - 1).setGroup(
										xpp.getAttributeValue(i));
							} else if (xpp.getAttributeName(i).equals("child")) {
								marks.get(marks.size() - 1).setChild(
										xpp.getAttributeValue(i));
							} else if (xpp.getAttributeName(i).equals("index")) {
								marks.get(marks.size() - 1).setIndex(
										xpp.getAttributeValue(i));
							} else if (xpp.getAttributeName(i).equals("value")) {
								marks.get(marks.size() - 1).setValue(
										xpp.getAttributeValue(i));
							}
						}

					}
					break; // конец тэга
				default:
					break;
				} // следующий элемент
				xpp.next();
			}

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String getType(int i, int j) {
		return menuData.get(i).mChilren.get(j).type;
	}

	public static String getCountStr() {
		int size = textData.get(mGroupPosition).get(mChildPosition).size();
		return "" + (size - getRestCount(mGroupPosition, mChildPosition)) + "/" + size;
	}

	public static String getCountStr(int mGroupPosition, int mChildPosition) {
		int size = textData.get(mGroupPosition).get(mChildPosition).size();
		return "" + (size - getRestCount(mGroupPosition, mChildPosition)) + "/" + size;
	}

	public static boolean isFinished(int i, int j) {

		return getRestCount(i, j) == 0;
	}

	public static int getRestCount(int mGroupPosition, int mChildPosition) {
		int result = 0;
		for (MarkedString m : textData.get(mGroupPosition).get(mChildPosition)) {
			if (m.mFlag != 3)
				result++;
		}

		return result;
	}

	public static int getRestCount() {
		int result = 0;
		for (MarkedString m : textData.get(mGroupPosition).get(mChildPosition)) {
			if (m.mFlag != 3)
				result++;
		}

		return result;
	}

	public static String getText() {
		mText = textData.get(mGroupPosition).get(mChildPosition).get(mIndex).mText; 
		return mText;
	}

	public static void setStepCompleted(int typeOfstep) {

		textData.get(mGroupPosition).get(mChildPosition).get(mIndex).mFlag = textData.get(mGroupPosition).get(mChildPosition)
				.get(mIndex).mFlag
				| typeOfstep;

	}

	public static int findIndex(String text) {
		Log.d("",""+mGroupPosition);
		Log.d("",""+mChildPosition);
		if (mGroupPosition >= textData.size()
			|| mChildPosition >= textData.get(mGroupPosition).size())
			return -1;
			
		for (MarkedString m: textData.get(mGroupPosition).get(mChildPosition))
			if (m.mText.equals(text) && m.mFlag < 3){
				
				return textData.get(mGroupPosition).get(mChildPosition).indexOf(m);
			}	
		
		return -1;
	}
	
	public static int next() {
		int size = textData.get(mGroupPosition).get(mChildPosition).size();

		mIndex = (int) (Math.random() * size);
		while (mIndex < size && textData.get(mGroupPosition).get(mChildPosition).get(mIndex).mFlag == 3)
			mIndex++;

		if (mIndex == size) {
			mIndex = 0;
			while (mIndex < size && textData.get(mGroupPosition).get(mChildPosition).get(mIndex).mFlag == 3)
				mIndex++;
		}
Log.d("","mi"+mIndex);
		if (mIndex < textData.get(mGroupPosition).get(mChildPosition).size())
			return mIndex;
		else {
			mIndex = -1;
			return -1;
		}
	}

	public static void resetDataFlag() {
		for (MarkedString m : textData.get(mGroupPosition).get(mChildPosition)) {
			m.mFlag = 0;
		}
	}

	public static void getTypeOfTheStep(TextView textViewText, TextView textViewTranslation) {
		MarkedString data = textData.get(mGroupPosition).get(mChildPosition).get(mIndex);

		if (data.mFlag == 1)
			direction = 1; // span->rus
		else if (data.mFlag == 2)
			direction =  0; // rus->span
		else if (Math.random() > 0.5)
			direction =  0;
		else
			direction =  1;
			
		
		if (direction == 0){
    		mText = getText();
    		translation = MenuData.getTranslation(mText, mGroupPosition, mChildPosition, mIndex);
        } else {
    		translation = getText();
    		mText = MenuData.getTranslation(translation, mGroupPosition, mChildPosition, mIndex);
        }
		
		textViewText.setText(mText);

		textViewTranslation.setText(translation);
		

	}

	public static void load(Context context, boolean reread) {
		mContext = context;

		menuData = new ArrayList<MenuGroup>();

		loadData(menuData);

		String marksString = "";

		SharedPreferences prefs;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		marksString = prefs.getString("MARKS", "");

	//	Log.d("", "LOAD");
		//Log.d("", marksString);

		if (marksString.length() > 0) {
			ArrayList<Mark> marks = new ArrayList<Mark>();
			loadMarks(marks, marksString);

			//for (Mark m : marks) {
			//	textData.get(m.menuGroupPosition).get(m.menuChildPosition)
			//			.get(m.index);
			//}

		}

	}

	public static String getTranslation(String text, int mGroupPosition, int mChildPosition, int index) {
		String rusText = textData.get(mGroupPosition).get(mChildPosition).get(index).mRusText;
		
		if (rusText.length() > 0)
			return rusText;
		else return	
			Dictionary.getTranslation(text).getTranslation();
		
	}

	public static void nextTestIndex() {
		if (mGroupPosition >= textData.size() ||
			mChildPosition >= textData.get(mGroupPosition).size()){
				mIndex = -1;
				return;
			}
			
		mIndex = 0;
		int size = textData.get(mGroupPosition).get(mChildPosition).size();
		if (mIndex < size && mIndex >= 0 &&
				textData.get(mGroupPosition).get(mChildPosition).get(mIndex).mFlag < 3
				) ;
		else mIndex = next();
	}

	public static void setText(TextView mTextViewText, TextView mTranslation) {
		mText = textData.get(mGroupPosition).get(mChildPosition).get(mIndex).mText; 
		Log.d("", "--"+MenuData.mText);
		mTextViewText.setText(firstLetterToUpperCase(MenuData.mText));
		mTranslation.setText(Dictionary.getTranslation(MenuData.mText).getTranslation());
	}

	public static int getGroupPosition() {
		return mGroupPosition;
	}

	public static int getChildPosition() {
		return mChildPosition;
	}

	public static int getDirection() {
		return direction;
	}

	public static void saveParameters(Editor editor) {
		editor.putInt(MENU_GROUP_POSITION, MenuData.mGroupPosition);
		editor.putInt(MENU_CHILD_POSITION, MenuData.mChildPosition);
		editor.putString(MTEXT, MenuData.mText);
		editor.putInt(DIRECTION, MenuData.direction);
		editor.putInt(MENU_INDEX, MenuData.mIndex);
		
		String listString = "";

		listString = listString + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		listString = listString + "<data version = \"1\">\n";
		
		for (int i = 0; i < menuData.size(); i++){
			for (int j = 0; j < menuData.get(i).mChilren.size(); j++){

				for (MarkedString s: textData.get(i).get(j)){
					
						listString = listString + "<mark group = \""+ i 
								+"\" child = \"" + j 
								+"\" index = \"" + textData.get(i).get(j).indexOf(s) 
								+ "\" value = \"" + s.mFlag  
								+ "\"></mark>\n"; 
				}
			}
		}
		
		listString = listString + "</data>";
		editor.putString("MARKS", listString);
		
	}

	public static void loadParameters(SharedPreferences prefs) {
		mGroupPosition = prefs.getInt(MENU_GROUP_POSITION, 0);
		mChildPosition = prefs.getInt(MENU_CHILD_POSITION, 0);
		
		direction = prefs.getInt(DIRECTION, 0);
		String text = prefs.getString(MTEXT, "");
		
		if (text.length() > 0){
			mIndex = MenuData.findIndex(text);
		} else {
	    	MenuData.nextTestIndex();
		}
		
	}

	public static void setPosition(int groupPosition, int childPosition) {
		mGroupPosition = groupPosition;
		mChildPosition = childPosition;
	}

	public static String getLanguage() {
		return language;
	}

	public static String getTranslation() {
		return translation;
	}

	public static int getHelpIndex(){
		if (mGroupPosition >= textData.size() ||
			mChildPosition >= textData.get(mGroupPosition).size()){

			return 0;
		} else if (menuData.get(mGroupPosition).mChilren.get(mChildPosition).mHelpIndex >= 0)
			return menuData.get(mGroupPosition).mChilren.get(mChildPosition).mHelpIndex;
		else return menuData.get(mGroupPosition).mHelpIndex;
	}

}
