package com.alexmochalov.dic;

import android.app.Activity;
import android.text.style.ClickableSpan;
import android.view.View;

public class ArticleSpan extends ClickableSpan {

	final Activity activity;
	final String articleId;

	public ArticleSpan(Activity activity, String articleId) {
		super();
		this.activity = activity;
		this.articleId = articleId;
	}

	@Override
	public void onClick(View arg0) {
	//	activity.setArticle(articleId);
	}

}
