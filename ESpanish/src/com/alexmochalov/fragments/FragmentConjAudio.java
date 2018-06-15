package com.alexmochalov.fragments;

import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.*;

import com.alexmochalov.alang.*;
import com.alexmochalov.dictionary.Dictionary;
import com.alexmochalov.menu.MenuData;
import com.alexmochalov.main.*;
import com.alexmochalov.rules.*;

import java.util.*;

import android.view.View.OnClickListener;
import android.text.*;

public class FragmentConjAudio extends FragmentM {
	private Button button_test;
	private String tense = "";
	private String non = "";
	private ListAdapterConjAudio adapter;

	private ViewGroup mLinearLayout;


	ArrayList<Pronoun> objects = Rules.clonePronouns();

	/**
	 * Fill the header of this dialog: text for the exercise and translation othe the text 
	 * Fill Pronounses in the random or initial order
	 * Fill tenseInfo 
	 * 
	 */
	private void next(boolean onCreateView) {
		// Заполняем заголовок
		mTextViewText = (TextView) rootView.findViewById(R.id.text);
		mTranslation = (TextView) rootView.findViewById(R.id.translation);

		TextView tenseInfo = (TextView) rootView
				.findViewById(R.id.TextViewTenseInfo);

		MenuData.setText(mTextViewText, mTranslation);

		tense = "";
		if (MenuData.getTense().equals("past"))
			tense = "Прошедшее время";

		non = "";
		if (MenuData.getNeg() == 0)
			;
		else if (MenuData.getNeg() == 2)
			non = "Отрицание";
		else if (Math.random() > 0.5)
			non = "Отрицание";

		if (tense.length() > 0 && non.length() > 0)
			tenseInfo.setText("(" + tense + ", " + non + ")");
		else if (tense.length() > 0)
			tenseInfo.setText("(" + tense + ")");
		else if (non.length() > 0)
			tenseInfo.setText("(" + non + ")");
		else
			tenseInfo.setText("");

		if (Utils.getRandomize())
			setOrder();

		//setPronouns(MenuData.getText());
	
		
		adapter = new ListAdapterConjAudio(mContext, 
					objects, 
					mTextViewText.getText().toString());

		ListView listView = (ListView)rootView.findViewById(R.id.listView);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				adapter.setSelected(position);
				
			}});

		
	}

	/**
	 * Sets order of the Objects (strings on the screen): random or initial. 
	 */
	private void setOrder() {

		if (Utils.getRandomize()) {
			for (int i = 1; i <= objects.size(); i++) {
				int j = (int) (Math.random() * objects.size());
				int k = (int) (Math.random() * objects.size());

				Pronoun p1 = objects.get(j);
				Pronoun p2 = objects.get(k);

				Pronoun p = new Pronoun(p1);
				p1.copy( p2 );
				p2.copy( p );

			}}
		else {
			objects = Rules.clonePronouns();
		}
			
		adapter.setObjects(objects);
	}

	// je aimer -> j'aimer
	private String setPronounsToVowel(String pronoun, String text) {

		if ( pronoun.equals("JE") && Utils.isVowel(text.substring(0 ,1)))
				return "J'";
			
		return pronoun;
		
	}

	/*
	private void setPronouns(String text) {

		for (PronounEdited p : objects) {
			
			((TextView) p.mLayout.findViewById(R.id.translation)).setText(
					p.mPronoun.getTranslation().toUpperCase()+" ");
		}
		
	}
*/
	
	
	@Override
	public void onStart() {
		//setPronouns(MenuData.getText());
		
	
		
		super.onStart();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_conj_audio, container, false);
		// MenuData.nextTestIndex();

		init();
		CheckBox checkBox = (CheckBox) rootView
				.findViewById(R.id.checkBoxRandom);
		// Log.d("d", "MainActivity.r "+MainActivity.randomize);
		checkBox.setChecked(Utils.getRandomize());
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Utils.setRandomize(  isChecked );

				setOrder();

				//setPronouns(MenuData.getText());
				
				
			}
		});

		
		button_test = (Button) rootView.findViewById(R.id.button_test);
		button_test.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

					if (MenuData.nextIndex() == -1) {
						getActivity().getFragmentManager().beginTransaction()
								.remove(thisFragment).commit();
						;
					} else {
						next(false);
					}

			}
		});		
		
		final ImageButton ibRecord = (ImageButton)rootView.findViewById(R.id.ibRecord);
		ibRecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				adapter.record(objects.get(0), ibRecord);
		

			}
		});		
		
		String text = MenuData.getText();

		if (text.length() == 0)
			MenuData.nextIndex();
		else {
			if (MenuData.findIndex(text) == -1)
				MenuData.nextIndex();
		}

		// Fill this Dialog
		next(true);

		return rootView;
	}

	protected boolean test(PronounEdited p, boolean showAnswer) {
		boolean result = true;
/*
		EditText editText = (EditText) p.mLayout
				.findViewById(100 + objects.indexOf(p)); //R.id.EditTextTranslation
		
		String text = editText.getText().toString();

		String verb = MenuData.getText();

		String textSample = "";
		if (non.equals("Отрицание"))
			textSample = "non ";

		int index = Rules.getPronouns().indexOf(p.mPronoun);

		if (tense.equals("Прошедшее время"))
			textSample = textSample + Rules.conj(index, "avere", false)
					+ " " + Rules.conj(index, verb, true);
		else
			textSample = p.mPronoun.conj(verb, false);

		if (showAnswer)
			editText.setText(textSample);

		if (text.toLowerCase()
			.replaceAll("à", "a")
			.replaceAll("ò", "o")
			.replaceAll("ù", "u")
			.replaceAll("è", "e")
			.replaceAll("é", "e")
			.replaceAll("ì", "i")
			.equals(textSample.toLowerCase()
		.replaceAll("à", "a")
			.replaceAll("ò", "o")
			.replaceAll("ù", "u")
			.replaceAll("è", "e")
			.replaceAll("é", "e")
			.replaceAll("ì", "i"))) {
			editText.setTextColor(mContext.getResources().getColor(
					R.color.green2));
		} else {
			editText.setTextColor(Color.RED);
			result = false;
		}
*/
		return result;
	}

	public String getTextToTTS() {
		String s = MenuData.getText();
/*
		for (PronounEdited p : objects)
			s = s + ":" 
					+ setPronounsToVowel(p.mPronoun.getText().toUpperCase(), MenuData.getText()) 
					+ " "
					+ p.mPronoun.conj(MenuData.getText(), false);
*/
		return s.replace("' ", "'");
	}

}
