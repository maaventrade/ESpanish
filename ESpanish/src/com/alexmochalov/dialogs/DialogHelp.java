package com.alexmochalov.dialogs;
import android.app.*;
import android.net.Uri;
import android.os.*;
import android.text.Html;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;

import com.alexmochalov.alang.*;
import com.alexmochalov.fragments.FragmentM.OnTestedListener;

import android.util.*;

public class DialogHelp extends Dialog implements android.view.View.OnClickListener
{
	Activity mContext;
	ImageButton btnSpeak;
	ImageButton btnNext;
	ImageButton btnPrev;
	
	String text = "";
	
	static int mIndex = 0;
	
	public OnDialogSchemeButtonListener mCallback;
	
	public interface OnDialogSchemeButtonListener {
		public void onSpeakButtonPressed(String text);
	}
	
	public DialogHelp(Activity a, int index){
		super(a);
		mContext = a;
		mIndex = index;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_help);
		
		
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
               WindowManager.LayoutParams.MATCH_PARENT);
		reset();
		/*
		btnSpeak = (ImageButton)findViewById(R.id.dialogSchemeImageButtonSpeak);
		btnSpeak.setOnClickListener(this);
		
		btnNext = (ImageButton)findViewById(R.id.dialogSchemeImageButtonNext);
		btnNext.setOnClickListener(this);
		
		btnPrev = (ImageButton)findViewById(R.id.dialogSchemeImageButtonPrev);
		btnPrev.setOnClickListener(this);
		*/
		
	}

	@Override
	public void onClick(View v) {
		/*
		if (v == btnNext){
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
		}
		*/
	}

	private void reset()
	{
		final String baseUrl = "file:///android_res/raw/help.html";
		
		WebView webView = (WebView)findViewById(R.id.webViewHelp);
		webView.loadUrl(baseUrl);

	}
}
