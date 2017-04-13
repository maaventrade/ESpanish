package com.alexmochalov.menu;

import android.content.*;
import android.content.SharedPreferences.*;
import android.preference.*;
import android.util.*;
import android.widget.*;

import com.alexmochalov.alang.*;

import java.io.*;
import java.util.*;

import org.xmlpull.v1.*;

import com.alexmochalov.dictionary.Dictionary;
import com.alexmochalov.fragments.PronounEdited;
import com.alexmochalov.main.Utils;
import com.alexmochalov.rules.*;
import com.alexmochalov.io.*;

public class MenuData {
	static Context mContext;
	static String LOG_TAG = "";

	private final static String MENU_GROUP_POSITION = "MENU_GROUP_POSITION";
	private final static String MENU_CHILD_POSITION = "MENU_CHILD_POSITION";
	private final static String DIRECTION = "DIRECTION";
	
	
	
	private static int mGroupPosition;
	private static int mChildPosition;
	private static int mIndex = -1;
	private static String mText = "";
	private static int direction;
	private static String translation;
	
	// меню
	static ArrayList<MenuGroup> menuGroup = new ArrayList<MenuGroup>();
	
	
	private static int[] randomizationOrder;

	public static void addMarkedString(MarkedString markedString)
	{
		//Log.d("my","MarketString "+markedString.getText());
		MenuGroup menuGroupLast = menuGroup.get(menuGroup.size() - 1);
		menuGroupLast.
			menuChild.get(menuGroupLast.menuChild.size()-1).markedStrings.add(markedString);
		return ;
	}

	public static MarkedString addMarkedString(String neg, Pronoun p, String verb)
	{
		MarkedString markedString = new MarkedString(neg, p, verb);
		
		MenuGroup menuGroupLast = menuGroup.get(menuGroup.size() - 1);
		menuGroupLast.
			menuChild.get(menuGroupLast.menuChild.size()-1).markedStrings.add(markedString);
		return markedString;
	}

	public static MarkedString addMarkedString(String text, String subj, String neg, String verb)
	{
		MarkedString markedString = new MarkedString(text,subj, neg, verb);
		MenuGroup menuGroupLast = menuGroup.get(menuGroup.size() - 1);
		menuGroupLast.
			menuChild.get(menuGroupLast.menuChild.size()-1).markedStrings.add(markedString);
		
		return markedString;
	}


	
	public static int getIndex(){
		return mIndex;
	}

	
	public static MenuGroup newMenuGroup()
	{
		MenuGroup menuGroupItem = new MenuGroup();
		menuGroup.add(menuGroupItem);
		return menuGroupItem;
	}

	public static ArrayList<MenuChild> getMenuCildren(int i)
	{
		return menuGroup.get(i).menuChild;
	}

	public static String getChildNote(int groupPosition, int childPosition)
	{
		return menuGroup.get(groupPosition).menuChild.get(childPosition).note;
	}

	public static String getChildTitle(int groupPosition, int childPosition)
	{
		return menuGroup.get(groupPosition).menuChild.get(childPosition).title;
	}

	public static String getGroupTitle(int groupPosition)
	{
		return menuGroup.get(groupPosition).title;
	}

	public static Object getMenuChild(int groupPosition, int childPosition)
	{
		return menuGroup.get(groupPosition).menuChild.get(childPosition);
	}

	public static Object getMenuGroup(int groupPosition)
	{

		return menuGroup.get(groupPosition);
	}

	public static int getChildrenCoun(int groupPosition)
	{
		return menuGroup.get(groupPosition).menuChild.size();
	}

	public static int getGroupsSize()
	{
		return menuGroup.size();
	}

	public static String getTenseLast()
	{
		
		return menuGroup.get(menuGroup.size()-1).
		menuChild.
			get(menuGroup.get(menuGroup.size()-1).
				menuChild.size()-1).tense;
	}
	
	
	public static MarkedString addMarkedString(String text, String rus)
	{
		MarkedString markedString = new MarkedString(text, rus);
		MenuGroup menuGroupLast = menuGroup.get(menuGroup.size() - 1);
		menuGroupLast.
			menuChild.get(menuGroupLast.menuChild.size()-1).markedStrings.add(markedString);
		return markedString;
	}
	
	public static MarkedString addMarkedString(String text)
	{
		MarkedString markedString = new MarkedString(text);
		MenuGroup menuGroupLast = menuGroup.get(menuGroup.size() - 1);
		menuGroupLast.
			menuChild.get(menuGroupLast.menuChild.size()-1).markedStrings.add(markedString);
		return markedString;
	}
	

	public static String getType(int i, int j) {
		return menuGroup.get(i).menuChild.get(j).type;
	}

