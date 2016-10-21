package com.alexmochalov.espanish;

import android.content.*;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.util.*;
import android.widget.*;

import java.util.*;

import org.xmlpull.v1.*;

import com.alex_mochalov.navdraw.R;

import java.io.*;

import android.preference.PreferenceManager;
import android.text.*;

public class MenuData {
	static Context mContext;
	static String LOG_TAG = "";

	// private static int menuGroupPosition;
	// private static int menuChildPosition;

	// меню
	static ArrayList<MenuGroup> menuData;
	static ArrayList<ArrayList<ArrayList<MarkedString>>> textData = new ArrayList<ArrayList<ArrayList<MarkedString>>>();

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
			
			negStr = Dictionary.getTranslation(negStr).translation;
			if (negStr.length() > 0)
				negStr = negStr + " ";
			
			Entry e = Dictionary.getTranslation(pronoun);
				/*	
			if (pronoun.length() > 0)
				pronoun = pronoun + " ";
			verb = Dictionary.getTranslation(verb);
			verb = Dictionary.fit(e, sub, "present");
			
			mRusText = firstLetterToUpperCase(Dictionary.getTranslation(text))+" "
					+ pronoun
					+ negStr
					+ verb + "?";
			*/
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
	}

	static class MenuGroup {
		String title;
		ArrayList<MenuChild> mChilren;

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

			xpp = mContext.getResources().getXml(R.xml.menu);

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

					}
					break; // конец тэга
				case XmlPullParser.END_TAG:
					if (xpp.getName().equals("entry") && mode.equals("Комбинации")){
						ArrayList<MarkedString> textDataItem = 
								textData.get(textData.size() - 1)
									.get(textData.get(
											textData.size() - 1).size() - 1);
						
						String verbs[] = rec.verbs.split(",");
						for (String verb : verbs){
							textDataItem.add(new MarkedString(rec.text, rec.sub, rec.neg, verb.trim()));
						}
						
					}
					// Log.d(LOG_TAG, "END_TAG: name = " + xpp.getName());
					break; // содержимое тэга
				case XmlPullParser.TEXT:
					if (mode.equals("Выражения") || mode.equals("Спряжения")) {

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
		Log.d("", "translation " + translation);
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

	private static XmlPullParser prepareParser() {

		return mContext.getResources().getXml(R.xml.menu);
	}

	public static String getType(int i, int j) {
		return menuData.get(i).mChilren.get(j).type;
	}

	public static String getCountStr(int i, int j) {
		int size = textData.get(i).get(j).size();
		return "" + (size - getRestCount(i, j)) + "/" + size;
	}

	public static boolean isFinished(int i, int j) {

		return getRestCount(i, j) == 0;
	}

	public static int getRestCount(int i, int j) {
		int result = 0;
		for (MarkedString m : textData.get(i).get(j)) {
			if (m.mFlag != 3)
				result++;
		}

		return result;
	}

	public static String getText(int i, int j, int index) {
		return textData.get(i).get(j).get(index).mText;
	}

	public static void setStepCompleted(int i, int j, int index, int typeOfstep) {

		textData.get(i).get(j).get(index).mFlag = textData.get(i).get(j)
				.get(index).mFlag
				| typeOfstep;

	}

	public static int next(int i, int j) {
		int size = textData.get(i).get(j).size();

		int index = (int) (Math.random() * size);
		while (index < size && textData.get(i).get(j).get(index).mFlag == 3)
			index++;

		if (index == size) {
			index = 0;
			while (index < size && textData.get(i).get(j).get(index).mFlag == 3)
				index++;
		}

		Log.d("", "index " + index);
		if (index < textData.get(i).get(j).size())
			return index;
		else {
			return -1;
		}
	}

	public static void resetDataFlag(int i, int j) {
		for (MarkedString m : textData.get(i).get(j)) {
			m.mFlag = 0;
		}
	}

	public static int getTypeOfTheStep(int i, int j, int index) {
		MarkedString data = textData.get(i).get(j).get(index);

		if (data.mFlag == 1)
			return 2; // span->rus
		else if (data.mFlag == 2)
			return 1; // rus->span
		else if (Math.random() > 0.5)
			return 1;
		else
			return 2;

	}

	public static void load(Context context, boolean reread) {
		mContext = context;

		menuData = new ArrayList<MenuGroup>();

		loadData(menuData);

		String marksString = "";

		SharedPreferences prefs;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		marksString = prefs.getString("MARKS", "");

		Log.d("", "LOAD");
		Log.d("", marksString);

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
			Dictionary.getTranslation(text).translation;
		
	}

}
