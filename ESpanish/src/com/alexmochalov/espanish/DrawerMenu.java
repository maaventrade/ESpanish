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

public class DrawerMenu {
	static Context mContext;
	static ExpListAdapter adapter;
	static String LOG_TAG = "";
	
	private static int menuGroupPosition;
	private static int menuChildPosition;
	
    //  меню
    static ArrayList<MenuGroup> menuData;
    static ArrayList<ArrayList<ArrayList<MarkedString>>> textData = new ArrayList<ArrayList<ArrayList<MarkedString>>>();
     

    static class MarkedString{
    	public MarkedString(String text) {
    		mText = text;
    		mMarked = false;
    		flag = 0;
		}
    	String mText;
    	Boolean mMarked;
    	int flag; 
    	// 001 spa->ru completed  
    	// 010 ru->spa completed  
    	// 100 audio completed ?????????  
    }
    

    static class MenuChild{
		public MenuChild(String childName) {
			title = childName; 
		}
		String title;
		String type;
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
    }
    
public static void fillMenu(Context context, ExpandableListView mDrawerTree){
	mContext = context;
	
	menuData = new ArrayList<MenuGroup>();

	SharedPreferences prefs;
	prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
	
	
	String dataString = prefs.getString("efrwer", "");
//	ArrayList<String> dataArray = new ArrayList<String>(Arrays.asList(listString.split("\n")));
	
	loadData(menuData, dataString);
	
    adapter = new ExpListAdapter(context, menuData);
    
    mDrawerTree.setAdapter(adapter);
    
	Log.d(LOG_TAG, "adapter "+adapter);

}


private static void loadData(ArrayList<MenuGroup> groupData, String dataString)
{
	
	String mode = "";
	MenuGroup menuGroup = null;
	
	try 
	{ 
		XmlPullParser xpp = null;
		
		if (dataString.equals(""))
			xpp = mContext.getResources().getXml(R.xml.menu);
		else {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);
			xpp = factory.newPullParser();
			StringReader sw = new StringReader(dataString); 
			xpp.setInput(sw); 	
	}

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
					if (xpp.getAttributeName(i).equals("proc")){
						
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


public static String getType() {
	return menuData.get(menuGroupPosition).mChilren.get(menuChildPosition).type;
}


public static String getCountStr(int i, int j) {
	int size = textData.get(i).get(j).size();
	return ""+getDataSize(i, j)+"/"+size;
}

public static String getCountStr() {
	int size = textData.get(menuGroupPosition).get(menuChildPosition).size();
	return ""+getDataSize(menuGroupPosition, menuChildPosition)+"/"+size;
}

public static void setPositions(int groupPosition, int childPosition) {
	menuGroupPosition = groupPosition;
	menuChildPosition = childPosition;
}


public static int getGroupPosition() {
	return menuGroupPosition;
}

public static int getChildPosition() {
	return menuChildPosition;
}


public static int getDataSize(int i, int j) {
	int result = 0;
	for (MarkedString m: textData.get(i).get(j)){
		if (!m.mMarked)
			result++;
	}
		
	return result;
}


public static int getDataSize() {
	int result = 0;
	for (MarkedString m: textData.get(menuGroupPosition).get(menuChildPosition)){
		if (!m.mMarked)
			result++;
	}
		
	return result;
}


public static String getText(int index) {
	return textData.get(menuGroupPosition).get(menuChildPosition).get(index).mText;
}


public static void setStepCompleted(int index, int typeOfstep) {
	
	textData.get(menuGroupPosition).get(menuChildPosition).get(index).flag = 
			textData.get(menuGroupPosition).get(menuChildPosition).get(index).flag | typeOfstep;
	
	if (textData.get(menuGroupPosition).get(menuChildPosition).get(index).flag == 3){
		textData.get(menuGroupPosition).get(menuChildPosition).get(index).mMarked = true;
		adapter.notifyDataSetChanged();
	}
}


public static int next() {
	int size = textData.get(menuGroupPosition).get(menuChildPosition).size();
	
	int index = (int)(Math.random() * size);
	while (index < size && textData.get(menuGroupPosition).get(menuChildPosition).get(index).mMarked 
			)
		index++;
	
	if (index == size){
		index = 0;
		while (index < size && textData.get(menuGroupPosition).get(menuChildPosition).get(index).mMarked  
				)
			index++;
	}
	

	return index;
}


public static void resetDataFlag() {
	for (MarkedString m: textData.get(menuGroupPosition).get(menuChildPosition)){
		m.flag = 0;
		m.mMarked = false;
	}
	adapter.notifyDataSetChanged();
}


public static int getTypeOfTheStep(int index) {
	MarkedString data = textData.get(menuGroupPosition).get(menuChildPosition).get(index);
	
	if (data.flag == 1)
		return 2; // span->rus
	else if (data.flag == 2)
		return 1; // rus->span
	else if (Math.random() > 0.5) return 1;
		else return 2;
		
}



}

