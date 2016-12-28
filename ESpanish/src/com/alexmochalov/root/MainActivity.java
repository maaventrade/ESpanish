package com.alexmochalov.root;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;   

import com.alexmochalov.alang.R;
import com.alexmochalov.dictionary.ArrayAdapterDictionary;
import com.alexmochalov.dictionary.Dictionary;
import com.alexmochalov.dictionary.Entry;
import com.alexmochalov.dictionary.EntryEditor;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import com.alexmochalov.files.Dic;
import com.alexmochalov.fragments.*;
import com.alexmochalov.menu.DialogScheme;
import com.alexmochalov.menu.FragmentMenu;
import com.alexmochalov.menu.MenuData;
import com.alexmochalov.menu.FragmentMenu.OnMenuItemSelectedListener;

public class MainActivity extends Activity implements OnInitListener, 
OnMenuItemSelectedListener, FragmentM.OnTestedListener
{
	public static Context mContext;
	
    ActionBar actionBar;
    Menu mMenu;
	
	private SharedPreferences prefs;

	private final String RANDOMIZE = "RANDOMIZE";
	
	FragmentM fragment;
	FragmentMenu fragmentMenu;
	
	public static boolean randomize = false;
	
	private int MY_DATA_CHECK_CODE = 0;
	private boolean  langSupported;

	TextToSpeech tts;
	Locale locale;

	String mType;
	
	//private int randomize;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = this;
		
		actionBar = getActionBar();
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setHomeButtonEnabled(true);
		if (Utils.getLanguage().equals("ita"))
			actionBar.setIcon(R.drawable.ic_launcher1);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		Dictionary.load(this);
		MenuData.load(this, false);

		loadParameters();
		
		// 
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
		
		
		//if (savedInstanceState != null) {
        //    fragment = (Fragment) getFragmentManager().findFragmentByTag("your_fragment_tag");
       // }		

		//Log.d("", "CREATE");
		
//		DrawerMenu.setPositions(menuGroupPosition, menuChildPosition);

		//if (menuChildPosition >= 0 &&  DrawerMenu.getDataSize() > 0) {
		//	selectItem(DrawerMenu.getType());
		//}
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				tts = new TextToSpeech(this, this);
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
		if (tts != null) tts.shutdown();
		saveParameters();
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
		}

		if (type.equals("Выражения") || type.equals("Комбинации")){
			fragment = new FragmentPhrase();
		} else if (type.equals("Спряжения")){
			fragment = new FragmentConj();
		} else if (type.equals("Говорим")){
			fragment = new FragmentSpeak();
		} else if (type.equals("Запоминание")){
			fragment = new FragmentRemember();
		}
		
		if (fragment != null) {
			//fragment.setParams(this, mGroupPosition, mChildPosition, 0);
	    	//mIndex = MenuData.nextTestIndex(mGroupPosition, mChildPosition, mIndex);
			//fragment.saveParams(this);

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
		
		File file = new File(Utils.REC_FOLDER);
		if(!file.exists()){                          
			file.mkdirs();                  
		}
		
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void saveParameters() {
		Editor editor = prefs.edit();
		editor.putBoolean(RANDOMIZE, randomize);

		Utils.resetFile();
		Utils.writeFile(RANDOMIZE, ""+randomize);
		
		MenuData.saveParameters(editor);
		
		editor.commit();
	}
	
	private void loadParameters() {
		MenuData.loadParameters(prefs);
		
//		randomize = prefs.getBoolean(RANDOMIZE, false);
		randomize = Utils.readBoolean(RANDOMIZE, false);
		
	}
	
	@SuppressWarnings("deprecation")
	private void ttsUnder20(String text) {
		HashMap<String, String> map = new HashMap<>();
		map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void ttsGreater21(String text) {
		String utteranceId = this.hashCode() + "";
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
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
			if (fragment != null){
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				ttsGreater21(fragment.getTextToTTS());
			} else {
				ttsUnder20(fragment.getTextToTTS());
			}
			}
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
				DialogScheme d = new DialogScheme(this, MenuData.getHelpIndex());
				d.mCallback = new DialogScheme.OnDialogSchemeButtonListener() {
					@Override
					public void onSpeakButtonPressed(String text) {
						text = Html.fromHtml(text).toString();
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
							ttsGreater21(text);
						} else {
							ttsUnder20(text);
						}
					}
				};
				d.show();
				
			return true;
		default:	return super.onOptionsItemSelected(item);
		}
		
	}
//EntryEditor
	private void loadLangueges(){
    	Locale locale[] = Locale.getAvailableLocales();
        //Spinner spinnerLanguages = ((Spinner)findViewById(R.id.spinnerLanguages));
    	/*
        ArrayList<String> languages = new ArrayList<String>();
        for (int i=0; i< locale.length; i++)
        	if (tts.isLanguageAvailable(locale[i]) != tts.LANG_NOT_SUPPORTED)
        		if (! languages.contains(locale[i].getISO3Language()))
        			languages.add(locale[i].getISO3Language()); 
        adapter = new ArrayAdapter(this,
        		android.R.layout.simple_spinner_item, languages);
        spinner.setAdapter(adapter);
        */
    }
	
	@Override
	public void onInit(int status) {
	      if (status == TextToSpeech.SUCCESS) {
	    	  	loadLangueges();
	    	  
	    	  	langSupported = false;
	    	  	/*
	    	  	if (tts.isLanguageAvailable(Locale.JAPAN) != tts.LANG_NOT_SUPPORTED){ 
	    	  		tts.setLanguage(Locale.JAPAN);
		    	  	langSupported = true;
	    	  	} else
	    	  		Toast.makeText(this, getResources().getString(R.string.error_lang_not_supported)+" ("+language+")", Toast.LENGTH_LONG).show();
	    	  	*/
	    	  	
	        	Locale locale[] = Locale.getAvailableLocales();
	            for (int i=0; i< locale.length; i++){
	            	//Log.d("", ""+locale[i].getISO3Language());
	    	  		//Toast.makeText(this, locale[i].getLanguage().toUpperCase()+"  "+language, Toast.LENGTH_LONG).show();
	            	if (locale[i].getISO3Language().equals(Utils.getLanguage())){
	    	    	  	if (tts.isLanguageAvailable(locale[i]) == tts.LANG_NOT_SUPPORTED){
	    	    	  		Toast.makeText(this, getResources().getString(R.string.error_lang_not_supported)+" ("+Utils.getLanguage()+") "+getResources().getString(R.string.error_lang), Toast.LENGTH_LONG).show();
	    	    	  		langSupported = false;
	    	    	  	} else {
	    	    	  		langSupported = true;
	    	    	  		tts.setLanguage(locale[i]);
	    	    	  	}	
	    	    	  	return; 
	            	}}
	            Toast.makeText(this, getResources().getString(R.string.error_lang_not_found)+" ("+Utils.getLanguage()+") "+getResources().getString(R.string.error_lang), Toast.LENGTH_LONG).show();
	            
	      }
	      	else if (status == TextToSpeech.ERROR) {
	            Toast.makeText(this, "Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
	      }
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
	
	@Override
	public void onTested()
	{
		fragmentMenu.refresh();
	}

	@Override
	public void onFinished(Fragment thisFragment) {
		fragmentMenu.refresh();
	}
	
}
