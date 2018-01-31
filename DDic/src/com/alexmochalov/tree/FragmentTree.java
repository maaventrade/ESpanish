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

	public void find()
	{
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		// TRANSLATION
		final EditText etText = new EditText(mContext);
		etText.setInputType(InputType.TYPE_CLASS_TEXT);
		//llp1.setMargins(0, 0, 0, 60); 
		//translation.setLayoutParams(llp1);
		etText.setHint("text");
		//translation.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
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
										if (item.getName1().startsWith(text)
											|| item.getName2().startsWith(text)
											|| item.getTranslation().startsWith(text)){
											lvTree.expandGroup(i);
											select(i, k);
											return;
											}
									}
								}
								for (int i = 0; i <= sg; i++){
									LineGroup l = groups.get(i);
									for (int k = 0; k <= sg; k++){
										LineItem item = childs.get(l).get(k);
										if (item.getName1().startsWith(text)
											|| item.getName2().startsWith(text)
											|| item.getTranslation().startsWith(text)){
											lvTree.expandGroup(i);
											select(i, k);
											return;
										}
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

		Toast.makeText(mContext, "" + selectedGroupIndex + "  " + selectedItemIndex + "  " + index, Toast.LENGTH_LONG).show();			   

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
				public void run()
				{
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
		Tree.save(mContext, "tree" + ".xml");
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

					callItemSelected();

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

		if (firstVisible >= 0)
		{
			//int index = lvTree.getFlatListPosition(ExpandableListView
			//										.getPackedPositionForChild(selectedGroupIndex, selectedItemIndex));

			lvTree.setSelection(firstVisible);
		}


		return rootView;
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
	public void onPause()
	{
		super.onPause();
	}

	public void addItem()
	{
		DialogEdit dialog = new DialogEdit(mContext, (LineItem)Tree.getLine(selectedGroupIndex, selectedItemIndex), adapterTree, true);
		dialog.show();
		mModified = true;
	}

	public void edit(final boolean newGroup)
	{
		if (selectedItemIndex >= 0 && !newGroup)
		{
			editItem();
		}
		else 
			editGroup(newGroup);
	}

	private void editItem()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		builder.setView(layout);

		//	LayoutParams lp = (LayoutParams)((ViewGroup)layout).getLayoutParams();
		//	((MarginLayoutParams) lp).leftMargin = 10;

		FrameLayout layoutName = new FrameLayout(mContext);
		android.widget.FrameLayout.LayoutParams llpName = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//LinearLayout layoutName = new LinearLayout(mContext);
		//layout.setOrientation(LinearLayout.HORIZONTAL);
		//android.widget.LinearLayout.LayoutParams llpName = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		//layout1.setOrientation(LinearLayout.HORIZONTAL);
		layoutName.setLayoutParams(llpName);
		//layoutName.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
		layout.addView(layoutName);

		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		llp.setMargins(10, 0, 0, 0); 

		/*
		 TextView nameCaption = new TextView(mContext);
		 nameCaption.setText("Name:");

		 nameCaption.setLayoutParams(llp);
		 layout1.addView(nameCaption);
		 */

		LinearLayout.LayoutParams llp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		llp1.setMargins(0, 0, 0, 0); 
		llp1.gravity = Gravity.BOTTOM;

		// NAME

		final AutoCompleteTextView name = new AutoCompleteTextView(mContext);
		name.setInputType(InputType.TYPE_CLASS_TEXT);
		name.setLayoutParams(llp1);
		name.setHint("Name");
		name.setTextAppearance(mContext, android.R.style.TextAppearance_Large);
		//name.setBackgroundColor(mContext.getResources().getColor(R.color.blue));

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice, 
																Utils.getExpressions());

		name.setThreshold(1);
		name.setAdapter(adapter);
		layoutName.addView(name);


////		
		ImageButton ibDropDown = new ImageButton(mContext);
		android.widget.FrameLayout.LayoutParams llpDropDown = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		llpDropDown.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		ibDropDown.setImageResource(R.drawable.dropdown);
		ibDropDown.setBackgroundColor(Color.TRANSPARENT);
		ibDropDown.setLayoutParams(llpDropDown);
		ibDropDown.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v)
				{
					name.showDropDown();
				}});

		layoutName.addView(ibDropDown);

//////////////		

		LinearLayout layout2 = new LinearLayout(mContext);
		layout2.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(layout2);

		// TRANSLATION
		final EditText translation = new EditText(mContext);
		translation.setInputType(InputType.TYPE_CLASS_TEXT);
		llp1.setMargins(0, 0, 0, 60); 
		translation.setLayoutParams(llp1);
		translation.setHint("Translation");
		//translation.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
		translation.setTextAppearance(mContext, android.R.style.TextAppearance_Large);
		layout2.addView(translation);

		//llp.setMargins(0, 0, 0, 20); 
		//layout2.setLayoutParams(llp);

		builder.setTitle("Edit");
		//name.setText(Tree.getName(selectedGroupIndex, selectedItemIndex));
		translation.setText(Tree.getTranslation(selectedGroupIndex, selectedItemIndex));

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					mModified = true;
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

	public void addChild(String text)
	{
		mModified = true;
		selectedItemIndex = Tree.insertItem(selectedGroupIndex, text);
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
		if (Tree.delete(selectedGroupIndex, selectedItemIndex))
			adapterTree.notifyDataSetChanged();
		else
			Toast.makeText(mContext, "Group is not empty!", Toast.LENGTH_LONG).show();
	}




}
