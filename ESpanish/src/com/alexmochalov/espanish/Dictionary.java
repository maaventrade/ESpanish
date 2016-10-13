package com.alexmochalov.espanish;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.alex_mochalov.navdraw.R;

import android.content.Context;
import android.util.Log;

public class Dictionary {

	private static class Entry {
		String mText;
		String translation;
	}

	private static ArrayList<Entry> entries = new ArrayList<Entry>();

	static class Pronoun {
		String mText;
		String translation;
		String verb_ending;

		String conj(String verb) {
			return verb.substring(0, verb.length() - 2) + verb_ending;
		}
	}

	private static ArrayList<Pronoun> pronouns = new ArrayList<Pronoun>();

	public static void addEntry(String text) {
		Entry e = new Entry();
		e.mText = text;
		entries.add(e);
	}

	public static void setTranslation(String text) {
		entries.get(entries.size() - 1).translation = text;
	}

	public static String getTranslation(String searchString) {
		searchString = searchString.replaceAll("[^a-zA-Z]", "").toLowerCase();

		for (Entry e : entries) {
			if (e.mText.replaceAll("[^a-zA-Z]", "").toLowerCase()
					.contains(searchString)) {
				return e.translation;
			}
		}
		return "";
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

	public static ArrayList<Pronoun> getPronouns() {
		return pronouns;
	}

	public static boolean testRus(String translation, String text, int direction) {

		Log.d("", "" + translation + "  " + text);

		if (direction == 1) {
			translation = translation.replaceAll("[^a-zA-Z]", "")
					.replaceAll("á", "a").replaceAll("ó", "o")
					.replaceAll("ú", "u").replaceAll("á", "a").toLowerCase();
			return translation.equals(text.replaceAll("[^a-zA-Z]", "")
					.replaceAll("á", "a").replaceAll("ó", "o")
					.replaceAll("ú", "u").replaceAll("á", "a").toLowerCase());
		} else {
			translation = translation.replaceAll("[^а-яА-Я]", "").toLowerCase();
			return translation.equals(text.replaceAll("[^а-яА-Я]", "")
					.toLowerCase());
		}

	}

	public static void load(Context context) {
		entries.clear();
		pronouns.clear();

		String mode = "";

		try {
			XmlPullParser xpp = context.getResources().getXml(R.xml.dictionary);

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
								setTranslation(xpp.getAttributeValue(i));
							}
						}
					} else if (xpp.getName().equals("pronoun")) {
						mode = "pronoun";
					} else if (xpp.getName().equals("entry")
							&& mode.equals("pronoun")) {
						for (int i = 0; i < xpp.getAttributeCount(); i++) {
							if (xpp.getAttributeName(i).equals("text")) {
								addPronoun(xpp.getAttributeValue(i));
							} else if (xpp.getAttributeName(i).equals("transl")) {
								setProniunTranslation(xpp.getAttributeValue(i));
							} else if (xpp.getAttributeName(i).equals(
									"verb_ending")) {
								setVerbEnding(xpp.getAttributeValue(i));
							}
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

}
