package com.alexmochalov.tree;

import java.util.ArrayList;

import com.alexmochalov.ddic.R;
import com.alexmochalov.dic.ArrayAdapterDictionary;
import com.alexmochalov.dic.Dictionary;
import com.alexmochalov.dic.Entry;
import com.alexmochalov.dic.IndexEntry;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DialogEdit extends Dialog {

	private Activity mContext;
	
	private ListView lvDictionary;
	private ArrayAdapterDictionary adapter;
	private int currentPosition = -1;
	
	private TextView tvWord;
	private TextView tvPhonetic;
	private TextView tvTranslation;
	
	private EditText etEntry;
	
	protected DialogEdit(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	protected DialogEdit(Activity context) {
		super(context);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.edit);
		
		lvDictionary = (ListView)findViewById(R.id.lvDictionary);
		
		adapter = new ArrayAdapterDictionary(mContext,
											 R.layout.dic_string,
											 (ArrayList<IndexEntry>) Dictionary
											 .getIndexEntries());

		lvDictionary.setAdapter(adapter);
		lvDictionary
			.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(
					AdapterView<?> adapterView, View p2,
					int position, long p4) {
					currentPosition = position;
					
					setTranslation(adapter.getItem(position));
					
					View view = mContext.getCurrentFocus();
					if (view != null) {  
						InputMethodManager imm = (InputMethodManager)mContext.
							getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					}										
				}
			});
		
		etEntry = (EditText)findViewById(R.id.etEntry);
		
		etEntry.addTextChangedListener(new TextWatcher() {

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
				if (adapter != null){
					adapter.getFilter().filter(s);
				}
			}
		});
		
		tvWord = (TextView)findViewById(R.id.tvWord);
		tvTranslation = (TextView)findViewById(R.id.tvTranslation);
		tvTranslation.setMovementMethod(new ScrollingMovementMethod());		
		tvPhonetic = (TextView)findViewById(R.id.tvPhonetic);
		
		//getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
        //      WindowManager.LayoutParams.MATCH_PARENT);
	}
	
	public void setTranslation(IndexEntry indexEntry) {
		String str = Dictionary
				.readTranslation(indexEntry);

		Entry entry = new Entry();
		entry.setTranslationAndPhonetic(str,
				indexEntry.getText());

		tvWord.setText(indexEntry.getText());

		tvTranslation.setText(Html.fromHtml(entry
				.getTranslation().toString()));
		tvTranslation.scrollTo(0, 0);

		String phonetic = entry.getPhonetic();
		if (phonetic.length() > 0)
			tvPhonetic
					.setText("[" + phonetic + "]");
		else
			tvPhonetic.setText("");
	}
	
}
