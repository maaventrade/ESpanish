package com.alexmochalov.dialogs;
import android.app.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;

import com.alexmochalov.alang.*;
import com.alexmochalov.files.Dic;
import com.alexmochalov.files.IndexEntry;
import com.alexmochalov.main.*;

import java.io.*;
import java.util.ArrayList;

public class DialogHelp extends Dialog implements android.view.View.OnClickListener
{
	Activity mContext;
	DialogHelp dialog;
	
	ImageButton btnSpeak;
	ImageButton btnNext;
	ImageButton btnPrev;
	ImageButton btnBack;
	
	String text = "";
	
	WebView webView;
	
	static int mIndex = 0;
	
	ArrayList<String> prevArrayList = new ArrayList<String>(); 
	
	public OnDialogSchemeButtonListener mCallback;
	
	private String mWord;
	
	public interface OnDialogSchemeButtonListener {
		public void onSpeakButtonPressed(String text);
	}
	
	public DialogHelp(Activity a, int index){
		super(a);
		
		mContext = a;
		mIndex = index;
		
		dialog = this;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_help);
		
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
               WindowManager.LayoutParams.MATCH_PARENT);
		
		btnSpeak = (ImageButton)findViewById(R.id.dialogSchemeImageButtonSpeak);
		btnSpeak.setOnClickListener(this);
		
		btnNext = (ImageButton)findViewById(R.id.dialogSchemeImageButtonNext);
		btnNext.setOnClickListener(this);
		
		btnPrev = (ImageButton)findViewById(R.id.dialogSchemeImageButtonPrev);
		btnPrev.setOnClickListener(this);

		btnBack = (ImageButton)findViewById(R.id.dialogSchemeImageBack);
		btnBack.setOnClickListener(this);
		
		webView = (WebView)findViewById(R.id.webViewHelp);

		if (mIndex == 999){
			
			  String translation = Dic.getTranslation(mWord);
			  
			  webView.loadData(translation, "text/html; charset=utf-8", "UTF-8");
			  
		} else {
			
			reset();
		}
		
	}

	@Override
	public void onClick(View v) {
		if (v == btnNext){
			mIndex++;
			//if (mIndex >= MenuData.schemes.size())
			//	mIndex = 0;
			reset();
		}
		else if (v == btnPrev){
			mIndex--;
			//if (mIndex < 0)
			//	mIndex = MenuData.schemes.size()-1;
			reset();
		}
		else if (v == btnBack){
			
			if (webView.canGoBack()) {
                webView.goBack();
            } else {
                dialog.dismiss();
            }			
		}
		else if (v == btnSpeak){
			
			final String baseUrl = "help"+mIndex;
			int id = mContext.getResources().
				getIdentifier(baseUrl, "raw", mContext.getPackageName());
			
			InputStream is =
				mContext.getResources().openRawResource(id);
				
			String s = "";
			
			BufferedReader reader = new BufferedReader(
			new InputStreamReader(is));
			String line = null;
			try
			{
				while ((line = reader.readLine()) != null){
					line = Html.fromHtml(line).toString();
					line = line.replaceAll("->", ":");
					line = line.replaceAll("[^A-Za-zÁ:]", " ");
					line = line.replaceAll(" i ", " ");
					line = line.replaceAll(" ii ", " ");
					line = line.replaceAll(" ci ", " ");
					line = line.replaceAll(" chi ", " ");
					line = line.replaceAll("Á", "A").trim();
					if (line.length() > 0)
						s = s + line+":";
				}
			}
			catch (IOException e)
			{}
			
			Log.d("s",s);
			TtsUtils.speak(s);
		}
	}

	private void reset()
	{
		final String baseUrl = "file:///android_res/raw/help"+mIndex+".html";
		
		prevArrayList.add(baseUrl);
		
		webView.loadUrl(baseUrl);

	}

	public void setWord(String word) {
		mWord = word;
	}
}
