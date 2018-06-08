package com.alexmochalov.tstest;

public class Item {
	int x;
	int y;
	int radius;
	int radius2;
	Pixel pixel;
	int alpha;
	int rgb;
	
	Item(int x, int y, int radius, Brush brush){ 
		this.x = x;
		this.y = y;
		
		this.radius = radius;
		this.radius2 = radius*radius;
		this.pixel = brush.getPixel();
		
		int t = brush.getThickness();
		if (t > 200)
			this.alpha = 255;
		else
			this.alpha = t;
		
		rgb = brush.getColor();

		//Log.d("", "this.alpha  "+this.alpha+"  "+brush.getTransparency()+"  "+(this.alpha/100f*brush.getTransparency()));
	//	this.alpha = (int)(this.alpha/100f*brush.getTransparency());
	};

	public Item(Item item0, Item item1) {
		this.x = (item0.x + item1.x)/2;
		this.y = (item0.y + item1.y)/2;
		
		this.radius = item0.radius;
		this.radius2 = item0.radius2;
		//this.pixel = item0.pixel;
		this.alpha = item0.alpha;
		this.rgb = item0.rgb;
	}
	
}
