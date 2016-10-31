package com.alexmochalov.espanish;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.alex_mochalov.navdraw.*;

public class DialogScheme extends Dialog
{
	Activity mContext;
	
	public DialogScheme(Activity a){
		super(a);
		mContext = a;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_scheme);
		
		ViewGroup mLinearLayout = (ViewGroup)findViewById(R.id.fc_linearLayout);
		
		for (Scheme s: MenuData.schemes){
			View layout2 = LayoutInflater.from(mContext).inflate(R.layout.dialog_scheme_item, mLinearLayout, false);
			mLinearLayout.addView(layout2);
		}
		
		//yes = (Button) findViewById(R.id.btn_yes);
		//no = (Button) findViewById(R.id.btn_no);
		//yes.setOnClickListener(this);
		//no.setOnClickListener(this);

	}
}
