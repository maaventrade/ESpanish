package com.alexmochalov.main;

import android.app.*;
import android.content.*;
import android.content.SharedPreferences.Editor;
import android.os.*;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.*;
import android.text.method.ScrollingMovementMethod;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AdapterView.*;

import java.util.*;

import com.alexmochalov.dic.ArrayAdapterDictionary;
import com.alexmochalov.dic.Dictionary;
import com.alexmochalov.dic.Entry;
import com.alexmochalov.dic.FragmentDic;
import com.alexmochalov.dic.FragmentDic.FragmentDicCallback;
import com.alexmochalov.dic.FragmentTranslation;
import com.alexmochalov.dic.IndexEntry;
import com.alexmochalov.ddic.R;
import com.alexmochalov.tree.FragmentTree;

public class MainActivity extends Activity implements OnClickListener, OnInitListener{

	private FragmentDic fragmentDic;
	private String TAG_FRAGMENT_DIC = "TAG_FRAGMENT_DIC";

	private FragmentTranslation fragmentTranslation;
	private String TAG_FRAGMENT_TRANSL = "TAG_FRAGMENT_TRANSL";

	private FragmentTree fragmentTree;
	private String TAG_FRAGMENT_TREE = "TAG_FRAGMENT_TREE";
	
	private final static String DICTIONARI_NAME = "DICTIONARI_NAME";
	

	private String CURRENTFRAG = "CURRENTFRAG";
	
	private Activity mContext;

	private int MY_DATA_CHECK_CODE = 0;
	
	private int mode = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		mContext = this;
		// No title bar is set for the activity
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Full screen is set for the Window
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// hideSystemUI();
		// hideSystemUI();
		// Log.d("a",""+Dictionary);

		//getActionBar().hide();
		SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(this);
		
		String name = prefs.getString(DICTIONARI_NAME, "en_ru.xdxf");
		Utils.setDictionaryName(name);
		Dictionary.setParams(mContext);
		
		Dictionary.eventCallback = new Dictionary.EventCallback() {
			@Override
			public void loadingFinishedCallBack(boolean result) {
				if (result) {
					//fragmentDic.setAdapter();
					/*
					if (adapter == null){
						Utils.addInformation(" new ArrayAdapterDictionary  ");				
						adapter = new ArrayAdapterDictionary(mContext,
								R.layout.dic_string,
								(ArrayList<IndexEntry>) Dictionary
										.getIndexEntries());

						lvDictionary.setAdapter(adapter);
						lvDictionary
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> adapterView, View p2,
											int position, long p4) {
										currentPosition = position;
										if (callback != null) 
											callback.itemSelected(adapter.getItem(position));
											//Log.d("e","= "+adapter.getItem(position).getText());
										View view = mContext.getCurrentFocus();
										if (view != null) {  
										    InputMethodManager imm = (InputMethodManager)mContext.
										    		getSystemService(Context.INPUT_METHOD_SERVICE);
										    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
										}										
										
									}
								});
					} else {
						adapter.notifyDataSetChanged();
						Log.d("e","changed");
					}
					
					adapter.callback = new ArrayAdapterDictionary.AdapterCallback() {
						@Override
						public void valuesFiltered() {
							if (currentPosition >= 0){
								lvDictionary.setSelection(currentPosition);
								IndexEntry IndexEntry = (IndexEntry)adapter.getItem(currentPosition);
								if (IndexEntry != null)
									if (callback != null) 
										callback.itemSelected(IndexEntry);
								//currentPosition = -1;
							}
						}
					};
					
					// If some text in the Search string exists, execute an action "setText"
					if (!etEntry.getText().toString().equals(""))
						etEntry.setText(etEntry.getText().toString());
					// If position in ListView was stored, set this position
					else if (currentPosition >= 0){
						lvDictionary.setSelection(currentPosition);
						if (callback != null) 
							callback.itemSelected(adapter.getItem(currentPosition));
					}
					*/
				} else {
					// If Index file not found show the message
					//queryReindex();
				}
			}

			@Override
			public void indexingFinishedCallBack(String dictionary_name) {
				Dictionary.loadIndex(dictionary_name, false);
			}
		};
        
		
		if (Dictionary.getSize() == 0)
			Dictionary.loadIndex(Utils.getDictionaryName(), true);
		

