package com.alexmochalov.espanish;
import android.app.*;
import android.os.*;
import android.text.Html;
import android.view.*;
import android.widget.*;

import com.alex_mochalov.navdraw.*;
import com.alexmochalov.espanish.fragments.FragmentM.OnTestedListener;

import android.util.*;

public class DialogScheme extends Dialog implements android.view.View.OnClickListener
{
	Activity mContext;
	ImageButton btnSpeak;
	ImageButton btnNext;
	ImageButton btnPrev;
	
	String text = "";
	
	static int mIndex = 0;
	
	public OnDialogSchemeButtonListener mCallback;
	
	public interface OnDialogSchemeButtonListener {
		public void onSpeakButtonPressed(String text);
	}
	
	public DialogScheme(Activity a, int index){
		super(a);
		mContext = a;
		mIndex = index;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_scheme);
		
		
		
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
               WindowManager.LayoutParams.MATCH_PARENT);
		reset();
		
		btnSpeak = (ImageButton)findViewById(R.id.dialogSchemeImageButtonSpeak);
		btnSpeak.setOnClickListener(this);
		
		btnNext = (ImageButton)findViewById(R.id.dialogSchemeImageButtonNext);
		btnNext.setOnClickListener(this);
		
		btnPrev = (ImageButton)findViewById(R.id.dialogSchemeImageButtonPrev);
		btnPrev.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		if (v == btnNext){
			mIndex++;
			if (mIndex >= MenuData.schemes.size())
				mIndex = 0;
			reset();
		}
		else if (v == btnPrev){
			mIndex--;
			if (mIndex < 0)
				mIndex = MenuData.schemes.size()-1;
			reset();
		}
		else if (v == btnSpeak){
			if (mCallback != null)
				mCallback.onSpeakButtonPressed(text);
		}
	}

	private void reset()
	{
		Scheme s = MenuData.schemes.get(mIndex);
		this.setTitle(s.title);
		
		ViewGroup mLinearLayout = (ViewGroup)findViewById(R.id.fc_linearLayout);
		//mLinearLayout.removeAllViewsInLayout();
		mLinearLayout.removeAllViews();
		
		TextView textView = (TextView)findViewById(R.id.dialogSchemeTextViewWord);
		if (s.text.length() > 0){
			textView.setText(Html.fromHtml(s.text));
			textView.setVisibility(View.VISIBLE);
		} else {
			//ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
		    //        .findViewById(android.R.id.content)).getChildAt(0);
			//viewGroup.removeView(textView);
			textView.setVisibility(View.GONE);
		}

		text = "";
		for (String str: s.strings){
			View layout2 = LayoutInflater.from(mContext).inflate(R.layout.dialog_scheme_item, mLinearLayout, false);
			mLinearLayout.addView(layout2);

			String str1[] = str.split("-");

			textView = (TextView)layout2.findViewById(R.id.dialogschemeitemTextViewString);
			textView.setText(Html.fromHtml(str1[0].trim()));
			
			text = text + str1[0].trim() + ". ";

			textView = (TextView)layout2.findViewById(R.id.dialogschemeitemTextViewTranslation);
			textView.setText(Html.fromHtml(str1[1].trim()));

		}
		
	}
}
