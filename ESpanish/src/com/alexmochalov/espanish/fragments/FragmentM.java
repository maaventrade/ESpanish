package com.alexmochalov.espanish.fragments;
import android.app.*;
import android.content.*;
import android.content.SharedPreferences.Editor;
import android.widget.*;
import android.preference.PreferenceManager;
import android.view.*;

import com.alex_mochalov.navdraw.*;
import com.alexmochalov.espanish.MainActivity;
import com.alexmochalov.espanish.MenuData;

public class FragmentM extends Fragment
{
	private final String MENU_GROUP_POSITION = "MENU_GROUP_POSITION";
	private final String MENU_CHILD_POSITION = "MENU_CHILD_POSITION";
	private final String DIRECTION = "DIRECTION";
	private final String MTEXT = "TEXT";
	
	private final String MENU_INDEX = "MENU_INDEX";

	String mText;
	String translation;
	int direction;
	
	TextView mTextViewText;
	TextView mTranslation;
	
	int mGroupPosition;
	int mChildPosition;
	int mIndex = 0;
	
	Context mContext;
	Fragment thisFragment;
	View rootView;
	
	// Index of the current Phrase
	 
	public OnTestedListener mCallback;

	public String getTextR()
	{
		return null;
	}

	public interface OnTestedListener {
		public void onTested(int groupPosition, int childPosition);
		public void onFinished(Fragment thisFragment);
	}
	
	public void setParams(Context context, int groupPosition, int childPosition, int index) {
		mContext = context;
		mGroupPosition = groupPosition;
		mChildPosition = childPosition;
		mIndex = index;
		
    	mIndex = MenuData.nextTestIndex(mGroupPosition, mChildPosition, mIndex);
	}
	
	void setTested(int direction){
		
		MenuData.setStepCompleted(mGroupPosition, mChildPosition, mIndex, direction);
		TextView TextViewPhraseInfo =  (TextView)rootView.findViewById(R.id.TextViewInfo);
		TextViewPhraseInfo.setText(MenuData.getCountStr(mGroupPosition, mChildPosition));
		
		if (mCallback != null)
			mCallback.onTested(mGroupPosition, mChildPosition);

		if (MenuData.getRestCount(mGroupPosition, mChildPosition) == 0){
			Button button_test = (Button)rootView.findViewById(R.id.button_test);
			button_test.setEnabled(false);
			
			if (mCallback != null)
				mCallback.onFinished(thisFragment);
		}
	}
	
	
	void init(){
		thisFragment = this;
		mContext = this.getActivity();

		SharedPreferences prefs;
		prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		
		mGroupPosition = prefs.getInt(MENU_GROUP_POSITION, 0);
		mChildPosition = prefs.getInt(MENU_CHILD_POSITION, 0);
		
		direction = prefs.getInt(DIRECTION, 0);
		String text = prefs.getString(MTEXT, "");
		
		if (text.length() > 0){
	    	mIndex = MenuData.findIndex(mGroupPosition, mChildPosition, text);
		} else {
			mIndex = 0;
	    	mIndex = MenuData.nextTestIndex(mGroupPosition, mChildPosition, mIndex);
		}
		
		TextView TextViewPhraseInfo =  (TextView)rootView.findViewById(R.id.TextViewInfo);
		TextViewPhraseInfo.setText(MenuData.getCountStr(mGroupPosition, mChildPosition));
	}


	public void saveParams(Context context) {
		mContext = context;
		
		SharedPreferences prefs;
		prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor = prefs.edit();
		
		editor.putInt(MENU_GROUP_POSITION, mGroupPosition);
		editor.putInt(MENU_CHILD_POSITION, mChildPosition);
		editor.putString(MTEXT, mText);
		editor.putInt(DIRECTION, direction);
		
		editor.putInt(MENU_INDEX, mIndex);

		editor.putInt(MENU_CHILD_POSITION, mChildPosition);
		
		editor.commit();
	}
}
