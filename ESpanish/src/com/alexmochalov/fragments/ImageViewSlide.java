package com.alexmochalov.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class ImageViewSlide extends ImageView{
	private String text1 = "text 1";
	private String text2 = "text  2";

	private Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintBg = new Paint();
	
	private int height;
	private int width;

	private int topText;
	private int topText1;
	private int leftText1;
	
	private int topText2;
	private int leftText2;
	
	private int textSize = 42;
	
	private int y0 = 0;
	private int delta = 0;
	
	private Handler h = new Handler();
	
	public OnEventListener listener;

	private boolean mIsrus;

	public interface OnEventListener{
		public void onSlided(boolean isRus);
	}
	
	public ImageViewSlide(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}
	
	public ImageViewSlide(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}
	
	public ImageViewSlide(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		paintText.setTextSize(textSize);
		paintBg.setColor(Color.YELLOW);

		/*
		ViewTreeObserver vto = getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
		    public boolean onPreDraw() {
		        getViewTreeObserver().removeOnPreDrawListener(this);

				getLayoutParams().height = textSize * 3;
		        height = textSize * 3;
		        width = getMeasuredWidth();
		        setTextPositions();
		        
		        return true;
		    }
		});
		*/		
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

	    width = widthSize;
	    height = textSize * 3;

	    setMeasuredDimension(width, height);
	     
	    setTextPositions();
	     
	 }
	
	protected void setTextPositions() {
		Rect bounds = new Rect();
		
		paintText.getTextBounds(text1, 0, text1.length(), bounds);				
		leftText1 = (width - bounds.width()) / 2;
		topText = (int) ((height / 2) - ((paintText.descent() + paintText.ascent()) / 2)) ;
		topText1 = topText;
		
		paintText.getTextBounds(text2, 0, text2.length(), bounds);				
		leftText2 = (width - bounds.width()) / 2;
		topText2 =   (int)((-height + height / 2) + ((paintText.descent() + paintText.ascent()) / 2)) ; 
		//Log.d("z","set text izrus "+mIsrus);
		if (mIsrus){
			topText2 = topText;
			topText1 = topText * 3;
		}
			
	}

	@Override
	public void draw(Canvas canvas){
		
		/*
		Shader shader = new LinearGradient(0, 0, 0, 40, Color.WHITE, Color.BLACK, TileMode.CLAMP);
		Paint paint = new Paint(); 
		paint.setShader(shader); 
		canvas.drawRect(new RectF(0, 0, width, height), paint); */		
		
		canvas.drawColor(Color.YELLOW);
		canvas.drawText(text1, leftText1, topText1, paintText);
		canvas.drawText(text2, leftText2, topText2, paintText);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		int y = (int)event.getY();
		
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			y0 = y;
			h.removeCallbacks(showInfo);
        	invalidate();
        	return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
        	delta = (y - y0) * 2;
        	
        	//Log.d("z", "topText "+topText+" topText2 "+topText2+" delta "+delta+" HEIGHT "+height);
        	
        	if (topText1 + delta >= topText && topText2 + delta <= topText){
        		topText1 = topText1 + delta;
        		topText2 = topText2 + delta;
        	} 
        	
			y0 = y;
            invalidate();
        	return true;
        } else if (action == MotionEvent.ACTION_UP) {
        	slide();
        	invalidate();
        	return true;
        }
		
		return false;
	}
	
	private void slide(){
    	if (topText1 + delta >= topText && topText2 + delta <= topText){
    		topText1 = topText1 + delta;
    		topText2 = topText2 + delta;
    		
    		h.postDelayed(showInfo, 10);
    	} else {   
			h.removeCallbacks(showInfo);
			if (delta > 0){
				topText2 = topText;
	    		topText1 = topText * 3;
			} else {
	    		topText1 = topText;
	    		topText2 = -topText;
			}
		if (listener != null)
				listener.onSlided(topText2 == topText);


    	}	
        invalidate();
	}
	
	Runnable showInfo = new Runnable() {
		public void run() {
			slide();
		}
	};

	public void setTexts(String text, String rus, boolean isRus) {
		text1 = text;
		text2 = rus;
		
		mIsrus = isRus;
        setTextPositions();
    	invalidate();
	}	
}
