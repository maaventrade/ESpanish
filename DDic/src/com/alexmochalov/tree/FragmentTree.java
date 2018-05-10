package com.alexmochalov.tree;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.*;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.alexmochalov.conj.Conj;
import com.alexmochalov.ddic.R;

import java.io.File;
import java.util.*;

import com.alexmochalov.dic.Dictionary;
import com.alexmochalov.dic.FragmentDic;
import com.alexmochalov.dic.IndexEntry;
import com.alexmochalov.main.MainActivity;
import com.alexmochalov.main.TtsUtils;
import com.alexmochalov.main.Utils;
import com.alexmochalov.test.DialogSelectTest;
import com.alexmochalov.tree.DialogEdit.CallbackOk;

import android.view.SurfaceHolder.*;
import android.view.inputmethod.InputMethodManager;

import com.alexmochalov.dic.*;

public class FragmentTree extends Fragment
{
	private Activity mContext;
	private boolean mModified = false;

	private View rootView;

	private ExpandableListView lvTree;
	private AdapterTree adapterTree;

	private int selectedGroupIndex = -1;
	private int selectedItemIndex = -1;

	private int firstVisible = -1;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		setHasOptionsMenu(true);

		mContext.getActionBar().setDisplayHomeAsUpEnabled(true);
		mContext.getActionBar().setDisplayShowHomeEnabled(false);
		
		setTitle();
		
        rootView = inflater.inflate(R.layout.fragment_tree, container, false);

		lvTree = (ExpandableListView)rootView.findViewById(R.id.ListViewTree);
		lvTree.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		adapterTree = new AdapterTree(mContext, mContext, Tree.getGroups(), Tree.getChilds());

		adapterTree.listener = new AdapterTree.OnButtonClickListener(){

			@Override
			public void onEdit(String text)
			{
				//Toast.makeText(mContext, ""+selectedItemIndex, Toast.LENGTH_LONG).show();
				//if (listener != null && text.length() > 0)
				//listener.onGoSelected(text);

			}

			@Override
			public void onAdd(String text)
			{

			}
		};

		lvTree.setAdapter(adapterTree);

		lvTree.setOnGroupClickListener(new OnGroupClickListener() {
				@Override
				public boolean onGroupClick(ExpandableListView parent, View v,
											int groupPosition, long id)
				{

					int index = parent.getFlatListPosition(ExpandableListView
														   .getPackedPositionForGroup(groupPosition));
					parent.setItemChecked(index, true);

					selectedGroupIndex = groupPosition;
					selectedItemIndex = -1;

					return false;
				}
			});

