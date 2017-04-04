package com.alexmochalov.fragments;
import android.app.*;
import android.content.*;
import android.content.SharedPreferences.Editor;
import android.widget.*;
import android.preference.PreferenceManager;
import android.view.*;
   
import com.alexmochalov.alang.*;
import com.alexmochalov.menu.MenuData;
import com.alexmochalov.main.MainActivity;

public class FragmentM extends Fragment
{
	
	
	
	TextView mTextViewText;
	TextView mTranslation;
	
	
	Context mContext;
	Fragment thisFragment;
	View rootView;
	
	
	// Index of the current Phrase
	 
	public OnTestedListener mCallback;

	public String getTextToTTS()
	{
		return null;
	}

	public interface OnTestedListener {
		public void onTested();
		public void onFinished(Fragment thisFragment);
		public void onButtonStartTestingClick();
	}
	
	public void setParams(Context context) {
		mContext = context;
	}
	
	void setTested(int direction){
		
		MenuData.setStepCompleted(direction);
		TextView TextViewPhraseInfo =  (TextView)rootView.findViewById(R.id.TextViewInfo);
		TextViewPhraseInfo.setText(MenuData.getCountStr());
		
		if (mCallback != null)
			mCallback.onTested();

		if (MenuData.getRestCount() == 0){
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
		
		TextView TextViewPhraseInfo =  (TextView)rootView.findViewById(R.id.TextViewInfo);
		TextViewPhraseInfo.setText(MenuData.getCountStr());
	}

	@Override
	public void onDestroy() {
		super.onDestroy(); 
	}

	public String getWord() {
		return "";
	}


}
