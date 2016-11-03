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
	
	public MarkedString(String text, int sub, String neg, String verb) {
		
		String negStr = " ";
		String negStrRus = " ";
		String pronoun = "";
		
	//Log.d("uu","neg "+neg);
		if (neg.equals("true"))
			negStr = Utils.getNeg();
		else if (neg.equals("false"))
			negStr = "";
		else if (neg.equals(""))
			if (Math.random() > 0.5)
				negStr = Utils.getNeg();
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
		
		
		//Log.d("",negStr);
		Entry e = Dictionary.getTranslation(negStr);
		if ( e != null)
			negStr = e.getTranslation();
			
		if (negStr.length() > 1)
			negStr = negStr + " ";
		else negStr = "";
		
		e = Dictionary.getTranslation(pronoun);
		if ( e != null)
			pronoun = e.getTranslation().trim();
		
		if (pronoun.length() > 0)
			pronoun = pronoun + " ";
			
		e = Dictionary.getTranslation(verb);			
		verb = Dictionary.fit(e, sub, "present").trim();
		
		//Log.d("", text);
		//Log.d("", Dictionary.getTranslation(text).translation);
		mRusText = Utils.firstLetterToUpperCase(Dictionary.getTranslation(text).getTranslation())+" "
				+ pronoun
				+ negStr
				+ verb + "?";
			}
			
		mFlag = 0;
	}

	public MarkedString(String neg, Pronoun pronoun, String verb) {

		String negStr = " ";
		String negStrRus = " ";

		if (neg) {
			negStr = " non ";
			negStrRus = " не ";
		}
		
		Entry e = Dictionary.getTranslation(verb);
		e = Dictionary.getTranslation(verb);			
		
		this.mRusText = Utils.firstLetterToUpperCase(pronoun.getTranslation()) + 
				negStrRus + 
				Dictionary.fit(e,  Dictionary.getPronouns().indexOf(pronoun)+1 , "present").trim();
		
		this.mText = pronoun.getText() + negStr + pronoun.conj(verb, false);
		
		mFlag = 0;
	}
	
	public void setFlag(String flag) {
		mFlag = Integer.parseInt(flag);
	}
}
