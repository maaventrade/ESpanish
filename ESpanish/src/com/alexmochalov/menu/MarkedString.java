package com.alexmochalov.menu;

import android.util.*;
import com.alexmochalov.dictionary.*;
import com.alexmochalov.root.*;

class MarkedString {

	String mText;
	String mRusText = "";

	int mFlag;

	// 001 spa->ru completed
	// 010 ru->spa completed
	// 100 audio completed ?????????
	
	
	public MarkedString(String text) {
		mText = text;
		mFlag = 0;
	}
	
	public MarkedString(String text, String subj, String neg, String verb) {
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
			pronoun = Dictionary.getRandomPronoun();
			pronounStr = pronoun.getText()+" ";
			pronounStrRus = Dictionary.getTranslationAsIs(pronoun.getText()).getTranslation()+" ";
		} else {
			pronoun = Dictionary.getPronouns().get(2);
			pronounStr = "";
			pronounStrRus = "";
		}
		
		int index = Dictionary.getPronouns().indexOf(pronoun);
		
	//Log.d("uu","neg "+neg);
		if (neg.equals("true"))
			negStr = Utils.getNeg();
		else if (neg.equals("false"))
			negStr = "";
		else if (neg.equals(""))
			if (Math.random() > 0.5)
				negStr = Utils.getNeg();
		e = Dictionary.getTranslation(negStr);
		if ( e != null)
			negStrRus = e.getTranslation()+" ";
		if (negStr.length() > 0)
			negStr = negStr + " ";

		String timeStr = MenuData.getTenseLast();
		
		Log.d("","timeStr "+timeStr);
		if (timeStr.length() == 0){
			if (Math.random() > 0.5)
				timeStr = "present";
			else 
				timeStr = "past";
		}
		
		if (timeStr.equals("present"))
			verbStr = pronoun.conj(verb, false);
		else 
			verbStr = Dictionary.conj(index, "avere", false) +
            		" "+
            		Dictionary.conj(index, verb, true);		
	

		e = Dictionary.getTranslation(verb);
		
		verbStrRus = Dictionary.fit(e, pronoun, timeStr).trim();

			/*
		if (sub == 3) 
			mText = "¿"+text+negStr+ Dictionary.conj(2, verb, false)+"?";
		else {
			pronoun = "tú";
			mText = "¿"+text+" tú"+ negStr +Dictionary.conj(1, verb, false)+"?";
		}	
		
		if (mText.contains("Cómo tú llamas")){
			mRusText = "Как тебя зовут?";
			mText = "¿Cómo té llamas?";
		} else {
		*/
		
		mText = text+" "+
				pronounStr+
				negStr+
				verbStr+
				"?";
		
		mRusText = Utils.firstLetterToUpperCase(Dictionary.getTranslation(text).getTranslation())+" "+
				pronounStrRus+
				negStrRus+
				verbStrRus+
				"?";
			
		mFlag = 0;
	}

	public MarkedString(String neg, Pronoun pronoun, String verb) {

		String negStr = " ";
		String negStrRus = " ";

		if (neg.equals("true"))
			negStr = Utils.getNeg();
		else if (neg.equals("false"))
			negStr = "";
		else if (neg.equals(""))
			if (Math.random() > 0.5)
				negStr = Utils.getNeg();
		if (negStr.length() > 0)
			negStr = " " + negStr + " ";
		
		Entry e = Dictionary.getTranslation(verb);
		e = Dictionary.getTranslation(verb);			
		
		this.mRusText = Utils.firstLetterToUpperCase(pronoun.getTranslation()) + 
				negStrRus + 
				Dictionary.fit(e,  Dictionary.getPronouns().indexOf(pronoun) , "present").trim();
		
		this.mText = pronoun.getText() + negStr + pronoun.conj(verb, false);
		
		mFlag = 0;
	}
	
	public void setFlag(String flag) {
		mFlag = Integer.parseInt(flag);
	}
}
