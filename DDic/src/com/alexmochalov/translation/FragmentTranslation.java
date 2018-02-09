package com.alexmochalov.translation;

import java.util.ArrayList;

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
import com.alexmochalov.dic.Dictionary;
import com.alexmochalov.dic.Entry;
import com.alexmochalov.dic.IndexEntry;
import com.alexmochalov.expr.DialogExpr;
import com.alexmochalov.main.*;

public class FragmentTranslation extends Fragment   implements OnClickListener{

	private Activity mContext;

	private TextView tvWord;
	private TextView tvPhonetic;
	private TextView tvTranslation;

	private ImageButton ibSpeak;
	private ImageButton ibBack;
	private ImageButton ibList;
	
	public FragmentTranslationCallback callback = null;

	
	public interface FragmentTranslationCallback {
		void btnForwardClicked(); 
		void btnSelectDictionary(String name);
		void btnReindex();
	} 
		
	private ArrayList<String> stackArray;
	
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

		ibBack = (ImageButton)rootView.findViewById(R.id.ibBack);
		ibBack.setOnClickListener(this);

		ibList = (ImageButton)rootView.findViewById(R.id.ibList);
		ibList.setOnClickListener(this);
		
		tvWord = (TextView)rootView.findViewById(R.id.tvWord);
		
		tvTranslation = (TextView)rootView.findViewById(R.id.tvTranslation);
		tvTranslation.setLinksClickable(true);
		tvTranslation.setMovementMethod(new LinkMovementMethod());
		
//		tvTranslation.setMovementMethod(new ScrollingMovementMethod());
		
		tvPhonetic = (TextView)rootView.findViewById(R.id.tvPhonetic);
        
		stackArray = new ArrayList<String>();
		
        return rootView;
    }
	
	/**
	 * 
	 * @param indexEntry - object, contains Text and address of Translation 
	 * @param saveToStack - 0 don't save, 1 save, 2 clear stack and save  
	 */
	public void setTranslation(IndexEntry indexEntry, int saveToStack ) {
						
		Entry entry = new Entry();
		if (indexEntry != null){
			
			String oldWord = tvWord.getText().toString();
			if (oldWord.length() == 0);
			else if (saveToStack == 2){
				stackArray.clear();
				stackArray.add(oldWord);
			} else if (saveToStack == 1){
				stackArray.add(oldWord);
				if (stackArray.size() > 100)
					stackArray.remove(0);
			}
			
			String str = Dictionary
				.readTranslation(indexEntry);
			entry.setTranslationAndPhonetic(str,
											indexEntry.getText());
			tvWord.setText(indexEntry.getText());

			String text = entry.getTranslation().toString();
			fillExpressionsList(text);
			text = "<br>"+text;
			//Log.d("",text);
			//for (int y = 0; y < text.length();y++)
			//Log.d("",""+(int)text.charAt(y)+"  "+text.charAt(y));
			//text.
			Spanned spannedText = Html.fromHtml(text, htmlImageGetter, htmlTagHandler);
	    	Spannable reversedText = revertSpanned(spannedText);
			
			tvTranslation.setText(reversedText);
			tvTranslation.scrollTo(0, 0);
		
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
			tvTranslation.scrollTo(0, 0);
			
			tvPhonetic.setText("");		
		}
		
//		tvTranslation.scrollTo(0, 0);

	}
	
    private String fillExpressionsList(String text) {
		int start = -1;
		int end = -1;
		boolean tag = false;
		
		Utils.getExpressions().clear();
		
		for (int i = 0; i < text.length(); i++){
			char c = text.charAt(i);
			
			if (c == '<'){
				if (start >= 0){
					end = i;
					addExpression(text, start, end);
					start = -1;
					end = -1;
					
				}
				tag = true;
			} else if (tag){
				if (c == '>')
					tag = false;
			}
			
			else if (start < 0 && !tag){
				if (c >= 0x41 && c <= 0x7a  // latinian
						||c >= 0xe0 && c <= 0x17e  // diacretioan
						|| c == ' ' || c == '/' || c == '*' || c == '\''){
					start = i;
				}
			} else {
				if (start >= 0 && 
						(c < 0x41 || c > 0x7a ) // latinian
						&& (c < 0xe0 || c > 0x172 ) // latinian
						&& c != ' ' && c != '/' && c != '*' && c != '\''){
					end = i;
					addExpression(text, start, end);
					start = -1;
					end = -1;
				}
			}
//			Log.d("", ""+c+" "+(int)c);
		}

		//if (start >= 0 && end >= 0){
			//text = text.substring(0, start)+"<sel>"+text.substring(start, end)+"</sel>"+text.substring(end);
		//}
    	
		return text;
	}

	private void addExpression(String text, int start, int end) {
		String s = text.substring(start, end).trim().replaceAll("\\*", " ");
		
		if (start >= 0 
				&& end >= 0
				&& s.length() > 1
				&& !s.startsWith("_"))
			Utils.addExpression(s);
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
				span = new KrefSpan(FragmentTranslation.this, ""); 
			//else if (tag.startsWith("sel"))
				//span = new SelSpan(FragmentTranslation.this, ""); 
			else if ("title".equalsIgnoreCase(tag)) span = new AppearanceSpan(0xffff2020, AppearanceSpan.NONE, 20, true, true, false, false);
			else if (tag.startsWith("color_")) span = new ParameterizedSpan(tag.substring(6));
			else if (tag.startsWith("abr")) span = new ColorSpan();
			
			if (span != null) processSpan(opening, output, span);
			
			//Element table = source.getFirstElement();
			//String tableContent = table.getContent().toString();
			
			//String[] tds = StringUtils.substringsBetween(testHtml, "<td>", "</td>");
		}
	};
	
	int len0 = -1;
	void processSpan(boolean opening, Editable output, Object span) {
		int len = output.length();
		if (opening) {
			output.setSpan(span, len, len, Spannable.SPAN_MARK_MARK);
			len0 = len;
			
		} else {
			Object[] objs = output.getSpans(0, len, span.getClass());
			int where = len;
			if (objs.length > 0) {
				for(int i = objs.length - 1; i >= 0; --i) {
					if (output.getSpanFlags(objs[i]) == Spannable.SPAN_MARK_MARK) {
						where = output.getSpanStart(objs[i]);

						if (span instanceof KrefSpan){
							KrefSpan k = (KrefSpan)span;
							k.setArticleId(output.toString(), len0);
						}
						
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
		} else if (v == ibList){
			ShowListOfExpressions();
		} else if (v == ibBack){
			if (stackArray.size() > 0){
				IndexEntry indexEntry =  Dictionary.find(stackArray.get(stackArray.size()-1));
				if (indexEntry != null)
					setTranslation(indexEntry, 0);
				stackArray.remove(stackArray.size()-1);
			}
		}	
	}

	private void ShowListOfExpressions() {
		DialogExpr dialog = new DialogExpr(mContext, Utils.getExpressions(), false);
		dialog.show();
	}
	
}
