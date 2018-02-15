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
import com.alexmochalov.tree.LineItem;
import com.alexmochalov.tree.Tree;

public class FragmentTest extends Fragment
 {

	private Context mContext;
	
	private Button button_test;
	
	private TextView tvText;
	private TextView tvTranslation;
	
	private View rootView;
	
	private ArrayList<LineItem> list;
	
	private int index = 0;

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
		double random = Math.random();
		int direction;
		
		if (random > 0.5f)
			direction =  2;
		else
			direction =  1;		
		
		String text = "";
		String translation = "";
		
		LineItem l = list.get(index);
		String name;

		if (direction == 1)
			name = l.getTranslation();
		else
			name = l.getName1();

		//IndexEntry e = com.alexmochalov.dic.Dictionary.find(name);
		
		tvText.setText(Utils.firstLetterToUpperCase(name));
		//tvTranslation.setText(Utils.firstLetterToUpperCase(translation));
		
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
		mContext = this.getActivity();

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
        
		list = new ArrayList(Tree.getItems(selectedGroupIndex));
        
        init();
		//MenuData.nextIndex();
    	next();
    	
	    button_test = (Button)rootView.findViewById(R.id.button_test);
	    button_test.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				// Button Next is pressed
				if (button_test.getText().equals(mContext.getResources().getString(R.string.button_next))){
			    /*
					if (MenuData.nextIndex() == -1){
						getActivity().getFragmentManager().beginTransaction().remove(thisFragment).commit();
					} else {
						next();
						
						EditText editText = (EditText)rootView.findViewById(R.id.editText_phrase_transl);
						editText.setText("");
						
						button_test.setText(mContext.getResources().getString(R.string.button_test));
					}
					*/
				}	
				else {
					// Button Проверить is pressed
					button_test.setText(mContext.getResources().getString(R.string.button_next));
					EditText editText = (EditText)rootView.findViewById(R.id.editText_phrase_transl);

					TextView mTranslation = (TextView)rootView.findViewById(R.id.TextViewPhraseTranslation);
					
					boolean result = false;//Rules.testRus(editText.getText().toString(), mTranslation.getText().toString(),MenuData.getDirection());
					
					//result = true;
					mTranslation.setVisibility(View.VISIBLE);
					
					if (result){
						//mTranslation.setTextColor(getColor(mContext, R.color.green1));
						
						//setTested(MenuData.getDirection());
						
					}
					else 
						mTranslation.setTextColor(Color.RED);
				}	
				
			}});
        
        return rootView;
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
	
}

