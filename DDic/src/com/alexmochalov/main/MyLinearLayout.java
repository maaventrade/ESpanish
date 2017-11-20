package com.alexmochalov.main;

import com.alexmochalov.dic.ArrayAdapterDictionary.AdapterCallback;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout{

	private Context mContext;
	
	public LayoutCallback callback = null;
	public interface LayoutCallback {
		void move(); 
		void down(); 
	} 
	
	public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
	}

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public MyLinearLayout(Context context) {
		super(context);
		mContext = context;
	}
	
	 //@Override
     //public boolean onInterceptTouchEvent(MotionEvent ev) {
     //  return true; // With this i tell my layout to consume all the touch events from its childs
    //}
	 
	 @Override
	    public boolean onTouchEvent(MotionEvent event) {
		 	float x = event.getX();
		 	float y = event.getY();
		 
	        switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	        	if (callback != null)
	        		callback.down();
	            break;
	        case MotionEvent.ACTION_MOVE:
	        	if (callback != null)
	        		callback.move();
	            break;
	        case MotionEvent.ACTION_UP:
	            break;
	        }
	        return true;
	    }
	
}
