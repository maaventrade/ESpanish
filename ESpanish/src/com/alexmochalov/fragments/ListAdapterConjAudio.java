package com.alexmochalov.fragments;

import android.app.AlertDialog;
import android.content.*;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.*;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.alexmochalov.alang.*;
import com.alexmochalov.dialogs.DialogRecord;
import com.alexmochalov.main.Media;
import com.alexmochalov.main.Media.OnMediaEventListener;
import com.alexmochalov.main.TtsUtils;
import com.alexmochalov.main.Utils;
import com.alexmochalov.menu.MenuData;
import com.alexmochalov.menu.MenuData.*;
import com.alexmochalov.rules.Entry;
import com.alexmochalov.rules.Pronoun;
import com.alexmochalov.rules.Rules;

import java.util.*;

public class ListAdapterConjAudio extends BaseAdapter {

	//private ArrayList<MenuData> mGroups;
	private Context mContext;
	private LayoutInflater lInflater;
	private ArrayList<Pronoun> objects;
	private Pronoun selected = null;
	private String verb;
	
	private boolean recordAll = false;
	
	private String textRus;

	private DialogRecord dialog;
	
	public ListAdapterConjAudio(Context context, ArrayList<Pronoun> pObjects, String pVerb) {
		mContext = context;
		objects = pObjects;
		verb = pVerb;
		
		selected = objects.get(0); 
		
		lInflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public boolean record(Pronoun p, final ImageButton ibRecord)
	{

		recordAll = true;
		setSelected(objects.indexOf(p));
		
		Media.stopRecording();
    	Media.stopPlaing();
		
		startRecording(p);
		
		return true;
	}

	private void startRecording(Pronoun p)
	{
		
		if (Media.isRecording()){
			Media.stopRecording();
		}
		else {
			String verbRus = Rules.fit(Rules.translate(verb),  
					Rules.indexOf(p) , "present").trim();
		
			textRus =  p.getTranslation().toUpperCase()
				+" "
				+verbRus.toUpperCase();
		
			if (dialog == null){
				dialog = new DialogRecord(mContext, textRus, 5000);
				dialog.listener = new DialogRecord.OnDialogEventListener(){

					@Override
					public void stopRecording() {
						recordAll = false;
						Media.stopRecording();
						
						if (dialog != null)
							dialog = null;

					}};
					
				dialog.show();
			} else	
				dialog.setText(textRus);

			Media.startRecording(mContext, p.getText()+"_"+verb, dialog);
		}

	}
	
	public Pronoun getSelectedObject()
	{
		return selected;
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
	    if (view == null) {
	    	//if (useTimer)
	  	    //  view = lInflater.inflate(R.layout.fragment_conj_item_audio1, parent, false);
	    	//else
		      view = lInflater.inflate(R.layout.fragment_conj_item_audio, parent, false);
	    }
	 
	    Pronoun p = (Pronoun)getItem(position);
		Entry e = Rules.translate(verb);
		String verbRus = Rules.fit(e,  Rules.indexOf(p) , "present").trim();
		textRus =  p.getTranslation().toUpperCase()
				+" "
				+verbRus.toUpperCase();
		
	    ((TextView) view.findViewById(R.id.translation)).setText(textRus);
	    
	    LinearLayout layoutButtons = (LinearLayout)view.findViewById(R.id.layoutButtons); 
	    
	    if (p == selected){
	    	
	    	if (recordAll){
	    		int yy = 1;
	    	}
	    	else {
		    	layoutButtons.setVisibility(View.VISIBLE);
	    		setButtons(view, p, textRus);
	    	}
	    	
	    }
	    else {
	    	layoutButtons.setVisibility(View.INVISIBLE);
	    }	
	 
	    return view;
	    }

	private void setButtons(View view, final Pronoun p, final String textRus) {
		
    	ImageButton ibSpeak = (ImageButton) view.findViewById(R.id.ibSpeak);
    	ibSpeak.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String s = Utils.setPronounsToVowel(p.getText().toUpperCase(), MenuData.getText()) 
				+ " "
				+ p.conj(MenuData.getText(), false).toUpperCase();
				
				AlertDialog.Builder builder;
			    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			        builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
			    } else {
			        builder = new AlertDialog.Builder(mContext);
			    }
			    
			    s = s.replace("' ", "'");
			    
			    TtsUtils.speak(s,
			    
			    builder.setMessage(s)
			    .setIcon(android.R.drawable.ic_dialog_alert)
			    .show());
			    
			}
		});
    	
    	final ImageButton ibRecord = (ImageButton) view.findViewById(R.id.ibRecord);
    	final ImageButton ibPlay = (ImageButton) view.findViewById(R.id.ibPlay);
    	
    	Media.stopRecording();
    	Media.stopPlaing();
    	Media.listener = new OnMediaEventListener(){

			@Override
			public void recordingFinished(boolean isRecording)
			{ // recordAll &&
				
				if (recordAll){
					if (objects.indexOf(selected)+1 < objects.size())
							record(objects.get(
									objects.indexOf(selected)+1),
								ibRecord);
					else {
						recordAll = false;
						if (dialog != null){
							dialog.dismiss();
							dialog = null;
						}
					}
				} else {
					if (dialog != null){
						dialog.dismiss();
						dialog = null;
					}
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
				
				startRecording(p);
				
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
				else	
					Media.startPlaying(p.getText()+"_"+verb);
				
			}
		});
    	
	}

	public void setSelected(int position) {
		
		selected = objects.get(position);
		//if (position >= 0)
		//	this.setSelected(position);
		this.notifyDataSetChanged();
		
	}

	public void setObjects(ArrayList<Pronoun> pObjects) {
		
		objects = pObjects;
		this.notifyDataSetChanged();
		
	}


}
