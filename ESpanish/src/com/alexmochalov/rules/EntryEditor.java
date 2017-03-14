package com.alexmochalov.rules;

import android.app.*;
import android.content.*;
import android.text.*;
import android.text.method.ScrollingMovementMethod;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;

import java.util.*;

import com.alexmochalov.alang.R;
import com.alexmochalov.dictionary.*;
import com.alexmochalov.dictionary.Dictionary;

public class EntryEditor {
	static AutoCompleteTextView text = null;
	TextView phonetic;
	TextView translation;
	
	static Context mContext;
	static Entry entry = null;
	
	public static void start(Context context, View layout) {
		
		mContext = context;
        text = (AutoCompleteTextView)layout.findViewById(R.id.editTextEntry);
			 
		final ArrayAdapterDictionary adapter = new ArrayAdapterDictionary(mContext, 
															R.layout.dic_string, 
															(ArrayList<IndexEntry>)Dictionary.getEntries().clone());
        text.setAdapter(adapter);
		
		text.setOnItemClickListener(new OnItemClickListener(){
			  @Override
			  public void onItemClick(AdapterView<?> adapterView, View p2, int position, long p4)
			  {
				  IndexEntry entry = (IndexEntry)adapterView.getItemAtPosition(position);
				  
				  AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				  builder.setMessage(entry.getText()+" "+entry.getTranslation())
				         .setCancelable(false)
				         .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				             public void onClick(DialogInterface dialog, int id) {
				                  dialog.dismiss();
				             }
				         });
				  AlertDialog alert = builder.create();
				  alert.show();
				  
			  }});

		//text.setText(entry.getText());
		//phonetic.setText(entry.getPhonetic());
		//translation.setText(Html.fromHtml(entry.getTranslation().toString()));
		//translation.setMovementMethod(new ScrollingMovementMethod());
	}

}


