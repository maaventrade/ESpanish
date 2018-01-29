package com.alexmochalov.tree;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.alexmochalov.ddic.*;
import com.alexmochalov.dic.*;

public class DialogEdit extends Dialog
{
/*
	public CallbackOk callback = null;
	public interface CallbackOk {
		void onOk(); 
	} 
	
	@Override
	public void onClick(View view)
	{
			Button btnOk = (Button)findViewById(R.id.btnOk);
		
			
			if (view == btnOk){
				mLine.setName(etName.getText().toString());
				mLine.setTranslation(etTranslation.getText().toString());
				//mAdapter.notifyDataSetChanged();
				if (callback != null)
					callback.onOk();
					
			}
			dismiss();
	}
	*/

	private Activity mContext;

	private AdapterTree mAdapter;
	
	private EditText etName;
	private EditText etTranslation;

	private LineItem mLine;

	private boolean mModeAdd = false;
	
	protected DialogEdit(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	protected DialogEdit(Activity context, LineItem line, AdapterTree adapter, boolean modeAdd ) {
		super(context);
		mContext = context;
		mLine = line;
		mAdapter = adapter;
		mModeAdd = modeAdd;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.edit);

		etName = (EditText)findViewById(R.id.etText);
		etTranslation = (EditText)findViewById(R.id.etTranslation);
		
		if (mModeAdd){
			
		}
		etName.setText(mLine.getName1());
		etTranslation.setText(mLine.getTranslation());
		
		Button btnOk = (Button)findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					mLine.setName1(etName.getText().toString());
					mLine.setTranslation(etTranslation.getText().toString());
					mAdapter.notifyDataSetChanged();
					
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
			
		//getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
        //      WindowManager.LayoutParams.MATCH_PARENT);
	}
	
	
	
}