	public static String getCountStr() {
		int size = menuGroup.get(mGroupPosition).
			menuChild.get(mChildPosition).
			markedStrings.size();
		return "" + (size - getRestCount(mGroupPosition, mChildPosition)) + "/" + size;
	}

	public static ArrayList<MarkedString> getMarkedStrings() {
		return menuGroup.get(mGroupPosition).
			menuChild.get(mChildPosition).
			markedStrings;
	}

	public static ArrayList<MarkedString> getMarkedStrings1(int i, int j) {
		return menuGroup.get(i).
			menuChild.get(j).
			markedStrings;
	}
	
	public static String getCountStr(int mGroupPosition, int mChildPosition) {
		int size = menuGroup.get(mGroupPosition).
			menuChild.get(mChildPosition).
			markedStrings.size();
		
		return "" + (size - getRestCount(mGroupPosition, mChildPosition)) + "/" + size;
	}

	public static boolean isFinished(int i, int j) {

		return getRestCount(i, j) == 0;
	}

	public static int getRestCount(int mGroupPosition, int mChildPosition) {
		int result = 0;
		for (MarkedString m : getMarkedStrings(mGroupPosition, mChildPosition)) {
			if (m.getFlag() != 3)
				result++;
		}

		return result;
	}

	public static ArrayList<MarkedString>  getMarkedStrings(int mGroupPosition, int mChildPosition)
	{
		return menuGroup.get(mGroupPosition).
			menuChild.get(mChildPosition).
			markedStrings;
	}

	public static int getRestCount() {
		int result = 0;
		for (MarkedString m : getMarkedStrings(mGroupPosition, mChildPosition)) {
			if (m.getFlag() != 3)
				result++;
		}

		return result;
	}

	public static String getText() {
		if (mIndex == -1) return "";
		else mText = getMarkedStrings(mGroupPosition, mChildPosition).get(mIndex).mText; 
		
		//for (MarkedString m : MenuData.getMarkedStrings(3, 1) )
		//	Log.d("m","m1 "+m.getText());
		
		
		//Log.d("m","mText "+mText+" "+mGroupPosition+" "+mChildPosition+" "+mIndex);
		return mText;
	}

	
	public static void setStepCompleted(int typeOfstep) {

		getMarkedStrings(mGroupPosition,
		mChildPosition).
		get(mIndex).
			setFlag(getMarkedStrings(mGroupPosition, mChildPosition)
				.get(mIndex).getFlag()
				| typeOfstep);

	}

	public static int findIndex(String text) {
		
		//Log.d("",""+mGroupPosition);
		//Log.d("",""+mChildPosition);
		if (mGroupPosition >= getGroupsSize()
			|| mChildPosition >= getChildrenCoun(mChildPosition))
			return -1;
			
		for (MarkedString m: getMarkedStrings(mGroupPosition, mChildPosition))
			if (m.mText.equals(text) && m.getFlag() < 3){
				
				return getMarkedStrings(mGroupPosition, mChildPosition).indexOf(m);
			}	
		
		return -1;
	}
	
	public static void setIndex(int index) {
		mIndex = index;
	}
	
	public static int nextIndex() {
		int size = getMarkedStrings(mGroupPosition, mChildPosition).size();

		mIndex = (int) (Math.random() * size);
		while (mIndex < size && getMarkedStrings(mGroupPosition, mChildPosition).get(mIndex).getFlag() == 3)
			mIndex++;

		if (mIndex == size) {
			mIndex = 0;
			while (mIndex < size && getMarkedStrings(mGroupPosition, mChildPosition).get(mIndex).getFlag() == 3)
				mIndex++;
		}
//Log.d("i","mi"+mIndex);
		if (mIndex <size)
			return mIndex;
		else {
			mIndex = -1;
			return -1;
		}
	}

	public static void resetDataFlag() {
		for (MarkedString m : getMarkedStrings(mGroupPosition, mChildPosition)) {
			m.setFlag(0);
		}
	}

	public static void getTypeOfTheStep(TextView textViewText, TextView textViewTranslation) {
		MarkedString data = getMarkedStrings(mGroupPosition, mChildPosition) .get(mIndex);
		//					textData.get(mGroupPosition).get(mChildPosition).get(mIndex).mFlag = textData.get(mGroupPosition).get(mChildPosition)
		//		.get(mIndex).mFlag
		//		| typeOfstep;
		
		double random = Math.random();
		//Log.d("", "-> "+random+" data.mFlag "+data.mFlag);
		
		if (data.getFlag() == 1)
			direction = 2; // span->rus
		else if (data.getFlag() == 2)
			direction =  1; // rus->span
		else if (random > 0.5f)
			direction =  2;
		else
			direction =  1;
			
		
		if (direction == 1){
    		mText = getText();
    		translation = MenuData.getTranslation(mText, mGroupPosition, mChildPosition, mIndex);
			
		} else {
    		translation = getText();
    		mText = MenuData.getTranslation(translation, mGroupPosition, mChildPosition, mIndex);
			
		}
		
		
		textViewText.setText(Utils.firstLetterToUpperCase(mText));

		textViewTranslation.setText(Utils.firstLetterToUpperCase(translation));
		

	}

