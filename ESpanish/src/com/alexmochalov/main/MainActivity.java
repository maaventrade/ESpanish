package com.alexmochalov.main;

import java.io.File;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alexmochalov.alang.R;
import com.alexmochalov.dialogs.DialogHelp;
import com.alexmochalov.dialogs.SelectFileDialog;
import com.alexmochalov.dictionary.Dictionary;
import com.alexmochalov.dictionary.DictionaryDialog;
import com.alexmochalov.fragments.FragmentConj;
import com.alexmochalov.fragments.FragmentConjAudio;
import com.alexmochalov.fragments.FragmentM;
import com.alexmochalov.fragments.FragmentPhrase;
import com.alexmochalov.fragments.FragmentRemember;
import com.alexmochalov.fragments.FragmentSpeak;
import com.alexmochalov.menu.FragmentMenu;
import com.alexmochalov.menu.FragmentMenu.OnMenuItemSelectedListener;
import com.alexmochalov.menu.MenuData;
import com.alexmochalov.rules.Rules;
import com.alexmochalov.rules.EntryEditor;

public class MainActivity extends Activity implements OnInitListener, 
OnMenuItemSelectedListener, FragmentM.OnTestedListener
{
	public static Context mContext;
	
    ActionBar actionBar;
    Menu mMenu;
	
	private SharedPreferences prefs;

	private final static String RANDOMIZE = "RANDOMIZE";
	private final static String HELPTEXTSCALE = "HELPTEXTSCALE";
	private final static String MTEXT = "TEXT";
	private final static String RANDOMIZATION_ORDER = "RANDOMIZATION_ORDER";	
	private final static String LANGUAGE = "LANGUAGE";	
	private final static String DICT_PATH = "DICT_PATH";	
	
	
	
	private final static String DIC = "DIC";
	private final static String DIC_LASTWORD = "DIC_LASTWORD";
	private final static String DIC_TEXT = "DIC_TEXT";
	
	FragmentM fragment;
	FragmentMenu fragmentMenu;
	
	private int MY_DATA_CHECK_CODE = 0;
	private boolean  langSupported;

	private String mPath = "";
	private String mFileName = "";
	private final String fileExt[] = { "xdxf" };
	
	String mType;
	
	//private int randomize;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		mContext = this;

		actionBar = getActionBar();
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setHomeButtonEnabled(true);

		Utils.setLanguage(prefs.getString(LANGUAGE, "ita"), actionBar);
		
		Rules.load(this);
		MenuData.load(this, false);

		if (savedInstanceState != null){
			Utils.setRandomize( savedInstanceState.getBoolean(RANDOMIZE));
			Utils.setScale( savedInstanceState.getInt(HELPTEXTSCALE));
			
			Dictionary.setDictionaryName(savedInstanceState.getString(DIC));
			
			Dictionary.setLastWord(savedInstanceState.getString(DIC_LASTWORD));
			Dictionary.setText(savedInstanceState.getString(DIC_TEXT));
			
		} else {
			Utils.setScale( prefs.getInt(HELPTEXTSCALE, 110));
		}
		
		loadParameters();
		
		fragmentMenu = (FragmentMenu)getFragmentManager().findFragmentById(R.id.am_fragmentMenu);
		fragmentMenu.setMenu(this);
		fragmentMenu.mCallback = this;
		
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
		
		Dictionary.load(prefs.getString(DICT_PATH, ""), this);
		
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		MenuData.setText(savedInstanceState.getString(MTEXT));
		Utils.setRandomize(savedInstanceState.getBoolean(RANDOMIZE));
		Utils.setScale( savedInstanceState.getInt(HELPTEXTSCALE));
		
		//Log.d("aaa", "SET HELPTEXTSCALE "+savedInstanceState.getInt(HELPTEXTSCALE));
		
		MenuData.putRandomizationOrder(savedInstanceState.getIntArray(RANDOMIZATION_ORDER));
		
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putString(MTEXT, MenuData.getText());
		outState.putBoolean(RANDOMIZE, Utils.getRandomize());
		outState.putIntArray(RANDOMIZATION_ORDER, MenuData.getRandomizationOrder());
		
		outState.putInt( HELPTEXTSCALE, Utils.getScale());
		
		outState.putString( DIC, Dictionary.getDictionaryName());
		
		outState.putString(DIC_LASTWORD, Dictionary.getLastWord());
		outState.putString(DIC_TEXT, Dictionary.getText());
		//Log.d("aaa", "PUT "+Utils.getScale());
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				TtsUtils.newTts(this);
			} else {
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}

		} else
			super.onActivityResult(requestCode, resultCode, data);
	}

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
            case DialogInterface.BUTTON_POSITIVE:
				MenuData.resetDataFlag();
				
				fragmentMenu.refresh();
				selectItem(mType);
				
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				break;
			}
		}
	};

	private void selectItem(String type) {

		//String phragment_tag = "";
		
		FragmentManager fragmentManager = getFragmentManager();
		
		if (fragment != null){
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.detach(fragment);
			transaction.commit();
			fragment = null;
			
			fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			
			MenuData.setIndex(-1);
		}

		if (type.equals("Выражения") 
			|| type.equals("Комбинации")
			|| type.equals("Комбинации1")
			){
			fragment = new FragmentPhrase();
		} else if (type.equals("Спряжения")){
			fragment = new FragmentConj();
		} else if (type.equals("СпряженияАудио")){
			fragment = new FragmentConjAudio();
		} else if (type.equals("Говорим")){
			fragment = new FragmentSpeak();
		} else if (type.equals("Запоминание")){
			fragment = new FragmentRemember();
		}
		
		if (fragment != null) {

			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.fragment_container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
			
			fragment.mCallback = this;
		} else {
			Log.e("MainActivity", "Error in creating fragment");
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		mMenu = menu;
		
		return true;
	}


	@Override
	public void onResume() {
		super.onResume();
		//Log.d("a", "onResume");
		File file = new File(Utils.REC_FOLDER);
		if(!file.exists()){                          
			file.mkdirs();                  
		}
		
	}

	@Override
	public void onPause() {
		TtsUtils.destroy();
		
		Editor editor = prefs.edit();
		
		MenuData.saveParameters(editor);

		editor.putInt(HELPTEXTSCALE, Utils.getScale());
		editor.putString(LANGUAGE, Utils.getLanguage());
		editor.putString(DICT_PATH, Dictionary.getDictionaryName());
		
		editor.commit();
		
		super.onPause();
	}

	private void loadParameters() {
		MenuData.loadParameters(prefs);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();

		switch (item.getItemId()) {

		case R.id.action_reread: {
			MenuData.load(this, true);
			fragmentMenu.refresh();
			return true;
		}
		//case R.id.action_reindex: {
		//		Dictionary.unzip(this, "it_ru.xdxf", R.raw.it_ru);
		//		Dictionary.createIndexAsinc(this, "it_ru.xdxf");
		//		return true;
		//}
		
		case R.id.item_speak: {
			if (fragment != null)
				TtsUtils.speak(fragment.getTextToTTS());
			return true;
		}
		case android.R.id.home: {
	        actionBar.setDisplayHomeAsUpEnabled(false);
	        actionBar.setDisplayShowCustomEnabled(false);	        
	        actionBar.setTitle("ESpanish");

	        MenuItem i = mMenu.findItem(R.id.item_dictionary);
	        i.setVisible(true);
	        
			return true;
		}	
		case R.id.action_select_language: 
			
			Dialog dialog = createDialogSelectLanguage();
			dialog.show();
			
			return true;
		case R.id.item_dictionary: {
			if (Dictionary.getEntries().size() == 0)
				dialogSelectDictionary();				
			else
			new DictionaryDialog().show(getFragmentManager(), "tag"); 
			
			
			/*
			LayoutInflater inflator = (LayoutInflater) this
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View v = inflator.inflate(R.layout.search_layout, null);
	        
	        actionBar.setDisplayHomeAsUpEnabled(true);
	        actionBar.setDisplayShowCustomEnabled(true);	        
	        actionBar.setCustomView(v);
	        actionBar.setTitle("Search:");
	        
	        item.setVisible(false);

			EntryEditor.start(this, v);
			*/
			
			return true;
		} 
		case R.id.item_help:
			
			final int index = MenuData.getHelpIndex();
			
			DialogHelp d = new DialogHelp(this, index);
			if (index == 999 && fragment != null)
				d.setWord(fragment.getWord());
			
			d.show();
			
			return true;
		default:	return super.onOptionsItemSelected(item);
		}
		
	}

	public void dialogSelectDictionary() {
		SelectFileDialog selectFileDialog = new SelectFileDialog(mContext,
				mPath, mFileName, fileExt, "Select subtitle file", false,
				false, "");
		selectFileDialog.callback = new SelectFileDialog.MyCallback() {

			@Override
			public void callbackACTION_SELECTED(String fileName) {

				if ( Dictionary.copy(fileName, getApplicationContext())){

					Dictionary.load( Utils.getFileName(fileName), mContext);
					
				}
				
				mFileName = fileName;

				if (fileName.lastIndexOf("/") >= 0)
					mPath = fileName.substring(0, fileName.lastIndexOf("/"));

			}
		};

		selectFileDialog.show();
	}
	
	
	String language = "";
	private Dialog createDialogSelectLanguage() {
		// Initialize the Alert Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//Source of the data in the DIalog
		CharSequence[] array = {"Spanish", "Italian", "Franch"};
		

		// Set the dialog title
		builder.setTitle("Select Language")
		// Specify the list array, the items to be selected by default (null for none),
		// and the listener through which to receive callbacks when items are selected
		.setSingleChoiceItems(array, 1, new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == 0)
				language = "spa";
			else if (which == 1)
				language = "ita";
			else if (which == 2)
				language = "fra";

		}
		})

		// Set the action buttons
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int id) {
			
			Utils.setLanguage(language, actionBar);
			
			//if (Utils.getLanguage().equals("ita"))
			//	actionBar.setIcon(R.drawable.ic_launcher1);

			Rules.load(getApplicationContext());
			MenuData.load(getApplicationContext(), true);
			fragmentMenu.refresh();
			TtsUtils.init(getApplicationContext());

		}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int id) {

		}
		});

		return builder.create();
	}

	@Override
	public void onInit(int status) {
	      if (status == TextToSpeech.SUCCESS) 
		    TtsUtils.init(this);
	}

	@Override
	public void onMenuItemSelected(int groupPosition, int childPosition, String type) {
		mType = type;
		
		MenuData.setPosition(groupPosition, childPosition);
		
		if (MenuData.getRestCount(groupPosition, childPosition) == 0){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(this.getResources().getString(R.string.startover)).setPositiveButton(
					this.getResources().getString(R.string.yes), dialogClickListener)
			    .setNegativeButton(this.getResources().getString(R.string.no), dialogClickListener).show();
		} else
			selectItem(type);
	}      
	
	// 
	// 
	//
	@Override
	public void onTested()
	{
		fragmentMenu.refresh();
	}

	@Override
	public void onFinished(Fragment thisFragment) {
		fragmentMenu.refresh();
	}

	@Override
	public void onButtonStartTestingClick() {
		selectItem("Выражения");
	}

	@Override
	  protected void onDestroy() {
	   
		super.onDestroy();
	    //releasePlayer();
	    Media.releaseRecorder();
	    Media.releasePlayer();
	    
	  }	
}
