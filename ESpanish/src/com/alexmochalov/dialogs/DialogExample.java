package com.alexmochalov.dialogs;

import android.app.*;
import android.os.*;
import android.text.Html;
import android.view.*;
import android.widget.*;

import com.alexmochalov.alang.*;
import com.alexmochalov.fragments.FragmentM.OnTestedListener;

import android.util.*;
import android.content.*;

public class DialogExample extends Dialog implements android.view.View.OnClickListener
{
	Context mContext;
	ImageButton btnSpeak;
	ImageButton btnOk;

	private String mExample;

	private String mRus;


	public DialogExample(Context context, String example, String rus){
		super(context);
		mContext = context;
		
		mExample = example;
		mRus = rus;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_example);

		TextView text = (TextView)findViewById(R.id.dialogExampleExample);
		text.setText(mExample);
		
		text = (TextView)findViewById(R.id.dialogExampleRus);
		text.setText(mRus);
		
		btnSpeak = (ImageButton)findViewById(R.id.dialogSchemeImageButtonSpeak);
		btnSpeak.setOnClickListener(this);


	}

	@Override
	public void onClick(View v) {
		/*if (v == btnNext){
			mIndex++;
			if (mIndex >= MenuData.schemes.size())
				mIndex = 0;
			reset();
		}
		else if (v == btnPrev){
			mIndex--;
			if (mIndex < 0)
				mIndex = MenuData.schemes.size()-1;
			reset();
		}
		else if (v == btnSpeak){
			if (mCallback != null)
				mCallback.onSpeakButtonPressed(text);
		}*/
	}


}
