package com.alexmochalov.conj;

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

/**
 * 
 * @author Alexey Mochalov
 * @param <TestEntry>
 *
 */   
public class ArrayAdapterConj  extends BaseAdapter
{
	private LayoutInflater inflater;
	private List<TestItem> objects = null;

    private Context context;
    private int resource;
    
	public ArrayAdapterConj(Context context, ArrayList<TestItem> values){

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
			convertView = inflater.inflate(R.layout.string_conj, null);
		}
		
		TestItem entry = objects.get(position);

		TextView text = (TextView)convertView.findViewById(R.id.tvText);
		text.setText(Html.fromHtml(entry.getText().toString()));

		TextView translation = (TextView)convertView.findViewById(R.id.tvTranslation);
		translation.setText(""+entry.getTranslation());

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
 
