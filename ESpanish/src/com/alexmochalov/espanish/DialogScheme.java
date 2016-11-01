package com.alexmochalov.espanish;
import android.app.*;
import android.os.*;
import android.text.Html;
import android.view.*;
import android.widget.*;

import com.alex_mochalov.navdraw.*;
import android.util.*;

public class DialogScheme extends Dialog implements android.view.View.OnClickListener
{
	Activity mContext;
	Button btnNext;
	
	static int mIndex = 0;
	
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
		
		ImageButton b = (ImageButton)findViewById(R.id.dialogSchemeImageButtonSpeak);
		b.setOnClickListener(this);
		
		btnNext = (Button)findViewById(R.id.dialogSchemeImageButtonNext);
		btnNext.setOnClickListener(new android.view.View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					mIndex++;
					if (mIndex >= MenuData.schemes.size())
						mIndex = 0;
					reset();
				}
			});
	}

	@Override
	public void onClick(View v) {
		if (v == btnNext){
			
			
			
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
		textView.setText(Html.fromHtml(s.text));

		for (String str: s.strings){
			View layout2 = LayoutInflater.from(mContext).inflate(R.layout.dialog_scheme_item, mLinearLayout, false);
			mLinearLayout.addView(layout2);

			String str1[] = str.split("-");

			Log.d("","str1 "+str1[0].trim());
			textView = (TextView)layout2.findViewById(R.id.dialogschemeitemTextViewString);
			textView.setText(Html.fromHtml(str1[0].trim()));

			textView = (TextView)layout2.findViewById(R.id.dialogschemeitemTextViewTranslation);
			textView.setText(Html.fromHtml(str1[1].trim()));

		}
		
	}
}
