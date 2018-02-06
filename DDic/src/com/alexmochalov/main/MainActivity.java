package com.alexmochalov.main;

import android.app.*;
import android.content.*;
import android.content.SharedPreferences.*;
import android.os.*;
import android.preference.*;
import android.speech.tts.*;
import android.speech.tts.TextToSpeech.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.alexmochalov.ddic.*;
import com.alexmochalov.dic.*;
import com.alexmochalov.dic.FragmentDic.*;
import com.alexmochalov.translation.FragmentTranslation;
import com.alexmochalov.tree.*;

public class MainActivity extends Activity implements OnClickListener, OnInitListener
{

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

	private MenuItem maSwitch;

	private boolean refreshTranslationRemitted;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
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

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);

		SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(this);

		String name = prefs.getString(DICTIONARI_NAME, "en_ru.xdxf");
		Utils.setDictionaryName(name);

		Dictionary.setParams(mContext);

		Dictionary.eventCallback = new Dictionary.EventCallback() {


			@Override
			public void loadingFinishedCallBack(boolean result)
			{
				if (result)
				{
					if (getVisibleFragmentTag().equals(TAG_FRAGMENT_DIC))
						fragmentDic.setAdapter();
					if (refreshTranslationRemitted)
					{
						refreshTranslationRemitted = false;
						fragmentTree.callItemSelected();
					}
				}
				else
				{
					// If Index file not found show the message
					//queryReindex();
				}
			}

			@Override
			public void indexingFinishedCallBack(String dictionary_name)
			{
				Dictionary.loadIndex(dictionary_name, false);
			}
		};


		if (Dictionary.getSize() == 0)
			Dictionary.loadIndex(Utils.getDictionaryName(), true);


		if (savedInstanceState != null)
		{

			fragmentDic = (FragmentDic) getFragmentManager()
				.findFragmentByTag(TAG_FRAGMENT_DIC);
			fragmentDic.setContext(this);
			fragmentDic.callback = new FragmentDicCallback() {
				@Override
				public void itemSelected(IndexEntry indexEntry)
				{
					// Set translation for current item of the dictionary list
					fragmentTranslation.setTranslation(indexEntry, 2);
				}
			};

//			fragmentFiles.setParams(this);

			fragmentTranslation = (FragmentTranslation) getFragmentManager()
				.findFragmentByTag(TAG_FRAGMENT_TRANSL);
//			fragmentFiles.setParams(this);

			fragmentTree = (FragmentTree) getFragmentManager()
				.findFragmentByTag(TAG_FRAGMENT_TREE);

		}
		else
		{
			FragmentTransaction ft = getFragmentManager().beginTransaction();

			fragmentTranslation = new FragmentTranslation(this);

			ft.add(R.id.fcTranslation, fragmentTranslation, TAG_FRAGMENT_TRANSL);
			ft.commit();

			ft = getFragmentManager().beginTransaction();
			fragmentDic = new FragmentDic(this);
			fragmentDic.callback = new FragmentDicCallback() {
				@Override
				public void itemSelected(IndexEntry indexEntry)
				{
					// Set translation for current item of the dictionary list
					fragmentTranslation.setTranslation(indexEntry, 2);

				}
			};
			fragmentTree = new FragmentTree(mContext);
			fragmentTree.listener = new FragmentTree.OnTreeEventListener(){

				@Override
				public void onEditSelected(String text)
				{
					/*
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					fragmentDic.setMode(1);
					ft.replace(R.id.fcDictionary, fragmentDic, TAG_FRAGMENT_DIC);
					ft.addToBackStack(null);
					ft.commit();
					fragmentDic.setText(text);
					*/
				}

				@Override
				public void onAddSelected(int selectedGroupIndex)
				{
					/*
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					fragmentDic.setMode(2);
					ft.replace(R.id.fcDictionary, fragmentDic, TAG_FRAGMENT_DIC);
					ft.addToBackStack(null);
					ft.commit();
					*/
				}

				@Override
				public void itemSelected(IndexEntry indexEntry)
				{
					// Set translation for current item of the tree
					fragmentTranslation.setTranslation(indexEntry, 2);
				}
			};


			//String tag = prefs.getString(CURRENTFRAG, "TAG_FRAGMENT_DIC");
			//if (tag.equals(TAG_FRAGMENT_DIC))
				ft.add(R.id.fcDictionary, fragmentDic, TAG_FRAGMENT_DIC);
			//else
			//	ft.add(R.id.fcDictionary, fragmentTree, TAG_FRAGMENT_TREE);
			ft.commit();



		}

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

	}

	/*
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	 switch (item.getItemId()) {
	 case android.R.id.home:
	 Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show(); 
	 break;
	 }
	 return true;
	 }
	 */	

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);


		maSwitch = menu.findItem(R.id.action_switch);
		Utils.setTitle(maSwitch, mContext);


		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		FragmentTransaction ft ;

		AlertDialog.Builder dialog;

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
        switch (item.getItemId())
		{
			case R.id.action_tree:
				FragmentTree myFragment = (FragmentTree)getFragmentManager().findFragmentByTag(TAG_FRAGMENT_TREE);
				if (myFragment == null)
				{
					ft = getFragmentManager().beginTransaction();
					Bundle args = new Bundle();
					// args.putString("name", record.getName());
					// fragmentDic.setArguments(args);
					ft.replace(R.id.fcDictionary, fragmentTree, TAG_FRAGMENT_TREE);
					//ft.add(R.id.fcDictionary, fragmentTree, TAG_FRAGMENT_TREE);
					ft.addToBackStack(null);
					ft.commit();

				}
				else
				{
					getFragmentManager().popBackStack();
				}

				return true;
			/*case R.id.action_choose:
				String text = fragmentDic.getSelectedText();
				getFragmentManager().popBackStack();

				if (text.length() > 0)
				{
					if (mode == 1)
						fragmentTree.setText(text);
					else if (mode == 2)
					{
						fragmentTree.addChild(text);
						fragmentTree.select();
						fragmentTree.edit(false);
					}

				}

				
				return true;
				*/
			case R.id.action_add_item:
				fragmentTree.edit(false, true);
				return true;
			case R.id.action_dictionary:
				FragmentDic myFragmentD = (FragmentDic)getFragmentManager().findFragmentByTag(TAG_FRAGMENT_DIC);
				if (myFragmentD == null)
				{
					ft = getFragmentManager().beginTransaction();
					ft.replace(R.id.fcDictionary, fragmentDic, TAG_FRAGMENT_DIC);
					ft.addToBackStack(null);
					ft.commit();
				}
				else
				{
					getFragmentManager().popBackStack();
				}

				return true;

			case R.id.action_add_group:
				fragmentTree.edit(true, false);
				return true;
			case R.id.action_edit:
				fragmentTree.edit(false, false);
				return true;
			case R.id.action_save:
				fragmentTree.save();
				return true;
			case R.id.action_info:
				Utils.showInfo(mContext);
				return true;
			case R.id.action_find:
				fragmentTree.find();
				return true;
				
			case R.id.action_cut:
				fragmentTree.copyItem();
				fragmentTree.deleteItem();
				return true;

			case R.id.action_paste:
				fragmentTree.paste();
				fragmentTree.select();
				return true;

			case R.id.action_switch:
				dialog = new AlertDialog.Builder(mContext);
				dialog.setTitle("Select divtionaty");
				//dialog.setMessage(getResources().getString(R.string.action_move));

				final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice);
				arrayAdapter1.add("en-ru");
				arrayAdapter1.add("ru-en");
				arrayAdapter1.add("it-ru");
				arrayAdapter1.add("ru-it");

				dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
						}
					});

				dialog.setAdapter(arrayAdapter1, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							String strName = arrayAdapter1.getItem(which);
							if (strName.equals("en-ru"))
								Utils.setDictionaryName("en_ru.xdxf");
							else if (strName.equals("ru-it"))
								Utils.setDictionaryName("ru_it.xdxf");
							else if (strName.equals("it-ru"))
								Utils.setDictionaryName("it_ru.xdxf");
							else if (strName.equals("ru-en"))
								Utils.setDictionaryName("ru_en.xdxf");
							TtsUtils.setLanguage(mContext);
							fragmentDic.setHint(strName);
							Dictionary.loadIndex(Utils.getDictionaryName(), false);

							fragmentTree.reload(Utils.getLanguageNoRus());

							refreshTranslationRemitted = true;
							//Utils.setRefreshTranslatiinRemitted();


							Utils.setTitle(maSwitch, mContext);
						}
					});
				dialog.show();


				/*
				 Utils.switchDictionary();
				 TtsUtils.setLanguage(mContext);
				 fragmentDic.setHint(Utils.getDictionaryName());
				 Dictionary.loadIndex(Utils.getDictionaryName(), false);
				 */
				return true;

			case R.id.action_delete:
				new AlertDialog.Builder(MainActivity.this)
					.setIcon(R.drawable.ic_launcher)

					.setTitle(getResources().getString(R.string.query_delete) + "\""  + fragmentTree.getCurrentText() + "\"?")
					.setMessage(getResources().getString(R.string.are_you_shure))
					.setPositiveButton("Yes", new DialogInterface.OnClickListener()
				 	{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							fragmentTree.deleteItem();
						}
				 	})
					.setNegativeButton("No", new DialogInterface.OnClickListener()
				 	{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							return;
						}
				 	})
					.show();

				return true;


			case R.id.action_reindex:
				fragmentDic.reindex();
				return true;

			default:
                return false;
        }
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);

		// etMagnify.setText(savedInstanceState.getString(MTEXT));

	}

	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		outState.putString(CURRENTFRAG, getVisibleFragmentTag());

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
	public void onPause()
	{
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
	public void onResume()
	{
		super.onResume();
		fragmentDic.setContext(this);
	}

	@Override
	public void onClick(View v)
	{
	}

	@Override
	public void onInit(int status)
	{
		if (status == TextToSpeech.SUCCESS)
		{
			TtsUtils.init(this);
//			TtsUtils.setLanguage(mContext);

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		if (requestCode == MY_DATA_CHECK_CODE)
		{
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
			{
				TtsUtils.newTts(this, this);
			}
			else
			{
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
	  
			if (fragmentTree != null
			&& fragmentTree.isModified())
				exitAfterSaving();
				
			else	
					super.onBackPressed();
			


		//this.finishAffinity();
		// super.onBackPressed();  // optional depending on your needs
	}

	private void exitAfterSaving()
	{
	
			new AlertDialog.Builder(MainActivity.this)

				.setIcon(R.drawable.ic_launcher)

				.setTitle(getResources().getString(R.string.tree_was_changed))

				.setMessage(getResources().getString(R.string.save_tree_before_exit))

				.setPositiveButton("Yes", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						fragmentTree.save();
						finishAffinity();
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						return;
					}
				})
				.setNeutralButton("No", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						finishAffinity();
					}
				})
				.show();
	}	

}
