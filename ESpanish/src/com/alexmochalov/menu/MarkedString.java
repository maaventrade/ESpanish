package com.alexmochalov.menu;

import android.util.*;

import com.alexmochalov.main.*;
import com.alexmochalov.rules.*;

import java.util.ArrayList;

public class MarkedString {
	
	String mNeg = "";
	String mVerb = "";
	
	String mText = "";
	String mRusText = "";

	private int mFlag;
	
	//private ArrayList<MarkedString> children;

	// 001 spa->ru completed
	// 010 ru->spa completed
	// 100 audio completed ?????????
	
	
	public MarkedString(String text) {
		mText = text;
		mFlag = 0;
	}
	
	public MarkedString(String text, String rus) {
		mText = text;
		mRusText = rus;
		mFlag = 0;
	}
	
	public MarkedString(String text, String subj, String neg, String verb) {
		mNeg = neg;
		mVerb = verb;
		
		Entry e;
		
		//text = "Quanto";
		//neg = "false";
		//verb = "lavorare";
		
		Pronoun pronoun;
		String pronounStr = "";
		String pronounStrRus = "";
		
		String negStr = "";
		String negStrRus = "";
		
		String verbStr = "";
		String verbStrRus = "";

		if (!subj.equals("false")){
			pronoun = Rules.getRandomPronoun();
			pronounStr = pronoun.getText()+" ";
			pronounStrRus = Rules.getTranslationAsIs(pronoun.getText()).getTranslation()+" ";
		} else {
			pronoun = Rules.getPronouns().get(2);
			pronounStr = "";
			pronounStrRus = "";
		}
		
		int index = Rules.getPronouns().indexOf(pronoun);
		
	//Log.d("uu","neg "+neg);
		if (neg.equals("true"))
			negStr = Utils.getNeg();
		else if (neg.equals("false"))
			negStr = "";
		else if (neg.equals(""))
			if (Math.random() > 0.5)
				negStr = Utils.getNeg();
		e = Rules.translate(negStr);
		if ( e != null)
			negStrRus = e.getTranslation()+" ";
		if (negStr.length() > 0)
			negStr = negStr + " ";

		String timeStr = MenuData.getTenseLast();
		
		//Log.d("","timeStr "+timeStr);
		if (timeStr.length() == 0){
			if (Math.random() > 0.5)
				timeStr = "present";
			else 
				timeStr = "past";
		}
		
		if (timeStr.equals("present"))
			verbStr = pronoun.conj(verb, false);
		else 
			verbStr = Rules.conj(index, "avere", false) +
            		" "+
            		Rules.conj(index, verb, true);		
	

		e = Rules.translate(verb);
		
		verbStrRus = Rules.fit(e, pronoun, timeStr).trim();

		
		mText = text+" "+
				pronounStr+
				negStr+
				verbStr+
				"?";
		
		mRusText = Utils.firstLetterToUpperCase(Rules.translate(text).getTranslation())+" "+
				pronounStrRus+
				negStrRus+
				verbStrRus+
				"?";
		
		mFlag = 0;
	}

	public MarkedString(String neg, Pronoun pronoun, String verb) {
		mNeg = neg;
		mVerb = verb;
		
		String negStr = " ";
		String negStrRus = " ";

		//Log.d("d","neg = "+neg);
		
		if (neg.equals("true")){
			negStr = Utils.getNeg();
			
		}else if (neg.equals("false"))
			negStr = " "; /// ????
		else if (neg.equals(""))
			if (Math.random() > 0.5)
				negStr = Utils.getNeg();
		if (negStr.trim().length() > 0){
			negStr = " " + negStr + " ";
			negStrRus = " не ";
		}
	
		Entry e = Rules.translate(verb);
		e = Rules.translate(verb);		
		
		//negStrRus = Rules.getTranslation(negStr);
		
		this.mRusText = Utils.firstLetterToUpperCase(pronoun.getTranslation()) + 
				negStrRus + 
				Rules.fit(e,  Rules.getPronouns().indexOf(pronoun) , "present").trim();
		
		this.mText = pronoun.getText() + negStr + pronoun.conj(verb, false);
	
		mFlag = 0;
	}
/*
	public void setVerbs(String neg, String pverbs)
	{
		children = new ArrayList<MarkedString>();
		String verbs[] = pverbs.split(",");
		for (String verb : verbs){
			for (Pronoun p: com.alexmochalov.Rules.Rules.getPronouns()){
				children.add(new MarkedString(neg, p, verb.trim()));
			}
		}
	}
*/
	public void setRusText(String rusText)
	{
		mRusText = rusText;
		//Log.d("d","rusText "+rusText);
	}

	public void setText(String text)
	{
		mText = text;
	}

	public void setFlag(int flag)
	{
		mFlag = flag;
	}

	

	public int getFlag()
	{
		return mFlag;
	}
	
	public void setFlag(String flag) {
		if (flag.length() == 0)
			mFlag = 0;
		else
			mFlag = Integer.parseInt(flag);
		
	}

	public String getText() {
		return mText;
	}

	public String getRusText() {
		return mRusText;
	}
	
	public String getNeg() {
		return mNeg; 
	}
	
	//public String getVerb() {
		//return mVerb; 
	//}
}
