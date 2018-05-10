package com.alexmochalov.test;

import java.util.ArrayList;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.alexmochalov.ddic.*;
import com.alexmochalov.dic.IndexEntry;
import com.alexmochalov.main.Utils;
import com.alexmochalov.test.FragmentRemember.EventListener;
import com.alexmochalov.tree.LineItem;
import com.alexmochalov.tree.Tree;

public class FragmentTest extends Fragment
 {

	private Context mContext;
	
	private ImageButton ibTest;
	private boolean buttonNext = false;
	
	private TextView tvText;
	private TextView tvTranslation;
	
	private View rootView;
	
	private ArrayList<TestItem> list;
	
	private int mIndex = 0;
	
	private int direction;

	private EventListener mCallback;
	
	public interface EventListener {
		public void onTested();
		public void onFinished(Fragment thisFragment);
		public void onButtonStartTestingClick();
		public void onNextWord(IndexEntry e);
	}
 	  
	public void setListener(EventListener e){
		mCallback = e; 
	}
	
	public FragmentTest(Activity context)
	{
		super();
		mContext = context;
	}
	
	
    /**
     * Prepare next step of the task
     */
    private boolean next() {
    	// 001 spa->ru completed  
    	// 010 ru->spa completed  
		
		tvText = (TextView)rootView.findViewById(R.id.tvText);
		tvTranslation = (TextView)rootView.findViewById(R.id.tvTranslation);

		//MenuData.getTypeOfTheStep(mTextViewText, mTranslation);
		//////////////////////////////////////////////////////////
		
		
		String text = "";
		String translation = "";
		
		TestItem l = list.get(mIndex);
		if (l.getCount() == 0){
			double random = Math.random();

			if (random > 0.5f)
				direction =  2;
			else
				direction =  1;		
		} else direction = 3 - l.getCount();
		
		String name;

		if (direction == 1){
			name = l.getTranslation();
			translation = l.getText();
		}	
		else {
			name = l.getText();
			translation = l.getTranslation();
		}	

		//IndexEntry e = com.alexmochalov.dic.Dictionary.find(name);
		
		tvText.setText(Utils.firstLetterToUpperCase(name));
		tvTranslation.setText(Utils.firstLetterToUpperCase(translation));
		
		//////////////////////////////////////////////////////////
		
        
		tvTranslation.setVisibility(View.INVISIBLE);   
		
		return true;
	}


	@Override
    public void onPause()
	{
		super.onPause();
	}
	
	@Override
    public void onResume()
	{
		super.onResume();
	}
	

	@Override
	public void onDestroy() {
		super.onDestroy(); 
	}

	void init(){
		/*thisFragment = this;
		mContext =this.getActivity();

		SharedPreferences prefs;
		prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		
		TextView TextViewPhraseInfo =  (TextView)rootView.findViewById(R.id.TextViewInfo);
		TextViewPhraseInfo.setText(MenuData.getCountStr());*/
	}

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
        rootView = inflater.inflate(R.layout.fragment_test, container, false);
        //MenuData.nextTestIndex();

		Bundle args = getArguments();
		int selectedGroupIndex =  args.getInt("selectedGroupIndex");
        
		list = new ArrayList<TestItem>();
		for (LineItem l:Tree.getItems(selectedGroupIndex))
			list.add(new TestItem(l.getText(), l.getTranslation()));
			
		
			
		setTitle();
		
       // init();
		nextIndex();
    	next();
    	
    	ibTest = (ImageButton)rootView.findViewById(R.id.ibTest);
    	ibTest.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				// Button Next is pressed
				if (buttonNext){
					if (nextIndex() == -1){
						//getActivity().getFragmentManager().beginTransaction().remove(thisFragment).commit();
					} else {
						next();
						
						EditText editText = (EditText)rootView.findViewById(R.id.etTranslation);
						editText.setText("");
						
						ibTest.setImageResource(R.drawable.icon_ok);
						buttonNext = false;
				
					}
				}	
				else {
					// Button Проверить is pressed
					ibTest.setImageResource(R.drawable.icon_next);
					buttonNext = true;
					
					EditText etTranslation = (EditText)rootView.findViewById(R.id.etTranslation);

					//TextView mTranslation = (TextView)rootView.findViewById(R.id.TextViewPhraseTranslation);
					
					tvTranslation.setVisibility(View.VISIBLE);
					
					if (list.get(mIndex).test(etTranslation.getText().toString(), direction)) 
					{
						tvTranslation.setTextColor(getColor(mContext, R.color.green1));
						
						//setTested();
						
						setTitle();
						
						
					}
					else 
						tvTranslation.setTextColor(Color.RED);
						
				}	
				
			}

			});
        
        return rootView;
    }

	private void setTitle()
	{
		getActivity().
		getActionBar().setTitle(mContext.getResources().getString(R.string.action_test)
										 +": "
										 + getTested()
										 +"/"+list.size()+"");

		
	}

	private int getTested()
	{
		int tested = 0;
		
		for (TestItem t: list)
			if (t.getCount() >= 3)
				tested ++;
		return tested;
	}
	
	public static final int getColor(Context context, int id) {
	    final int version = Build.VERSION.SDK_INT;
	    if (version >= 23) {
	        return ContextCompat.getColor(context, id);
	    } else {
	        return context.getResources().getColor(id);
	    }
	}	

	/*
	private void setText() {
		if (index >= 0){
			
			String text = mData.get(index);
			
			mText.setText(text);
			
			mTranslation.setText(Dictionary.getTranslation(text));
		}
	}
	*/

	public String getTextToTTS() {
		/*
		if (MenuData.getDirection() == 1)
			return MenuData.getText();
		else 
			return MenuData.getTranslation();
			*/
			return "";
	}	
	
	//private void setTested()
	//{
		//list.get(mIndex).
	//}
	
	public int nextIndex() {
		int size = list.size();

		mIndex = (int) (Math.random() * size);
		while (mIndex < size && list.get(mIndex).getCount() == 3)
			mIndex++;

		if (mIndex == size) {
			mIndex = 0;
			while (mIndex < size && list.get(mIndex).getCount() == 3)
				mIndex++;
		}
//Log.d("i","mi"+mIndex);
		if (mIndex <size)
			return mIndex;
		else {
			
			new AlertDialog.Builder(getActivity())
				.setIcon(R.drawable.ic_launcher)
				.setTitle(
				getResources().getString(R.string.testCompleted))
				.setMessage(
				getResources().getString(R.string.askRepeat))
				.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
										int which)
					{
						for (TestItem t: list)
							t.setCount(0);
						
						clearCounts();
						setTitle();
						nextIndex() ;
						
					}

					private void clearCounts()
					{
						// TODO: Implement this method
					}
				})
				.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
										int which)
					{
						return;
					}
				}).show();
			
			
			mIndex = -1;
			return -1;
		}
	}
	
}

