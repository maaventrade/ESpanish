package com.alexmochalov.io;
import android.content.*;
import android.util.*;
import android.widget.Toast;

import com.alexmochalov.alang.*;
import com.alexmochalov.menu.*;
import com.alexmochalov.main.*;
import com.alexmochalov.rules.*;

import java.io.*;
import java.util.ArrayList;

import org.xmlpull.v1.*;

public class FilesIO
{

	public static void loadMenu(Context mContext, boolean reRead) {

		String mode = "";
		MenuGroup menuGroupItem = null;
    
		class Record {
			String text = "";
			String neg = "";
			String verbs = "";
			String subj = "";    
			String tense="";   
			public void clear() {
				text = "";
				neg = "";
				verbs = "";
				subj = "";
				tense="";
			} 
		}  
		Record rec = new Record();
		MarkedString markedStringLast = null;
		
		try {
			XmlPullParser xpp = null;
			File file = new File(Utils.APP_FOLDER + "/menu_it.xml");
			if (!file.exists() || reRead){
				// Load menu from resource 
				if (Utils.getLanguage().equals("ita")) 
					xpp = mContext.getResources().getXml(R.xml.menu_it);
				else if (Utils.getLanguage().equals("spa"))
					xpp = mContext.getResources().getXml(R.xml.menu_spa);
			} else {
				// Load menu from file
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
																					 Utils.APP_FOLDER + "/menu_it.xml")));
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
				factory.setNamespaceAware(true);         
				xpp = factory.newPullParser();

