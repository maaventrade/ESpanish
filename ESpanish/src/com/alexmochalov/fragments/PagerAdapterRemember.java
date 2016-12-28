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
        text.setTexts(remEntry.getText(), remEntry.getRus());

        ListView listView = (ListView)layout.findViewById(R.id.samples);
        ArrayList<String> arrayList = new ArrayList<String>();
        
        String translation = Dic.getTranslation(remEntry.getText());
        
        arrayList.add("qwerqwrq");
        arrayList.add("356");
        arrayList.add("qwe4356456346rqwrq");
        arrayList.add("dfsdaf");

        ArrayAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayList);

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

    @Override
    public CharSequence getPageTitle(int position) {
    	RemEntry remEntry = mObjects.get(position);
    	return "";
        //return mContext.getString(customPagerEnum.getTitleResId());
    }

}

