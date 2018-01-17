package com.alexmochalov.dic;

import android.app.Activity;
import android.text.style.ClickableSpan;
import android.view.View;

public class KrefSpan extends ClickableSpan {

	final FragmentTranslation fragmentTranslation;
	final String articleId;

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

}
