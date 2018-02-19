package com.alexmochalov.test;

import java.util.ArrayList;
import java.util.List;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.alexmochalov.conj.ArrayAdapterConj;
import com.alexmochalov.ddic.*;
import com.alexmochalov.dic.*;
import com.alexmochalov.main.Utils;
import com.alexmochalov.tree.LineGroup;
import com.alexmochalov.tree.Tree;

public class DialogSelectTest extends Dialog
{

	public CallbackOk callback = null;
	public interface CallbackOk {
		void onOk(int text); 
	} 
	
	private Context mContext;
	private int selected;

	public DialogSelectTest(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dialog_select_test);

		setTitle(mContext.getResources().getString(R.string.dialog_select_text));
		
		final ArrayList<LineGroup> list = Tree.getGroups();
		
		final ArrayAdapterSelectTest arrayAdapterTest = new ArrayAdapterSelectTest(
			mContext, list);
		
		final ListView lvTest = (ListView) findViewById(R.id.lvTest);
		
		lvTest.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		lvTest.setAdapter(arrayAdapterTest);
		
////		
		lvTest.setOnItemClickListener(new OnItemClickListener(){


			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selected = position;
			}});


		
		Button btnOk = (Button)findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if (callback != null ) 
						callback.onOk(selected);
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
	
	
}
