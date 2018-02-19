package com.alexmochalov.test;

import android.content.*;
import android.text.Html;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import com.alexmochalov.ddic.R;
import com.alexmochalov.ddic.R.id;
import com.alexmochalov.ddic.R.layout;
import com.alexmochalov.test.TestItem;
import com.alexmochalov.tree.LineGroup;

/**
 */   
public class ArrayAdapterSelectTest  extends BaseAdapter
{
	private LayoutInflater inflater;
	private List<LineGroup> objects = null;

    private Context context;
    private int resource;
    
	public ArrayAdapterSelectTest(Context context, ArrayList<LineGroup> values){

		this.objects = values;
		this.context = context;
		
		inflater = (LayoutInflater)context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		
	}
/*
	public void reset(ArrayList<IndexEntry> values)
	{
		this.valuesAll = (ArrayList<IndexEntry>) values.clone();

	}
*/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) { 
			LayoutInflater inflater = (LayoutInflater) context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			convertView = inflater.inflate(R.layout.item_select_test, null);
		}
		
		LineGroup entry = objects.get(position);

		TextView text = (TextView)convertView.findViewById(R.id.tvName);
		text.setText(Html.fromHtml(entry.getName()));

		return convertView;
	}

	public int getCount(){
		return objects.size();
	}

	public long getItemId(int position){
		return position;
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}	
	

}
 
