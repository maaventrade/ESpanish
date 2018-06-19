package com.alexmochalov.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexmochalov.alang.R;
import com.alexmochalov.main.Media.OnMediaEventListener;

public class DialogRecord extends Dialog implements
		android.view.View.OnClickListener {

	private Context mContext;
	private DialogRecord dialog;

	private ProgressBar progressBar;
	private Button btnStop;
	private int max;
	private String textRus;
	
	public interface OnDialogEventListener {
		public void stopRecording();
	}

	public static OnDialogEventListener listener;



	public DialogRecord(Context a, String pTextRus, int pMax) {
		
		super(a);
		
		setCanceledOnTouchOutside(false);
		dialog = this;
		max = pMax;
		
		textRus = pTextRus;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_record);

		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setMax(max);
		
		TextView text = (TextView) findViewById(R.id.text);
		text.setText(textRus);
		
		btnStop = (Button) findViewById(R.id.btnStop);
		btnStop.setOnClickListener(this);

	}
// android:keepScreenOn="true"
	public void onClick(View v) {

		if (v == btnStop) {
			if (listener != null)
				listener.stopRecording();
			
			dialog.dismiss();

		}
	}

	public void setProgress(int ms) {

		progressBar.setProgress(ms);
		
	}

	public void setText(String pTextRus) {
		
		TextView text = (TextView) findViewById(R.id.text);
		text.setText(pTextRus);
		
	}
}
