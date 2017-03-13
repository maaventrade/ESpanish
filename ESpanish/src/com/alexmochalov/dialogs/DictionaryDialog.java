package com.alexmochalov.dialogs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.graphics.Bitmap;

import com.alexmochalov.alang.R;
import com.alexmochalov.dictionary.ArrayAdapterDictionary;
import com.alexmochalov.files.Dic;
import com.alexmochalov.files.IndexEntry;
import com.alexmochalov.main.TtsUtils;
import com.alexmochalov.main.Utils;

public class DictionaryDialog extends Dialog  implements
android.view.View.OnClickListener {

	private Context mContext;
	private DictionaryDialog dialog;
	private ArrayList<IndexEntry> mEntries;
	
	private ListView listView;
	private WebView webView;
	
	private ImageButton btnSpeak;
	private ImageButton btnBack;
	private ImageButton btnPlus;
	private ImageButton btnMinus;

	public DictionaryDialog(Context context, ArrayList<IndexEntry> entries) {
		super(context);
		dialog = this;
		mContext = context;
		mEntries = entries;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dictionary);

		webView = (WebView) findViewById(R.id.webView);
		listView = (ListView) findViewById(R.id.listView);

		ArrayAdapterEntries itemsAdapter = new ArrayAdapterEntries(mContext,
				android.R.layout.simple_list_item_1, mEntries);

		listView.setAdapter(itemsAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View p2,
					int position, long p4) {
				IndexEntry entry = (IndexEntry) adapterView
						.getItemAtPosition(position);

				showTranslation(entry);

			}
		});

		EditText editText = (EditText) findViewById(R.id.editText);
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString().toLowerCase();
				
				for (IndexEntry i: mEntries)
					if (i.getText().toLowerCase().startsWith(str)){
						listView.setSelection(mEntries.indexOf(i)); 
						showTranslation(i);
						break;
					};
			}
			
		});
		
		
		btnSpeak = (ImageButton) findViewById(R.id.dialogSchemeImageButtonSpeak);
		btnSpeak.setOnClickListener(this);

		btnBack = (ImageButton) findViewById(R.id.dialogSchemeImageBack);
		btnBack.setOnClickListener(this);

		btnPlus = (ImageButton) findViewById(R.id.dialogSchemeImageButtonPlus);
		btnPlus.setOnClickListener(this);

		btnMinus = (ImageButton) findViewById(R.id.dialogSchemeImageButtonMinus);
		btnMinus.setOnClickListener(this);
		
	}

	protected void showTranslation(IndexEntry entry) {
		webView = (WebView) findViewById(R.id.webView);

		WebSettings webSettings = webView.getSettings();
		webSettings.setTextZoom(Utils.getScale());

		Utils.loadHTML(entry.getText(), entry.getTranslation(), webView);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view,
					String url) {
				url = url.replace("http:", "");
				url = url.replace("/", "");

				String translation = Dic.getTranslation(url);
				Utils.loadHTML(url, translation, webView);

				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url,
					Bitmap favicon) {
			}

		});
	}

	private class ArrayAdapterEntries extends ArrayAdapter<IndexEntry> {

		public ArrayAdapterEntries(Context context, int resource,
				List<IndexEntry> objects) {
			super(context, resource, objects);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.dic_string1, null);
			}
			IndexEntry entry = mEntries.get(position);

			TextView text = (TextView) convertView
					.findViewById(R.id.textViewText);
			text.setText(entry.getText());

			return convertView;
		}

		public int getCount() {
			return mEntries.size();
		}

		public long getItemId(int position) {
			return position;
		}

	}

	@Override
	public void onClick(View v) {
		if (v == btnPlus) {
			Utils.incScale();
			WebSettings webSettings = webView.getSettings();
			webSettings.setTextZoom(Utils.getScale());
		} else if (v == btnMinus) {
			Utils.decScale();
			WebSettings webSettings = webView.getSettings();
			webSettings.setTextZoom(Utils.getScale());
		} else if (v == btnBack) {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				dialog.dismiss();
			}
		} else if (v == btnSpeak) {
			/*
			String s = "";
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
			TtsUtils.speak(s);*/
		}
	}

}
