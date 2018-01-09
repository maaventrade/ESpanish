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
import com.alexmochalov.main.Utils;
import android.view.SurfaceHolder.*;

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
											   
		Toast.makeText(mContext, ""+selectedGroupIndex+"  "+selectedItemIndex+"  "+index, Toast.LENGTH_LONG).show();			   
											   
		lvTree.setItemChecked(index, true);
		
	}

	public void select()
	{
		/*lvTree.expandGroup(selectedGroupIndex);
		
		int index = lvTree.getFlatListPosition(ExpandableListView
											   .getPackedPositionForChild(selectedGroupIndex, selectedItemIndex));
		lvTree.setItemChecked(index, true);
		*/
		new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					int index = lvTree.getFlatListPosition(ExpandableListView
														   .getPackedPositionForChild(selectedGroupIndex, selectedItemIndex));
					lvTree.setItemChecked(index, true);
					//lvTree.setSelection(index);
					//adapterTree.notifyDataSetChanged();
				}
			}, 500);
		
	

	}
	
	public void save()
	{
		Tree.save(mContext, "tree_"+Utils.getLanguageNoRus()+".xml");
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
		
		Tree.copy(mContext, "tree_"+Utils.getLanguageNoRus()+".xml", "tree_"+Utils.getLanguageNoRus()+"_bak.xml"); 
		
		Tree.loadXML(mContext, "tree_"+Utils.getLanguageNoRus()+".xml");
		
	}

	public FragmentTree()
	{
		super();
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		
		setHasOptionsMenu(true);
		
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
					
					if (listener != null) 
						listener.itemSelected(
								com.alexmochalov.dic.Dictionary.find(
								Tree.getName(groupPosition, childPosition)));
					

					return true;
				}

			});


		/*
		 lvTree.setOnItemClickListener( new ListView.OnItemClickListener(){
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
		 
		 if (firstVisible >= 0){
			 //int index = lvTree.getFlatListPosition(ExpandableListView
			//										.getPackedPositionForChild(selectedGroupIndex, selectedItemIndex));
			
			 lvTree.setSelection(firstVisible);
		 }
			

		return rootView;
	}

	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.tree, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public void addGroup() {
		Tree.addGroup(-1);	
		adapterTree.notifyDataSetChanged();
	}

	@Override
	public void onPause() {
		// Tree.save(mContext, "tree_"+Utils.getLanguageNoRus()+".xml");
		super.onPause();
	}

	public void addItem() {
		
		DialogEdit dialog = new DialogEdit(mContext, Tree.getLine(selectedGroupIndex, selectedItemIndex), adapterTree, true);
		dialog.show();
		mModified = true;
		//if (selectedGroupIndex >= 0)
			//if (listener != null)
				//listener.onAddSelected(selectedGroupIndex);
	}

	public void edit() {
		mModified = true;
		if (selectedItemIndex >= 0){
			/*
			if (listener != null)
				listener.onEditSelected(Tree.getName(selectedGroupIndex, selectedItemIndex));*/
				
			DialogEdit dialog = new DialogEdit(mContext, Tree.getLine(selectedGroupIndex, selectedItemIndex), adapterTree, false);
			/*dialog.callback = new DialogEdit.CallbackOk(){

				@Override
				public void onOk()
				{
					adapterTree.notifyDataSetChanged();
				}
			};   */
				dialog.show();
				
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("Edit");

			final EditText name = new EditText(mContext);
			name.setInputType(InputType.TYPE_CLASS_TEXT);
			builder.setView(name);
			
			name.setText(Tree.getName(selectedGroupIndex, selectedItemIndex));

			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						Tree.setName(selectedGroupIndex, selectedItemIndex, name.getText().toString());
						
						//listViewFiles.setItemChecked(selectedItemIndex, true); /// ???????????

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
	}

	public void setText(String text) {
		if (selectedItemIndex >= 0){
			Tree.setName(selectedGroupIndex, selectedItemIndex, text);
			adapterTree.notifyDataSetChanged();
		}
	}

	public void addChild(String text) {
		mModified = true;
		selectedItemIndex = Tree.insertItem(selectedGroupIndex, text);
	}

	public void getFirstVisiblePosition() {
		firstVisible = lvTree.getFirstVisiblePosition();
		//mModified = true;
		//listView.getFirstVisiblePosition();
		//listView.getChildAt(0)
		//setSelection(index) 
		
	}

	public boolean isModified() {
		return mModified;
	}

	public String getCurrentText() {
		return Tree.getName(selectedGroupIndex, selectedItemIndex);		
	}

	public void deleteItem() {
		mModified = true;
		Tree.delete(selectedGroupIndex, selectedItemIndex);
		adapterTree.notifyDataSetChanged();
	}
	

}
