package com.alexmochalov.dic;

import org.xml.sax.XMLReader;

import android.app.*;
import android.content.*;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.renderscript.Element;
import android.text.*;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.*;
import android.view.View.*;
import android.webkit.WebView;
import android.widget.*;

import com.alexmochalov.ddic.*;
import com.alexmochalov.main.*;

public class FragmentTranslation extends Fragment   implements OnClickListener{

	private Activity mContext;

	private TextView tvWord;
	private TextView tvPhonetic;
	private TextView tvTranslation;

	private ImageButton ibSpeak;
	private ImageButton ibMenu;
	
	public FragmentTranslationCallback callback = null;
	public interface FragmentTranslationCallback {
		void btnForwardClicked(); 
		void btnSelectDictionary(String name);
		void btnReindex();
	} 
	
	
	public FragmentTranslation(Activity context)
	{
		super();
		mContext = context;
	}

	public FragmentTranslation()
	{
		super();
	}
	
	@Override
    public void onPause()
	{
		super.onPause();
	}
	
	@Override
    public void onResume()
	{
		super.onResume();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_translation, container, false);
        
		ibSpeak = (ImageButton)rootView.findViewById(R.id.ibSpeak);
		ibSpeak.setOnClickListener(this);
		
		tvWord = (TextView)rootView.findViewById(R.id.tvWord);
		
		tvTranslation = (TextView)rootView.findViewById(R.id.tvTranslation);
		tvTranslation.setLinksClickable(true);
		tvTranslation.setMovementMethod(new LinkMovementMethod());
		
//		tvTranslation.setMovementMethod(new ScrollingMovementMethod());
		
		tvPhonetic = (TextView)rootView.findViewById(R.id.tvPhonetic);
        
        return rootView;
    }

	public void setTranslation(IndexEntry indexEntry) {
						
		Entry entry = new Entry();
		if (indexEntry != null){
			String str = Dictionary
				.readTranslation(indexEntry);
			entry.setTranslationAndPhonetic(str,
											indexEntry.getText());
			tvWord.setText(indexEntry.getText());

			String text = entry.getTranslation().toString();
			
	    	Spanned spannedText = Html.fromHtml(text, htmlImageGetter, htmlTagHandler);
	    	Spannable reversedText = revertSpanned(spannedText);
			
			tvTranslation.setText(reversedText);
		
			String phonetic = entry.getPhonetic();
			if (phonetic.length() > 0)
				tvPhonetic
					.setText("[" + phonetic + "]");
			else
				tvPhonetic.setText("");						
		} else {
			entry.setTranslationAndPhonetic("",
										"");
			tvWord.setText("");

			tvTranslation.setText("");
			
			tvPhonetic.setText("");		
		}
		
//		tvTranslation.scrollTo(0, 0);

	}
	
    Html.ImageGetter htmlImageGetter = new Html.ImageGetter() {
		public Drawable getDrawable(String source) {
			return null;
//			int resId = getResources().getIdentifier(source, "drawable", getPackageName());
//			Drawable ret = MainActivity.this.getResources().getDrawable(resId);
//			ret.setBounds(0, 0, ret.getIntrinsicWidth(), ret.getIntrinsicHeight());
//			return ret;
		}
	};
	
	Html.TagHandler htmlTagHandler = new Html.TagHandler() {
		public void handleTag(boolean opening, String tag, Editable output,	XMLReader xmlReader) {
			Object span = null; 
			if (tag.startsWith("article_")) span = new ArticleSpan(mContext, tag);
			else if (tag.startsWith("kref"))
				span = new KrefSpan(FragmentTranslation.this, tag);
			else if ("title".equalsIgnoreCase(tag)) span = new AppearanceSpan(0xffff2020, AppearanceSpan.NONE, 20, true, true, false, false);
			else if (tag.startsWith("color_")) span = new ParameterizedSpan(tag.substring(6));
			if (span != null) processSpan(opening, output, span);
			
			//Element table = source.getFirstElement();
			//String tableContent = table.getContent().toString();
			
			//String[] tds = StringUtils.substringsBetween(testHtml, "<td>", "</td>");
		}
	};
	
	void processSpan(boolean opening, Editable output, Object span) {
		int len = output.length();
		if (opening) {
			output.setSpan(span, len, len, Spannable.SPAN_MARK_MARK);
		} else {
			Object[] objs = output.getSpans(0, len, span.getClass());
			int where = len;
			if (objs.length > 0) {
				for(int i = objs.length - 1; i >= 0; --i) {
					if (output.getSpanFlags(objs[i]) == Spannable.SPAN_MARK_MARK) {
						where = output.getSpanStart(objs[i]);
						output.removeSpan(objs[i]);
						break;
					}
				}
			}

			if (where != len) {
				output.setSpan(span, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
	}
	
	final Spannable revertSpanned(Spanned stext) {
		Object[] spans = stext.getSpans(0, stext.length(), Object.class);
		Spannable ret = Spannable.Factory.getInstance().newSpannable(stext.toString());
		if (spans != null && spans.length > 0) {
			for(int i = spans.length - 1; i >= 0; --i) {
				ret.setSpan(spans[i], stext.getSpanStart(spans[i]), stext.getSpanEnd(spans[i]), stext.getSpanFlags(spans[i]));
			}
		}

		return ret;
	}	
	
	@Override
	public void onClick(View v) {
		if (v == ibSpeak){
			TtsUtils.speak(tvWord.getText().toString());
		}
	}
	
}
