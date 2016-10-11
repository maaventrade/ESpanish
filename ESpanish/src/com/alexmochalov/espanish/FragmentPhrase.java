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

public class FragmentPhrase extends Fragment {
	private SharedPreferences prefs;
	private Fragment thisFragment;
	
	// Index of the current Phrase
	private int index;
	// Type of the current step
	private int typeOfstep;
	// Current text
	private String text;
	// Current translation
	private String translation;
	
	private int direction;
	
	View rootView;
	Button button_test;
	//private TextView mText;
	//private TextView mTranslation;
	private Context mContext;
	
    public FragmentPhrase(Context context) {
		//Log.d("", "NEW");
    	mContext = context;
    	thisFragment = this;
    }
    
    /**
     * Prepare next step of the task
     */
    private void next() {
    	index = DrawerMenu.next();
    	
    	// 001 spa->ru completed  
    	// 010 ru->spa completed  
    	typeOfstep = DrawerMenu.getTypeOfTheStep(index); 
    	
        TextView mText = (TextView)rootView.findViewById(R.id.TextViewPhrase);
        TextView mTranslation = (TextView)rootView.findViewById(R.id.TextViewPhraseTranslation);
        
        if (typeOfstep == 1){
    		text = DrawerMenu.getText(index);
    		translation = Dictionary.getTranslation(text);
    		direction = 0;
        } else {
    		translation = DrawerMenu.getText(index);
    		text = Dictionary.getTranslation(translation);
    		direction = 1;
        }
		mText.setText(text);
		mTranslation.setText(translation);
		
		mTranslation.setVisibility(View.INVISIBLE);    	
		
		TextView TextViewPhraseInfo =  (TextView)rootView.findViewById(R.id.TextViewPhraseInfo);
		TextViewPhraseInfo.setText(DrawerMenu.getCountStr());
		
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("", "CREATE VIEW");
		prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

    	getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    	
        rootView = inflater.inflate(R.layout.fragment_phrase, container, false);

    	next();
        
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
					
					// result = true;
					mTranslation.setVisibility(View.VISIBLE);
					if (result){
						mTranslation.setTextColor(getColor(mContext, R.color.green1));
						DrawerMenu.setStepCompleted(index, typeOfstep);
						if (DrawerMenu.getDataSize() == 0){
							Toast.makeText(mContext, "THAT IS ALL", Toast.LENGTH_LONG).show();
							// Close Fragment
							getActivity().getFragmentManager().beginTransaction().remove(thisFragment).commit();
						}
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

		//Log.d("", "PAUSE");
		Editor editor = prefs.edit();

		String listString = "";

		listString = listString + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		listString = listString + "<data version = \"1\">\n";
		
		for (MenuGroup m: DrawerMenu.menuData){
			listString = listString + "<level0 title = \""+ m.title +"\">\n";
			for (MenuChild c: m.mChilren){
				listString = listString + "<level1 title = \""+ c.title +"\" type = \""+c.type+"\">\n";

				for (MarkedString s: DrawerMenu.textData.get( 
				
				DrawerMenu.menuData.indexOf(m)).get(m.mChilren.indexOf(c))){
					listString = listString + "<entry text = \""+ s.mText +"\" proc = \"" + s.mMarked + "\"></entry>\n"; 
				}
				
			}
		}
		listString = listString + "</data>";
		
		Log.d("", "->"+listString);
		
		editor.putString("efrwer", listString);
		
//		editor.commit();		
		
		editor.apply();
	}
	
	public String getTextR() {
		return "ertwerter";
	}	
	
}

