package com.alexmochalov.conj;

import java.util.*;

public class ConjRus
{


	private static ArrayList<CR> conjRus = new ArrayList<CR>();

	public static String get(String translation, int index)
	{
		for (CR cr: conjRus)
			for (String me: cr.mainEnding)
				if (translation.endsWith(me)){
					String z = translation.substring(0, 
													 translation.indexOf(me)) + 
						cr.endings.get(index);
						
					if (z.endsWith("ТЮ"))
						z = z.substring(0, z.indexOf("ТЮ")) + "ЧУ";
						
						
					return z;
					
				}
					



		return "";
	}


	public static void init()
	{

		ArrayList<String> l = new ArrayList<String>();
		ArrayList<String> s = new ArrayList<String>();
		s.add("М");
		s.add("ШЬ");
		s.add("СТ");
		s.add("СТ");
		s.add("ДИТЕ");
		s.add("ДИМ");
		s.add("ДИТЕ");
		s.add("ДЯТ");

		l.add("СТЬ");
		conjRus.add(new CR(l, s));

		l.clear();
		s.clear();

		s.add("ДУ");
		s.add("ДЁШЬ");
		s.add("ДЁТ");
		s.add("ДЁТ");
		s.add("ДЁТЕ");
		s.add("ДЁМ");
		s.add("ДЁТЕ");
		s.add("ДУТ");

		l.add("СТИ");
		conjRus.add(new CR(l, s));

		l.clear();
		s.clear();

		s.add("УЮ");
		s.add("УЕШЬ");
		s.add("УЕТ");
		s.add("УЕТ");
		s.add("УЕТЕ");
		s.add("УЕМ");
		s.add("УЕТЕ");
		s.add("УЮТ");

		l.add("ОВАТЬ");
		conjRus.add(new CR(l, s));
		
		l.clear();
		s.clear();

		l.clear();
		s.clear();

		s.add("ЫВАЮ");
		s.add("ЫВАЕШЬ");
		s.add("ЫВАЕТ");
		s.add("ЫВАЕТ");
		s.add("ЫВАЕТЕ");
		s.add("ЫВАЕМ");
		s.add("ЫВАЕТЕ");
		s.add("ЫВАЮТ");

		l.add("ЫВАТЬ");
		conjRus.add(new CR(l, s));
		
		l.clear();
		s.clear();
		
		s.add("ИВАЮ");
		s.add("ИВАЕШЬ");
		s.add("ИВАЕТ");
		s.add("ИВАЕТ");
		s.add("ИВАЕТЕ");
		s.add("ИВАЕМ");
		s.add("ИВАЕТЕ");
		s.add("ИВАЮТ");

		l.add("ИВАТЬ");
		conjRus.add(new CR(l, s));
		
		
		l.clear();
		s.clear();

		s.add("ЫШУ");
		s.add("ЫШИШЬ");
		s.add("ЫШИТ");
		s.add("ЫШИТ");
		s.add("ЫШИТЕ");
		s.add("ЫШИМ");
		s.add("ЫШИТЕ");
		s.add("ЫШАТ");

		l.add("ЫШАТЬ");
		conjRus.add(new CR(l, s));
		
		l.clear();
		s.clear();
		
		s.add("Ю");
		s.add("ЁШЬ");
		s.add("ЁТ");
		s.add("ЁТ");
		s.add("ЁТЕ");
		s.add("ЁМ");
		s.add("ЁТЕ");
		s.add("ЮТ");

		l.add("ВАТЬ");
		conjRus.add(new CR(l, s));

		
		l.clear();
		s.clear();

		s.add("ЮСЬ");
		s.add("ЕШЬСЯ");
		s.add("ЕТСЯ");
		s.add("ЕТСЯ");
		s.add("ЕТЕСЬ");
		s.add("ЕМСЯ");
		s.add("ЕТЕСЬ");
		s.add("ЮТСЯ");

		l.add("ТЬСЯ");
		conjRus.add(new CR(l, s));
		
		
		l.clear();
		s.clear();

		s.add("ЖУ");
		s.add("ДИШЬ");
		s.add("ДИТ");
		s.add("ДИТ");
		s.add("ДИТЕ");
		s.add("ДИМ");
		s.add("ДИТЕ");
		s.add("ДЯТ");

		l.add("ДИТЬ");
		conjRus.add(new CR(l, s));
		
		l.clear();
		s.clear();

		s.add("ЖУ");
		s.add("ЖЕШЬ");
		s.add("ЖЕТ");
		s.add("ЖЕТ");
		s.add("ЖЕТЕ");
		s.add("ЖЕМ");
		s.add("ЖЕТЕ");
		s.add("ЖУТ");

		l.add("ЗАТЬ");
		conjRus.add(new CR(l, s));
		
		l.clear();
		s.clear();
		
		s.add("Ю");
		s.add("ИШЬ");
		s.add("ИТ");
		s.add("ИТ");
		s.add("ИТЕ");
		s.add("ИМ");
		s.add("ИТЕ");
		s.add("ЯТ");

		l.add("ИТЬ");
		conjRus.add(new CR(l, s));

		l.clear();
		s.clear();

		s.add("Ю");
		s.add("ЕШЬ");
		s.add("ЕТ");
		s.add("ЕТ");
		s.add("ЕТЕ");
		s.add("ЕМ");
		s.add("ЕТЕ");
		s.add("ЮТ");

		l.add("ТЬ");
		conjRus.add(new CR(l, s));

		l.clear();
		s.clear();
		
		

	}

}
