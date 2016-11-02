package com.alexmochalov.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alex_mochalov.navdraw.R;
import com.alexmochalov.dictionary.Dictionary;
import com.alexmochalov.menu.MenuData;

public class FragmentPhrase extends FragmentM {
	Button button_test;
	
    /**
     * Prepare next step of the task
     */
    private boolean next() {
    	// 001 spa->ru completed  
    	// 010 ru->spa completed  
		
		mTextViewText = (TextView)rootView.findViewById(R.id.TextViewPhrase);
        mTranslation = (TextView)rootView.findViewById(R.id.TextViewPhraseTranslation);

		MenuData.getTypeOfTheStep(mTextViewText, mTranslation);
		
        mTextViewText = (TextView)rootView.findViewById(R.id.TextViewPhrase);
        mTranslation = (TextView)rootView.findViewById(R.id.TextViewPhraseTranslation);
        
		mTranslation.setVisibility(View.INVISIBLE);   
		
		return true;
	}


	@Override
    public void onPause()
	{
		super.onPause();
	}
	
	@Override
    public void onResume()
	{
		super.onResume();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
        rootView = inflater.inflate(R.layout.fragment_phrase, container, false);
        MenuData.nextTestIndex();

        init();
    	next();
    	
	    button_test = (Button)rootView.findViewById(R.id.button_test);
	    button_test.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				// Button Next is pressed
				if (button_test.getText().equals(mContext.getResources().getString(R.string.button_next))){
			    
					if (MenuData.next() == -1){
						getActivity().getFragmentManager().beginTransaction().remove(thisFragment).commit();
					} else {
						next();
						
						EditText editText = (EditText)rootView.findViewById(R.id.editText_phrase_transl);
						editText.setText("");
						
						button_test.setText(mContext.getResources().getString(R.string.button_test));
					}
				}	
				else {
					// Button Проверить is pressed
					button_test.setText(mContext.getResources().getString(R.string.button_next));
					EditText editText = (EditText)rootView.findViewById(R.id.editText_phrase_transl);

					TextView mTranslation = (TextView)rootView.findViewById(R.id.TextViewPhraseTranslation);
					
					boolean result = Dictionary.testRus(editText.getText().toString(), mTranslation.getText().toString(),MenuData.getDirection());
					
					//result = true;
					mTranslation.setVisibility(View.VISIBLE);
					
					if (result){
						mTranslation.setTextColor(getColor(mContext, R.color.green1));
						
						setTested(MenuData.getDirection());
						
					}
					else 
						mTranslation.setTextColor(Color.RED);
				}	
				
			}});
        
        return rootView;
    }
	
	public static final int getColor(Context context, int id) {
	    final int version = Build.VERSION.SDK_INT;
	    if (version >= 23) {
	        return ContextCompat.getColor(context, id);
	    } else {
	        return context.getResources().getColor(id);
	    }
	}	
/*
	private void setText() {
		if (index >= 0){
			
			String text = mData.get(index);
			
			mText.setText(text);
			
			mTranslation.setText(Dictionary.getTranslation(text));
		}
		
	}
*/
	public String getTextToTTS() {
		if (MenuData.getDirection() == 1)
			return MenuData.getText();
		else 
			return MenuData.getTranslation();
	}	
	
}

