package com.alexmochalov.dictionary;

public class Pronoun {
	String mText;
	String translation;
	String verb_ending;

	public String conj(String verb) {

		// split "verb_ending" to pairs: type and ending  
		String[] sep1 = verb_ending.split(",");
		for (int i = 0; i < sep1.length; i++){

			// split pairs: first is type, second is ending
			String[] sep2 = sep1[i].split(":");
			//Log.d("",verb.substring(verb.length() - 2)+ "  "+sep2[0]);

			if (verb.equals("ser")){
				if (verb.equals(sep2[0]))
					return sep2[1];
			} else 
			if (verb.equals("estar")){
				if (verb.equals(sep2[0]))
					return sep2[1];
			} else 
			{
				int ending_length = sep2[0].length();
				if (verb.substring(verb.length() - ending_length).equals(sep2[0]))
					return 
						(verb.substring(0, verb.length() - ending_length) + sep2[1]).
						replace("ii", "i"). // mangiare [iamo]
						replace("ci", "chi"); // giocare [chiamo]

			}
		}
		return "";
	}

	public CharSequence getText() {
		return mText;
	}

	public CharSequence getTranslation() {
		return translation;
	}

	public static String firstLetterToUpperCase(String translation) {
		return translation.substring(0,1).toUpperCase() + translation.substring(1);
	}


	public CharSequence getTranslation(boolean UpperFirst) {
		if (UpperFirst)
			return firstLetterToUpperCase(translation);
		else	
			return translation;
	}
}
