package com.alexmochalov.espanish;
import android.app.*;
import android.os.*;
import android.text.Html;
import android.view.*;
import android.widget.*;

import com.alex_mochalov.navdraw.*;

public class DialogScheme extends Dialog implements android.view.View.OnClickListener
{
	Activity mContext;
	
	public DialogScheme(Activity a){
		super(a);
		mContext = a;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_scheme);
		
		ViewGroup mLinearLayout = (ViewGroup)findViewById(R.id.fc_linearLayout);
		
		//getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
         //       WindowManager.LayoutParams.MATCH_PARENT);
		
		Scheme s = MenuData.schemes.get(0);
		this.setTitle(s.title);
		
		TextView textView = (TextView)findViewById(R.id.dialogSchemeTextViewWord);
		textView.setText(Html.fromHtml(s.text));
		
		for (String str: s.strings){
			View layout2 = LayoutInflater.from(mContext).inflate(R.layout.dialog_scheme_item, mLinearLayout, false);
			mLinearLayout.addView(layout2);
			
			String str1[] = str.split("-");
			
			textView = (TextView)layout2.findViewById(R.id.dialogschemeitemTextViewString);
			textView.setText(Html.fromHtml(str1[0].trim()));
			
			textView = (TextView)layout2.findViewById(R.id.dialogschemeitemTextViewTranslation);
			textView.setText(Html.fromHtml(str1[1].trim()));
			
		}
		
		ImageButton b = (ImageButton)findViewById(R.id.dialogSchemeImageButtonSpeak);
		b.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
	}
}
