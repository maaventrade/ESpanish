package com.alexmochalov.conj;

import java.util.ArrayList;

public class ConjRus {

	static class CR {
		public CR(String m, String e[]) {
			mainEnding = m;
			endings = e.clone();
		}
		
		String mainEnding;
		String endings[];
	}
	   
	private static ArrayList<CR> conjRus = new ArrayList<CR>();
	
		public static String get(String translation, int index) {
			for (CR cr: conjRus){
				if (translation.endsWith(cr.mainEnding)){
					return translation.substring(0, 
							translation.indexOf(cr.mainEnding)) + 
						cr.endings[index];
				}
			}
			
			return "";
		}
		
		public static void init() {
			String s[] = {"М","ШЬ","СТ","СТ","ДИТЕ","ДИМ","ДИТЕ","ДЯТ"};
			conjRus.add(new CR("СТЬ", s));
		}

}
