package com.alexmochalov.dic;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.CharacterStyle;

public class ColorSpan extends CharacterStyle {

	int color = 0;

	public ColorSpan() {
		try {
			color = Color.parseColor("#ff00CC00");
		} catch(Exception ex) { }
	}

	@Override
	public void updateDrawState(TextPaint tp) {
		tp.setColor(color);
	}

}
