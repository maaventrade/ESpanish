package com.alexmochalov.dialogs;

import android.app.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import android.graphics.Bitmap;

import com.alexmochalov.alang.*;
import com.alexmochalov.files.Dic;
import com.alexmochalov.files.IndexEntry;
import com.alexmochalov.main.*;

import java.io.*;
import java.util.ArrayList;

public class DialogHelp extends Dialog implements
		android.view.View.OnClickListener {
	Activity mContext;
	DialogHelp dialog;

	ImageButton btnSpeak;
	ImageButton btnNext;
	ImageButton btnPrev;
	ImageButton btnBack;
	ImageButton btnPlus;
	ImageButton btnMinus;

	String text = "";

	int scale;

	WebView webView;

	static int mIndex = 0;

	ArrayList<String> prevArrayList = new ArrayList<String>();

	public OnDialogSchemeButtonListener mCallback;

	private String mWord;

	public interface OnDialogSchemeButtonListener {
		public void onSpeakButtonPressed(String text);
	}

	public DialogHelp(Activity a, int index) {
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

		btnSpeak = (ImageButton) findViewById(R.id.dialogSchemeImageButtonSpeak);
		btnSpeak.setOnClickListener(this);

		btnNext = (ImageButton) findViewById(R.id.dialogSchemeImageButtonNext);
		btnNext.setOnClickListener(this);

		btnPrev = (ImageButton) findViewById(R.id.dialogSchemeImageButtonPrev);
		btnPrev.setOnClickListener(this);

		btnBack = (ImageButton) findViewById(R.id.dialogSchemeImageBack);
		btnBack.setOnClickListener(this);

		btnPlus = (ImageButton) findViewById(R.id.dialogSchemeImageButtonPlus);
		btnPlus.setOnClickListener(this);

		btnMinus = (ImageButton) findViewById(R.id.dialogSchemeImageButtonMinus);
		btnMinus.setOnClickListener(this);

		webView = (WebView) findViewById(R.id.webViewHelp);
/*
		scale = scale + 1;
		webView.setInitialScale(150);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
	*/
		scale = (int)webView.getScale();
		Log.d("a","scale "+webView.getScale());
		scale = 100;
		
		WebSettings webSettings = webView.getSettings();

		//webSettings.setTextSize(WebSettings.TextSize.LARGER);
		
		if (mIndex == 999) {
			btnNext.setVisibility(View.INVISIBLE);
			btnPrev.setVisibility(View.INVISIBLE);

			String translation = Dic.getTranslation(mWord);
			loadHTML(translation);

			webView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {

					url = url.replace("http:", "");
					url = url.replace("/", "");

					// Log.d("", "SHOULD "+url);

					mWord = url;
					String translation = Dic.getTranslation(mWord);
					loadHTML(translation);

					return true;
				}

				@Override
				public void onPageStarted(WebView view, String url,
						Bitmap favicon) {
				}

			});

		} else {

			reset();
		}

	}

	private void loadHTML(String translation) {
		translation = "<b>" + Utils.firstLetterToUpperCase(mWord) + "</b>"
				+ translation;

		translation = translation.replace("\n", "<br>");
		translation = translation.replace("<abr>", "<font color = #00aa00>");
		translation = translation.replace("</abr>", "</font>");

		translation = translation.replace("<ex>", "<font color = #aa7777>");
		translation = translation.replace("</ex>", "</font>");

		int start = translation.indexOf("<kref>");
		int end = translation.indexOf("</kref>");
		while (start >= 0 && end >= 0) {
			String text = translation.substring(start + 6, end);

			translation = translation.substring(0, start) + "<a href =\""
					+ "http://" + text + "\">" + text + "</a>"
					+ translation.substring(end + 7);

			start = translation.indexOf("<kref>");
			end = translation.indexOf("</kref>");
		}

		webView.loadData(translation, "text/html; charset=utf-8", "UTF-8");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if (v == btnNext) {
			mIndex++;
			// if (mIndex >= MenuData.schemes.size())
			// mIndex = 0;
			reset();
		} else if (v == btnPrev) {
			mIndex--;
			// if (mIndex < 0)
			// mIndex = MenuData.schemes.size()-1;
			reset();
		} else if (v == btnPlus) {
			
			scale = scale + 10;
			WebSettings webSettings = webView.getSettings();

			webSettings.setTextZoom(scale);
			
			
			
		} else if (v == btnMinus) {
			scale = scale  - 10;
			WebSettings webSettings = webView.getSettings();

			webSettings.setTextZoom(scale);
			
		} else if (v == btnBack) {

			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				dialog.dismiss();
			}
		} else if (v == btnSpeak) {

			String s = "";
			if (mIndex == 999) {

				String translation = Dic.getTranslation(mWord);
				String line = translation.replace("\n", ":");

				line = Html.fromHtml(line).toString();

				line = line.replaceAll("->", ":");
				line = line.replaceAll("[^A-Za-zÁ:]", " ");
				line = line.replaceAll(" i ", " ");
				line = line.replaceAll(" ii ", " ");
				line = line.replaceAll(" ci ", " ");
				line = line.replaceAll(" chi ", " ");
				line = line.replaceAll("Á", "A").trim();

				s = line;

			} else {
				final String baseUrl = "help" + mIndex;
				int id = mContext.getResources().getIdentifier(baseUrl, "raw",
						mContext.getPackageName());

				InputStream is = mContext.getResources().openRawResource(id);

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						line = Html.fromHtml(line).toString();
						line = line.replaceAll("->", ":");
						line = line.replaceAll("[^A-Za-zÁ:]", " ");
						line = line.replaceAll(" i ", " ");
						line = line.replaceAll(" ii ", " ");
						line = line.replaceAll(" ci ", " ");
						line = line.replaceAll(" chi ", " ");
						line = line.replaceAll("Á", "A").trim();
						if (line.length() > 0)
							s = s + line + ":";
					}
				} catch (IOException e) {
				}
			}

			Log.d("s", s);
			TtsUtils.speak(s);
		}
	}

	private void reset() {
		final String baseUrl = "file:///android_res/raw/help" + mIndex
				+ ".html";

		prevArrayList.add(baseUrl);

		webView.loadUrl(baseUrl);

	}

	public void setWord(String word) {
		mWord = word;
	}
}
