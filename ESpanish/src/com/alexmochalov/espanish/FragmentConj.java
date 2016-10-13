package com.alexmochalov.espanish;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alex_mochalov.navdraw.R;
import com.alexmochalov.espanish.Dictionary.Pronoun;

public class FragmentConj extends Fragment
{
	private Fragment thisFragment;
	
	private Context mContext;

	private String mText;
	// Current translation
	private String translation;

	private int index = 0;

	private TextView mTextViewText;
	private TextView mTranslation;

	private View rootView;
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
	
    private void next()
	{
    	index = DrawerMenu.next();

    	// Заполняем заголовок
    	mTextViewText = (TextView)rootView.findViewById(R.id.text);
        mTranslation = (TextView)rootView.findViewById(R.id.translation);

        mText = DrawerMenu.getText(index);

		mTextViewText.setText(mText);
		mTranslation.setText(Dictionary.getTranslation(mText));

		randomize();
		
		setVerb(mText);
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
    	    ((TextView)p.mLayout.findViewById(R.id.text)).setText(p.mPronoun.mText);
    	    ((TextView)p.mLayout.findViewById(R.id.translation)).setText(p.mPronoun.translation);
    	}
    	
		
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		thisFragment = this;
		mContext = this.getActivity();
		
        rootView = inflater.inflate(R.layout.fragment_conj, container, false);
        ViewGroup mLinearLayout = (ViewGroup)rootView.findViewById(R.id.fc_linearLayout);
        
		TextView TextViewPhraseInfo =  (TextView)rootView.findViewById(R.id.TextViewConjInfo);
		TextViewPhraseInfo.setText(DrawerMenu.getCountStr());
        
	    for (Pronoun p: Dictionary.getPronouns())
		{
            View layout2 = LayoutInflater.from(mContext).inflate(R.layout.fragment_conj_item, mLinearLayout, false);
            mLinearLayout.addView(layout2);
            
	    	objects.add(new PronounEdited(p, layout2));
		}
        
    	next();
    	
	    button_test = (Button)rootView.findViewById(R.id.button_conj_test);
	    button_test.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{

					// Button Next is pressed
					if (button_test.getText().equals(mContext.getResources().getString(R.string.button_next)))
					{
						next();
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
				            
				    		if ( text.
				    				replaceAll("á", "a").
				    				replaceAll("ó", "o").
				    				replaceAll("ú", "u").
				    				replaceAll("á", "a").
				    				toLowerCase().
				    				equals(p.mPronoun.conj(mText).
				    					   replaceAll("á", "a").
				    					   replaceAll("ó", "o").
				    					   replaceAll("ú", "u").
				    					   replaceAll("á", "a").
				    					   toLowerCase())){
				    			editText.setTextColor(Color.GREEN);
				    		} else {
				    			editText.setTextColor(Color.RED);
								allChecked = false;
				    		}

				            editText.setText(p.mPronoun.conj(mText));
				    	}
						
				    	allChecked = true;
				    	
						if (allChecked)
						{
							DrawerMenu.setStepCompleted(index, 3);
							TextView textView = ((TextView) rootView.findViewById(R.id.TextViewConjInfo));
							textView.setText(DrawerMenu.getCountStr());
							
							TextView TextViewPhraseInfo =  (TextView)rootView.findViewById(R.id.TextViewConjInfo);
							TextViewPhraseInfo.setText(DrawerMenu.getCountStr());
							
							if (DrawerMenu.getDataSize() == 0)
							{
								Toast.makeText(MainActivity.mContext, "THAT IS ALL", Toast.LENGTH_LONG).show();
								// Close Fragment
								getActivity().getFragmentManager().beginTransaction().remove(thisFragment).commit();
							}
						}

				}	

			}});
	    
		TextView textView = ((TextView) rootView.findViewById(R.id.TextViewConjInfo));
		textView.setText(DrawerMenu.getCountStr());
	return rootView;
    }

	public void setParams(Context context) {
		mContext = context;
	}	

}
