package com.alexmochalov.expr;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import com.alexmochalov.ddic.R;
import com.alexmochalov.ddic.R.id;
import com.alexmochalov.ddic.R.layout;

public class ArrayAdapterExpr  extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<String> objects;
	
	//public interface OnButtonClickListener {
	//	public void onEdit(String text);
	//	public void onAdd(String text);
	//}
	
	//public OnButtonClickListener listener;

	ArrayAdapterExpr(Context context, ArrayList<String> obj) {
		inflater = (LayoutInflater)context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		objects = obj;
	}
	
	
	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) { 
			convertView = inflater.inflate(R.layout.item_expr, null);
		}
		String record = objects.get(position);
				
		TextView textViewName = (TextView)convertView.findViewById(R.id.tvName);
		
    	textViewName.setText(record);
		
		return convertView;
	}
}
