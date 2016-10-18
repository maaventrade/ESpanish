package com.alexmochalov.espanish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;   

import com.alex_mochalov.navdraw.R;
import com.alexmochalov.espanish.MenuData.MarkedString;
import com.alexmochalov.espanish.MenuData.MenuChild;
import com.alexmochalov.espanish.MenuData.MenuGroup;
import com.alexmochalov.espanish.FragmentMenu.OnMenuItemSelectedListener;
import com.alexmochalov.espanish.fragments.FragmentConj;
import com.alexmochalov.espanish.fragments.FragmentM;
import com.alexmochalov.espanish.fragments.FragmentPhrase;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

public class MainActivity extends Activity implements OnInitListener, 
OnMenuItemSelectedListener, FragmentM.OnTestedListener
{
	public static Context mContext;
	
	private SharedPreferences prefs;

	private final String MENU_GROUP_POSITION = "MENU_GROUP_POSITION";
	private final String MENU_CHILD_POSITION = "MENU_CHILD_POSITION";

	FragmentM fragment;
	FragmentMenu fragmentMenu;

	private int MY_DATA_CHECK_CODE = 0;
	private boolean  langSupported;
	private String language = "spa";

	TextToSpeech tts;
	Locale locale;

	int mGroupPosition;
	int mChildPosition;
	String mType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = this;
		
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setHomeButtonEnabled(true);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

//		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//		mDrawerTree = (ExpandableListView) findViewById(R.id.left_drawer_exp);

		Dictionary.load(this);
		MenuData.load(this, false);
		
		// 
		fragmentMenu = (FragmentMenu)getFragmentManager().findFragmentById(R.id.am_fragmentMenu);
		fragmentMenu.setMenu(this);
		fragmentMenu.mCallback = this;
		
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
		
		//if (savedInstanceState != null) {
        //    fragment = (Fragment) getFragmentManager().findFragmentByTag("your_fragment_tag");
       // }		

		Log.d("", "CREATE");
		
		int menuGroupPosition = prefs.getInt(MENU_GROUP_POSITION, -1);
		int menuChildPosition = prefs.getInt(MENU_CHILD_POSITION, -1);
		
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
		super.onDestroy(); 
	}
	
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
            case DialogInterface.BUTTON_POSITIVE:
				MenuData.resetDataFlag(mGroupPosition, mChildPosition);
				
				fragmentMenu.refresh();
				selectItem(mType);
				
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				break;
			}
		}
	};

	private void selectItem(String type) {

		FragmentManager fragmentManager = getFragmentManager();
		
		if (fragment != null){
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.detach(fragment);
			transaction.commit();
			fragment = null;
			
			fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}

		if (type.equals("Выражения"))
			fragment = new FragmentPhrase();
		else if (type.equals("Спряжения"))
			fragment = new FragmentConj();

		if (fragment != null) {
			
			fragment.setParams(this, mGroupPosition, mChildPosition);

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
		return true;
	}


	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		Editor editor = prefs.edit();
		editor.putInt(MENU_GROUP_POSITION, mGroupPosition);
		editor.putInt(MENU_CHILD_POSITION, mChildPosition);

		String listString = "";

		listString = listString + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		listString = listString + "<data version = \"1\">\n";
		
		for (MenuGroup m: MenuData.menuData){
			listString = listString + "<level0 title = \""+ m.title +"\">\n";
			for (MenuChild c: m.mChilren){
				listString = listString + "<level1 title = \""+ c.title +"\" type = \""+c.type+"\" note = \""+ c.note +"\">\n";

				for (MarkedString s: MenuData.textData.get( 
						MenuData.menuData.indexOf(m)).get(m.mChilren.indexOf(c))){
					listString = listString + "<entry text = \""+ s.mText +"\" flag = \"" + s.mFlag + "\"></entry>\n"; 
				}
				listString = listString + "</level1>\n";
			}
			listString = listString + "</level0>\n";
		}
		listString = listString + "</data>";
		Log.d("", "SAVE");
		Log.d("", "listString "+listString);
		editor.putString("efrwer", listString);
		
		editor.commit();
		
		super.onPause();
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
			return true;
		}
		case R.id.item_speak: {

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				ttsGreater21(((FragmentPhrase) fragment).getTextR());
			} else {
				ttsUnder20(((FragmentPhrase) fragment).getTextR());
			}

			return true;
		}
		case android.R.id.home:
			return true;
		}

		if (id == R.id.action_settings) {
			return true;
		} else

			return super.onOptionsItemSelected(item);
	}

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
	    	  		//Toast.makeText(this, locale[i].getLanguage().toUpperCase()+"  "+language, Toast.LENGTH_LONG).show();
	            	if (locale[i].getISO3Language().equals(language)){
	    	    	  	if (tts.isLanguageAvailable(locale[i]) == tts.LANG_NOT_SUPPORTED){
	    	    	  		Toast.makeText(this, getResources().getString(R.string.error_lang_not_supported)+" ("+language+") "+getResources().getString(R.string.error_lang), Toast.LENGTH_LONG).show();
	    	    	  		langSupported = false;
	    	    	  	} else {
	    	    	  		langSupported = true;
	    	    	  		tts.setLanguage(locale[i]);
	    	    	  	}	
	    	    	  	return; 
	            	}}
	            Toast.makeText(this, getResources().getString(R.string.error_lang_not_found)+" ("+language+") "+getResources().getString(R.string.error_lang), Toast.LENGTH_LONG).show();
	            
	      }
	      	else if (status == TextToSpeech.ERROR) {
	            Toast.makeText(this, "Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
	      }
	}

	@Override
	public void onMenuItemSelected(int groupPosition, int childPosition, String type) {
		mGroupPosition = groupPosition;
		mChildPosition = childPosition;
		mType = type;
		
		if (MenuData.getRestCount(mGroupPosition, mChildPosition) == 0){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(this.getResources().getString(R.string.startover)).setPositiveButton(
					this.getResources().getString(R.string.yes), dialogClickListener)
			    .setNegativeButton(this.getResources().getString(R.string.no), dialogClickListener).show();
		} else
			selectItem(type);
	}      
	
	@Override
	public void onTested(int groupPosition, int childPosition)
	{
		fragmentMenu.refresh();
	}

	@Override
	public void onFinished(Fragment thisFragment) {/*
		FragmentManager fragmentManager = getFragmentManager();
		if (thisFragment != null){
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.detach(thisFragment);
			transaction.commit();
			fragment = null;
			
			fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}*/
		fragmentMenu.refresh();
	}
	
}
