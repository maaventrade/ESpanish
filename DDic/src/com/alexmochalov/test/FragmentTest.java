package com.alexmochalov.test;

import android.app.*;
import android.os.*;
import android.support.v4.view.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import com.alexmochalov.ddic.*;
import com.alexmochalov.tree.*;
import java.util.*;

public class FragmentTest extends FragmentM   implements OnClickListener{

 	private ViewPager pager;
 	  
	private PagerAdapterRemember adapter;
	
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_test, container, false);
		
		pager = (ViewPager)rootView.findViewById(R.id.pager);
		
		Bundle args = getArguments();
		int selectedGroupIndex =  args.getInt("selectedGroupIndex");
		
		

		ArrayList<LineItem> list = new ArrayList(Tree.getItems(selectedGroupIndex));
		
		adapter = new PagerAdapterRemember(getActivity(), list);
		
		adapter.listener = new PagerAdapterRemember.OnEventListener() {
			@Override
			public void onButtonChangeClick() {
				int position = pager.getCurrentItem();
		        pager.setAdapter(adapter);
		        pager.setCurrentItem(position);
			}

			@Override
			public void onButtonStartTestingClick() {
				if (mCallback != null)
					mCallback.onButtonStartTestingClick();
			}
		};
		Log.d("rem","created");
        pager.setAdapter(adapter);
        Log.d("rem","created all");
		return rootView;
	}

	public String getTextToTTS() {
		return adapter.getText(pager.getCurrentItem());
	}

	public String getWord() {
		return adapter.getText(pager.getCurrentItem());
	}

	
	private Activity mContext;

	
	public FragmentTest(Activity context)
	{
		super();
		mContext = context;
	}

	public FragmentTest()
	{
		super();
	}
	
	@Override
    public void onPause()
	{
		super.onPause();
	}
	
	@Override
    public void onResume()
	{
		super.onResume();
	}
	
	
	@Override
	public void onClick(View v) {
		/*
		if (v == ibSpeak){
			TtsUtils.speak(tvWord.getText().toString());
		} else if (v == ibList){
			ShowListOfExpressions();
		} else if (v == ibBack){
			if (stackArray.size() > 0){
				IndexEntry indexEntry =  Dictionary.find(stackArray.get(stackArray.size()-1));
				if (indexEntry != null)
					setTranslation(indexEntry, 0);
				stackArray.remove(stackArray.size()-1);
			}
		}*/	
	}

	
}
