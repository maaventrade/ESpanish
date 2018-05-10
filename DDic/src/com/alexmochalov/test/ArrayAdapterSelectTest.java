package com.alexmochalov.test;

import android.content.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import com.alexmochalov.ddic.*;
import com.alexmochalov.main.*;
import com.alexmochalov.tree.*;
import java.util.*;

/**
 */   
public class ArrayAdapterSelectTest  extends BaseAdapter
{
	private LayoutInflater inflater;
	private List<LineGroup> objects = null;

    private Context mContext;
    private int resource;
	
	
    
	public ArrayAdapterSelectTest(Context context, ArrayList<LineGroup> values){

		this.objects = values;
		mContext = context;
		
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
			LayoutInflater inflater = (LayoutInflater) mContext.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			convertView = inflater.inflate(R.layout.item_select_test, null);
		}
		
		LineGroup entry = objects.get(position);

		TextView text = (TextView)convertView.findViewById(R.id.tvName);
		text.setText(Html.fromHtml(entry.getName()));
		
		TextView text1 = (TextView)convertView.findViewById(R.id.tvCompleted);
		text1.setText(""+ entry.getCompleted() );
		
		
		if (entry.getName().equals( Utils.getLastName())){
			text.setTextColor( mContext.getResources().getColor(R.color.yellow));
			text1.setTextColor( mContext.getResources().getColor(R.color.yellow));
		} else {
			text.setTextColor( mContext.getResources().getColor(R.color.navy));
			text1.setTextColor( mContext.getResources().getColor(R.color.navy));
		}
		
	
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
 
