package com.alexmochalov.fragments;

import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.CompoundButton.*;

import com.alexmochalov.alang.*;
import com.alexmochalov.dictionary.*;
import com.alexmochalov.menu.MenuData;
import com.alexmochalov.main.*;

import java.util.*;

import android.view.View.OnClickListener;

import com.alexmochalov.dictionary.Dictionary;
import android.text.*;

public class FragmentConj extends FragmentM
{
	private Button button_test;
	private String tense = "";
	private String non = "";
	
	//private Handler h = new Handler();
	private EditText editTextLast;
	
	ViewGroup mLinearLayout;

	static class PronounEdited
	{

		private int mIndex;
		Pronoun mPronoun = null;
		View mLayout = null;
		

		public int getIndex()
		{
			return mIndex;
		}
		
		public PronounEdited(Pronoun pronoun, View layout, int index)
		{
			mPronoun = pronoun;
			//Log.d("my", "!!!yyy "+pronoun);
			//Log.d("d","+ "+pronoun.getText()+" "+index);
			
			mLayout = layout;
			mIndex = index;
		}
		
		public void put(PronounEdited from)
		{
			mPronoun = from.mPronoun;
			mIndex = from.mIndex;
			//mLayout = from.mLayout;
		}

		public PronounEdited getCopy()
		{
		//Log.d("d", "getClpy "+mPronoun.getText()+" "+mIndex);
			return new PronounEdited(mPronoun, mLayout, mIndex);
		}
	};

	ArrayList<PronounEdited> objects = new ArrayList<PronounEdited>();

    private boolean next()
	{
    	// Заполняем заголовок
    	mTextViewText = (TextView)rootView.findViewById(R.id.text);
        mTranslation = (TextView)rootView.findViewById(R.id.translation);

        TextView tenseInfo = (TextView)rootView.findViewById(R.id.TextViewTenseInfo); 

        MenuData.setText(mTextViewText, mTranslation);

		tense = "";
		if (MenuData.getTense().equals("past"))
			tense = "Прошедшее время";

		non = "";
		if (MenuData.getNeg() == 0);
		else if (MenuData.getNeg() == 2) non = "Отрицание";
		else if (Math.random() > 0.5) non = "Отрицание";

		if (tense.length() > 0 && non.length() > 0)
			tenseInfo.setText("(" + tense + ", " + non + ")");
		else if (tense.length() > 0)
			tenseInfo.setText("(" + tense + ")");
		else if (non.length() > 0)
			tenseInfo.setText("(" + non + ")");
		else
			tenseInfo.setText("");

		if (MainActivity.randomize)
			setOrder();

		setVerb(MenuData.getText());

		return true;
	}


	public static String firstLetterToUpperCase(String t)
	{
		Log.d("my", "translation " + t);
		return t.substring(0, 1).toUpperCase() + t.substring(1);
	}


    private void setOrder()
	{
		if (MainActivity.randomize)
		{
		
			for (int i = 1; i <= objects.size(); i++)
			{
				int j = (int)(Math.random() * objects.size());
				int k = (int)(Math.random() * objects.size());

				PronounEdited p = objects.get(j).getCopy();
				objects.get(j).put(objects.get(k));
				objects.get(k).put(p);
			}
			
		
			
		}
		else
		{ 
			for (int i = 0; i < objects.size(); i++){
			for (int k = 0; k < objects.size(); k++){
				if (objects.get(k).mIndex == i){
					PronounEdited p = objects.get(i).getCopy();
					objects.get(i).put(objects.get(k));
					objects.get(k).put(p);
					break;
				}
			}
			}
		
		/*
		PronounComparator ec = new PronounComparator();
			java.util.Collections.sort(objects, ec);	
			
			for (PronounEdited o:objects)
				Log.d("d",o.mPronoun.getText()+" "+o.getIndex());
				
			
				*/
			
				
		}
	}


	static private final class PronounComparator implements Comparator<PronounEdited> {
		public int compare(PronounEdited e1, PronounEdited e2) {
		    return e1.mIndex - e2.mIndex;
		}
	}
	
    private void setVerb(String text)
	{

    	for (PronounEdited p: objects)
		{
    	    ((TextView)p.mLayout.findViewById(R.id.text)).setText(Utils.firstLetterToUpperCase(p.mPronoun.getText()));
    	    ((TextView)p.mLayout.findViewById(R.id.translation)).setText(p.mPronoun.getTranslation());
    	}


	}

