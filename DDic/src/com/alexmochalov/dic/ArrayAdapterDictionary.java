package com.alexmochalov.dic;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import com.alexmochalov.ddic.R;
import com.alexmochalov.ddic.R.id;
import com.alexmochalov.ddic.R.layout;

/**
 * 
 * @author Alexey Mochalov
 * This Adapter shows the drop-down list in the Dictionary 
 *
 */
public class ArrayAdapterDictionary  extends ArrayAdapter<IndexEntry>
{
	private List<IndexEntry> originalData = null;
	private List<IndexEntry> filteredData = null;

    private ItemFilter mFilter = new ItemFilter();
    
    private Context context;
    private int resource;
    
	public AdapterCallback callback = null;
	public interface AdapterCallback {
		void valuesFiltered(); 
	} 


	public ArrayAdapterDictionary(Context context, int res, ArrayList<IndexEntry> values){
		super(context, res, values);

		this.originalData = values;
		this.filteredData = values;

		this.resource = res;
		this.context = context;
		
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
			convertView = inflater.inflate(R.layout.dic_string, null);
		}
		IndexEntry entry = filteredData.get(position);

		TextView text = (TextView)convertView.findViewById(R.id.dicstringTextView);
		text.setText(""+entry.getText());

		return convertView;
	}

	public int getCount(){
		return filteredData.size();
	}

	public long getItemId(int position){
		return position;
	}	
	

	private class  ItemFilter extends Filter 
	{
		
		/*
		@Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<IndexEntry> list = suggestions;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }

    } */
		
		@Override 
		public String convertResultToString(Object resultValue) {
			return ((IndexEntry)resultValue).getText();
		}

//        if(indexEntry.getText().toLowerCase().startsWith(constraint.toString().toLowerCase())){
		
		@Override 
		protected FilterResults performFiltering(CharSequence constraint) { 
			String filterString = constraint.toString().toLowerCase();
			
			FilterResults results = new FilterResults();
			
			final List<IndexEntry> list = originalData;

			int count = list.size();
			final ArrayList<IndexEntry> nlist = new ArrayList<IndexEntry>(count);

			IndexEntry indexEntry ;
			
			for (int i = 0; i < count; i++) {
				indexEntry = list.get(i);
		        if (indexEntry.getText().toLowerCase().startsWith(constraint.toString().toLowerCase())){
					nlist.add(indexEntry);
				}
			}
			
			results.values = nlist;
			results.count = nlist.size();

			return results;
		}
		
		//adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, products);
        //lv.setAdapter(adapter);    

		@Override 
		protected void publishResults(CharSequence constraint, FilterResults results)
		{
			
            
            filteredData = (ArrayList<IndexEntry>) results.values;
            notifyDataSetChanged();
           
                if (callback != null){
                	callback.valuesFiltered();
                	callback = null;
                }
                
		} 
	}
	

	@Override 
	public Filter getFilter() { 
		return mFilter; 
	}
	
	public IndexEntry getItem(int position) {
		if (position < filteredData.size() )
			return filteredData.get(position);
		else
			return null;
	}

}
 
