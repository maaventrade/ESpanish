package com.alexmochalov.espanish;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.alex_mochalov.navdraw.R;

public class FragmentMenu extends Fragment {
	View rootView;
	ExpListAdapter adapter;
	ExpandableListView mDrawerTree;

	OnMenuItemSelectedListener mCallback;

	public void refresh()
	{
		adapter.notifyDataSetChanged();
	}

	public interface OnMenuItemSelectedListener {
		public void onMenuItemSelected(int groupPosition, int childPosition,String type);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.fragment_menu, container, false);

		mDrawerTree = (ExpandableListView) rootView
				.findViewById(R.id.fm_listView);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	public void setMenu(Context context) {
		
		adapter = new ExpListAdapter(context, MenuData.menuData);
		mDrawerTree.setAdapter(adapter);
		mDrawerTree.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				if (mCallback != null)
mCallback.onMenuItemSelected(groupPosition, childPosition,MenuData.getType(groupPosition, childPosition));
				
				return false;
			}
		});
	}

}
