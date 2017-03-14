package com.alexmochalov.rules;

import java.util.ArrayList;

import com.alexmochalov.alang.R;
import com.alexmochalov.dictionary.*;

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
public class ArrayAdapterDictionary  extends ArrayAdapter<IndexEntry>
{
	private ArrayList<IndexEntry> values;
	private ArrayList<IndexEntry> suggestions;
	private ArrayList<IndexEntry> valuesAll;
	
	Context context;
	int resource;

	public ArrayAdapterDictionary(Context context, int res, ArrayList<IndexEntry> values){
		super(context, res, values);
		this.values = values;
		this.valuesAll = (ArrayList<IndexEntry>) values.clone();
		this.suggestions = new ArrayList<IndexEntry>();
		
		this.resource = res;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) { 
			LayoutInflater inflater = (LayoutInflater) context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			convertView = inflater.inflate(R.layout.dic_string, null);
		}
		IndexEntry entry = values.get(position);
				
		TextView text = (TextView)convertView.findViewById(R.id.textViewText);
		text.setText(entry.getText());

		text = (TextView)convertView.findViewById(R.id.textViewTranslation);
		//text.setText(entry.getTranslation());
		
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
		@Override 
		public String convertResultToString(Object resultValue) {
			return ((IndexEntry)resultValue).getText();
		}
		
		@Override 
		protected FilterResults performFiltering(CharSequence constraint) { 
			if(constraint != null) {
            	//Log.d("z", "START <"+constraint+">");
                suggestions.clear();
                
                String constraintString = constraint.toString().toLowerCase().
	    				replaceAll("á", "a").
	    				replaceAll("ó", "o").
	    				replaceAll("ú", "u").
	    				replaceAll("é", "e");
                int i;
                for (i = 0; i < valuesAll.size(); i++) 
                	if (constraintString.charAt(0) == valuesAll.get(i).getText().charAt(0)){
                		break;
                	}	

            	//Log.d("z", "i "+i);
                while (i < valuesAll.size()){
                	if (constraintString.charAt(0) != valuesAll.get(i).getText().toLowerCase().charAt(0))
                		break;
                	
                    if(valuesAll.get(i).getText().
                    		toLowerCase().
		    				replaceAll("á", "a").
		    				replaceAll("ó", "o").
		    				replaceAll("ú", "u").
		    				replaceAll("é", "e").
                    		startsWith(constraintString))
                        suggestions.add(valuesAll.get(i));
                     i++;
                }
                
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
            	//Log.d("z", "filterResults.count "+filterResults.count);
                return filterResults;
            } else {
            	//Log.d("z", "ZERO");
                return new FilterResults();
            }
		}
		
		@Override 
		protected void publishResults(CharSequence constraint, FilterResults results)
		{
			ArrayList<IndexEntry> filteredList = (ArrayList<IndexEntry>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (IndexEntry c : filteredList) {
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
 