		if (savedInstanceState != null) {

			fragmentDic = (FragmentDic) getFragmentManager()
					.findFragmentByTag(TAG_FRAGMENT_DIC);
			fragmentDic.callback = new FragmentDicCallback() {
				@Override
				public void itemSelected(IndexEntry indexEntry) {
					fragmentTranslation.setTranslation(indexEntry);
				}
			};
			
//			fragmentFiles.setParams(this);

			fragmentTranslation = (FragmentTranslation) getFragmentManager()
					.findFragmentByTag(TAG_FRAGMENT_TRANSL);
//			fragmentFiles.setParams(this);
					
			fragmentTree = (FragmentTree) getFragmentManager()
				.findFragmentByTag(TAG_FRAGMENT_TREE);

		} else {
			FragmentTransaction ft = getFragmentManager().beginTransaction();

			fragmentTranslation = new FragmentTranslation(this);

			ft.add(R.id.fcTranslation, fragmentTranslation, TAG_FRAGMENT_TRANSL);
			ft.commit();

			ft = getFragmentManager().beginTransaction();
			fragmentDic = new FragmentDic(this);
			fragmentDic.callback = new FragmentDicCallback() {
				@Override
				public void itemSelected(IndexEntry indexEntry) {
					fragmentTranslation.setTranslation(indexEntry);

				}
			};
			fragmentTree = new FragmentTree(mContext);
			fragmentTree.listener = new FragmentTree.OnTreeEventListener(){

				@Override
				public void onEditSelected(String text)
				{
					mode = 1;
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					fragmentDic.setMode(1);
					ft.replace(R.id.fcDictionary, fragmentDic, TAG_FRAGMENT_DIC);
					ft.addToBackStack(null);
					ft.commit();
					fragmentDic.setText(text);
				}

				@Override
				public void onAddSelected(int selectedGroupIndex) {
					mode = 2;
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					fragmentDic.setMode(2);
					ft.replace(R.id.fcDictionary, fragmentDic, TAG_FRAGMENT_DIC);
					ft.addToBackStack(null);
					ft.commit();
				}

				@Override
				public void itemSelected(IndexEntry indexEntry) {
					fragmentTranslation.setTranslation(indexEntry);
				}
			};
			
			
			String tag = prefs.getString(CURRENTFRAG, "TAG_FRAGMENT_DIC");
			if (tag.equals(TAG_FRAGMENT_DIC))
				ft.add(R.id.fcDictionary, fragmentDic, TAG_FRAGMENT_DIC);
			else
				ft.add(R.id.fcDictionary, fragmentTree, TAG_FRAGMENT_TREE);
			ft.commit();
			
			
			
		}

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
        switch (item.getItemId()) {
        case R.id.action_tree:
    		FragmentTree myFragment = (FragmentTree)getFragmentManager().findFragmentByTag(TAG_FRAGMENT_TREE);
    		if (myFragment == null){
        		FragmentTransaction ft = getFragmentManager().beginTransaction();
        		Bundle args = new Bundle();
        		// args.putString("name", record.getName());
        		// fragmentDic.setArguments(args);
        		ft.replace(R.id.fcDictionary, fragmentTree, TAG_FRAGMENT_TREE);
        		//ft.add(R.id.fcDictionary, fragmentTree, TAG_FRAGMENT_TREE);
        		ft.addToBackStack(null);
        		ft.commit();
        		
    		} else {
    			getFragmentManager().popBackStack();
    		}
        	
            return true;
        case R.id.action_choose:
        	String text = fragmentDic.getSelectedText();
			getFragmentManager().popBackStack();
        	
			if (text.length() > 0)
				fragmentTree.setText(text);
			
            return true;
        case R.id.action_add:
        	text = fragmentDic.getSelectedText();
        	
			if (text.length() > 0)
				fragmentTree.addChild(text);
			
            return true;
        case R.id.action_dictionary:
    		FragmentDic myFragmentD = (FragmentDic)getFragmentManager().findFragmentByTag(TAG_FRAGMENT_DIC);
    		if (myFragmentD == null){
        		FragmentTransaction ft = getFragmentManager().beginTransaction();
        		ft.replace(R.id.fcDictionary, fragmentDic, TAG_FRAGMENT_DIC);
        		ft.addToBackStack(null);
        		ft.commit();
    		} else {
    			getFragmentManager().popBackStack();
    		}
        	
            return true;
            
        case R.id.action_add_group:
			fragmentTree.addGroup();
            return true;
        case R.id.action_add_item:
			fragmentTree.addItem();
            return true;
        case R.id.action_edit:
			fragmentTree.edit();
            return true;
        case R.id.action_info:
			Utils.showInfo(mContext);
            return true;
        case R.id.action_reindex:
			fragmentDic.reindex();
            return true;
        case R.id.action_select_dic:
				AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
				dialog.setTitle("Select divtionaty");
				//dialog.setMessage(getResources().getString(R.string.action_move));

				final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice);
				arrayAdapter.add("en-ru");
				arrayAdapter.add("it-ru");
				arrayAdapter.add("ru-it");

				dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

				dialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String strName = arrayAdapter.getItem(which);
							if (strName.equals("en-ru"))
								Utils.setDictionaryName("en_ru.xdxf");
							else if (strName.equals("ru-it"))
								Utils.setDictionaryName("ru_it.xdxf");
							else if (strName.equals("it-ru"))
								Utils.setDictionaryName("it_ru.xdxf");

							TtsUtils.setLanguage(mContext);
							fragmentDic.setHint(strName);
							Dictionary.loadIndex(Utils.getDictionaryName(), false);
						}
					});
				dialog.show();
			
			
			
            return true;
           default:
                return false;
        }
	}
	
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// etMagnify.setText(savedInstanceState.getString(MTEXT));

	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putString( CURRENTFRAG, getVisibleFragmentTag());
		
		// outState.putString(MTEXT, etMagnify.getText().toString());
		// outState.putString(MINDEX, MenuData.getText());
		// outState.put
		// outState.putString(MINDEX, MenuData.getText());
	}

	private String getVisibleFragmentTag()
	{
		Fragment f = getFragmentManager().findFragmentById(R.id.fcDictionary);
		if (f instanceof FragmentDic) 
			return TAG_FRAGMENT_DIC;
		else
			return TAG_FRAGMENT_TREE;
		
	}

	@Override
	public void onPause() {
		TtsUtils.destroy();
		
		SharedPreferences prefs;
		prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor = prefs.edit();

		editor.putString(CURRENTFRAG, getVisibleFragmentTag());
		editor.putString(DICTIONARI_NAME, Utils.getDictionaryName());
		
		editor.commit();

		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		fragmentDic.setContext(this);
	}

	@Override
	public void onClick(View v) {
	}
	
	@Override
	public void onInit(int status) {
	      if (status == TextToSpeech.SUCCESS){
			    TtsUtils.init(this);
	    	  
	      }
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				TtsUtils.newTts(this, this);
			} else {
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
		else super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed()
	{
	    if (mode > 0){
			mode = 0;
			super.onBackPressed(); 
		}
		/*
		FragmentTree myFragment1 = (FragmentTree)getFragmentManager().findFragmentByTag(TAG_FRAGMENT_TREE);
		if (myFragment1 != null && myFragment1.isVisible()) {
			Toast.makeText(mContext, "dic", Toast.LENGTH_LONG).show();
			FragmentTransaction ft = getFragmentManager()
				.beginTransaction();
			
			ft.replace(R.id.fcDictionary, fragmentDic, TAG_FRAGMENT_DIC);
			//ft.addToBackStack(null);
			ft.commit();
			
		}*/
		else
			
			this.finishAffinity();
	  	  // super.onBackPressed();  // optional depending on your needs
	}	
	
}
