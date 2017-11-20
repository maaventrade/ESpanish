package com.alexmochalov.tree;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;

import com.alexmochalov.ddic.R;

import java.text.*;
import java.util.*;

import com.alexmochalov.dic.IndexEntry;

public class AdapterTree extends BaseExpandableListAdapter {
	private LayoutInflater inflater;
	
	private Activity mActivity;
	private Context mContext;
	
	private List<String> mGroups; // header titles
	private HashMap<String, List<IndexEntry>> mChilds;
	
	public interface OnButtonClickListener {
		public void onEdit(String text);
		public void onAdd(String text);
	}
	public OnButtonClickListener listener;

	AdapterTree(Activity activity, Context context, List<String> groups,
			HashMap<String, List<IndexEntry>> childs) {
		
		mContext = context;
		mActivity = activity;
		inflater = (LayoutInflater)context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mGroups = groups;
		mChilds = childs;
		
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return mChilds.get(mGroups.get(groupPosition)).get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		return getView(groupPosition, childPosition, isLastChild, false,
				convertView, parent);
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {

		if (mChilds.get(mGroups.get(groupPosition)) == null)
			return 0;

		else
			return mChilds.get(mGroups.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGroups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mGroups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		return getView(groupPosition, -1, false, isExpanded, convertView,
				parent);
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@SuppressLint("NewApi")
	private View getView(final int groupPosition, final int childPosition,
			boolean isLastChild, boolean isExpanded, View convertView,
			ViewGroup parent) {
		
		if (convertView == null) { 
			convertView = inflater.inflate(R.layout.item_tree, null);
		}
				
		if (childPosition >= 0){
			convertView.setPadding(40, 0, 0, 0);
		}
		TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
		
		if (childPosition < 0)
			tvName.setText( (String) getGroup(groupPosition));
		else {
			IndexEntry record = (IndexEntry)getChild(groupPosition, childPosition);
			tvName.setText(record.getText());
		}

		ImageButton ibFolder = (ImageButton)convertView.findViewById(R.id.ibFolder);
		//if (record.isDirectory())
		//	ibFile.setImageResource(R.drawable.folder);
		//else	
		//	ibFile.setImageResource(R.drawable.file);
		
		

		return convertView;
	}
}
