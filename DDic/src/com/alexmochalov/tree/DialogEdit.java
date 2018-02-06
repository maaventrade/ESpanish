package com.alexmochalov.tree;

import java.util.List;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.alexmochalov.ddic.*;
import com.alexmochalov.dic.*;
import com.alexmochalov.main.Utils;

public class DialogEdit extends Dialog
{

	public CallbackOk callback = null;
	public interface CallbackOk {
		void onOk(String text, String translation); 
	} 
	
	private Activity mContext;

	private AdapterTree mAdapter;
	
	private AutoCompleteTextView etText;
	private EditText etTranslation;

	private LineItem mLine;

	private boolean mModeAdd = false;

	private int mSelectedGroupIndex;

	private int mSelectedItemIndex;
	
	protected DialogEdit(Activity context, boolean modeAdd, int selectedGroupIndex, int selectedItemIndex ) {
		super(context);
		mContext = context;
		
		mSelectedGroupIndex = selectedGroupIndex;
		mSelectedItemIndex = selectedItemIndex;
		mModeAdd = modeAdd;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.dialog_edit);

		etText = (AutoCompleteTextView)findViewById(R.id.etText);
		etTranslation = (EditText)findViewById(R.id.etTranslation);
		
		if (mModeAdd){
			
		}
		
		etText.setThreshold(1);
		
		ArrayAdapter<String> adapter = new KArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice, 
				Utils.getExpressions());

		etText.setThreshold(1);
		etText.setAdapter(adapter);

		if (!mModeAdd){
			setTitle(mContext.getString(R.string.action_edit));
			etText.setText(Tree.getName(mSelectedGroupIndex, mSelectedItemIndex));
			etTranslation.setText(Tree.getTranslation(mSelectedGroupIndex, mSelectedItemIndex));
		} else 
			setTitle("Add");
		
		
////		
		ImageButton ibDropDown = (ImageButton)findViewById(R.id.ibDropDown);;
		ibDropDown.setOnClickListener(new ImageButton.OnClickListener(){
				@Override
				public void onClick(View v) {
					etText.showDropDown();
				}});

		
		Button btnOk = (Button)findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if (callback != null ) 
						callback.onOk(etText.getText().toString(), etTranslation.getText().toString());
					dismiss();
				}
			});
			
		Button btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					dismiss();
				}
			});
	}
	
	
	/**********************/
	class KArrayAdapter<T> 
	extends ArrayAdapter<T>
	{
	    private Filter filter = new KNoFilter();
	    public List<T> items;

	    @Override
	    public Filter getFilter() {
	        return filter;
	    }

	    public KArrayAdapter(Context context, int textViewResourceId,
	            List<T> objects) {
	        super(context, textViewResourceId, objects);
	        items = objects;
	    }

	    private class KNoFilter extends Filter {

	        @Override
	        protected FilterResults performFiltering(CharSequence arg0) {
	            FilterResults result = new FilterResults();
	                result.values = items;
	                result.count = items.size(); 
	            return result;
	        }

	        @Override
	        protected void publishResults(CharSequence arg0, FilterResults arg1) {
	            notifyDataSetChanged();
	        }
	    }
	}
	
}
