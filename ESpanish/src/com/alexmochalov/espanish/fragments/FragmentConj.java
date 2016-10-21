package com.alexmochalov.espanish.fragments;

import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.alex_mochalov.navdraw.*;
import com.alexmochalov.espanish.*;
import com.alexmochalov.espanish.Dictionary.*;
import java.util.*;

import com.alexmochalov.espanish.Dictionary;

public class FragmentConj extends FragmentM
{
	private String mText;

	private TextView mTextViewText;
	private TextView mTranslation;
	
	private Button button_test;
	
	static class PronounEdited
	{
		public PronounEdited(Pronoun pronoun, View layout)
		{
			mPronoun = pronoun;
			mLayout = layout;
		}
		Pronoun mPronoun = null;
		View mLayout = null;

		public void put(PronounEdited from)
		{
			mPronoun = from.mPronoun;
			//mLayout = from.mLayout;
		}

		public PronounEdited getCopy()
		{
			return new PronounEdited(mPronoun, mLayout);
		}
	};

	ArrayList<PronounEdited> objects = new ArrayList<PronounEdited>();
	
    private boolean next()
	{
    	index = MenuData.next(mGroupPosition, mChildPosition);
		if (index == -1) return false;
		
    	// Заполняем заголовок
    	mTextViewText = (TextView)rootView.findViewById(R.id.text);
        mTranslation = (TextView)rootView.findViewById(R.id.translation);

        mText = MenuData.getText(mGroupPosition, mChildPosition, index);

		mTextViewText.setText(mText);
		mTranslation.setText(Dictionary.getTranslation(mText).translation);

		
		CheckBox checkBox = (CheckBox)rootView.findViewById(R.id.checkBoxRandom);
		if (checkBox.isChecked())
			randomize();
		
		setVerb(mText);
		
		return true;
	}

    private void randomize() {
		for (int i = 1; i <= objects.size(); i++){
			int j = (int)(Math.random() * objects.size());
			int k = (int)(Math.random() * objects.size());
			
			PronounEdited p = objects.get(j).getCopy();
			objects.get(j).put(objects.get(k));
			objects.get(k).put(p);
		}
	}
    
    private void setVerb(String text) {
    	
    	for (PronounEdited p: objects){
    	    ((TextView)p.mLayout.findViewById(R.id.text)).setText(p.mPronoun.getText());
    	    ((TextView)p.mLayout.findViewById(R.id.translation)).setText(p.mPronoun.getTranslation());
    	}
    	
		
	}

	@Override
    public void onStart()
	{
        mText = MenuData.getText(mGroupPosition, mChildPosition, index);
		setVerb(mText);
		
		super.onStart();
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

        rootView = inflater.inflate(R.layout.fragment_conj, container, false);

        init();
    	next();

        ViewGroup mLinearLayout = (ViewGroup)rootView.findViewById(R.id.fc_linearLayout);
	    for (Pronoun p: Dictionary.getPronouns())
		{
            View layout2 = LayoutInflater.from(mContext).inflate(R.layout.fragment_conj_item, mLinearLayout, false);
            mLinearLayout.addView(layout2);
            
	    	objects.add(new PronounEdited(p, layout2));
		}
    	
	    button_test = (Button)rootView.findViewById(R.id.button_test);
	    button_test.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{

					// Button Next is pressed
					if (button_test.getText().equals(mContext.getResources().getString(R.string.button_next)))
					{
						if (!next()){
							getActivity().getFragmentManager().beginTransaction().remove(thisFragment).commit();;
						}
						
						button_test.setText(mContext.getResources().getString(R.string.button_test));
				    	for (PronounEdited p: objects){
				            EditText editText = (EditText)p.mLayout.findViewById(R.id.EditTextTranslation); 
			    			editText.setTextColor(Color.BLACK);
				            editText.setText("");
				    	}
					}	
					else
					{
						// Button Проверить is pressed
						button_test.setText(MainActivity.mContext.getResources().getString(R.string.button_next));

						boolean allChecked = true;
						
				    	for (PronounEdited p: objects){
				            EditText editText = (EditText)p.mLayout.findViewById(R.id.EditTextTranslation);
				            String text = editText.getText().toString();
				            
				    		if ( text.toLowerCase().
				    				replaceAll("á", "a").
				    				replaceAll("ó", "o").
				    				replaceAll("ú", "u").
				    				replaceAll("á", "a").
				    				
								equals(p.mPronoun.conj(mText).toLowerCase().
				    					   replaceAll("á", "a").
				    					   replaceAll("ó", "o").
				    					   replaceAll("ú", "u").
				    					   replaceAll("á", "a"))){
				    			editText.setTextColor(mContext.getResources().getColor(R.color.green2));
				    		} else {
				    			editText.setTextColor(Color.RED);
								allChecked = false;
				    		}

				            editText.setText(p.mPronoun.conj(mText));
				    	}
						
				    	//allChecked = true;
				    	
						if (allChecked)
						{
							
							setTested(3);
							
						}

				}	

			}});
	    
			return rootView;
    }

	public String getTextR() {
		String text = "";
		
		for (PronounEdited p: objects){
    	   	text = text + p.mPronoun.getText()+" ";
    	    text = text +p.mPronoun.conj(mText) + ". ";
    	}
		
		return text;
	}	
}
