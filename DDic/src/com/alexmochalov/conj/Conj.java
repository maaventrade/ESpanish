package com.alexmochalov.conj;

import java.util.ArrayList;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.content.*;
import android.text.Html;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.alexmochalov.ddic.*;
import com.alexmochalov.dic.Dictionary;
import com.alexmochalov.dic.IndexEntry;
import com.alexmochalov.main.TtsUtils;
import com.alexmochalov.main.Utils;
import com.alexmochalov.test.TestItem;
import com.alexmochalov.tree.LineItem;
import com.alexmochalov.tree.Tree;

public class Conj extends Fragment
 {

	private Context mContext;
	
	public Conj(Activity context)
	{
		super();
		mContext = context;
	}
   
	
	static final String RED = "<font color=red>";
	static final String FINRED = "<font/>";
	
	public static void showTable(Activity context, LineItem selectedItem) {
		if (selectedItem == null){
			Toast.makeText(context, "Item not selected", Toast.LENGTH_LONG).show();
			return;
		}
		
		String text = selectedItem.getText().toUpperCase();
		String translation = selectedItem.getTranslation().toUpperCase();
		
		ArrayList<TestItem> list = new ArrayList<TestItem>(); 
		final ArrayAdapterConj arrayAdapter1 = new ArrayAdapterConj(
				context, list);
		
		ConjRus.init();
		
		if (text.endsWith("ARE")){
			String left = text.substring(0, text.length()-3); 
			
			text = left+RED+"ARE"+ "</font>";
			
			list.add(new TestItem("IO"+" "+left   + RED + "О", "Я"+" "+ConjRus.get(translation, 0)));
			list.add(new TestItem("TU"+" "+left   + RED + "I", "ТЫ"+" "+ConjRus.get(translation, 1)));
			list.add(new TestItem("LUI"+" "+left  + RED + "A", "ОН"+" "+ConjRus.get(translation, 2)));
			list.add(new TestItem("LEY"+" "+left  + RED + "A", "ОНА"+" "+ConjRus.get(translation, 3)));
			list.add(new TestItem("LEY"+" "+left  + RED + "A", "ВЫ"+" "+ConjRus.get(translation, 4)));
			list.add(new TestItem("NOI"+" "+left  + RED + "AMO", "МЫ"+" "+ConjRus.get(translation, 5)));
			list.add(new TestItem("VOI"+" "+left  + RED + "ATE", "ВЫ"+" "+ConjRus.get(translation, 6)));
			list.add(new TestItem("LORO"+" "+left + RED + "ANO", "ОНИ"+" "+ConjRus.get(translation, 7)));
			      
			
			
		} else {
			Toast.makeText(context, "Not found", Toast.LENGTH_LONG).show();
			return;
		}
		
		AlertDialog.Builder dialog;
		
		dialog = new AlertDialog.Builder(context);
		dialog.setTitle("Спряжения глаголов");
		
		LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);

        TextView tv = new TextView(context);
        
        tv.setText(Html.fromHtml(text + " - "+translation));
        
        tv.setPadding(40, 40, 40, 40);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);
		
        layout.addView(tv,parms);
        dialog.setView(layout);
        

		dialog.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		dialog.setAdapter(arrayAdapter1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		dialog.show();
		
	}
	
	/*
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_conj, container, false);

		Bundle args = getArguments();
		int selectedGroupIndex =  args.getInt("selectedGroupIndex");
        return rootView;
    }
	*/
	
	
}