		lvTree.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
											int groupPosition, int childPosition, long id)
				{

					int index = parent.getFlatListPosition(ExpandableListView
														   .getPackedPositionForChild(groupPosition, childPosition));
					parent.setItemChecked(index, true);

					selectedGroupIndex = groupPosition;
					selectedItemIndex = childPosition;

					callItemSelected();

					return true;
				}


			});


		if (firstVisible >= 0)
		{
			lvTree.setSelection(firstVisible);
		}

		return rootView;
	}
	 
	private void setTitle() {
		mContext.getActionBar().setTitle(mContext.getResources().getString(R.string.action_tree)
				+ " ("
				+ Tree.getChildsCount()
				+")");
	}

	public int getSelectedGroupIndex()
	{
		return selectedGroupIndex;
	}

	public void find()
	{
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		// TRANSLATION
		final EditText etText = new EditText(mContext);
		etText.setInputType(InputType.TYPE_CLASS_TEXT);
		etText.setHint("text");
		etText.setTextAppearance(mContext, android.R.style.TextAppearance_Large);
		layout.addView(etText);
		
		final AlertDialog dialog = 
			new AlertDialog.Builder(mContext)
			.setIcon(R.drawable.ic_launcher)
			.setTitle(getResources().getString(R.string.find))
			.setView(layout)
			.setPositiveButton(android.R.string.ok, null)
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					return;
				}
			
			}).create();
			
		dialog.setOnShowListener(new DialogInterface.OnShowListener(){

				@Override
				public void onShow(DialogInterface p1)
				{
					
					Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
					button.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View view) {
																
								// TODO Do something
								HashMap<LineGroup, List<LineItem>> childs =  Tree.getChilds();
								ArrayList<LineGroup> groups =  Tree.getGroups();
								
								String text = etText.getText().toString();
								
								int sg = selectedGroupIndex;
								//if (sg < 0)
								//	sg = 0;
									
								int si = selectedItemIndex;
								//if (selectedItemIndex < 0)
								//	si = 0;
									
								for (int i = sg+1; i < groups.size(); i++){
									LineGroup l = groups.get(i);
									for (int k = si+1; k < childs.get(l).size(); k++){
										LineItem item = childs.get(l).get(k);
										if (item.getText().startsWith(text)
											|| item.getName2().startsWith(text)
											|| item.getTranslation().startsWith(text)){
											lvTree.expandGroup(i);
											select(i, k);
											select();
											hideHeyboard();
											return;
											}
									}
								}
								for (int i = 0; i < groups.size(); i++){
									LineGroup l = groups.get(i);
									for (int k = 0; k < childs.get(l).size(); k++){
										//Log.d("", ""+l.getName()+"  "+k);
										LineItem item = childs.get(l).get(k);
										if (item.getText().startsWith(text)
											|| item.getName2().startsWith(text)
											|| item.getTranslation().startsWith(text)){
											lvTree.expandGroup(i);
											select(i, k);
											select();
											hideHeyboard();
											return;
										};
										
										if (i == sg && k == si) return;

									}
								}
							}

						});
					/*
					
					
					*/
				}
			});
			
		dialog.show();
	}

	private void hideHeyboard() {

		InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
			
	}
	
	public void reload(String name)
	{
//		Tree.clear();
//		Tree.loadXML(mContext, "tree.xml");
		if (adapterTree != null)
			adapterTree.notifyDataSetChanged();
	}

	public void paste()
	{
		mModified = true;
		selectedItemIndex = Tree.paste(selectedGroupIndex);
		adapterTree.notifyDataSetChanged();
		setTitle();
	}

	public void copyItem()
	{
		mModified = true;
		Tree.copyItem(selectedGroupIndex, selectedItemIndex);
	}

	public void dialogSelect()
	{
		DialogSelect dialogSelect = new DialogSelect(mContext, true);
		dialogSelect.show();

	}

	public void select(int p0, int p1)
	{
		selectedGroupIndex = p0;
		selectedItemIndex = p1;

		int index = lvTree.getFlatListPosition(ExpandableListView
											   .getPackedPositionForChild(selectedGroupIndex, selectedItemIndex));
		lvTree.setItemChecked(index, true);
	}

	public void select()
	{

		new Handler().postDelayed(new Runnable() {
				@Override
				public void run()
				{
					
					if (selectedGroupIndex < 0 || selectedItemIndex < 0)
						return;
					
					int index = lvTree.getFlatListPosition(ExpandableListView
														   .getPackedPositionForChild(selectedGroupIndex, selectedItemIndex));
					lvTree.setItemChecked(index, true);
					lvTree.setSelection(index);
					callItemSelected();
				}
			}, 500);
	}

	public void save()
	{
		Tree.save(mContext, "tree" + ".xml");
		mModified = false;		
	}

	public interface OnTreeEventListener
	{
		public void onEditSelected(String text);
		public void onAddSelected(int selectedGroupIndex);
		public void itemSelected(IndexEntry indexEntry);

	}

	public OnTreeEventListener listener;

	public FragmentTree(Activity context)
	{
		super();
		mContext = context;

		Tree.copy(mContext, "tree.xml", "tree_bak.xml"); 
		Tree.loadXML(mContext, "tree.xml");

	}

	public FragmentTree()
	{
		super();
	}


	public void callItemSelected()
	{

		if (listener != null)
		{
			String name;
			if (Utils.isInvertedDic())
				name = Tree.getTranslation(selectedGroupIndex, selectedItemIndex);
			else
				name = Tree.getName(selectedGroupIndex, selectedItemIndex);
			IndexEntry e = com.alexmochalov.dic.Dictionary.find(name);
			listener.itemSelected(e);
		}

	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.tree, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				getActivity().onBackPressed();
				return true;
			default:
				return false;
		}
	}
	
	
	@Override
	public void onPause()
	{
		super.onPause();
	}

	public void edit(final boolean newGroup, final boolean newItem)
	{
		if (selectedItemIndex >= 0 && !newGroup && !newItem)
			editItem(false);
		else if (newItem)
			editItem(true);
		else 
			editGroup(newGroup);
	}

	private void editItem(final boolean newItem)
	{
		DialogEdit dialogEdit = new DialogEdit(mContext, newItem, selectedGroupIndex, selectedItemIndex);
		dialogEdit.callback = new CallbackOk() {
			@Override
			public void onOk(String text, String translation) {
				mModified = true;
				
				if (newItem){
					addChild(text, translation);
					select();
				} else {
					Tree.setName(selectedGroupIndex, selectedItemIndex, text);
					Tree.setTranslation(selectedGroupIndex, selectedItemIndex, translation);
				}
			}
		};
		dialogEdit.show();
		
	}

	private void editGroup(final boolean newGroup)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

		final EditText name = new EditText(mContext);
		name.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(name);

		if (newGroup)
		{
			builder.setTitle("New group");
		}
		else
		{
			builder.setTitle("Edit group");
			name.setText(Tree.getName(selectedGroupIndex, selectedItemIndex));
		}

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					mModified = true;
					if (newGroup)
					{
						Tree.addGroup(selectedGroupIndex, name.getText().toString());	
					}
					else 
						Tree.setName(selectedGroupIndex, selectedItemIndex, name.getText().toString());
					adapterTree.notifyDataSetChanged();
				}
			});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int p2)
				{
					dialog.cancel();
				}
			});
		builder.show();
	}

	public void setText(String text)
	{
		if (selectedItemIndex >= 0)
		{
			Tree.setName(selectedGroupIndex, selectedItemIndex, text);
			adapterTree.notifyDataSetChanged();
		}
	}

	public void addChild(String name, String translation)
	{
		mModified = true;
		selectedItemIndex = Tree.insertItem(selectedGroupIndex, name, translation);
		setTitle();
	}

	public void getFirstVisiblePosition()
	{
		firstVisible = lvTree.getFirstVisiblePosition();
		//mModified = true;
		//listView.getFirstVisiblePosition();
		//listView.getChildAt(0)
		//setSelection(index) 

	}

	public boolean isModified()
	{
		return mModified;
	}

	public String getCurrentText()
	{
		return Tree.getName(selectedGroupIndex, selectedItemIndex);		
	}

	public void deleteItem()
	{
		mModified = true;
		if (Tree.delete(selectedGroupIndex, selectedItemIndex)){
			adapterTree.notifyDataSetChanged();
			setTitle();
		}	
		else
			Toast.makeText(mContext, "Group is not empty!", Toast.LENGTH_LONG).show();
	}

	public LineItem getSelectedItem() {
		return Tree.getSelectedItem(selectedGroupIndex, selectedItemIndex);
	}




}
