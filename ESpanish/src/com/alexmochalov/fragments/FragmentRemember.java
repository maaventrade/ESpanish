package com.alexmochalov.fragments;

import android.media.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.alexmochalov.alang.R;
import com.alexmochalov.dictionary.*;
import com.alexmochalov.fragments.PagerAdapterRemember.OnEventListener;
import com.alexmochalov.menu.MenuData;
import com.alexmochalov.root.*;

import java.io.*;

import android.support.v4.view.*;
import android.support.v4.app.*;

import java.util.*;

public class FragmentRemember extends FragmentM{
 	private ViewPager pager;
  
	private PagerAdapterRemember adapter;
	
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	} 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_remember, container, false);
		pager = (ViewPager)rootView.findViewById(R.id.pager);
		adapter = new PagerAdapterRemember(getActivity());
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
		
        pager.setAdapter(adapter);
        
		return rootView;
	}

	public String getTextToTTS() {
		return adapter.getText(pager.getCurrentItem());
	}

}