				xpp.setInput(reader);
			}
			
			/*
			if (Utils.getLanguage().equals("ita")) 
				xpp = mContext.getResources().getXml(R.xml.menu_it);
			else if (Utils.getLanguage().equals("spa"))
				xpp = mContext.getResources().getXml(R.xml.menu_spa);
			*/	
			
			while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {

				switch (xpp.getEventType()) { // начало документа
					case XmlPullParser.START_DOCUMENT:
						break; // начало тэга
					case XmlPullParser.START_TAG:
						//Log.d("d", "start "+xpp.getName());
						if (xpp.getName().equals("level0")) {
							//textData.add(new ArrayList<ArrayList<MarkedString>>());

							// Добавляем группу в меню
							menuGroupItem = MenuData.newMenuGroup();
							

							for (int i = 0; i < xpp.getAttributeCount(); i++) {
								if (xpp.getAttributeName(i).equals("title")) {
									menuGroupItem.setTitle(xpp.getAttributeValue(i));
								}
							}

						} else if (xpp.getName().equals("level1")) {
							// Child element
String ttt = "";
							for (int i = 0; i < xpp.getAttributeCount(); i++) {
								if (xpp.getAttributeName(i).equals("title")) {
									menuGroupItem.addItem(xpp.getAttributeValue(i));
									ttt = xpp.getAttributeValue(i);
								} else if (xpp.getAttributeName(i).equals("helpindex")) {
									menuGroupItem.setChildHelpIndex(xpp.getAttributeValue(i));
								} else if (xpp.getAttributeName(i).equals("tense")) {
									menuGroupItem.setTense(xpp.getAttributeValue(i));
								} else if (xpp.getAttributeName(i).equals("neg")) {
									menuGroupItem.setNeg(xpp.getAttributeValue(i));
								} else if (xpp.getAttributeName(i).equals("type")) {
									if (xpp.getAttributeValue(i)
										.equals("Выражения")) {
										mode = "Выражения";
										menuGroupItem.setChildType(mode);
									} else if (xpp.getAttributeValue(i).equals(
												   "Спряжения")) {
										mode = "Спряжения";
										menuGroupItem.setChildType(mode);
									} else if (xpp.getAttributeValue(i).equals(
												   "Комбинации")) {
										mode = "Комбинации";
										menuGroupItem.setChildType("Комбинации");
									} else if (xpp.getAttributeValue(i).equals(
												   "Комбинации1")) {
										mode = "Комбинации1";
										menuGroupItem.setChildType("Комбинации1");
									} else if (xpp.getAttributeValue(i).equals(
												   "Говорим")) {
										menuGroupItem.setChildType("Говорим");
									} else if (xpp.getAttributeValue(i).equals(
												   "Запоминание")) {
										menuGroupItem.setChildType("Запоминание");
									}
								} else if (xpp.getAttributeName(i).equals("note")) {
									menuGroupItem
										.setChildNote(xpp.getAttributeValue(i));
								}
							}
							// Level1
							//textData.get(textData.size() - 1).add(
							//new ArrayList<MarkedString>());
							Log.d("d","mode = Комбинации "+ttt);
						} else if (xpp.getName().equals("entry")) {
							//ArrayList<ArrayList<MarkedString>> textDataLast
							//= textData.get(textData.size() - 1);


							markedStringLast = new MarkedString("");

							for (int i = 0; i < xpp.getAttributeCount(); i++) {
								//Log.d("d", "start "+xpp.getAttributeName(i));
								if (xpp.getAttributeName(i).equals("text") && mode.equals("Комбинации")) {
									rec.text = xpp
										.getAttributeValue(i);
								} else if (xpp.getAttributeName(i).equals("text")) {
									markedStringLast.setText(xpp.getAttributeValue(i));
								} else if (xpp.getAttributeName(i).equals("flag")) {
									markedStringLast.setFlag(xpp.getAttributeValue(i));
								} else if (xpp.getAttributeName(i).equals("subj")) {
									rec.subj = xpp.getAttributeValue(i);
								} else if (xpp.getAttributeName(i).equals("neg")) {
									rec.neg = xpp
										.getAttributeValue(i);
								} else if (xpp.getAttributeName(i).equals("verbs")) {
									rec.verbs = xpp
										.getAttributeValue(i);
								} else if (xpp.getAttributeName(i).equals("rus")) {
									markedStringLast.setRusText(
										xpp.getAttributeValue(i));
								}
							}
						
						}
						break; // конец тэга
					case XmlPullParser.END_TAG:
						//Log.d("d", "end "+xpp.getName());
						if (rec.verbs == null || rec.verbs.equals("")){
							if (xpp.getName().equals("entry")){
						//if (xpp.getName().equals("entry") && 
							//! mode.contains("Комбинации")){
							//Log.d("my","Add ");
							MenuData.addMarkedString(markedStringLast);
							}
						}
						else if (xpp.getName().equals("entry") && 
							mode.contains("Комбинации")){
								
							//markedStringLast.setVerbs(rec.neg, rec.verbs);
							//if (1==1) break;
					   //Log.d("d","Комбинации 1 ");
							
							if (rec.verbs == null || rec.verbs.equals(""))
								break;
							
							 String verbs[] = rec.verbs.split(",");
							 //Log.d("","textDataItem "+textDataItem.toString());
							// Log.d("",""+verbs.toString());

							 for (String verb : verbs){
							 //Log.d("",rec.toString());
							 //Log.d("d", "verbs "+rec.verbs);
							 
							
							 if (mode.equals("Комбинации"))
								MenuData.addMarkedString(rec.text, rec.subj, rec.neg, verb.trim());
							 else{
								 for (Pronoun p: Rules.getPronouns()){
									 Log.d("io",p.getTranslation());
									 if (!p.getTranslation().equals("Вы(вежл.)")){
										 Log.d("io", "added");
										 MenuData.addMarkedString(rec.neg, p, verb.trim());
									 }
										 
								}
							 } 
							 }
							 
							rec.clear();
						}
						// Log.d(LOG_TAG, "END_TAG: name = " + xpp.getName());
						break; // содержимое тэга
					case XmlPullParser.TEXT:
						//Log.d("d", "text ");
						if (mode.equals("Выражения") || mode.equals("Спряжения")) {

			
						}

						break;
					default:
						break;
				} // следующий элемент
				xpp.next();
			}

		} catch (XmlPullParserException e) {
			Log.d("d",e.toString());

		} catch (IOException e) {
			Log.d("d",e.toString());
		} catch (Exception e) {
			Log.d("d",e.toString());
		}
		
		for (MarkedString m : MenuData.getMarkedStrings(3, 1) )
			Log.d("m","m0 "+m.getText());
		//  " "+(menuGroupLast.menuChild.size()-1));
	}
	

	public static void saveMenu(Context mContext)
	{
		//if (1==1) return;
	File file = new File(Utils.APP_FOLDER+"/menu_it.xml");
		try
		{
			FileOutputStream fos = new FileOutputStream(file, false);
		
		OutputStreamWriter os = new OutputStreamWriter(fos);

		//outputStreamWriter.write(name + " "+ text.replace("\n", ";")+"\n");
		//outputStreamWriter.close();
		
		os.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		os.write( "<data version = \"1\">\n");
			
		for (MarkedString m : MenuData.getMarkedStrings(3, 1) )
				Log.d("m","m2 "+m.getText());
				
		for (int i = 0; i < MenuData.getGroupsSize(); i++){
			os.write( "<level0 title = \""+MenuData.getGroupTitle(i)+"\">\n");
			for (MenuChild m: MenuData.getMenuCildren(i)){
				os.write( "<level1 ");
				
				if (!m.getTitleStr().equals(""))
					os.write( " title = \""+ m.getTitleStr()+"\"");
				
				if (!m.getType().equals(""))
					os.write( " type = \""+ m.getType()+"\"");
					
				if (!(""+m.getHelpIndex()).equals(""))
					os.write( " helpindex = \""+ m.getHelpIndex()+"\"");
				

				if (!(""+m.getNote()).equals(""))
					os.write( " note = \""+ m.getNote()+"\"");
				
					
				if (!m.getTense().equals(""))
					os.write( " tense = \""+ m.getTense()+"\"");
				
				if (!m.getNeg().equals(""))
					os.write( " neg = \""+ m.getNeg()+"\"");
				
				os.write( ">\n");
					
				//Log.d("d", menuData.get(i).getMChildren().get(j).getTitleStr());
				//Log.d("d", "textData.get(i).get(j) "+textData.get(i).get(j).size());
				int j = MenuData.getMenuCildren(i).indexOf(m);
				for (MarkedString s: MenuData.getMarkedStrings(i, j)){
					//Log.d("d", s.getVerbs());
						
					if (i==3 && j == 1)
						Log.d("m","m222 "+s.getText());
					
						os.write( "<entry");
						if (!s.getText().equals(""))
							os.write( " text = \""+ s.getText()+"\"");
						if (!s.getNeg().equals(""))
							os.write( " neg = \""+ s.getNeg()+"\"");
						//if (!s.getVerbs().equals(""))
							//os.write( " verbs = \""+ s.getVerbs()+"\"");
						if (!s.getRusText().equals(""))
							os.write( " rus = \""+ s.getRusText()+"\"");
						
						os.write( " flag = \""+ s.getFlag()+"\"");
					
					os.write( "></entry>\n");
				}
				
				os.write( "</level1>\n");
			}
			os.write( "</level0>\n");
		}
	
			
		os.write( "</data>");
		
		os.close();
		}
		catch (FileNotFoundException e)
		{
			Toast.makeText(mContext, "Error "+e, Toast.LENGTH_LONG).show();
		}
		catch (IOException e)
		{
			Toast.makeText(mContext, "Error "+e, Toast.LENGTH_LONG).show();
		}
	}
}
