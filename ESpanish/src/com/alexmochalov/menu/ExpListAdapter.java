package com.alexmochalov.menu;

import android.content.*;
import android.text.*;
import android.view.*;
import android.widget.*;

import com.alexmochalov.alang.*;
import com.alexmochalov.menu.MenuData.*;

import java.util.*;

public class ExpListAdapter extends BaseExpandableListAdapter {

	//private ArrayList<MenuData> mGroups;
	private Context mContext;

	public ExpListAdapter(Context context) {
		mContext = context;
		//mGroups = menuData;
	}

	@Override
	public int getGroupCount() {
		return  MenuData.getGroupsSize(); 
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return MenuData.getChildrenCoun(groupPosition);
	}

	@Override
	public Object getGroup(int groupPosition) {
		return MenuData.getMenuGroup(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return MenuData.getMenuChild(groupPosition, childPosition);
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
		textGroup.setText(MenuData.getGroupTitle(groupPosition));

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
		textChild.setText(Html.fromHtml(MenuData.getChildTitle(groupPosition, childPosition)));
		
		TextView textChildCount = (TextView) convertView
				.findViewById(R.id.textChildCount);
		textChildCount.setText(MenuData.getCountStr(groupPosition, childPosition));

		TextView textChildNote = (TextView) convertView
				.findViewById(R.id.textNote);
		textChildNote.setText(Html.fromHtml(
			MenuData.getChildNote(groupPosition, childPosition)));
		
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.imageView1);
		
		if (MenuData.isFinished(groupPosition, childPosition)){
			imageView.setVisibility(View.VISIBLE);
			textChildCount.setVisibility(View.INVISIBLE);
		} else {
			imageView.setVisibility(View.INVISIBLE);
			textChildCount.setVisibility(View.VISIBLE);
		}
		
		
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
