package com.alexmochalov.fragments;

import android.media.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.alexmochalov.alang.*;
import com.alexmochalov.menu.MenuData;
import com.alexmochalov.main.*;
import com.alexmochalov.rules.*;

import java.io.*;

public class FragmentSpeak extends FragmentM implements OnClickListener {
	private Button buttonTest;
	private ImageButton buttonRec;
	private ImageButton buttonPlay;

	private String text;

	private enum States {
		play, record, stop
	};

	private States state = States.stop;

	private MediaRecorder mRecorder = null;
	private MediaPlayer mPlayer = null;

	private boolean next() {
		// Заполняем заголовок
		mTextViewText = (TextView) rootView.findViewById(R.id.text);
		mTranslation = (TextView) rootView.findViewById(R.id.translation);

		MenuData.setText(mTextViewText, mTranslation);

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

		rootView = inflater.inflate(R.layout.fragment_speak, container, false);
		MenuData.nextTestIndex();

		init();

		next();

		ViewGroup mLinearLayout = (ViewGroup) rootView
				.findViewById(R.id.fc_linearLayout);

		String s = "";
		text = "";

		int i = 0;
		for (Pronoun p : Rules.getPronouns()) {
			Entry e = Rules.translate(MenuData.getText());
			String verb = Rules.fit(e, i, "present").trim();

			
		View layout2 = LayoutInflater.from(mContext).inflate(
				R.layout.fragment_speak_item, mLinearLayout, false);
		mLinearLayout.addView(layout2);
		
		TextView textViewSpeak = (TextView) layout2
				.findViewById(R.id.fragmentSpeakItemtextViewText);
		textViewSpeak.setText(p.getTranslation(true) + " " + verb + ". ");
			
			text = text + p.getText() + " "
					+ Rules.conj(i, MenuData.getText(), false) + ". ";
			i++;
		}


		buttonTest = (Button) rootView.findViewById(R.id.button_test);
		buttonTest.setOnClickListener(this);

		buttonRec = (ImageButton) rootView.findViewById(R.id.button_rec);
		buttonRec.setOnClickListener(this);

		buttonPlay = (ImageButton) rootView.findViewById(R.id.button_play);
		buttonPlay.setOnClickListener(this);

		return rootView;
	}

	public String getTextToTTS() {
		Log.d("", "--" + text);
		return text;
	}

	@Override
	public void onClick(View v) {
		if (v == buttonTest) {
			// Button Next is pressed
			if (buttonTest.getText().equals(
					mContext.getResources().getString(R.string.button_next))) {
				if (MenuData.nextIndex() == -1) {
					getActivity().getFragmentManager().beginTransaction()
							.remove(thisFragment).commit();
					;
				} else {
					next();
					buttonTest.setText(mContext.getResources().getString(
							R.string.button_test));
				}
			} else {
				// Button Проверить is pressed
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
