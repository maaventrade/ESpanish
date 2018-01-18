package com.alexmochalov.dic;

import android.app.Activity;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;

public class KrefSpan extends ClickableSpan {

	private final FragmentTranslation fragmentTranslation;
	private String articleId;

	public KrefSpan(FragmentTranslation fragmentTranslation, String articleId) {
		super();
		this.fragmentTranslation = fragmentTranslation;
		this.articleId = articleId;
	}

	@Override
	public void onClick(View arg0) {
		IndexEntry indexEntry =  Dictionary.find(articleId);
		fragmentTranslation.setTranslation(indexEntry);
	}

	public void setArticleId(String spannedText, int start) {
		articleId = spannedText.toString().substring(start);
		
	}

}
