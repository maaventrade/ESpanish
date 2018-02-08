package com.alexmochalov.test;

import android.app.*;
import android.os.*;
import android.text.Html;
import android.view.*;
import android.widget.*;

import com.alexmochalov.ddic.R;
import com.alexmochalov.main.TtsUtils;

import android.util.*;
import android.content.*;

public class DialogExample extends Dialog implements android.view.View.OnClickListener
{
	Context mContext;
	ImageButton btnSpeak;
	Button btnOk;

	private String mTitle;
	private String mExample;
	private String mRus;
 

	public DialogExample(Context context, String title, String example, String rus){
		super(context);
		mContext = context;
		
		mTitle = title;
		mExample = example;
		mRus = rus;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_example);

		setTitle(mTitle);
		
		TextView text = (TextView)findViewById(R.id.dialogExampleExample);
		text.setText(mExample);
		
		text = (TextView)findViewById(R.id.dialogExampleRus);
		text.setText(mRus);
		
		btnSpeak = (ImageButton)findViewById(R.id.dialogExampleImageButtonSpeak);
		btnSpeak.setOnClickListener(this);
		
		btnOk = (Button)findViewById(R.id.dialogExampleImageButtonOk);
		btnOk.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v == btnSpeak)
			TtsUtils.speak(mExample);
		else if (v == btnOk){
			cancel();
		}
	}


}
