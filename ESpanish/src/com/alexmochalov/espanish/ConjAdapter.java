package com.alexmochalov.espanish;

import java.util.ArrayList;

import com.alex_mochalov.navdraw.R;
import com.alexmochalov.espanish.Dictionary.Pronoun;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class ConjAdapter extends BaseAdapter {
	Context mContext;
	LayoutInflater layoutInflater;
	
	static class PronounChecked {
		public PronounChecked(Pronoun pronoun) {
			mPronoun = pronoun;
			checked = false;
		}
		Pronoun mPronoun;
		Boolean checked;
	};
	
	ArrayList<PronounChecked> objects = new ArrayList<PronounChecked>();
	
	String mVerb = "";
		
	private boolean mAnswer = false;
	  
	public ConjAdapter(Context context, String verb) {
	    mContext = MainActivity.mContext;
	    mVerb = verb;
	    
	    for (Pronoun p: Dictionary.getPronouns()){
	    	objects.add(new PronounChecked(p));
		}
	    
	    layoutInflater = (LayoutInflater) mContext
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  }
	
	public void setAnswer(boolean answer){
		mAnswer = answer;
		if (answer){
			
		}
		else 
			for (PronounChecked p: objects)
				p.checked = false;
	}
	
	public void setVerb(String verb){
		mVerb = verb;
	}    
	
	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
	    if (view == null) {
	      view = layoutInflater.inflate(R.layout.fragment_conj_item, parent, false);
	    }
	 
	    PronounChecked pronoun = (PronounChecked)getItem(position);
	 
	    ((TextView) view.findViewById(R.id.text)).setText(pronoun.mPronoun.mText);
	    ((TextView) view.findViewById(R.id.translation)).setText(pronoun.mPronoun.translation);
	    
		EditText EditTextTranslation;
	    EditTextTranslation = ((EditText) view.findViewById(R.id.EditTextTranslation));
	    
	    EditTextTranslation.addTextChangedListener(new TextWatcher() {

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
				Log.d("", "S "+s);
			}
	    	
	    });


	    if (mAnswer){
	    	if (pronoun.checked);
	    	else {
	        	String translation = EditTextTranslation.getText().toString();
	        	Log.d("", "position "+position+" EditTextTranslation "+translation);
	        	
	        	if (translation.toLowerCase().equals(pronoun.mPronoun.conj(mVerb).toLowerCase() ))
	        		EditTextTranslation.setTextColor(Color.GREEN);
	        	else 
	        		EditTextTranslation.setTextColor(Color.RED);
	        	pronoun.checked = true;
	    	}
	    	EditTextTranslation.setText( pronoun.mPronoun.conj(mVerb));
	    } else {
	    	EditTextTranslation.setTextColor(Color.BLACK);
	    	EditTextTranslation.setText( "" );
	    }
	    
	    return view;	
	 }

}
