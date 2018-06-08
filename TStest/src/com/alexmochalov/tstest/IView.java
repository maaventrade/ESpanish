package com.alexmochalov.tstest;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class IView extends View {

	private TextView mTv;
	private Canvas canvas;
	private ArrayList<Item> items = new ArrayList<Item>();

	private Bitmap mBitmap;
	private Rect mRect = new Rect();
	private Rect mRectBitmap = new Rect();
	
	private Bitmap mBitmapBg;
	private Rect mRectBG;

	private Brush mBrush;
	
	private int width = 1000;
	private int height = 1000;
	
	private float offsetX = 0;  
	private float offsetY = 0;  
	private float kZooming = 1; 
	
	private Canvas mCanvas;
	
	private int idBG;


	private byte r[][];
	private byte y[][];
	private byte b[][];
	private byte w[][];
	private byte t[][];
	private boolean modified[][];

	
	private Paint mPaint = new Paint();
	private float xx;
	private float yy;
	
	
	class PointB{
		
		public PointB(float pX, float pY, int pW) {
			x = pX;
			y = pY;
			width = pW;
		}
		
		float x;
		float y;
		int width;
	}
	
	private ArrayList<PointB> path = new ArrayList<PointB>(); 
	
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
	protected void onSizeChanged(int wd, int h, int oldw, int oldh) {
		super.onSizeChanged(wd, h, oldw, oldh);

		width = Math.max(wd, h);
		height = Math.max(wd, h);

		if (isInEditMode())
			return;

		load();

		if (offsetX > width)
			offsetX = 0;
		if (offsetY > height)
			offsetY = 0;

		mRect.set(0, 0, (int) (width * kZooming), (int) (height * kZooming));
		//mRect.offset(offsetX, offsetY);

		//int i = prefs.getInt(PREFS_BRUSH_TRANSP, 100);
		//int j = prefs.getInt(PREFS_BRUSH_SIZE, 1003);

		//brush.setTransparency(i);
		//brush.setSize(j, width);
	}

	public void setBrush(Brush brush)
	{
		mBrush = brush;
	}
	
    private void load() {

    	Bitmap newBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);

		Canvas newCanvas = new Canvas();
		newCanvas.setBitmap(newBitmap);
		if (mBitmap != null) {
			newCanvas.drawBitmap(mBitmap, 0, 0, null);
		}
		
		mBitmap = newBitmap;
		mCanvas = newCanvas;
		
		mRectBitmap = new Rect(0,0, width, height);
		
		r = new byte[width][height];
		y = new byte[width][height];
		b = new byte[width][height];
		w = new byte[width][height];
		t = new byte[width][height];
		modified = new boolean[width][height];

		for (int x = 0; x < width; x++)
			for (int z = 0; z < height; z++){
				r[x][z] = 0;
				y[x][z] = 0;
				b[x][z] = 0;
				w[x][z] = 0;
				t[x][z] = 0;
				modified[x][z] = false;
			}
	}

    
	@Override
    public boolean onTouchEvent(MotionEvent event) {
     
		int N = event.getHistorySize();
		
        switch (event.getAction()) {
    	case MotionEvent.ACTION_UP:
    	    
    		path.clear();
    		
    		return true;
    	case MotionEvent.ACTION_DOWN:

			//path.add(new PointB(event.getX(), event.getY(), (int)(event.getSize()*5000)));
            drawPoint( event.getX(), event.getY(), event.getPressure(),
  				  event.getSize());
    		
    		return true;
    	case MotionEvent.ACTION_MOVE:

            drawPoint( event.getX(), event.getY(), event.getPressure(),
  				  event.getSize());

    		//path.add(new PointB(event.getX(), event.getY(), (int) (event.getSize()*5000)));

			/*
    		if (N > 0)
    			for (int i=0; i<N; i++) 
    				path.add(new PointF(event.getHistoricalX(i), event.getHistoricalY(i)));
    			
    		*/
    		
    		//return true;
        default:
        	return true;
        }    
        
    }

	private void drawPoint(float x, float y, float pressure, float size) {
		int mCurX;
		int mCurY;
		float mCurPressure;
		float mCurSize;
		int mCurWidth;

		mCurX = (int)x;
		mCurY = (int)y;
		
		if (size == 0 && pressure != 1)
			size = pressure/2;
		
		mCurPressure = pressure;
		mCurSize = size;
		
//		mCurWidth = 1 ; //(int)(mCurSize*(brush.getSize()));
		mCurWidth = (int)(mCurSize*3000);
		if (mCurWidth < 1) mCurWidth = 1;
		
		int n = mCurWidth*2;
		
		mPaint.setColor(Color.BLUE);
		if (mBitmap != null)
			items.add(new Item(mCurX, mCurY, mCurWidth, mBrush)); //
	
	}
    
	public void setT(TextView tv) {
		mTv = tv;
		

		
	}
	
	private void set(int i, int j, Pixel pixel, int alpha) {
		if (pixel == null) return;
		
		r[i][j] = (byte)pixel.red;
		y[i][j] = (byte)pixel.yellow;
		b[i][j] = (byte)pixel.blue;
		w[i][j] = (byte)pixel.white;
		t[i][j] = (byte)alpha;
		
		modified[i][j] = true;
	}

	public void paint()
	{
	
		mPaint.setColor(Color.BLACK); 
		mPaint.setAlpha(255);
		
		/*
		for (Item item : items){
			for (int i = -item.radius; i <= item.radius ; i++)
				for (int j = -item.radius; j <= item.radius ; j++){
					int x1 = item.x+i;
					int y1 = item.y+j;
					if (x1 >= 0 && y1 >=0 && x1 < width && y1 < height && !modified[x1][y1]){
						
						//int brushColor = brush.getColor(i+item.radius, j+item.radius, item.radius);
						int brushColor = 0;
						
						float radius = (i)*(i)+(j)*(j);
						
						if (brushColor != -1 && radius < item.radius2){
							set(x1, y1, item.pixel, item.alpha);
							
							if (radius >= item.radius2-1000)
								mPaint.setColor(Color.BLACK);
							else if (radius >= item.radius2-1100)
								mPaint.setColor(Color.rgb(50, 50, 50));
							else if (radius >= item.radius2-1300)
								mPaint.setColor(Color.rgb(100, 100, 100));
							else 
								mPaint.setColor(Color.rgb(200, 200, 200));
							
//							mPaint.setColor(item.rgb);
//							mPaint.setAlpha(item.alpha);
							mCanvas.drawPoint(x1, y1, mPaint);
							
						}
					}
				}
		}
		*/
		items.clear();
		invalidate();
	}
	
	public void setBG(int idBG)
	{
		this.idBG = idBG;
		mBitmapBg = BitmapFactory.decodeResource( getContext().getResources(), idBG);
		mRectBG = new Rect(0, 0,  mBitmapBg.getWidth(), mBitmapBg.getHeight());
	}

	@Override 
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		
		if (isInEditMode()) return;
		
		//canvas.drawBitmap(mBitmapBg, mRectBG, mRect, null);
		canvas.drawColor(Color.WHITE);
		
		if (mBitmap != null) {
			canvas.drawBitmap(mBitmap, mRectBitmap, mRect, null);
		}
		
		mPaint.setColor(Color.BLUE);
		mPaint.setTextSize(35);
		canvas.drawText(""+mBitmap.getHeight()+"  "+mBitmap.getWidth(), 10, 40, mPaint);
		
	}
	
	public void refresh()
	{
		/*
		mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		for (int x = 0; x < width; x++)
			for (int z = 0; z < height; z++)
				mBitmap.setPixel(x, z, Var.ryb2rgb(r[x][z], 
													 y[x][z],
													 b[x][z],
													 w[x][z]));
		*/

		invalidate();
	}

	public void clear() {
		mBitmap.eraseColor(Color.TRANSPARENT);
		invalidate();
		
	}


}
