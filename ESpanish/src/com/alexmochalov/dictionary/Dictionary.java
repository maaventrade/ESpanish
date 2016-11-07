package com.alexmochalov.dictionary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.alex_mochalov.navdraw.R;

import android.content.Context;
import android.util.Log;

import com.alexmochalov.dictionary.Dictionary.*;
import com.alexmochalov.menu.MenuData;
import com.alexmochalov.root.*;

public class Dictionary
{

	public static boolean isLast(Pronoun p)
	{
		//return false;
		return pronouns.indexOf(p) == pronouns.size() - 1;
	}

	private static class Fit {
		int fitType;
		String obj[];
		String objPast[];
		
		public Fit(String attributeValue) {
			fitType = Integer.parseInt(attributeValue);
		}

		public String getEnding(int sub, String time)
		{
			if (time.equals("past"))
				return objPast[sub].trim();
			else
				return obj[sub].trim();
		}
		
		public void setObj(String attributeValue) {
			obj = attributeValue.split(":");
		}
		
		public void setObjPast(String attributeValue) {
			objPast = attributeValue.split(":");
		}
	}
	
	private static ArrayList<Entry> entries = new ArrayList<Entry>();
	private static ArrayList<Fit> fits = new ArrayList<Fit>();

	

	private static ArrayList<Pronoun> pronouns = new ArrayList<Pronoun>();

	public static void addEntry(String text) {
		Entry e = new Entry();
		e.mText = text;
	
		
		entries.add(e);
		
		
	}

	/*
	public static String getTranslation(String searchString) {
		if (searchString.trim().length() == 0)
			return "";
		
		searchString = searchString.replaceAll("[^a-zA-Záóúé]", "").toLowerCase();

		for (Entry e : entries) {
			if (e.mText.replaceAll("[^a-zA-Záóúé]", "").toLowerCase()
					.equals(searchString)) {
				return e.translation;
			}
		}
		for (Entry e : entries) {
			if (e.mText.replaceAll("[^a-zA-Záóúé]", "").toLowerCase()
					.contains(searchString)) {
				return e.translation;
			}
		}
		return "";
	}
	*/

	public static Entry getTranslation(String searchString) {
		if (searchString.trim().length() == 0)
			return null;
		
		searchString = searchString.replaceAll("[^a-zA-Záóúé]", "").toLowerCase();

		for (Entry e : entries) {
			if (e.mText.replaceAll("[^a-zA-Záóúé]", "").toLowerCase()
					.equals(searchString)) {
				return e;
			}
		}
		for (Entry e : entries) {
			if (e.mText.replaceAll("[^a-zA-Záóúé]", "").toLowerCase()
					.contains(searchString)) {
				return e;
			}
		}
		return null;
	}

	public static Entry getTranslationAsIs(String searchString) {
		if (searchString.trim().length() == 0)
			return null;
		
		searchString = searchString.replaceAll("[^a-zA-Záóúé]", "");

		for (Entry e : entries) {
			if (e.mText.replaceAll("[^a-zA-Záóúé]", "").
					equals(searchString)) {
				return e;
			}
		}
		for (Entry e : entries) {
			if (e.mText.replaceAll("[^a-zA-Záóúé]", "").
					contains(searchString)) {
				return e;
			}
		}
		return null;
	}
	
	public static void addPronoun(String text) {
		Pronoun e = new Pronoun();
		e.mText = text;
		pronouns.add(e);
	}

	public static void setProniunTranslation(String text) {
		pronouns.get(pronouns.size() - 1).translation = text;
	}

	public static void setVerbEnding(String text) {
		pronouns.get(pronouns.size() - 1).verb_ending = text;
	}

	public static void setVerbEndingPast(String text) {
		pronouns.get(pronouns.size() - 1).verb_ending_past = text;
	}

	public static ArrayList<Pronoun> getPronouns() {
		return pronouns;
	}

	public static Pronoun getRandomPronoun() {
		return pronouns.get( (int) (Math.random()*pronouns.size())  );
	}
	
	public static boolean testRus(String translation, String text, int direction) {

	//Log.d("", "" + translation + "  " + text);

		if (direction == 0) {
			translation = translation.toLowerCase()
					.replaceAll("á", "a")
					.replaceAll("ó", "o")
					.replaceAll("ú", "u").
					replaceAll("é", "e").
					replaceAll("á", "a").
					replaceAll("í", "e").
					replaceAll("[^a-zA-Z]", "");
			
			String text1 = text.toLowerCase()
					.replaceAll("á", "a").replaceAll("ó", "o")
					.replaceAll("ú", "u").replaceAll("é", "e").
					replaceAll("á", "a").
					replaceAll("í", "e").
									  replaceAll("[^a-zA-Z]", "");

			if ( translation.equals(text1)) return true;
			else if (text1.contains(translation+",") || text1.contains(", "+translation)) return true;
			else return false;	
			
		} else {
			translation = translation.replaceAll("[^а-яА-Я]", "").toLowerCase();
			
			String text1 = text.toLowerCase();
			
			if ( translation.equals(text1.replaceAll("[^а-яА-Я]", ""))) return true;
			else if (text1.contains(translation+",") || text1.contains(", "+translation)) return true;
			else return false;	
		}
	}

