package com.alexmochalov.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.graphics.Bitmap;

import com.alexmochalov.alang.R;
import com.alexmochalov.main.TtsUtils;
import com.alexmochalov.main.Utils;
import com.alexmochalov.rules.ArrayAdapterDictionary;

public class DictionaryDialog extends DialogFragment implements
android.view.View.OnClickListener
{

	private Context mContext;
	private DictionaryDialog dialog;
	private ArrayList<IndexEntry> mEntries;

	private ListView listView;
	private ArrayAdapterEntries itemsAdapter;
	private WebView webView;
	
	private boolean onCreationView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{

		mContext = getActivity();

		getDialog().setTitle(mContext.getString(R.string.dic_name_ita));

		View v = inflater.inflate(R.layout.dictionary, null);

		v.findViewById(R.id.dialogSchemeImageButtonSpeak).setOnClickListener(
			this);
		v.findViewById(R.id.dialogSchemeImageBack).setOnClickListener(this);
		v.findViewById(R.id.dialogSchemeImageButtonPlus).setOnClickListener(
			this);
		v.findViewById(R.id.dialogSchemeImageButtonMinus).setOnClickListener(
			this);
		v.findViewById(R.id.dialogSchemeImageButtonDictionary)
			.setOnClickListener(this);

		webView = (WebView) v.findViewById(R.id.webView);

		listView = (ListView) v.findViewById(R.id.listView);

		mEntries = Dictionary.getEntries();

		itemsAdapter = new ArrayAdapterEntries(mContext,
											   android.R.layout.simple_list_item_1, mEntries);

		listView.setAdapter(itemsAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterView, View p2,
										int position, long p4)
				{
					IndexEntry entry = mEntries.get(position); //(IndexEntry) adapterView
					//.getItemAtPosition(position);

					showTranslation(entry);

					Dictionary.setLastWord(entry.getText());

				}
			});

		EditText editText = (EditText) v.findViewById(R.id.editText);
		
		editText.setText(Dictionary.getText());
		
		editText.addTextChangedListener(new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
											  int after)
				{
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before,
										  int count)
				{
				}

				@Override
				public void afterTextChanged(Editable s)
				{
					String str = s.toString().toLowerCase();
					Dictionary.setText(s.toString());
	/*				
					if (str.length() > 0)
						if (str.substring(0, 1).matches("[а-яА-Я]"))
							if (Dictionary.getDictionaryName().equals("it_ru")) 
								selectDictionary("ru_it");	
							else;
						else 
							if (Dictionary.getDictionaryName().equals("ru_it")) 
								selectDictionary("it_ru");	
							else;
*/
					for (IndexEntry i : mEntries)
						if (i.getText().toLowerCase().startsWith(str))
						{
							listView.setSelection(mEntries.indexOf(i));
							showTranslation(i);

							Dictionary.setLastWord(i.getText());
							break;
						}
					;
				}

			});


		String s = Dictionary.getLastWord();
		if (!s.equals(""))
		{
			for (IndexEntry i : mEntries)
				if (i.getText().toLowerCase().equals(s))
				{
					listView.setSelection(mEntries.indexOf(i));
					showTranslation(i);
				}
		}

		return v;
	}

	@Override
	public void onStart()
	{
		super.onStart();

		Dialog dialog = getDialog();
		if (dialog != null)
		{
			dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
										 ViewGroup.LayoutParams.MATCH_PARENT);
		}

	}

	/*
	 * public DictionaryDialog(Context context, ArrayList<IndexEntry> entries) {
	 * super(context); dialog = this; mContext = context; mEntries = entries; }
	 * 
	 * @Override protected void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState);
	 * 
	 * setContentView(R.layout.dictionary);
	 * 
	 * getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
	 * WindowManager.LayoutParams.MATCH_PARENT);
	 * 
	 * webView = (WebView) findViewById(R.id.webView); listView = (ListView)
	 * findViewById(R.id.listView);
	 * 
	 * ArrayAdapterEntries itemsAdapter = new ArrayAdapterEntries(mContext,
	 * android.R.layout.simple_list_item_1, mEntries);
	 * 
	 * listView.setAdapter(itemsAdapter); listView.setOnItemClickListener(new
	 * OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> adapterView, View p2,
	 * int position, long p4) { IndexEntry entry = (IndexEntry) adapterView
	 * .getItemAtPosition(position);
	 * 
	 * showTranslation(entry);
	 * 
	 * } });
	 * 
	 * EditText editText = (EditText) findViewById(R.id.editText);
	 * editText.addTextChangedListener(new TextWatcher() {
	 * 
	 * @Override public void beforeTextChanged(CharSequence s, int start, int
	 * count, int after) { }
	 * 
	 * @Override public void onTextChanged(CharSequence s, int start, int
	 * before, int count) { }
	 * 
	 * @Override public void afterTextChanged(Editable s) { String str =
	 * s.toString().toLowerCase();
	 * 
	 * for (IndexEntry i: mEntries) if
	 * (i.getText().toLowerCase().startsWith(str)){
	 * listView.setSelection(mEntries.indexOf(i)); showTranslation(i); break; };
	 * }
	 * 
	 * });
	 * 
	 * 
	 * btnSpeak = (ImageButton) findViewById(R.id.dialogSchemeImageButtonSpeak);
	 * btnSpeak.setOnClickListener(this);
	 * 
	 * btnBack = (ImageButton) findViewById(R.id.dialogSchemeImageBack);
	 * btnBack.setOnClickListener(this);
	 * 
	 * btnPlus = (ImageButton) findViewById(R.id.dialogSchemeImageButtonPlus);
	 * btnPlus.setOnClickListener(this);
	 * 
	 * btnMinus = (ImageButton) findViewById(R.id.dialogSchemeImageButtonMinus);
	 * btnMinus.setOnClickListener(this);
	 * 
	 * Dic.setDialogOpen(true);
	 * 
	 * this.setOnDismissListener(new OnDismissListener(){
	 * 
	 * @Override public void onDismiss(DialogInterface dialog) { Log.d("",
	 * "DISM!!!!"); }}); }
	 */

	protected void showTranslation(IndexEntry entry)
	{

		WebSettings webSettings = webView.getSettings();
		webSettings.setTextZoom(Utils.getScale());

		Utils.loadHTML(entry.getText(), entry.getTranslation(), webView);

		webView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url)
				{
					url = url.replace("http:", "");
					url = url.replace("/", "");

					String translation = Dictionary.getTranslation(url);
					Utils.loadHTML(url, translation, webView);

					return true;
				}

				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon)
				{
				}

			});
	}

	private class ArrayAdapterEntries extends ArrayAdapter<IndexEntry>
	{

		public ArrayAdapterEntries(Context context, int resource,
								   List<IndexEntry> objects)
		{
			super(context, resource, objects);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
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

		public int getCount()
		{
			return mEntries.size();
		}

		public long getItemId(int position)
		{
			return position;
		}

	}

	public void onDismiss(DialogInterface dialog)
	{
		super.onDismiss(dialog);
		Log.d("", "Dialog 1: onDismiss");
	}

	public void onCancel(DialogInterface dialog)
	{
		super.onCancel(dialog);
		Log.d("", "Dialog 1: onCancel");


	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.dialogSchemeImageButtonPlus)
		{
			Utils.incScale();
			WebSettings webSettings = webView.getSettings();
			webSettings.setTextZoom(Utils.getScale());
		}
		else if (v.getId() == R.id.dialogSchemeImageButtonMinus)
		{
			Utils.decScale();
			WebSettings webSettings = webView.getSettings();
			webSettings.setTextZoom(Utils.getScale());
		}
		else if (v.getId() == R.id.dialogSchemeImageButtonDictionary)
		{
			showSelectDictionary(v);
		}
		else if (v.getId() == R.id.dialogSchemeImageBack)
		{
			if (webView.canGoBack())
			{
				webView.goBack();
			}
			else
			{
				dialog.dismiss();
			}
		}
		else if (v.getId() == R.id.dialogExampleImageButtonSpeak)
		{
			/*
			 * String s = ""; final String baseUrl = "help" + mIndex; int id =
			 * mContext.getResources().getIdentifier(baseUrl, "raw",
			 * mContext.getPackageName());
			 * 
			 * InputStream is = mContext.getResources().openRawResource(id);
			 * 
			 * BufferedReader reader = new BufferedReader( new
			 * InputStreamReader(is)); String line = null; try { while ((line =
			 * reader.readLine()) != null) { line =
			 * Html.fromHtml(line).toString(); line = line.replaceAll("->",
			 * ":"); line = line.replaceAll("[^A-Za-zÁ:]", " "); line =
			 * line.replaceAll(" i ", " "); line = line.replaceAll(" ii ", " ");
			 * line = line.replaceAll(" ci ", " "); line =
			 * line.replaceAll(" chi ", " "); line = line.replaceAll("Á",
			 * "A").trim(); if (line.length() > 0) s = s + line + ":"; } } catch
			 * (IOException e) { } TtsUtils.speak(s);
			 */
		}
	}
	
	private void selectDictionary(final String name){
		Dictionary.eventCallback = new Dictionary.EventCallback() {
			@Override
			public void loadingFinishedCallBack()
			{
				mEntries = Dictionary.getEntries();
				itemsAdapter.notifyDataSetChanged();
				if (name.equals("ru_it"))
					getDialog().setTitle(mContext.getString(R.string.dic_name_rus));
				else
					getDialog().setTitle(mContext.getString(R.string.dic_name_ita));
			}
		}; 
		//Dictionary.setDictionaryName(name);
		//Dictionary.load(mContext);
	}

	private void showSelectDictionary(View v)
	{
		PopupMenu popupMenu = new PopupMenu(mContext, v);
		popupMenu.inflate(R.menu.select_dictionary);

		popupMenu
			.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item)
				{
					// Toast.makeText(PopupMenuDemoActivity.this,
					// item.toString(), Toast.LENGTH_LONG).show();
					// return true;
					switch (item.getItemId())
					{
						case R.id.action_it_ru:
							selectDictionary("it_ru");	
							return true;
						case R.id.action_ru_it:
							//selectDictionary("ru_it");	
							//Dictionary.load(mContext);

							return true;
						default:
							return false;
					}
				}

			});

		popupMenu.show();
	}

}
