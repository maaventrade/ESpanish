package com.alexmochalov.espanish;
import android.app.*;
import android.content.*;
import android.widget.*;
import android.view.*;
import com.alex_mochalov.navdraw.*;

public class FragmentM extends Fragment
{

	int mGroupPosition;
	int mChildPosition;
	Context mContext;
	Fragment thisFragment;
	View rootView;
	
	// Index of the current Phrase
	int index = 0;
	 
	OnTestedListener mCallback;

	public interface OnTestedListener {
		public void onTested(int groupPosition, int childPosition);
	}
	
	
	public void setParams(Context context, int groupPosition, int childPosition) {
		mContext = context;
		mGroupPosition = groupPosition;
		mChildPosition = childPosition;
	}	
	
	void setTested(int typeOfstep){
		
		DrawerMenu.setStepCompleted(mGroupPosition, mChildPosition, index, typeOfstep);
		TextView TextViewPhraseInfo =  (TextView)rootView.findViewById(R.id.TextViewInfo);
		TextViewPhraseInfo.setText(DrawerMenu.getCountStr(mGroupPosition, mChildPosition));
		
		if (mCallback != null)
			mCallback.onTested(mGroupPosition, mChildPosition);

		if (DrawerMenu.getDataSize(mGroupPosition, mChildPosition) == 0){
			Toast.makeText(mContext, "THAT IS ALL", Toast.LENGTH_LONG).show();
			// Close Fragment
			getActivity().getFragmentManager().beginTransaction().remove(thisFragment).commit();
		}
	}
}