	public static void load(Context context) {
		entries.clear();
		pronouns.clear();
		fits.clear();

		String mode = "";

		try {

			XmlPullParser xpp = null;
			if (Utils.getLanguage().equals("ita")) 
				xpp = context.getResources().getXml(R.xml.dictionary_it);
			else if (Utils.getLanguage().equals("spa"))
				xpp = context.getResources().getXml(R.xml.dictionary_spa);
			
			while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
				switch (xpp.getEventType()) { // начало документа
				case XmlPullParser.START_DOCUMENT:
					// Log.d(LOG_TAG, "START_DOCUMENT");
					break; // начало тэга
				case XmlPullParser.START_TAG:
					if (xpp.getName().equals("dictionary")) {
						mode = "dictionary";
					} else if (xpp.getName().equals("entry")
							&& mode.equals("dictionary")) {
						// Log.d(LOG_TAG, "getAttributeCount: " +
						// xpp.getAttributeCount());
						for (int i = 0; i < xpp.getAttributeCount(); i++) {
							if (xpp.getAttributeName(i).equals("text")) {
								addEntry(xpp.getAttributeValue(i));
							} else if (xpp.getAttributeName(i).equals("transl")) {
								entries.get(entries.size() - 1).
									setTranslation(xpp.getAttributeValue(i));
							} else if (xpp.getAttributeName(i).equals("root")) {
								entries.get(entries.size() - 1).
									setRoot(xpp.getAttributeValue(i));
							} else if (xpp.getAttributeName(i).equals("fit_type")) {
								entries.get(entries.size() - 1).
									setFitType(xpp.getAttributeValue(i));
							}
						}
					} else if (xpp.getName().equals("entry")
							&& mode.equals("fits")) {
						for (int i = 0; i < xpp.getAttributeCount(); i++) {
							if (xpp.getAttributeName(i).equals("type")) {
								fits.add(new Fit(xpp.getAttributeValue(i)));
							} else if (xpp.getAttributeName(i).equals("obj")) 
								fits.get(fits.size() - 1).
									setObj(xpp.getAttributeValue(i));
							else if (xpp.getAttributeName(i).equals("obj_past")) 
								fits.get(fits.size() - 1).
									setObjPast(xpp.getAttributeValue(i));
						}
					} else if (xpp.getName().equals("pronoun")) {
						mode = "pronoun";
					} else if (xpp.getName().equals("fits")) {
						mode = "fits";
					} else if (xpp.getName().equals("entry")
							&& mode.equals("pronoun")) {
						for (int i = 0; i < xpp.getAttributeCount(); i++) {
							if (xpp.getAttributeName(i).equals("text")) {
								addPronoun(xpp.getAttributeValue(i));
							} else if (xpp.getAttributeName(i).equals("transl")) {
								setProniunTranslation(xpp.getAttributeValue(i));
							} else if (xpp.getAttributeName(i).equals(
									"verb_ending")) 
								setVerbEnding(xpp.getAttributeValue(i));
							 else if (xpp.getAttributeName(i).equals(
								"verb_ending_past")) 
							 setVerbEndingPast(xpp.getAttributeValue(i));
						}
					}
					break; // конец тэга
				case XmlPullParser.END_TAG:
					break; // содержимое тэга
				case XmlPullParser.TEXT:
					/*
					 * if (mode.equals("Выражения") ||
					 * mode.equals("Спряжения")){
					 * 
					 * ArrayList<String> itemsTextData = new
					 * ArrayList<String>(Arrays.asList(xpp.getText().
					 * split("\n"))); for (int i = 0; i < itemsTextData.size();
					 * i++) itemsTextData.set(i, itemsTextData.get(i).trim());
					 * 
					 * textData.get(textData.size()-1).add(new
					 * Data(itemsTextData)); }
					 */
					break;
				default:
					break;
				} // следующий элемент
				xpp.next();
			}
			// Log.d(LOG_TAG, "END_DOCUMENT");

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	}

	public static String conj(int i, String verb, boolean isPast) {
		return pronouns.get(i).conj(verb, isPast);
	}

//	public static CharSequence conj(CharSequence text, String verb) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	public static String fit(Entry e, int sub, String time) {
		String ending = "";
	
		for (Fit f : fits){
			int n = f.fitType;
			int k = e.fit_type;
			if (f.fitType == e.fit_type){
				ending = f.getEnding(sub, time);
				return e.root + ending;
			}
		}
		 
		return e.root + ending;
	}

	public static ArrayList<Entry> getEntries() {
		return entries;
	}

	public static String fit(Entry e, Pronoun pronoun, String time) {
		String ending = "";
		int sub = pronouns.indexOf(pronoun);
		
		for (Fit f : fits){
			int n = f.fitType;
			int k = e.fit_type;
			if (f.fitType == e.fit_type){
				ending = f.getEnding(sub, time);
				return e.root + ending;
			}
		}
		 
		return e.root + ending;
	}


}
