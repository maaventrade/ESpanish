package com.alexmochalov.main;

import java.io.File;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.*;

import com.alexmochalov.alang.R;
import com.alexmochalov.dialogs.DialogRecord;
import com.alexmochalov.menu.MenuData;
import com.alexmochalov.rules.Pronoun;
import com.alexmochalov.rules.Rules;

public class Media {

	private static DialogRecord dialog;	
	
	private static MediaRecorder mediaRecorder;
	private static MediaPlayer mediaPlayer;
	
	private static boolean isRecording = false;
	private static boolean isPlaing = false;
	
	private static CountDownTimer timer;

	private static boolean recordAll = false;
	
	public interface OnMediaEventListener {
		public void plaingCompleted();
		public void recordingFinished(boolean isRecording);
	}

	public static OnMediaEventListener listener;

	public static boolean startRecording(Context mContext, String verb, final DialogRecord dialog) {

		try {
			
			releaseRecorder();
			
			String fileName = Utils.APP_FOLDER + "/rec/" + verb + ".3gpp";
			
			File outFile = new File(fileName);
			if (outFile.exists()) {
				outFile.delete();
			}

			mediaRecorder = new MediaRecorder();
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mediaRecorder.setOutputFile(fileName);
			mediaRecorder.prepare();
			mediaRecorder.start();
			
			timer =	new CountDownTimer(5000, 100) {

				    public void onTick(long millisUntilFinished) {
				    	
				    	dialog.setProgress(5000 - (int)millisUntilFinished);
				    	
				    }

				    public void onFinish() {
				    	
				    	if (listener != null){

				    		listener.recordingFinished(isRecording);
				    		
				    	}
				    }

				}.start();
				
	
			isRecording = true;
	Log.d("a","is rec 1 "+isRecording);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public static void releaseRecorder() {
		
	    if (mediaRecorder != null) {
	      mediaRecorder.release();
	      mediaRecorder = null;
	    }
	    
	  }

	public static boolean isRecording() {
		return isRecording;
	}

	public static void stopRecording() {

		if (mediaRecorder != null && isRecording)
			try {
				mediaRecorder.stop();
			} catch (RuntimeException e) {
			} finally {
			}

		if (timer != null) {
			timer.cancel();
		}

		isRecording = false;
	}

	public static void stopPlaing() {
		if (mediaPlayer  != null && isPlaing) 
			mediaPlayer.stop();
		    
		isPlaing = false;
	}

	public static boolean isPlaing() {
		return isPlaing;
	}

	public static boolean startPlaying(String verb) {
		try {
		      
			releasePlayer();
			String fileName = Utils.APP_FOLDER + "/rec/" + verb + ".3gpp";
		      
		    mediaPlayer = new MediaPlayer();
		    mediaPlayer.setDataSource(fileName);
		    mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

	            @Override
	            public void onCompletion(MediaPlayer mp) {

	        		isPlaing = false;
	            	if (listener != null)
	            		listener.plaingCompleted();
	            }

	          });

		      mediaPlayer.prepare();
		      mediaPlayer.start();
		      isPlaing = true;
		      
		      return true;
		    } catch (Exception e) {
		      e.printStackTrace();
		      return false;
		    }
	}

	public static void releasePlayer() {

		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		    }
		
	}

	public static void setButtons(View rootView, final Context mContext, final ArrayList<Pronoun> objects, final String verb) {
		
    	final ImageButton ibRecord = (ImageButton) rootView.findViewById(R.id.ibRecord);
    	final ImageButton ibPlay = (ImageButton) rootView.findViewById(R.id.ibPlay);
    	
    	Media.stopRecording();
    	Media.stopPlaing();
    	Media.listener = new OnMediaEventListener(){

			@Override
			public void recordingFinished(boolean isRecording)
			{ // recordAll &&
				
					if (dialog != null){
						dialog.dismiss();
						dialog = null;
					}
			}

			@Override
			public void plaingCompleted() {
				ibPlay.setImageDrawable(
						ContextCompat.getDrawable(mContext, R.drawable.play));
			}};
    	
		ibRecord.setImageDrawable(
				ContextCompat.getDrawable(mContext, R.drawable.record));
    	
    	ibRecord.setOnClickListener(new OnClickListener() {
    		
			@Override
			public void onClick(View v) {
				
				if (objects != null)
					record(mContext, objects, objects.get(0), verb);
				else {
					String text;
					String textRus;
					
					if (MenuData.getDirection() == 1){
						text = MenuData.getText();
						textRus = MenuData.getTranslation();
					}	
					else{ 
						text = MenuData.getTranslation();
						textRus = MenuData.getText();
					}	
					
					startRecording(mContext, textRus, text);
				}	
				
			}
			
		});


    	ibPlay.setImageDrawable(
				ContextCompat.getDrawable(mContext, R.drawable.play));
    	
    	ibPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (Media.isPlaing()){
					Media.stopPlaing();
				}
				else {
					String text;
					if (MenuData.getDirection() == 1)
						text = MenuData.getText();
					else 
						text = MenuData.getTranslation();

					Media.startPlaying(text);
				}
				
			}
		});
	}
	
	public static boolean record(Context mContext, ArrayList<Pronoun> objects, Pronoun p, String verb)
	{

		recordAll = true;
		// setSelected(objects.indexOf(p));
		
		Media.stopRecording();
    	Media.stopPlaing();
		
    	
		String verbRus = Rules.fit(Rules.translate(verb),  
				Rules.indexOf(p) , "present").trim();
	
		String textRus =  p.getTranslation().toUpperCase()
			+" "
			+verbRus.toUpperCase();
    	
		startRecording(mContext, textRus, p.getText()+"_"+verb);
		
		return true;
	}
	
	private static void startRecording(Context mContext, String textRus, String fileName)
	{
		
		if (Media.isRecording()){
			Media.stopRecording();
		}
		else {
			
			/* ;
			if (MenuData.getDirection() == 1)
				textRus = MenuData.getText();
			else 
				textRus = MenuData.getTranslation();
			*/
		
			if (dialog == null){
				dialog = new DialogRecord(mContext, textRus, Utils.recDuration);
				dialog.listener = new DialogRecord.OnDialogEventListener(){

					@Override
					public void stopRecording() {
						Media.stopRecording();
						
						if (dialog != null)
							dialog = null;

					}};
					
				dialog.show();
			} else	
				dialog.setText(textRus);

			Media.startRecording(mContext, fileName, dialog);
		}

	}

}
