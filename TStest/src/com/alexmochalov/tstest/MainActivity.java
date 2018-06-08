package com.alexmochalov.tstest;

import java.io.File;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    
	/** How often to repaint the contents of the window (in ms). */
    static private final int REPAINT_DELAY = 10;
	/** Used as a pulse to gradually fade the contents of the window. */
    static private final int REPAINT_MSG = 1;

    private IView viewCanvas;

	private Brush mBrush;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();

//		// The ViewCanvas doesnt have its own method onResume
//		viewCanvas.onResume(PreferenceManager.getDefaultSharedPreferences(this));
		
		mBrush = new Brush(this);
		//Var.brushRadius = prefs.getInt(PREFS_BRUSH_RADIUS, 30);
		mBrush.setRadius(30, true);
		
		viewCanvas = (IView)findViewById(R.id.imageView1);
		viewCanvas.setBrush(mBrush);
		
	    SharedPreferences prefs;
		prefs =  PreferenceManager.getDefaultSharedPreferences(this);
		int idBG = prefs.getInt("PREFS_BG", R.drawable.canvas0);
		
		viewCanvas.setBG(idBG);
		
		
		startRepainting();
	}

    void startRepainting() {
        mHandler.removeMessages(REPAINT_MSG);
        mHandler.sendMessageDelayed(
			mHandler.obtainMessage(REPAINT_MSG), REPAINT_DELAY);
    }

    /**
     * Stop the pulse to repaint the screen.
     */
    void stopRepainting() {
        mHandler.removeMessages(REPAINT_MSG);
    }
	
	Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case REPAINT_MSG: {
                		viewCanvas.paint();						
						mHandler.sendMessageDelayed(
                            mHandler.obtainMessage(REPAINT_MSG), REPAINT_DELAY);
						break;
					}
                default:
                    super.handleMessage(msg);
            }
        }
    };

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
    		viewCanvas.clear();						
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
