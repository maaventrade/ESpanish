package com.alexmochalov.main;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.*;

import com.alexmochalov.dialogs.DialogRecord;

public class Media {

	private static MediaRecorder mediaRecorder;
	private static MediaPlayer mediaPlayer;
	
	private static boolean isRecording = false;
	private static boolean isPlaing = false;
	
	private static CountDownTimer timer;
	
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
}
