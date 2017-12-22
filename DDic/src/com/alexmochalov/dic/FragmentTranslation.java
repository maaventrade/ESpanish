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
		
		ibMenu = (ImageButton)rootView.findViewById(R.id.ibMenu);
		ibMenu.setOnClickListener(this);

		tvWord = (TextView)rootView.findViewById(R.id.tvWord);
		tvTranslation = (TextView)rootView.findViewById(R.id.tvTranslation);
		tvTranslation.setMovementMethod(new ScrollingMovementMethod());
		
		tvPhonetic = (TextView)rootView.findViewById(R.id.tvPhonetic);
        
        return rootView;
    }

	public void setTranslation(IndexEntry indexEntry) {
		String str = Dictionary
				.readTranslation(indexEntry);

		Entry entry = new Entry();
		entry.setTranslationAndPhonetic(str,
				indexEntry.getText());

		tvWord.setText(indexEntry.getText());

		tvTranslation.setText(Html.fromHtml(entry
				.getTranslation().toString()));
		tvTranslation.scrollTo(0, 0);

		String phonetic = entry.getPhonetic();
		if (phonetic.length() > 0)
			tvPhonetic
					.setText("[" + phonetic + "]");
		else
			tvPhonetic.setText("");
	}
	
	@Override
	public void onClick(View v) {
		if (v == ibMenu){
			showPopupMenu(v);
		} else if (v == ibSpeak){
			TtsUtils.speak(tvWord.getText().toString());
		}
	}

	private void showPopupMenu(View v) {
		PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.inflate(R.menu.popup); 

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Toast.makeText(PopupMenuDemoActivity.this,
                        // item.toString(), Toast.LENGTH_LONG).show();
                        // return true;
                        switch (item.getItemId()) {
                        case R.id.action_tree:
                			if (callback != null)
                				callback.btnForwardClicked();
                            return true;
                        case R.id.action_reindex:
            				callback.btnReindex();
                            return true;
                        case R.id.action_select_dic:
								AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
								dialog.setTitle("Select divtionaty");
								//dialog.setMessage(getResources().getString(R.string.action_move));

								final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice);
								arrayAdapter.add("en-ru");
								arrayAdapter.add("it-ru");
								arrayAdapter.add("ru-it");

								dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();
										}
									});

								dialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											String strName = arrayAdapter.getItem(which);
											if (callback != null)
												callback.btnSelectDictionary(strName);
										}
									});
								dialog.show();
							
							
                			
                            return true;
                           default:
                                return false;
                        }
                    }
                });
        popupMenu.show();	
       }
	
}
