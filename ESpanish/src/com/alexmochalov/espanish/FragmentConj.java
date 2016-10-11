package com.alexmochalov.espanish;

import java.util.ArrayList;
import java.util.List;

import com.alex_mochalov.navdraw.R;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentConj extends Fragment {
	private SharedPreferences prefs;
	private Fragment thisFragment;

	private String text;
	// Current translation
	private String translation;
	
	private int index = 0;
	
	private TextView mText;
	private TextView mTranslation;
	
	private ConjAdapter conjAdapter;
	
	private View rootView;
	private Button button_test;
	private Context mContext;
	
    public FragmentConj(Context context) {
    	mContext = context;
    	thisFragment = this;
    }
 
    private void next() {
    	index = DrawerMenu.next();
    	
        mText = (TextView)rootView.findViewById(R.id.text);
        mTranslation = (TextView)rootView.findViewById(R.id.translation);
        
		text = DrawerMenu.getText(index);
		
		mText.setText(text);
		mTranslation.setText(Dictionary.getTranslation(text));
		
		conjAdapter.setVerb(text);
		//conjAdapter.notifyDataSetChanged();
	}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.fragment_conj, container, false);
        
		conjAdapter = new ConjAdapter(mContext, text);
	    ListView listView = (ListView)rootView.findViewById(R.id.conjugations);
	    listView.setAdapter(conjAdapter);			

    	next();
	    
	    button_test = (Button)rootView.findViewById(R.id.button_conj_test);
	    button_test.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				// Button Next is pressed
				if (button_test.getText().equals(mContext.getResources().getString(R.string.button_next))){
					next();
					button_test.setText(mContext.getResources().getString(R.string.button_test));
					conjAdapter.setAnswer(false);
					conjAdapter.notifyDataSetChanged();
				}	
				else {
					// Button Проверить is pressed
					
					button_test.setText(mContext.getResources().getString(R.string.button_next));
					conjAdapter.setAnswer(true);
					conjAdapter.notifyDataSetChanged();
//					EditText editText = (EditText)rootView.findViewById(R.id.editText_phrase_transl);
/*
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
					*/
				}	
				
			}});
		
        return rootView;
    }


}