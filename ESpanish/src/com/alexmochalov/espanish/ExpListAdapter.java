package com.alexmochalov.espanish;

import java.util.ArrayList;

import com.alex_mochalov.navdraw.R;
import com.alexmochalov.espanish.MenuData.MenuGroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpListAdapter extends BaseExpandableListAdapter {

	private ArrayList<MenuGroup> mGroups;
	private Context mContext;

	public ExpListAdapter(Context context, ArrayList<MenuGroup> menuData) {
		mContext = context;
		mGroups = menuData;
	}

	@Override
	public int getGroupCount() {
		return mGroups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mGroups.get(groupPosition).mChilren.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGroups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mGroups.get(groupPosition).mChilren.get(childPosition);
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.menu_group_view, null);
		}

		if (isExpanded) {
			// Изменяем что-нибудь, если текущая Group раскрыта
		} else {
			// Изменяем что-нибудь, если текущая Group скрыта
		}

		TextView textGroup = (TextView) convertView
				.findViewById(R.id.textGroup);
		textGroup.setText(mGroups.get(groupPosition).title);

		return convertView;

	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.menu_child_view, null);
		}

		TextView textChild = (TextView) convertView
				.findViewById(R.id.textChild);
		textChild.setText(mGroups.get(groupPosition).mChilren.get(childPosition).title);
		
		TextView textChildCount = (TextView) convertView
				.findViewById(R.id.textChildCount);
		textChildCount.setText(MenuData.getCountStr(groupPosition, childPosition));

		/*
		Button button = (Button) convertView.findViewById(R.id.buttonChild);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(mContext, "button is pressed", 5000).show();
			}
		});
		*/
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


}
