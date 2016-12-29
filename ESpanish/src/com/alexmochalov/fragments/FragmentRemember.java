package com.alexmochalov.fragments;

import android.media.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.alexmochalov.alang.R;
import com.alexmochalov.dictionary.*;
import com.alexmochalov.menu.MenuData;
import com.alexmochalov.root.*;

import java.io.*;
import android.support.v4.view.*;
import android.support.v4.app.*;
import java.util.*;

public class FragmentRemember extends FragmentM implements OnClickListener {
	static final String TAG = "myLogs";
 	static final int PAGE_COUNT = 10;
 
  ViewPager pager;
  
	private Button buttonTest;
	private ImageButton buttonRec;
	private ImageButton buttonPlay;

	private String text;

	private PagerAdapterRemember adapter;
	
	private enum States {
		play, record, stop
		};

	private States state = States.stop;

	private MediaRecorder mRecorder = null;
	private MediaPlayer mPlayer = null;

	private boolean next() {
		// Р—Р°РїРѕР»РЅСЏРµРј Р·Р°РіРѕР»РѕРІРѕРє
		mTextViewText = (TextView) rootView.findViewById(R.id.text);
		mTranslation = (TextView) rootView.findViewById(R.id.translation);

		//MenuData.setText(mTextViewText, mTranslation);

		// setVerb(MenuData.getText());

		return true;
	}

	public static String firstLetterToUpperCase(String t) {
		Log.d("my", "translation " + t);
		return t.substring(0, 1).toUpperCase() + t.substring(1);
	}

	/*
	 * private void setVerb(String text) {
	 * 
	 * String s = ""; for (PronounEdited p: objects){ s = s +
	 * p.mPronoun.getTranslation() + ". ";
	 * //((TextView)p.mLayout.findViewById(R.
	 * id.translation)).setText(p.mPronoun.getTranslation()); }
	 * ((TextView)p.mLayout.findViewById(R.id.text)).setText(s);
	 * 
	 * 
	 * }
	 */

	@Override
	public void onStart() {
		// mText = MenuData.getText(MainActivity.mGroupPosition,
		// MainActivity.mChildPosition, mIndex);
		// setVerb(MenuData.getText());

		super.onStart();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	} 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_remember, container, false);
		pager = (ViewPager)rootView.findViewById(R.id.pager);
		adapter = new PagerAdapterRemember(getActivity());
		adapter.listener = new PagerAdapterRemember.OnEventListener() {
			@Override
			public void onButtonChangeClick() {
				int position = pager.getCurrentItem();
		        pager.setAdapter(adapter);
		        pager.setCurrentItem(position);
			}
		};
		
        pager.setAdapter(adapter);
        
		return rootView;
	}

	public String getTextToTTS() {

		return adapter.getText(pager.getCurrentItem());
	}

	@Override
	public void onClick(View v) {
		if (v == buttonTest) {
			// Button Next is pressed
			if (buttonTest.getText().equals(
					mContext.getResources().getString(R.string.button_next))) {
				if (MenuData.next() == -1) {
					getActivity().getFragmentManager().beginTransaction()
						.remove(thisFragment).commit();
					;
				} else {
					next();
					buttonTest.setText(mContext.getResources().getString(
										   R.string.button_test));
				}
			} else {
				// Button РџСЂРѕРІРµСЂРёС‚СЊ is pressed
				buttonTest.setText(MainActivity.mContext.getResources()
								   .getString(R.string.button_next));
				boolean allChecked = true;
				if (allChecked) {

					setTested(3);

				}

			}
		} else if (v == buttonRec) {
			if (state == States.stop) {
				mRecorder = new MediaRecorder();
				mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

				mRecorder.setOutputFile(Utils.getRecFolder() + "/rec"
										+ MenuData.getPositionsStr());

				mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

				try {
					mRecorder.prepare();
				} catch (IOException e) {
					Log.e("", "prepare() failed");
				}

				mRecorder.start();
				buttonRec.setImageResource(R.drawable.stop);
				state = States.record;

			} else {
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;

				buttonRec.setImageResource(R.drawable.mic);
				state = States.stop;
			}
		} else if (v == buttonPlay) {
			if (state == States.stop) {
				mPlayer = new MediaPlayer();
				mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						public void onCompletion(MediaPlayer mp) {
							buttonPlay.setImageResource(R.drawable.play);
							state = States.stop;
						}
					});

				try {
					mPlayer.setDataSource(Utils.getRecFolder() + "/rec"
										  + MenuData.getPositionsStr());
					mPlayer.prepare();
					mPlayer.start();
				} catch (IOException e) {
					Log.e("", "prepare() failed");
				}

				buttonPlay.setImageResource(R.drawable.stop);
				state = States.stop;

			} else {
				mPlayer.release();
				mPlayer = null;

				buttonPlay.setImageResource(R.drawable.play);
				state = States.stop;
			}
		}
	}
	
}
