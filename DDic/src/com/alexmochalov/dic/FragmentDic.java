package com.alexmochalov.dic;

import android.app.*;
import android.content.*;
import android.content.SharedPreferences.*;
import android.os.*;
import android.preference.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.inputmethod.*;
import android.widget.*;
import android.widget.AdapterView.*;

import com.alexmochalov.ddic.*;
import com.alexmochalov.main.*;

import java.util.*;

import android.view.View.OnClickListener;

public class FragmentDic extends Fragment   implements OnClickListener{

	private View rootView;
	private Activity mContext;

	private ListView lvDictionary;
	private ArrayAdapterDictionary adapter;
	private int currentPosition = -1;
	
	private EditText etEntry;

	private ImageButton ibRemove;
	
	private final static String MTEXT = "TEXT";
	private final static String MFILTER = "MFILTER";
	private final static String MINDEX = "ITEM";
	
	private boolean itemClicked = false;
	private boolean textChanged = false;
	
	public FragmentDicCallback callback = null;
	
	private int mMode = 0;
	private String mtext = "";
	private String mFilter = "";

	public void setMode(int mode)
	{
		mMode = mode;
	}
	
	public interface FragmentDicCallback {
		void itemSelected(IndexEntry indexEntry); 
	} 
	
	public FragmentDic(Activity context)
	{
		super();
		mContext = context;
	}

	public FragmentDic()
	{
		super();
	}
	
	@Override
    public void onPause()
	{
		SharedPreferences prefs;
		prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor = prefs.edit();
		
		editor.putString(MTEXT, etEntry.getText().toString());
		editor.putString(MFILTER, mFilter);
		editor.putInt(MINDEX, currentPosition);
		
		editor.commit();
		
		super.onPause();
	}

	@Override
    public void onStart()
	{
		super.onStart();
	}	
	
	@Override
    public void onResume()
	{
		super.onResume();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		// Define views
        rootView = inflater.inflate(R.layout.fragment_dic, container, false);
		ibRemove = (ImageButton)rootView.findViewById(R.id.ibRemove);
		etEntry = (EditText) rootView.findViewById(R.id.etEntry);
		lvDictionary = (ListView) rootView.findViewById(R.id.lvDictionary);
		
		// Read preferences 
		SharedPreferences prefs;
		prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		
		String z = prefs.getString(MTEXT, "");
		int n  = prefs.getInt(MINDEX, -1);;
		
		etEntry.setText(prefs.getString(MTEXT, ""));
		mFilter = prefs.getString(MFILTER, "");
		
		//currentPosition = prefs.getInt(MINDEX, -1);
		
		if (mtext.length() > 0){
			etEntry.setText(mtext);
			mtext = "";
		}
		
		setHint(Utils.getDictionaryName());
		
		setHasOptionsMenu(true);
		
		ibRemove.setOnClickListener(this);
		
		if (! Dictionary.isLoaded()){
			setAdapter();
		}
		
		if (adapter != null){
			if (mFilter.length() > 0){
				adapter.getFilter().filter(mFilter);
				mFilter = "";
			}
		}
		
		lvDictionary
			.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(
					AdapterView<?> adapterView, View p2,
					int position, long p4) {
					
					currentPosition = position;
					itemClicked = true;
					etEntry.setText(adapter.getItem(position).getText()); 
					itemClicked = false;
					
					if (callback != null) 
						callback.itemSelected(adapter.getItem(position));
					
					View view = mContext.getCurrentFocus();
					if (view != null) {  
						InputMethodManager imm = (InputMethodManager)mContext.
							getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					}										

				}
			});
		
		
		etEntry.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (! itemClicked)
					if (adapter != null){
						mFilter = s.toString();
						adapter.getFilter().filter(s);
					}
			}
		});
		

        return rootView;
    }

	public void setAdapter() {
		adapter = new ArrayAdapterDictionary(mContext,
				 R.layout.dic_string,
				 (ArrayList<IndexEntry>) Dictionary
				 .getIndexEntries());
		lvDictionary.setAdapter(adapter);  
		
		if (mFilter.length() > 0){
			adapter.getFilter().filter(mFilter);
			mFilter = "";
		}
	}

	protected void queryReindex() {
		AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

		alert.setTitle(getResources().getString(R.string.index_not_found)+" "+Utils.getIndexName());
		alert.setMessage(R.string.query_index_dictionary);

		AlertDialog dlg = alert.create();

		alert.setPositiveButton(getResources().getString(R.string.yes),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Dictionary.createIndexAsinc(Utils.getDictionaryName());
					}
				});

		alert.setNegativeButton(getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		alert.show();
	}

	
	@Override
	public void onClick(View v) {
		etEntry.setText("");				
	}

	public void setHint(String hint) {
		if (etEntry != null)
			etEntry.setHint("Search ("+hint+")");
		
	}

	public void reindex() {
		queryReindex();
	}

	public void setContext(Activity context) {
		mContext = context;
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.dic, menu);
		super.onCreateOptionsMenu(menu, inflater);
		
		MenuItem mItem;
		if (mMode == 1){
			mItem = menu.findItem(R.id.action_tree);
			mItem.setVisible(false);
			mItem = menu.findItem(R.id.action_choose);
			mItem.setVisible(true);
			//mItem = menu.findItem(R.id.action_add_item);
			//mItem.setVisible(false);
		}
		else if (mMode == 2){
			mItem = menu.findItem(R.id.action_tree);
			mItem.setVisible(false);
			mItem = menu.findItem(R.id.action_choose);
			mItem.setVisible(true);
			//mItem = menu.findItem(R.id.action_add_item);
			//mItem.setVisible(true);
		}
		else{
			mItem = menu.findItem(R.id.action_tree);
			mItem.setVisible(true);
			mItem = menu.findItem(R.id.action_choose);
			mItem.setVisible(false);
			//mItem = menu.findItem(R.id.action_add_item);
			//mItem.setVisible(false);
		}

	}

	public String getSelectedText() {
		if (currentPosition >= 0){
			IndexEntry l = adapter.getItem(currentPosition);
			if (l != null)
				return l.getText();
			else return etEntry.getText().toString();
		}
		else return etEntry.getText().toString();
	}

	public void setText(String text) {
		mtext = text;
	}

	
}
