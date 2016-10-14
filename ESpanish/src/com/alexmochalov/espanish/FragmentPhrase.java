package com.alexmochalov.espanish;

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
import com.alexmochalov.espanish.DrawerMenu.MarkedString;
import com.alexmochalov.espanish.DrawerMenu.MenuChild;
import com.alexmochalov.espanish.DrawerMenu.MenuGroup;

public class FragmentPhrase extends FragmentM {
	
	
	
	// Type of the current step
	private int typeOfstep;
	// Current text
	private String text;
	// Current translation
	private String translation;
	
	private int direction;
	
	Button button_test;
	
    /**
     * Prepare next step of the task
     */
    private boolean next() {
    	index = DrawerMenu.next(mGroupPosition, mChildPosition);
		if (index == -1) return false;
    	
    	// 001 spa->ru completed  
    	// 010 ru->spa completed  
    	typeOfstep = DrawerMenu.getTypeOfTheStep(mGroupPosition, mChildPosition, index); 
    	
        TextView mText = (TextView)rootView.findViewById(R.id.TextViewPhrase);
        TextView mTranslation = (TextView)rootView.findViewById(R.id.TextViewPhraseTranslation);
        
        if (typeOfstep == 1){
    		text = DrawerMenu.getText(mGroupPosition, mChildPosition, index);
    		translation = Dictionary.getTranslation(text);
    		direction = 0;
        } else {
    		translation = DrawerMenu.getText(mGroupPosition, mChildPosition, index);
    		text = Dictionary.getTranslation(translation);
    		direction = 1;
        }
		mText.setText(text);
		mTranslation.setText(translation);
		
		mTranslation.setVisibility(View.INVISIBLE);   
		
		return true;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		thisFragment = this;
		mContext = this.getActivity();
		
    	getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    	
        rootView = inflater.inflate(R.layout.fragment_phrase, container, false);

    	if (!next()){
			getActivity().getFragmentManager().beginTransaction().remove(thisFragment).commit();;
        }
		
		TextView TextViewPhraseInfo =  (TextView)rootView.findViewById(R.id.TextViewInfo);
		TextViewPhraseInfo.setText(DrawerMenu.getCountStr(mGroupPosition, mChildPosition));
    	
	    button_test = (Button)rootView.findViewById(R.id.button_phrase_test);
	    button_test.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				// Button Next is pressed
				if (button_test.getText().equals(mContext.getResources().getString(R.string.button_next))){
					next();
					EditText editText = (EditText)rootView.findViewById(R.id.editText_phrase_transl);
					editText.setText("");
					
					button_test.setText(mContext.getResources().getString(R.string.button_test));
				}	
				else {
					// Button Проверить is pressed
					button_test.setText(mContext.getResources().getString(R.string.button_next));
					EditText editText = (EditText)rootView.findViewById(R.id.editText_phrase_transl);

					TextView mTranslation = (TextView)rootView.findViewById(R.id.TextViewPhraseTranslation);
					
					boolean result = Dictionary.testRus(editText.getText().toString(), mTranslation.getText().toString(), direction);
					
					result = true;
					mTranslation.setVisibility(View.VISIBLE);
					
					if (result){
						mTranslation.setTextColor(getColor(mContext, R.color.green1));
						
						setTested(typeOfstep);
						
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

	@Override
	public void onPause() {
		super.onPause();
	}
	
	public String getTextR() {
		if (direction == 0)
			return text;
		else 
			return translation;
	}	
	

	
}

