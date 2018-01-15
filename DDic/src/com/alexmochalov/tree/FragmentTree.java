package com.alexmochalov.tree;

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.*;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.alexmochalov.ddic.R;

import java.io.File;
import java.util.*;

import com.alexmochalov.dic.IndexEntry;
import com.alexmochalov.main.Utils;

import android.view.SurfaceHolder.*;
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

public void copyTree(String name)
{
 	Tree.clear();
	Tree.loadXML(mContext, "tree_"+name+".xml");
	Tree.clearText();
	
	adapterTree.notifyDataSetChanged();
	mModified = true;
}

	public void paste()
	{
		mModified = true;
		selectedItemIndex = Tree.paste(selectedGroupIndex);
		adapterTree.notifyDataSetChanged();
		
		
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
					
					if (listener != null){
						String name;
						if (Utils.isInvertedDic())
							name = Tree.getTranslation(groupPosition, childPosition);
						else
							name = Tree.getName(groupPosition, childPosition);
						IndexEntry e = com.alexmochalov.dic.Dictionary.find(name);
						listener.itemSelected(e);
					}
						
					

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

	@Override
	public void onPause() {
		super.onPause();
	}

	public void addItem() {
		DialogEdit dialog = new DialogEdit(mContext, Tree.getLine(selectedGroupIndex, selectedItemIndex), adapterTree, true);
		dialog.show();
		mModified = true;
	}

	public void edit(final boolean newGroup) {
		mModified = true;
		if (selectedItemIndex >= 0 && !newGroup){
			editItem();

			// DialogEdit dialog = new DialogEdit(mContext, Tree.getLine(selectedGroupIndex, selectedItemIndex), adapterTree, false);
			// dialog.show();
			
		} else 
			editGroup(newGroup);
	}

	private void editItem() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		builder.setView(layout);

	//	LayoutParams lp = (LayoutParams)((ViewGroup)layout).getLayoutParams();
	//	((MarginLayoutParams) lp).leftMargin = 10;
		
		LinearLayout layout1 = new LinearLayout(mContext);
		layout1.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(layout1);
				
		TextView nameCaption = new TextView(mContext);
		nameCaption.setText("Name:");
		
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		llp.setMargins(10, 0, 0, 0); 
		nameCaption.setLayoutParams(llp);
		
		layout1.addView(nameCaption);
		
		final EditText name = new EditText(mContext);
		name.setInputType(InputType.TYPE_CLASS_TEXT);
		layout1.addView(name);

		LinearLayout layout2 = new LinearLayout(mContext);
		layout2.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(layout2);

		TextView translationCaption = new TextView(mContext);
		translationCaption.setText("Translation:");
		translationCaption.setLayoutParams(llp);
		
		layout2.addView(translationCaption);
		
		final EditText translation = new EditText(mContext);
		translation.setInputType(InputType.TYPE_CLASS_TEXT);
		llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		translation.setLayoutParams(llp);
		layout2.addView(translation);
		
		llp.setMargins(0, 0, 0, 20); 
		layout2.setLayoutParams(llp);

		builder.setTitle("Edit");
		name.setText(Tree.getName(selectedGroupIndex, selectedItemIndex));
		translation.setText(Tree.getTranslation(selectedGroupIndex, selectedItemIndex));

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					Tree.setName(selectedGroupIndex, selectedItemIndex, name.getText().toString());
					Tree.setTranslation(selectedGroupIndex, selectedItemIndex, translation.getText().toString());
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

	private void editGroup(final boolean newGroup) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

		final EditText name = new EditText(mContext);
		name.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(name);

		if (newGroup){
			builder.setTitle("New group");
		} else {
			builder.setTitle("Edit group");
			name.setText(Tree.getName(selectedGroupIndex, selectedItemIndex));
		}

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					if (newGroup) {
						Tree.addGroup(selectedGroupIndex, name.getText().toString());	
					} else 
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
		if (Tree.delete(selectedGroupIndex, selectedItemIndex))
			adapterTree.notifyDataSetChanged();
		else
			Toast.makeText(mContext, "Group is not empty!", Toast.LENGTH_LONG).show();
	}
	

}
