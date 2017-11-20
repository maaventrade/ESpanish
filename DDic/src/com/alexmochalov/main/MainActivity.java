package com.alexmochalov.main;

import android.app.*;
import android.content.*;
import android.content.SharedPreferences.Editor;
import android.os.*;
import android.preference.PreferenceManager;
import android.text.*;
import android.text.method.ScrollingMovementMethod;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
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

public class MainActivity extends Activity implements OnClickListener {

	private FragmentDic fragmentDic;
	private String TAG_FRAGMENT_DIC = "TAG_FRAGMENT_DIC";

	private FragmentTranslation fragmentTranslation;
	private String TAG_FRAGMENT_TRANSL = "TAG_FRAGMENT_TRANSL";

	private FragmentTree fragmentTree;
	private String TAG_FRAGMENT_TREE = "TAG_FRAGMENT_TREE";

	private Activity mContext;

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

		getActionBar().hide();

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

		} else {
			FragmentTransaction ft = getFragmentManager().beginTransaction();

			fragmentTranslation = new FragmentTranslation(this);
			fragmentTranslation.callback = new FragmentTranslation.FragmentTranslationCallback() {

				@Override
				public void btnSelectDictionary(String name) {
					if (name.equals("en-ru"))
						Utils.setDictionaryName("en_ru.xdxf");
					else if (name.equals("ru-it"))
						Utils.setDictionaryName("ru_it.xdxf");
					else if (name.equals("it-ru"))
						Utils.setDictionaryName("it_ru.xdxf");

					fragmentDic.setHint(name);
					Dictionary.loadIndex(Utils.getDictionaryName(), false);

				}

				@Override
				public void btnForwardClicked() {
					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();

					fragmentTree = new FragmentTree(mContext);

					Bundle args = new Bundle();

					// args.putString("name", record.getName());
					// fragmentDic.setArguments(args);

					ft.replace(R.id.fcDictionary, fragmentTree, TAG_FRAGMENT_TREE);
					ft.addToBackStack(null);

					ft.commit();
				}

				@Override
				public void btnReindex() {
					fragmentDic.reindex();
				}
			};

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
			ft.add(R.id.fcDictionary, fragmentDic, TAG_FRAGMENT_DIC);
			ft.commit();
		}

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// etMagnify.setText(savedInstanceState.getString(MTEXT));

	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// outState.putString(MTEXT, etMagnify.getText().toString());
		// outState.putString(MINDEX, MenuData.getText());
		// outState.put
		// outState.putString(MINDEX, MenuData.getText());
	}

	@Override
	public void onPause() {
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
}
