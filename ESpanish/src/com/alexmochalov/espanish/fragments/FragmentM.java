package com.alexmochalov.espanish.fragments;
import android.app.*;
import android.content.*;
import android.widget.*;
import android.view.*;

import com.alex_mochalov.navdraw.*;
import com.alexmochalov.espanish.MenuData;

public class FragmentM extends Fragment
{

	int mGroupPosition;
	int mChildPosition;
	Context mContext;
	Fragment thisFragment;
	View rootView;
	
	// Index of the current Phrase
	int index = 0;
	 
	public OnTestedListener mCallback;

	public interface OnTestedListener {
		public void onTested(int groupPosition, int childPosition);
		public void onFinished(Fragment thisFragment);
	}
	
	
	public void setParams(Context context, int groupPosition, int childPosition) {
		mContext = context;
		mGroupPosition = groupPosition;
		mChildPosition = childPosition;
	}	
	
	void setTested(int typeOfstep){
		
		MenuData.setStepCompleted(mGroupPosition, mChildPosition, index, typeOfstep);
		TextView TextViewPhraseInfo =  (TextView)rootView.findViewById(R.id.TextViewInfo);
		TextViewPhraseInfo.setText(MenuData.getCountStr(mGroupPosition, mChildPosition));
		
		if (mCallback != null)
			mCallback.onTested(mGroupPosition, mChildPosition);

		if (MenuData.getDataSize(mGroupPosition, mChildPosition) == 0){
			Button button_test = (Button)rootView.findViewById(R.id.button_test);
			button_test.setEnabled(false);
			
			if (mCallback != null)
				mCallback.onFinished(thisFragment);
		}
	}
	
	
	void init(){
		thisFragment = this;
		mContext = this.getActivity();

		TextView TextViewPhraseInfo =  (TextView)rootView.findViewById(R.id.TextViewInfo);
		TextViewPhraseInfo.setText(MenuData.getCountStr(mGroupPosition, mChildPosition));
	}
}
