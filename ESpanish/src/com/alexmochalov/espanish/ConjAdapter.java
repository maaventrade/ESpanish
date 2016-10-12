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
import com.alexmochalov.espanish.ConjAdapter.*;

public class ConjAdapter extends BaseAdapter
{
	Context mContext;
	LayoutInflater layoutInflater;

	public boolean allChecked()
	{
		for (PronounChecked p: objects)
			if (!p.checked) 
				return false;
		return true;
	}

	public void test(String translation, int i)
	{
		objects.get(i).checked = 
			translation.
			replaceAll("á", "a").
			replaceAll("ó", "o").
			replaceAll("ú", "u").
			replaceAll("á", "a").
			toLowerCase().
			equals(objects.get(i).mPronoun.conj(mVerb).
				   replaceAll("á", "a").
				   replaceAll("ó", "o").
				   replaceAll("ú", "u").
				   replaceAll("á", "a").
				   toLowerCase());

	}

	static class PronounChecked
	{
		public PronounChecked(Pronoun pronoun)
		{
			mPronoun = pronoun;
			checked = false;
		}
		Pronoun mPronoun;
		Boolean checked;

		public void put(ConjAdapter.PronounChecked from)
		{
			mPronoun = from.mPronoun;
		}

		public ConjAdapter.PronounChecked getCopy()
		{
			return new PronounChecked(mPronoun);
		}
	};

	ArrayList<PronounChecked> objects = new ArrayList<PronounChecked>();

	String mVerb = "";

	private boolean mAnswer = false;

	public ConjAdapter(Context context, String verb)
	{
	    mContext = MainActivity.mContext;
	    mVerb = verb;

	    for (Pronoun p: Dictionary.getPronouns())
		{
	    	objects.add(new PronounChecked(p));
		}

		for (int i = 1; i <= objects.size(); i++){
			int j = (int)Math.random() * objects.size();
			int k = (int)Math.random() * objects.size();
			
			PronounChecked p = objects.get(i).getCopy();
			objects.get(i).put(objects.get(j));
			objects.get(j).put(p);
			
		}
		
		
	    layoutInflater = (LayoutInflater) mContext
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setAnswer(boolean answer)
	{
		mAnswer = answer;
		if (answer)
		{

		}
		else 
			for (PronounChecked p: objects)
				p.checked = false;
	}

	public void setVerb(String verb)
	{
		mVerb = verb;
	}    

	@Override
	public int getCount()
	{
		return objects.size();
	}

	@Override
	public Object getItem(int position)
	{
		return objects.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = convertView;
	    if (view == null)
		{
			view = layoutInflater.inflate(R.layout.fragment_conj_item, parent, false);
	    }

	    PronounChecked pronoun = (PronounChecked)getItem(position);

	    ((TextView) view.findViewById(R.id.text)).setText(pronoun.mPronoun.mText);
	    ((TextView) view.findViewById(R.id.translation)).setText(pronoun.mPronoun.translation);

		EditText EditTextTranslation;
	    EditTextTranslation = ((EditText) view.findViewById(R.id.EditTextTranslation));




	    if (mAnswer)
		{
	    	if (pronoun.checked)
				EditTextTranslation.setTextColor(Color.GREEN);
	        else 
	        	EditTextTranslation.setTextColor(Color.RED);

	    	EditTextTranslation.setText(pronoun.mPronoun.conj(mVerb));
	    }
		else
		{
	    	EditTextTranslation.setTextColor(Color.BLACK);
	    	EditTextTranslation.setText("");
	    }

	    return view;	
	}

}