	public static void load(Context context, boolean reRead) {
		mContext = context;
		
		menuGroup.clear();

		FilesIO.loadMenu(context, reRead);

	
	}

	public static String getTranslation(String text, int mGroupPosition, int mChildPosition, int index) {
		String rusText =getMarkedStrings(mGroupPosition, mChildPosition) .get(index).mRusText;
		
		//Log.d("", "rusText "+rusText);
		
		if (rusText.length() > 0)
			return rusText;
		else return	
				Rules.translate(text).getTranslation();
		
	}

	public static void nextTestIndex() {
		if (mGroupPosition >= getGroupsSize() ||
			mChildPosition >= getChildrenCoun(mGroupPosition)){
				mIndex = -1;
				return;
			}
			
		mIndex = 0;
		int size = getMarkedStrings(mGroupPosition, mChildPosition).size();
		if (mIndex < size && mIndex >= 0 &&
			getMarkedStrings(mGroupPosition, mChildPosition).get(mIndex).getFlag() < 3
				) ;
		else mIndex = nextIndex();
	}

	public static void setText(TextView mTextViewText, TextView mTranslation) {
		mText = getMarkedStrings(mGroupPosition, mChildPosition).get(mIndex).mText; 
		
		mTextViewText.setText(Utils.firstLetterToUpperCase(MenuData.mText));
		
		//Entry entry = Dictionary.translate(MenuData.mText);
		String translation = Dictionary.getTranslationOnly(MenuData.mText);
		mTranslation.setText(translation);
		
		
		//if (entry != null)
		//	mTranslation.setText(entry.getTranslation());
	}

	public static void setText(String text) {
		mText = text; 
		//Log.d("", "mText ="+text);
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
		editor.putInt(DIRECTION, MenuData.direction);
		
		FilesIO.saveMenu(mContext);
		
	}

	public static void loadParameters(SharedPreferences prefs) {
		mGroupPosition = prefs.getInt(MENU_GROUP_POSITION, 0);
		mChildPosition = prefs.getInt(MENU_CHILD_POSITION, 0);
		direction = prefs.getInt(DIRECTION, 0);

	
		/*
		
		if (text.length() > 0){
			mIndex = MenuData.findIndex(text);
		} else {
	    	MenuData.nextTestIndex();
		}*/
		
	}

	public static void setPosition(int groupPosition, int childPosition) {
		mGroupPosition = groupPosition;
		mChildPosition = childPosition;
	}

	
	public static String getTranslation() {
		return translation;
	}

	public static int getHelpIndex(){
		if (mGroupPosition >= getGroupsSize() ||
			mChildPosition >= getChildrenCoun(mGroupPosition)){

			return 0;
		} else if (menuGroup.get(mGroupPosition).menuChild.get(mChildPosition).mHelpIndex >= 0)
			return menuGroup.get(mGroupPosition).menuChild.get(mChildPosition).mHelpIndex;
		else return menuGroup.get(mGroupPosition).mHelpIndex;
	}

	public static String getPositionsStr() {
		return ("0"+mGroupPosition).substring(0, 2)+
				("0"+mChildPosition).substring(0, 2)+
				("0"+mIndex).substring(0, 2);
	}

	public static String getTense() {
		
		return menuGroup.get(mGroupPosition).menuChild.get(mChildPosition).tense;
	}

	public static int getNeg() {
		return menuGroup.get(mGroupPosition).menuChild.get(mChildPosition).neg;
	}

	public static int[] getRandomizationOrder() {
		return randomizationOrder;
	}

	public static void putRandomizationOrder(ArrayList<PronounEdited> objects) {
		randomizationOrder = new int[objects.size()];
		for (int i = 0; i < objects.size(); i++)
			randomizationOrder[i] = objects.get(i).getIndex();
	}

	public static void putRandomizationOrder(int[] intArray) {
		if (intArray == null){
			randomizationOrder = new int[1];
			randomizationOrder[0] = 0;
		} else {
			randomizationOrder = new int[intArray.length];
			for (int i = 0; i < intArray.length; i++)
				randomizationOrder[i] = intArray[i];
		}
	}

}
