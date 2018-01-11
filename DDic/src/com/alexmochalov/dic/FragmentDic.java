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
	private final static String MINDEX = "ITEM";
	
	
	public FragmentDicCallback callback = null;
	
	private int mMode = 0;
	private String mtext = "";

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
		editor.putInt(MINDEX, currentPosition);

		editor.commit();
		
		super.onPause();
		
	}

	//@Override
    //public void onInit()
	//{
	//	super.onInit();
	//}	
	
	@Override
    public void onResume()
	{
		super.onResume();
		
		//Toast.makeText(mContext,"size "+Dictionary.getSize(),Toast.LENGTH_LONG).show();
		
		//Toast.makeText(mContext,"ON RESUME",Toast.LENGTH_LONG).show();
		
		SharedPreferences prefs;
		prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		
		
		etEntry.setText(prefs.getString(MTEXT, ""));
		currentPosition = prefs.getInt(MINDEX, -1);
		
		if (mtext.length() > 0){
			etEntry.setText(mtext);
			mtext = "";
		}
		
		
		setHint(Utils.getDictionaryName());
		
//		Dictionary.setParams(mContext);
		
//		if (Dictionary.getSize() == 0)
//			Dictionary.loadIndex(Utils.getDictionaryName(), true);
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		setHasOptionsMenu(true);
		
        rootView = inflater.inflate(R.layout.fragment_dic, container, false);
        
		ibRemove = (ImageButton)rootView.findViewById(R.id.ibRemove);
		ibRemove.setOnClickListener(this);
		

		lvDictionary = (ListView) rootView.findViewById(R.id.lvDictionary);
		
		if (! Dictionary.isLoaded()){
			setAdapter();
		}
		
		lvDictionary
			.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(
					AdapterView<?> adapterView, View p2,
					int position, long p4) {
					
					currentPosition = position;
					
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
		
		etEntry = (EditText) rootView.findViewById(R.id.etEntry);
		
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
				if (adapter != null){
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
