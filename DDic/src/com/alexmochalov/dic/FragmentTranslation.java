package com.alexmochalov.dic;

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.text.method.ScrollingMovementMethod;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.alexmochalov.ddic.*;
import com.alexmochalov.main.*;

public class FragmentTranslation extends Fragment   implements OnClickListener{

	private Activity mContext;

	private TextView tvWord;
	private TextView tvPhonetic;
	private TextView tvTranslation;

	private ImageButton ibSpeak;
	private ImageButton ibMenu;
	
	public FragmentTranslationCallback callback = null;
	public interface FragmentTranslationCallback {
		void btnForwardClicked(); 
		void btnSelectDictionary(String name);
		void btnReindex();
	} 
	
	
	public FragmentTranslation(Activity context)
	{
		super();
		mContext = context;
	}

	public FragmentTranslation()
	{
		super();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_translation, container, false);
        
		ibSpeak = (ImageButton)rootView.findViewById(R.id.ibSpeak);
		ibSpeak.setOnClickListener(this);
		
		tvWord = (TextView)rootView.findViewById(R.id.tvWord);
		tvTranslation = (TextView)rootView.findViewById(R.id.tvTranslation);
		tvTranslation.setMovementMethod(new ScrollingMovementMethod());
		
		tvPhonetic = (TextView)rootView.findViewById(R.id.tvPhonetic);
        
        return rootView;
    }

	public void setTranslation(IndexEntry indexEntry) {
		
		Entry entry = new Entry();
		if (indexEntry != null){
			String str = Dictionary
				.readTranslation(indexEntry);
			entry.setTranslationAndPhonetic(str,
											indexEntry.getText());
			tvWord.setText(indexEntry.getText());

			tvTranslation.setText(Html.fromHtml(entry
												.getTranslation().toString()));			
		
			String phonetic = entry.getPhonetic();
			if (phonetic.length() > 0)
				tvPhonetic
					.setText("[" + phonetic + "]");
			else
				tvPhonetic.setText("");						
		} else {
			entry.setTranslationAndPhonetic("",
										"");
			tvWord.setText("");

			tvTranslation.setText("");
			
			tvPhonetic.setText("");		
		}
		
		tvTranslation.scrollTo(0, 0);

		
	}
	
	@Override
	public void onClick(View v) {
		if (v == ibSpeak){
			TtsUtils.speak(tvWord.getText().toString());
		}
	}
	
}
