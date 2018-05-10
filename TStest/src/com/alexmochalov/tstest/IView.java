package com.alexmochalov.tstest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class IView extends ImageView {

	private TextView mTv;
	private Canvas canvas;

	public IView(Context context, AttributeSet attrs, int defStyleAttr
			) {
		super(context, attrs, defStyleAttr);
	}

	public IView(Context context, AttributeSet attrs
			) {
		super(context, attrs);
	}

	@SuppressLint("NewApi")
	public IView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        
        if (this.getHeight() > 0){
        	
        	Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);    
            canvas = new Canvas(bitmap);
            
            setImageBitmap(bitmap);
        }
    }
	
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
  
    	Paint paint = new Paint();
    	paint.setColor(Color.BLACK);
        
//    	canvas.drawCircle(0, 0, event.getSize() * 500, paint);
    	canvas.drawCircle(event.getX(), event.getY(), event.getSize() * 1000, paint);
    	
    	this.invalidate();
    	
    	
    	mTv.setText(
				""+(event.getSize()* 500)+
				"  "+event.getX()+
				"  "+event.getY()
				);

    	return true;
    	/*
        float x = event.getX() - Var.brushWidth;
        float y = event.getY() - Var.brushWidth;

        switch (event.getAction()) {
        	case MotionEvent.ACTION_DOWN:
        		brush.addColor(pixel);
        		return true;
        	case MotionEvent.ACTION_MOVE:
        		brush.addColor(pixel);
        		return true;
            default:
            	return true;
        }    */	
    }

	public void setT(TextView tv) {
		mTv = tv;
		
	}
	

}
