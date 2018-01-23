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

public class DialogExpr extends Dialog
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
	
	private int selectedStringIndex = -1;
	
	protected DialogExpr(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public DialogExpr(Activity context, ArrayList<String> a) {
		super(context);
		mContext = context;
		alExpressions = a;
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
		
		
		ImageButton ibSpeak = (ImageButton)findViewById(R.id.ibSpeak);
		ibSpeak.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if (selectedStringIndex != -1){
						TtsUtils.speak( alExpressions.get(selectedStringIndex) );
					}
				}
			});
		
		
		/*
		Button btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					Exercises.SaveExercises(mContext);
					dialog.dismiss();
				}
			});
		
		Button btnSelect = (Button)findViewById(R.id.btnSelect);
		btnSelect.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if (selectedStringIndex != -1){
						if (callback != null){
							Exercises.SaveExercises(mContext);
							callback.selected(Exercises.getExercises().get(selectedStringIndex));
							dialog.dismiss();
						}
					}
				}
			});
		
		Button btnEdit = (Button)findViewById(R.id.btnEdit);
		btnEdit.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if (selectedStringIndex != -1){
						DialogEditExercise dialogEditExercise = new DialogEditExercise(getContext(), Exercises.getExercises().get(selectedStringIndex), false);
						dialogEditExercise.show();
						dialogEditExercise.callback = new DialogEditExercise.MyCallback() {
							@Override
							public void callbackOk(Exercise exercise) {
								//Exercises
								adapter.notifyDataSetChanged();
							}
						};
					}
				}
			});
		
		Button btnAdd = (Button)findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					DialogEditExercise dialogEditExercise = new DialogEditExercise(getContext(), new Exercise(), true);
					dialogEditExercise.show();
					dialogEditExercise.callback = new DialogEditExercise.MyCallback() {
						@Override
						public void callbackOk(Exercise exercise) {
							Exercises.addExercise(exercise);
							adapter.notifyDataSetChanged();
							listViewExercice.setSelection(Exercises.getExercises().indexOf(exercise));							
						}
					};
				}
			});
		
		this.setOnCancelListener(new OnCancelListener(){
			@Override
			public void onCancel(DialogInterface dialog) {
				Exercises.SaveExercises(mContext);
			}});
		*/
	}

	

	
}
