package com.alexmochalov.translation;

import com.alexmochalov.dic.Dictionary;
import com.alexmochalov.dic.IndexEntry;

import android.text.style.*;
import android.util.*;
import android.view.*;
import android.text.*;

public class SelSpan extends CharacterStyle
{

	@Override
	public void updateDrawState(TextPaint p1)
	{
		// TODO: Implement this method
	}
	

	private final FragmentTranslation fragmentTranslation;
	private String articleId;

	public SelSpan(FragmentTranslation fragmentTranslation, String articleId) {
		super();
		this.fragmentTranslation = fragmentTranslation;
		this.articleId = articleId;
	
	}
/*
	@Override
	public void onClick(View arg0) {
		//IndexEntry indexEntry =  Dictionary.find(articleId);
		//if (indexEntry != null)
			//fragmentTranslation.setTranslation(indexEntry, 1);
	}
/*
	public void setArticleId(String spannedText, int start) {
		articleId = spannedText.toString().substring(start);

	}
*/
}
