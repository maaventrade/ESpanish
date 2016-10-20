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
	
	//private static int menuGroupPosition;
	//private static int menuChildPosition;
	
    //  меню
    static ArrayList<MenuGroup> menuData;
    static ArrayList<ArrayList<ArrayList<MarkedString>>> textData = new ArrayList<ArrayList<ArrayList<MarkedString>>>();

	
     

    static class MarkedString{
    	public MarkedString(String text) {
    		mText = text;
    		mFlag = 0;
		}
    	String mText;
    	
    	int mFlag; 
    	// 001 spa->ru completed  
    	// 010 ru->spa completed  
    	// 100 audio completed ?????????  
    	
		public void setFlag(String flag) {
			mFlag = Integer.parseInt(flag); 
		}
    }
    

    static class MenuChild{
		public MenuChild(String childName) {
			title = childName; 
		}
		String title;
		String type;
		String note;
    }

    static class MenuGroup{
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
			mChilren.add( new MenuChild(childName));
		}

		public void setChildType(String type) {
			mChilren.get(mChilren.size()-1).type = type;
			
		}

		public void setChildNote(String note) {
			mChilren.get(mChilren.size()-1).note = note;
		}
    }
    
private static void loadData(ArrayList<MenuGroup> groupData, String dataString)
{
	
	String mode = "";
	MenuGroup menuGroup = null;
	
	try 
	{ 
		XmlPullParser xpp = null;
		
		if (!dataString.equals("")){
			xpp = mContext.getResources().getXml(R.xml.menu);
			Log.d("","LOAD R");
		}else {
			Log.d("","LOAD Data string");
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);
			xpp = factory.newPullParser();
			StringReader sw = new StringReader(dataString); 
			xpp.setInput(sw); 	
		}

	groupData.clear();
		
	while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
		switch (xpp.getEventType()) { // начало документа 
		case XmlPullParser.START_DOCUMENT: 
			break; // начало тэга 
		case XmlPullParser.START_TAG:
			if (xpp.getName().equals("level0")){
				textData.add(new ArrayList<ArrayList<MarkedString>>());
				
				// Добавляем группу в меню
				menuGroup = new MenuGroup();
				groupData.add(menuGroup);
				
				for (int i = 0; i < xpp.getAttributeCount(); i++) { 
					if (xpp.getAttributeName(i).equals("title")){
						menuGroup.setName(xpp.getAttributeValue(i));
					} 
				}
				 
			} else	if (xpp.getName().equals("level1")){
				// Child element
				
				for (int i = 0; i < xpp.getAttributeCount(); i++) { 
					if (xpp.getAttributeName(i).equals("title")){
						menuGroup.addItem(xpp.getAttributeValue(i));
					} else
					if (xpp.getAttributeName(i).equals("type")){
						if (xpp.getAttributeValue(i).equals("Выражения")){
							mode = "Выражения";
							menuGroup.setChildType(mode);
						}
						else if (xpp.getAttributeValue(i).equals("Спряжения")){
							mode = "Спряжения";
							menuGroup.setChildType(mode);
						}	
					} else
					if (xpp.getAttributeName(i).equals("note")){
							menuGroup.setChildNote(xpp.getAttributeValue(i));
					}
				}
				// Level1
				textData.get(textData.size()-1).add(new ArrayList<MarkedString>());
				
			} else	if (xpp.getName().equals("entry")){
				// 
				
				for (int i = 0; i < xpp.getAttributeCount(); i++) { 
					if (xpp.getAttributeName(i).equals("text")){
						textData.get(textData.size()-1).get(
								textData.get(textData.size()-1).size()-1
									).add(new MarkedString(xpp.getAttributeValue(i)));
					} else
					if (xpp.getAttributeName(i).equals("flag")){
					
						textData.get(textData.size()-1).get(
								textData.get(textData.size()-1).size()-1
									).get(textData.get(textData.size()-1).get(
											textData.get(textData.size()-1).size()-1
											).size()-1).setFlag(xpp.getAttributeValue(i));
					}
				}

			}
			break; // конец тэга 
		case XmlPullParser.END_TAG: 
			//if (xpp.getName().equals("level0")){
			//	childData.add(childDataItem);
			//}
			//Log.d(LOG_TAG, "END_TAG: name = " + xpp.getName());
			break; // содержимое тэга
		case XmlPullParser.TEXT: 
			if (mode.equals("Выражения") || mode.equals("Спряжения")){
				
			}
			
			break;
	default: break;
	} // следующий элемент 
	xpp.next(); 
	} 

	} catch (XmlPullParserException e) { e.printStackTrace(); }
	catch (IOException e) { e.printStackTrace(); 
	} 
	
}

private static XmlPullParser prepareParser(){

	return mContext.getResources().getXml(R.xml.menu);
}

public static String getType(int i, int j) {
	return menuData.get(i).mChilren.get(j).type;
}


public static String getCountStr(int i, int j) {
	int size = textData.get(i).get(j).size();
	return ""+(size-getRestCount(i, j))+"/"+size;
}

	public static boolean isFinished(int i, int j)
	{

		return getRestCount(i, j) == 0;
	}


public static int getRestCount(int i, int j) {
	int result = 0;
	for (MarkedString m: textData.get(i).get(j)){
		if (m.mFlag != 3)
			result++;
	}
		
	return result;
}


public static String getText(int i, int j, int index) {
	return textData.get(i).get(j).get(index).mText;
}


public static void setStepCompleted(int i, int j,int index, int typeOfstep) {
	
	textData.get(i).get(j).get(index).mFlag = 
			textData.get(i).get(j).get(index).mFlag | typeOfstep;
	
	
}


public static int next(int i, int j) {
	int size = textData.get(i).get(j).size();
	
	int index = (int)(Math.random() * size);
	while (index < size && textData.get(i).get(j).get(index).mFlag == 3
			)
		index++;
	
	if (index == size){
		index = 0;
		while (index < size && textData.get(i).get(j).get(index).mFlag == 3
				)
			index++;
	}
	
	Log.d("", "index "+index);
	if (index < textData.get(i).get(j).size())
		return index;
	else {
		return -1;
	}
}

public static void resetDataFlag(int i, int j) {
	for (MarkedString m: textData.get(i).get(j)){
		m.mFlag = 0;
	}
}

public static int getTypeOfTheStep(int i, int j, int index) {
	MarkedString data = textData.get(i).get(j).get(index);
	
	if (data.mFlag == 1)
		return 2; // span->rus
	else if (data.mFlag == 2)
		return 1; // rus->span
	else if (Math.random() > 0.5) return 1;
		else return 2;
		
}


public static void load(Context context, boolean reread) {
	mContext = context;
	
	menuData = new ArrayList<MenuGroup>();

	String dataString = "";
	if (!reread){
		SharedPreferences prefs;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		dataString = prefs.getString("efrwer", "");
		Log.d("", "LOAD");
		Log.d("", dataString);
	}
	
	loadData(menuData, dataString);
}



}

