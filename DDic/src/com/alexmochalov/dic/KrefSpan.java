package com.alexmochalov.dic;

import android.text.style.*;
import android.util.*;
import android.view.*;

public class KrefSpan extends ClickableSpan {

	private final FragmentTranslation fragmentTranslation;
	private String articleId;

	public KrefSpan(FragmentTranslation fragmentTranslation, String articleId) {
		super();
		this.fragmentTranslation = fragmentTranslation;
		this.articleId = articleId;
		//Log.d("","NEW - "+this);
	}

	@Override
	public void onClick(View arg0) {
		IndexEntry indexEntry =  Dictionary.find(articleId);
		//Log.d("","Sel "+this);
		//Log.d("","articleId "+articleId);
		
		if (indexEntry != null)
			fragmentTranslation.setTranslation(indexEntry, 1);
	}

	public void setArticleId(String spannedText, int start) {
		articleId = spannedText.toString().substring(start);
		
	}

}
