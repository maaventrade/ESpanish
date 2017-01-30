package com.alexmochalov.main;

import java.io.File;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
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
import com.alexmochalov.dictionary.Dictionary;
import com.alexmochalov.dictionary.EntryEditor;
import com.alexmochalov.files.Dic;
import com.alexmochalov.fragments.FragmentConj;
import com.alexmochalov.fragments.FragmentM;
import com.alexmochalov.fragments.FragmentPhrase;
import com.alexmochalov.fragments.FragmentRemember;
import com.alexmochalov.fragments.FragmentSpeak;
import com.alexmochalov.menu.FragmentMenu;
import com.alexmochalov.menu.FragmentMenu.OnMenuItemSelectedListener;
import com.alexmochalov.menu.MenuData;

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
	
	FragmentM fragment;
	FragmentMenu fragmentMenu;
	
	private int MY_DATA_CHECK_CODE = 0;
	private boolean  langSupported;

	String mType;
	
	//private int randomize;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = this;
		Log.d("aaa", "onCreate");
		actionBar = getActionBar();
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setHomeButtonEnabled(true);
		if (Utils.getLanguage().equals("ita"))
			actionBar.setIcon(R.drawable.ic_launcher1);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		Dictionary.load(this);
		MenuData.load(this, false);

		if (savedInstanceState != null){
			Utils.setRandomize( savedInstanceState.getBoolean(RANDOMIZE));
			Utils.setScale( savedInstanceState.getInt(HELPTEXTSCALE));
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
		
		if (!Dic.fileExists("it_ru.xdxf")){
			Dic.unzip(this, "it_ru.xdxf", R.raw.it_ru);
			Dic.createIndexAsinc(this, "it_ru.xdxf");
		} else
			Dic.loadIndex(this, "it_ru.index");
		
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

	@Override 
	protected void onDestroy() {
		TtsUtils.destroy();
		
		Editor editor = prefs.edit();
		
		MenuData.saveParameters(editor);

		editor.putInt(HELPTEXTSCALE, Utils.getScale());
		
		editor.commit();
		
		super.onDestroy(); 
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
		//Log.d("a", "onPause");
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
		case R.id.item_dictionary: {
			LayoutInflater inflator = (LayoutInflater) this
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View v = inflator.inflate(R.layout.search_layout, null);
	        
	        actionBar.setDisplayHomeAsUpEnabled(true);
	        actionBar.setDisplayShowCustomEnabled(true);	        
	        actionBar.setCustomView(v);
	        actionBar.setTitle("Search:");
	        
	        item.setVisible(false);

			EntryEditor.start(this, v);
			
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
	
}
