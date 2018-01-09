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

public class AdapterTree extends BaseExpandableListAdapter
{



	
	
	private LayoutInflater inflater;
	
	private Activity mActivity;
	private Context mContext;
	
	private List<Line> mGroups; // Line titles
	private HashMap<Line, List<Line>> mLines;
	
	public interface OnButtonClickListener {
		public void onEdit(String text);
		public void onAdd(String text);
	}
	public OnButtonClickListener listener;

	AdapterTree(Activity activity, Context context, List<Line> groups,
			HashMap<Line, List<Line>> Lines) {
		
		mContext = context;
		mActivity = activity;
		inflater = (LayoutInflater)context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mGroups = groups;
		mLines = Lines;
		
	}
	
	@Override
	public Object getChild(int groupPosition, int LinePosititon) {
		return mLines.get(mGroups.get(groupPosition)).get(LinePosititon);
	}

	@Override
	public long getChildId(int groupPosition, int LinePosition) {
		return LinePosition;
	}

	@Override
	public View getChildView(int groupPosition, final int LinePosition,
			boolean isLastLine, View convertView, ViewGroup parent) {

		return getView(groupPosition, LinePosition, isLastLine, false,
				convertView, parent);
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {

		if (mLines.get(mGroups.get(groupPosition)) == null)
			return 0;

		else
			return mLines.get(mGroups.get(groupPosition)).size();
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
	public boolean isChildSelectable(int groupPosition, int LinePosition) {
		return true;
	}

	@SuppressLint("NewApi")
	private View getView(final int groupPosition, final int LinePosition,
			boolean isLastLine, boolean isExpanded, View convertView,
			ViewGroup parent) {
		
		if (convertView == null) { 
			if (LinePosition >= 0)
				convertView = inflater.inflate(R.layout.item_tree_child, null);
			else	
				convertView = inflater.inflate(R.layout.item_tree_header, null);
		}
				
		TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
		
		if (LinePosition < 0){
			ImageButton ibFolder = (ImageButton)convertView.findViewById(R.id.ibFolder);
			Line h = (Line) getGroup(groupPosition);
			tvName.setText(h.getName());
			ibFolder.setImageResource(R.drawable.folder);
		}	
		else {
			convertView.setPadding(40, 0, 0, 0);
			Line record = (Line)getChild(groupPosition, LinePosition);
			tvName.setText(record.getName());
			TextView tvTranslation = (TextView)convertView.findViewById(R.id.tvTranslation);
			tvTranslation.setText(record.getTranslation());
		}

		return convertView;
	}
}
