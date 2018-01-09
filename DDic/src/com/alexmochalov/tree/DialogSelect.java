package com.alexmochalov.tree;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import com.alexmochalov.ddic.*;
import com.alexmochalov.dic.*;

public class DialogSelect extends Dialog
{
	Context mContext;
	
	protected DialogSelect(Context context, boolean cancelable,
						 OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	protected DialogSelect(Context context, boolean is) {
		super(context);
		mContext = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.dialog_select);
		
		//FragmentDic fd = (FragmentDic)findViewById(R.id.dic);
		
	}
}
