package com.alexmochalov.fragments;

import android.content.*;
import android.support.v4.view.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.*;

import com.alexmochalov.alang.R;
import com.alexmochalov.dictionary.Entry;
import com.alexmochalov.menu.MarkedString;
import com.alexmochalov.menu.MenuData;
import com.alexmochalov.dictionary.Dictionary;
import com.alexmochalov.files.Dic;
import android.util.*;
import com.alexmochalov.root.*;
import com.alexmochalov.dialogs.*;
import android.widget.AdapterView.*;
import android.widget.*;

public class PagerAdapterRemember extends PagerAdapter
 {

    private Context mContext;
	private ArrayList<RemEntry> mObjects = new ArrayList<RemEntry>();
	private boolean isRus = false;
	//private int mPosition = 0;
	
	public OnEventListener listener;
	
	public interface OnEventListener{
		public void onButtonChangeClick();
	}
	

    public PagerAdapterRemember(Context context) {
        mContext = context;
        ArrayList<MarkedString> data =  MenuData.getDataArray();
        for (MarkedString m: data)
        	mObjects.add(new RemEntry(m.getText(), m.getRusText()));
        
//        thisAdapter = this;
        
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        RemEntry remEntry = mObjects.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(remEntry.getLayoutResId(), collection, false);
        collection.addView(layout);
       
        ImageViewSlide text = (ImageViewSlide)layout.findViewById(R.id.slider);
		
        text.setTexts(
			Utils.firstLetterToUpperCase(remEntry.getText()), 
			Utils.firstLetterToUpperCase(remEntry.getRus()), isRus);
		
		text.listener = new ImageViewSlide.OnEventListener(){
			@Override
			public void onSlided(boolean pIsRus)
			{
				isRus = pIsRus;
				if (listener != null)
					listener.onButtonChangeClick();
				//			thisAdapter.notifyDataSetChanged();
			}
		};
		
        ListView listView = (ListView)layout.findViewById(R.id.samples);
		
        final ArrayList<String> arrayListExamples = new ArrayList<String>();
		final ArrayList<String> arrayListRus = new ArrayList<String>();
		
		listView.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position , long p4)
				{
					DialogExample dialog = new DialogExample(mContext,
															 arrayListExamples.get(position),
															 arrayListRus.get(position));
					dialog.show();
				}
			});

        
        String translation = Dic.getTranslation(remEntry.getText());
        String stringsArray[] = translation.split("\n");
		for (String s:stringsArray)
			if (s.indexOf("<ex>") >= 0){
				int i = s.indexOf("—");
				int j = s.indexOf("</ex>");
				if (i > 0){
					String eEx = s.substring(
						s.indexOf("<ex>")+4, i);
					String eRu;
					if (j >= 0)
						eRu = s.substring(i+1, j);
					else
						eRu = s.substring(i+1);
					arrayListRus.add(eRu);
					arrayListExamples.add(eEx);
				}
				
			}
		
			ArrayAdapter adapter;
		if (isRus)
			adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayListRus);
		else 
			adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayListExamples);
			
       
        listView.setAdapter(adapter);  
        
        /*
        if (isRus)
        	text.setText(remEntry.getRus());
        else
        	text.setText(remEntry.getText());
        */	
  
       /*
        ImageButton btn = (ImageButton)layout.findViewById(R.id.imageButtonChange); 
        btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				isRus = !isRus;
				if (listener != null)
					listener.onButtonChangeClick();
	//			thisAdapter.notifyDataSetChanged();
			}});
*/        
        
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

   
    public String getText(int position) {
    	return mObjects.get(position).getText();
        //return mContext.getString(customPagerEnum.getTitleResId());
    }

}

