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
	private Menu mMenu;

	public void setMode(int mode)
	{
		mMode = mode;
		MenuItem mItem = mMenu.getItem(R.id.action_tree);
		mItem.setVisible(false);
		
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
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
		mMenu = menu;
	}

	
}
