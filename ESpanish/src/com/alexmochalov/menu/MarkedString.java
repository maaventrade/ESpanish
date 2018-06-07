package com.alexmochalov.menu;

import android.util.*;

import com.alexmochalov.main.*;
import com.alexmochalov.rules.*;

import java.util.ArrayList;

public class MarkedString {
	
	String mNeg1 = "";
	String mNeg2 = "";
	String mNegRus = "";
	
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
	
	public MarkedString(String text, String subj, String neg1, String verb, String neg2, String negRus) {
		mNeg1 = neg1;
		mNeg2 = neg2;
		mNegRus = negRus;
		
		mVerb = verb;
		
		Entry e;
		
		//text = "Quanto";
		//neg = "false";
		//verb = "lavorare";
		
		Pronoun pronoun;
		String pronounStr = "";
		String pronounStrRus = "";
		
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
		
		if (!neg1.equals(""))
			if (Utils.getLanguage().equals("fra")){
				if (Utils.isVowel(verb.substring(0, 1)))
					neg1 = " "+neg1.substring(0, neg1.length() - 1)+"'";
					else
						neg1 = " "+neg1+" ";
			}
			else
				neg1 = " "+neg1+" ";
		
		if (!neg2.equals(""))
			neg2 = " "+neg2+" ";
		
		if (!negRus.equals(""))
			negRus = " "+negRus+" ";
		
		//else if (neg.equals(""))
		//	if (Math.random() > 0.5)
		//		negStr = Utils.getNeg();
		
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
				neg1+
				verbStr+
				neg2+
				"?";
		
		mRusText = Utils.firstLetterToUpperCase(Rules.translate(text).getTranslation())+" "+
				pronounStrRus+
				negRus+
				verbStrRus+
				"?";
		
		mFlag = 0;
	}

	public MarkedString(String neg1, String neg2, String negRus, Pronoun pronoun, String verb) {
		
		mNeg1 = neg1;
		mNeg2 = neg2;
		mNegRus = negRus;
		
		mVerb = verb;

		Entry e = Rules.translate(verb);
		e = Rules.translate(verb);		
		
		if (!neg1.equals(""))
			if (Utils.getLanguage().equals("fra")){
				if (Utils.isVowel(verb.substring(0, 1)))
					neg1 = " "+neg1.substring(0, neg1.length() - 1)+"'";
					else
						neg1 = " "+neg1+" ";
			}
			else
				neg1 = " "+neg1+" ";
		
		if (!neg2.equals(""))
			neg2 = " "+neg2+" ";
		
		if (!negRus.equals(""))
			negRus = " "+negRus+" ";
		
		this.mRusText = Utils.firstLetterToUpperCase(pronoun.getTranslation()) + 
				negRus + 
				Rules.fit(e,  Rules.getPronouns().indexOf(pronoun) , "present").trim();
		
		this.mText = (pronoun.getText() + neg1 + pronoun.conj(verb, false) + neg2).toUpperCase();
	
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
		
		String S = "";
		
		if (mNeg1.trim().length() != 0)
			S = S + " neg1 = \""+ mNeg1 +"\" ";
		
		if (mNeg2.trim().length() != 0)
			S = S + " neg2 =  \""+ mNeg2 +"\" ";
		
		if (mNegRus.trim().length() != 0)
			S = S + " negrus = \""+ mNegRus
			+"\" ";
		
		return S; 
	}
	
	//public String getVerb() {
		//return mVerb; 
	//}
}
