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
	
	private List<LineGroup> mGroups; // Line titles
	private HashMap<LineGroup, List<LineItem>> mLines;
	
	public interface OnButtonClickListener {
		public void onEdit(String text);
		public void onAdd(String text);
	}
	public OnButtonClickListener listener;

	AdapterTree(Activity activity, Context context, List<LineGroup> groups,
			HashMap<LineGroup, List<LineItem>> Lines) {
		
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
				
		if (LinePosition < 0){
			TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
		
			ImageButton ibFolder = (ImageButton)convertView.findViewById(R.id.ibFolder);
			LineGroup h = (LineGroup) getGroup(groupPosition);
			tvName.setText(h.getName());
			ibFolder.setImageResource(R.drawable.folder);
			
			TextView tvCount =  (TextView)convertView.findViewById(R.id.tvCount);
			tvCount.setText("" + getChildrenCount(groupPosition));
		}	
		else {
			TextView tvName1 = (TextView)convertView.findViewById(R.id.tvName1);
		
			convertView.setPadding(40, 0, 0, 0);
			LineItem record = (LineItem)getChild(groupPosition, LinePosition);
			tvName1.setText(record.getText());
			TextView tvName2 = (TextView)convertView.findViewById(R.id.tvName2);
			tvName2.setText(record.getName2());
			
			TextView tvTranslation = (TextView)convertView.findViewById(R.id.tvTranslation);
			tvTranslation.setText(record.getTranslation());
		}

		return convertView;
	}
}
