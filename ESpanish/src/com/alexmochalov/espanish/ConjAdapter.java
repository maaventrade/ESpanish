package com.alexmochalov.espanish;

import java.util.ArrayList;

import com.alex_mochalov.navdraw.R;
import com.alexmochalov.espanish.Dictionary.Pronoun;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class ConjAdapter extends BaseAdapter {
	Context mContext;
	LayoutInflater layoutInflater;
	/*
	Class PronounMarked {
		Pronoun pronoun;
		Boolean checked;
	};
	
	ArrayList<PronounMarked> objects;
	*/
	String mVerb = "";
	
	
	private boolean mAnswer = false;
	  
	public ConjAdapter(Context context, String verb) {
	    mContext = context;
	    mVerb = verb;
	    
	    for (Pronoun p: Dictionary.getPronouns()){
			
		}
	    
	    layoutInflater = (LayoutInflater) mContext
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  }
	
	public void setAnswer(boolean answer){
		mAnswer = answer;
	}
	
	public void setVerb(String verb){
		mVerb = verb;
	}    
	
	@Override
	public int getCount() {
		return Dictionary.getPronouns().size();
	}

	@Override
	public Object getItem(int position) {
		return Dictionary.getPronouns().get(position);
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
	 
	    Pronoun p = (Pronoun)getItem(position);
	 
	    ((TextView) view.findViewById(R.id.text)).setText(p.mText);
	    ((TextView) view.findViewById(R.id.translation)).setText(p.translation);
	    
		EditText EditTextTranslation;
	    EditTextTranslation = ((EditText) view.findViewById(R.id.EditTextTranslation));

	    if (mAnswer){
	    	String translation = EditTextTranslation.getText().toString();
	    	if (translation.toLowerCase().equals(p.conj(mVerb).toLowerCase() ))
		    	EditTextTranslation.setTextColor(Color.GREEN);
	    	else
		    	EditTextTranslation.setTextColor(Color.RED);
	    	
	    	EditTextTranslation.setText( p.conj(mVerb));
	    } else {
	    	EditTextTranslation.setTextColor(Color.BLACK);
	    	EditTextTranslation.setText( "" );
	    }
	    
	    return view;	
	 }

}
