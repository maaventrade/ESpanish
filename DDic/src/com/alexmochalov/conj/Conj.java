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
import android.support.v4.util.*;

public class Conj extends Fragment
{

	private Context mContext;

	public Conj(Activity context)
	{
		super();
		mContext = context;
	}

	static class CI
	{
		CI(String m, String e[])
		{
			mainEnding = m;
			endings = e.clone();
		}

		String mainEnding;
		String endings[];
	}


	private static ArrayList<CI> conjIt = new ArrayList<CI>();

	static final String RED = "<font color=red>";
	static final String FINRED = "<font/>";

	public static void showTable(Activity context, LineItem selectedItem)
	{
		if (selectedItem == null)
		{
			Toast.makeText(context, "Item not selected", Toast.LENGTH_LONG).show();
			return;
		}

		String s[] = {"O","I","A","A","A","IAMO","ATE","ANO"};
		conjIt.add(new CI("ARE", s));
		
		String s1[] = {"O","I","E","E","E","IAMO","ETE","ONO"};
		conjIt.add(new CI("ERE", s1));
		
		String s2[] = {"O","I","E","E","E","IAMO","ITE","ONO"};
		conjIt.add(new CI("IRE", s2));
		
		String s3[] = {"O","I","E","E","E","IAMO","ITE","ONO"};
		conjIt.add(new CI("IRE", s3));
		

		String text = selectedItem.getText().toUpperCase();
		String translation = selectedItem.getTranslation().toUpperCase();

		ArrayList<TestItem> list = new ArrayList<TestItem>(); 
		final ArrayAdapterConj arrayAdapter1 = new ArrayAdapterConj(
			context, list);

		ConjRus.init();

		String it[] = {"IO","TU","LUI","LEY","LEY","NOY","VOY","LORO"};
		String ru[] = {"Я","ТЫ","ОН","ОНА","ВЫ","МЫ","ВЫ","ОНИ"};

		boolean found = false;
		for (CI cr: conjIt)
		{
			if (text.endsWith(cr.mainEnding))
			{

				String left = text.substring(0, text.indexOf(cr.mainEnding)); 

				text = left + RED + cr.mainEnding + "</font>";

				for (int i = 0; i < cr.endings.length; i++)
				{
					String leftI = left;
					if (left.endsWith("I") && cr.endings[i].startsWith("I"))
						leftI = leftI.substring(0, leftI.length()-1);
					
					list.add(new TestItem(it[i] + " " + leftI + 
										  RED + cr.endings[i], 
										  ru[i] + " " + ConjRus.get(translation, i)));
				}

				found = true;
			}
		}

		if (!found)
		{
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

        tv.setText(Html.fromHtml(text + " - " + translation));

        tv.setPadding(40, 40, 40, 40);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);

        layout.addView(tv, parms);
        dialog.setView(layout);


		dialog.setNegativeButton("cancel",
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});

		dialog.setAdapter(arrayAdapter1,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
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

