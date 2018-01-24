package com.alexmochalov.expr;

import java.util.ArrayList;

import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.alexmochalov.ddic.*;
import com.alexmochalov.dic.*;
import com.alexmochalov.main.TtsUtils;

public class DialogExpr extends Dialog implements android.view.View.OnClickListener
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

	private ListView lvExpr;
	private ArrayList<String> alExpressions;
	private ArrayAdapterExpr mAdapter;
	
	private ImageButton ibSpeak;
	private Button btnCancel;
	private Button btnCopy;
	
	private int selectedStringIndex = -1;
	private boolean mCopy;
	
	protected DialogExpr(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public DialogExpr(Activity context, ArrayList<String> a, boolean copy) {
		super(context);
		mContext = context;
		alExpressions = a;
		mCopy = copy;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setTitle(mContext.getResources().getString(R.string.expressions));
		setContentView(R.layout.dialog_expr);
	
		//getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
	    //          WindowManager.LayoutParams.MATCH_PARENT);

		lvExpr = (ListView)findViewById(R.id.lvExpr);
		lvExpr.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		mAdapter = new ArrayAdapterExpr(mContext, alExpressions);
		
		lvExpr.setAdapter(mAdapter);
		
		lvExpr.setOnItemClickListener( new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
			{
				if (selectedStringIndex == index){
					/* if (callback != null){
						Exercises.SaveExercises(mContext);
						callback.selected(Exercises.getExercises().get(index));
						dialog.dismiss();
					}*/
				}
				else selectedStringIndex = index;
			}}
		);	
		
		
		ibSpeak = (ImageButton)findViewById(R.id.ibSpeak);
		ibSpeak.setOnClickListener(this);
		
		btnCopy = (Button)findViewById(R.id.btnCopy);
		if (mCopy){
			btnCopy.setVisibility(View.VISIBLE);
			btnCopy.setOnClickListener(this);
		} else {
			btnCopy.setVisibility(View.INVISIBLE);
		}
		
		btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		if (v == ibSpeak){
			if (selectedStringIndex != -1){
				TtsUtils.speak( alExpressions.get(selectedStringIndex) );
			}
		} else if (v == btnCancel){
			dismiss();
		} else if (v == btnCopy){
			dismiss();
		}
	}

	
}
