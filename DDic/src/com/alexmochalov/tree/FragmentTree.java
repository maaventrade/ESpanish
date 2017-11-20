package com.alexmochalov.tree;

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.alexmochalov.ddic.R;

import java.io.File;
import java.util.*;

import com.alexmochalov.dic.IndexEntry;

public class FragmentTree extends Fragment
{
	private Activity mContext;
	private View rootView;

	private ExpandableListView listView;
	private AdapterTree adapter;

	private int selectedGroupIndex = -1;
	private int selectedItemIndex = -1;

	public interface OnStartProgrammListener
	{
		public void onGoSelected(String text);
		public void onEditSelected(String text);
	}

	public OnStartProgrammListener listener;

	public FragmentTree(Activity context)
	{
		super();
		mContext = context;
	}

	public FragmentTree()
	{
		super();
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        rootView = inflater.inflate(R.layout.fragment_tree, container, false);

		listView = (ExpandableListView)rootView.findViewById(R.id.ListViewTree); 

		Tree.readFilesList();
		
		adapter = new AdapterTree(mContext, mContext, Tree.getGroups(), Tree.getChilds());

		adapter.listener = new AdapterTree.OnButtonClickListener(){

			@Override
			public void onEdit(String text)
			{
				//Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
				if (listener != null && text.length() > 0)
					listener.onGoSelected(text);

			}

			@Override
			public void onAdd(String text)
			{

			}
		};

		listView.setAdapter(adapter);
/*
		listView.setOnGroupClickListener(new OnGroupClickListener() {
				@Override
				public boolean onGroupClick(ExpandableListView parent, View v,
											int groupPosition, long id)
				{

					int index = parent.getFlatListPosition(ExpandableListView
														   .getPackedPositionForGroup(groupPosition));
					parent.setItemChecked(index, true);

					IndexEntry pFile = (IndexEntry)adapter.getGroup(groupPosition);

					if (pFile.isDirectory())
					{
						selectedGroupIndex = groupPosition;
						selectedItemIndex = -1;
					}
					else
					{
						selectedGroupIndex = -1;
						selectedItemIndex = groupPosition;
					}

					return false;
				}
			});

		listView.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
											int groupPosition, int childPosition, long id)
				{

					int index = parent.getFlatListPosition(ExpandableListView
														   .getPackedPositionForChild(groupPosition, childPosition));
					parent.setItemChecked(index, true);

					selectedGroupIndex = groupPosition;
					selectedItemIndex = childPosition;

					return true;
				}

			});
*/

		/*
		 listView.setOnItemClickListener( new ListView.OnItemClickListener(){
		 @Override
		 public void onItemClick(AdapterView<?> adapter, View p2, int index, long p4)
		 {
		 //String selectedString1 = (String) adapter.getItemAtPosition(index);
		 if (selectedStringIndex == index){
		 FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();

		 FragmentPlayer fragmentPlayer = new FragmentPlayer(mContext);

		 Bundle args = new Bundle();
		 args.putString("name", files.get(selectedStringIndex));
		 fragmentPlayer.setArguments(args);

		 ft.replace(R.id.frgmCont, fragmentPlayer, FragmentPlayer.TAG_FRAGMENT_PLAYER);
		 ft.addToBackStack(null);

		 ft.commit();
		 }
		 else selectedStringIndex = index;
		 }}
		 );	
		 */

		return rootView;
	}

/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		switch (id)
		{
			case R.id.action_edit:
				if (selectedItemIndex >= 0)
				{
					FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();

					FragmentEditor fragmentEditor = new FragmentEditor(mContext);

					Bundle args = new Bundle();

					if (selectedGroupIndex == -1)
						args.putString("name",
									   Files.getItem(selectedGroupIndex, selectedItemIndex).getName()
									   );
					else
						args.putString("name",
									   Files.getGroup(selectedGroupIndex).getName() + "/" +
									   Files.getItem(selectedGroupIndex, selectedItemIndex).getName()
									   );

					fragmentEditor.setArguments(args);

					ft.replace(R.id.frgmCont, fragmentEditor, TAG_FRAGMENT_EDITOR);
					ft.addToBackStack(null);

					ft.commit();
				} else if (selectedGroupIndex >= 0){
					DialogAddPasteRename("renameGroup", getResources().getString(R.string.action_rename_group));
				}
				return true;

			case R.id.action_delete:
				if (selectedItemIndex >= 0)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setMessage("Delete " +
									   Files.getItem(selectedGroupIndex, selectedItemIndex) + ". Are you sure?").setPositiveButton("Yes", dialogClickListener)
					    .setNegativeButton("No", dialogClickListener).show();

				}

				return true;
			case R.id.action_copy:
				if (selectedItemIndex >= 0)
					copyPFile = Files.getItem(selectedGroupIndex, selectedItemIndex);
				else 
					Toast.makeText(mContext, getResources().getString(R.string.warning_select_programm), Toast.LENGTH_LONG).show();
				return true;
			case R.id.action_paste:
				if (copyPFile !=  null)
					DialogAddPasteRename("paste", getResources().getString(R.string.action_add_child));
				return true;
			case R.id.action_rename:
				//if (copyItemIndex >= 0)
				//{
				//	DialogAddPasteRename("rename", getResources().getString(R.string.action_add_child));
				//}

				return true;

			case R.id.action_add_child:
				DialogAddPasteRename("add", getResources().getString(R.string.action_add_child));
				return true;

			case R.id.action_add:
				DialogAddPasteRename("addGroup", getResources().getString(R.string.action_add_group));
				return true;

			case R.id.action_calendar:
				FragmentTransaction ft = mContext.getFragmentManager().beginTransaction();

				FragmentCalendar fragmentCalendar = new FragmentCalendar(mContext);

				Bundle arguments = new Bundle();
				return true;
			case R.id.action_archive:
				archive();
				return true;
			case R.id.action_extract:
				extract();
				return true;						
			default:	
				return super.onOptionsItemSelected(item);
		}
	}
*/

}
