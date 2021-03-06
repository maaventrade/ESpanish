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
		void onOk(int text, String action); 
	} 
	
	private Context mContext;
	private int selected;
	private ArrayList<LineGroup> list;

	public DialogSelectTest(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dialog_select_test);

		setTitle(mContext.getResources().getString(R.string.dialog_select_text));
		
		list = Tree.getGroups();
		
		final ArrayAdapterSelectTest arrayAdapterTest = new ArrayAdapterSelectTest(
			mContext, list);
		
		final ListView lvTest = (ListView) findViewById(R.id.lvTest);
		
		View view = View.inflate(getContext(), R.layout.list_header, null);
		
		lvTest.addHeaderView(view);
		
		lvTest.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		lvTest.setAdapter(arrayAdapterTest);
		
////		
		lvTest.setOnItemClickListener(new OnItemClickListener(){


			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selected = position;
			}});


		
		Button btnRemember = (Button)findViewById(R.id.btnRemember);
		btnRemember.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					
					if (selected < 0){
						Toast.makeText(mContext, "Select string", Toast.LENGTH_LONG).show();
						return;
					}
					
					Utils.setLastName( list.get( selected ).getName());
					
					if (callback != null ) 
						callback.onOk(selected, "remember");
					dismiss();
				}
			});
		
		Button btnTest = (Button)findViewById(R.id.btnTest);
		btnTest.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					
					if (selected < 0){
						Toast.makeText(mContext, "Select string", Toast.LENGTH_LONG).show();
						return;
					}
					
					Utils.setLastName( list.get( selected ).getName());
					
					if (callback != null ) 
						callback.onOk(selected, "test");
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
