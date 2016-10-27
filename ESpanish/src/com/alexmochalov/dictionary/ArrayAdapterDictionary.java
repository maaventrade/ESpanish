package com.alexmochalov.dictionary;

import java.util.ArrayList;

import com.alex_mochalov.navdraw.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

/**
 * 
 * @author Alexey Mochalov
 * This Adapter shows the drop-down list in the Dictionary 
 *
 */
public class ArrayAdapterDictionary  extends ArrayAdapter<Entry>
{
	private ArrayList<Entry> values;
	private ArrayList<Entry> suggestions;
	private ArrayList<Entry> valuesAll;
	
	Context context;
	int resource;

	public ArrayAdapterDictionary(Context context, int res, ArrayList<Entry> values){
		super(context, res, values);
		this.values = values;
		this.valuesAll = (ArrayList<Entry>) values.clone();
		this.suggestions = new ArrayList<Entry>();
		
		this.resource = res;
		this.context = context;
		
		Log.d("", "values----> "+values.size());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) { 
			LayoutInflater inflater = (LayoutInflater) context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			convertView = inflater.inflate(R.layout.dic_string, null);
		}
		Entry entry = values.get(position);
				
		TextView text = (TextView)convertView.findViewById(R.id.textViewText);
		text.setText(entry.getText());

		text = (TextView)convertView.findViewById(R.id.textViewTranslation);
		text.setText(entry.getTranslation());
		
		return convertView;
	}

	public int getCount(){
		return values.size();
	}

	public long getItemId(int position){
		return position;
	}	

	private Filter mFilter = new Filter() 
	{
		//@Override 
		public String convertResultToString(Object resultValue) {
			return ((Entry)resultValue).getText();
		}
		
		@Override 
		protected FilterResults performFiltering(CharSequence constraint) { 
			if(constraint != null) {
                suggestions.clear();
                for (Entry customer : valuesAll) {
                    if(customer.getText().
                    		toLowerCase().
		    				replaceAll("á", "a").
		    				replaceAll("ó", "o").
		    				replaceAll("ú", "u").
		    				replaceAll("é", "e").
                    		startsWith(
                    				constraint.toString().toLowerCase().
				    				replaceAll("á", "a").
				    				replaceAll("ó", "o").
				    				replaceAll("ú", "u").
				    				replaceAll("é", "e"))){
                    	
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
		}
		
		@Override 
		protected void publishResults(CharSequence constraint, FilterResults results)
		{
			ArrayList<Entry> filteredList = (ArrayList<Entry>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Entry c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
		} 
	};
		
		@Override 
		public Filter getFilter() { 
			return mFilter; 
		}

}
 