	@Override
    public void onStart()
	{
		//  mText = MenuData.getText(MainActivity.mGroupPosition, MainActivity.mChildPosition, mIndex);
		setVerb(MenuData.getText());

		super.onStart();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

        rootView = inflater.inflate(R.layout.fragment_conj, container, false);
        MenuData.nextTestIndex();

        init();
		CheckBox checkBox = (CheckBox)rootView.findViewById(R.id.checkBoxRandom);
		//Log.d("d", "MainActivity.r "+MainActivity.randomize);
		checkBox.setChecked(MainActivity.randomize);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
											 boolean isChecked)
				{
					MainActivity.randomize = isChecked;

					//Log.d("d", "set "+MainActivity.randomize);
					
					
					setOrder();

					setVerb(MenuData.getText());
				}});

    	
		mLinearLayout = (ViewGroup)rootView.findViewById(R.id.fc_linearLayout);
		
	    ArrayList<Pronoun> pronouns = Dictionary.getPronouns();
		for (Pronoun p: pronouns)
		{
			View layout2 = null;
			//if (Dictionary.getPronouns().indexOf(p) == Dictionary.getPronouns().size()-1){
				//layout2 = LayoutInflater.from(mContext).inflate(R.layout.fragment_conj_item_last, mLinearLayout, false);
			//}
				
			//else
				layout2 = LayoutInflater.from(mContext).inflate(R.layout.fragment_conj_item, mLinearLayout, false);
				//ImageButton btn = (ImageButton)layout2.findViewById(R.id.fragmentconjitemlastImageButtonOk);
			mLinearLayout.addView(layout2);
			PronounEdited pe = new PronounEdited(p, layout2,
												 pronouns.indexOf(p));
			objects.add(pe);
			
			EditText editText = (EditText)layout2.findViewById(R.id.EditTextTranslation);
			
			editText.setOnFocusChangeListener(new OnFocusChangeListener(){
					@Override
					public void onFocusChange(View view, boolean hasFocus)
					{
						
							for (PronounEdited p: objects)
							{
					            EditText editText = (EditText)p.mLayout.findViewById(R.id.EditTextTranslation); 
				    			if (editText == view){
									if (!hasFocus)
										test(p,false);
									else {
										 
										editText.setTextColor(Color.BLACK);
									}
									break;
								}
							}
						
					}
				});
				

			if (pronouns.indexOf(p) == pronouns.size()-1){
				editTextLast = editText;
				
				editTextLast.addTextChangedListener(new TextWatcher(){

						@Override
						public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
						{
			
						}

						@Override
						public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
						{
							// TODO: Implement this method
						}

						@Override
						public void afterTextChanged(Editable view)
						{
							
										test(objects.get(objects.size()-1),false);
									
						
								
							
						}
					});
			}
		}


	    button_test = (Button)rootView.findViewById(R.id.button_test);
	    button_test.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{

					// Button Next is pressed
					if (button_test.getText().equals(mContext.getResources().getString(R.string.button_next)))
					{

						if (MenuData.nextIndex() == -1)
						{
							getActivity().getFragmentManager().beginTransaction().remove(thisFragment).commit();;
						}
						else
						{
							next();
							button_test.setText(mContext.getResources().getString(R.string.button_test));
					    	for (PronounEdited p: objects)
							{
					            EditText editText = (EditText)p.mLayout.findViewById(R.id.EditTextTranslation); 
				    			editText.setTextColor(Color.BLACK);
					            editText.setText("");
							}

				    	}
					}	
					else
					{
						// Button Проверить is pressed
						button_test.setText(MainActivity.mContext.getResources().getString(R.string.button_next));

						boolean allChecked = true;

				    	for (PronounEdited p: objects)
						{
				    		allChecked = allChecked & test(p, true);
				    	}

				    	//allChecked = true;

						if (allChecked)
						{

							setTested(3);

						}

					}	

				}});
				
		String text = MenuData.getText();
		
		if (text.length() == 0) 
			MenuData.nextIndex();
		else {
			if (MenuData.findIndex(text) == -1)
				MenuData.nextIndex();
		}
			
		next();
		
		return rootView;
    }
	/*
	Runnable testLastTextView = new Runnable() {
		public void run() {
			
			PronounEdited p = objects.get(objects.size() - 1);
			
			EditText editText = (EditText)p.mLayout.findViewById(R.id.EditTextTranslation); 
			if (editText.hasFocus())
					test(p,false);
				else 
					editText.setTextColor(Color.BLACK);
			
			h.postDelayed(testLastTextView, 50);
		}
	};
*/
	protected boolean test(PronounEdited p, boolean showAnswer)
	{
		boolean result = true;

		EditText editText = (EditText)p.mLayout.findViewById(R.id.EditTextTranslation);
        String text = editText.getText().toString();

        String verb = MenuData.getText();

        String textSample = "";
        if (non.equals("Отрицание"))
            textSample = "non ";

        int index = Dictionary.getPronouns().indexOf(p.mPronoun);

        if (tense.equals("Прошедшее время"))
        	textSample = textSample + 
				Dictionary.conj(index, "avere", false) +
				" " +
				Dictionary.conj(index, verb, true);
        else 
			textSample = p.mPronoun.conj(verb, false);

		if (showAnswer)
       		 editText.setText(textSample);

		if (text.toLowerCase().
			replaceAll("á", "a").
			replaceAll("ó", "o").
			replaceAll("ú", "u").
			replaceAll("é", "e").
			replaceAll("í", "e").

			equals(textSample.toLowerCase().
				   replaceAll("á", "a").
				   replaceAll("ó", "o").
				   replaceAll("ú", "u").
				   replaceAll("é", "e").
				   replaceAll("í", "e")
				   ))
		{
			editText.setTextColor(mContext.getResources().getColor(R.color.green2));
		}
		else
		{
			editText.setTextColor(Color.RED);
			result = false;
		}

		return result;
	}


	public String getTextToTTS()
	{
		String s = MenuData.getText();

    	for (PronounEdited p: objects)
    		s = s + ":" + p.mPronoun.getText() + " " + p.mPronoun.conj(MenuData.getText(), false);

		return s;
	}	

}
