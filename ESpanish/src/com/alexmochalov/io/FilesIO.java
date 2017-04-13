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
    
		class RecordF {
			String text = "";
			String neg = "";
			String verbs = "";
			String subj = "";    
			String tense="";
			
			String rus = "";   
			String flag ="";   
			public void clear() {
				text = "";
				neg = "";
				verbs = "";
				subj = "";
				tense="";
				rus = "";   
				flag ="";   
			} 
		}  
		RecordF rec = new RecordF();
		//MarkedString markedStringLast = null;
		
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
							//Log.d("d","mode = Комбинации "+ttt);
						} else if (xpp.getName().equals("entry")) {
							rec.clear();
							//ArrayList<ArrayList<MarkedString>> textDataLast
							//= textData.get(textData.size() - 1);


							//markedStringLast = new MarkedString("");

							for (int i = 0; i < xpp.getAttributeCount(); i++) {
								//Log.d("d", "start "+xpp.getAttributeName(i));
								//if (xpp.getAttributeName(i).equals("text") && mode.equals("Комбинации")) {
								//	rec.text = xpp
								//		.getAttributeValue(i);
								if (xpp.getAttributeName(i).equals("text")) {
									rec.text = xpp.getAttributeValue(i);
								} else if (xpp.getAttributeName(i).equals("flag")) {
									rec.flag = xpp.getAttributeValue(i);
								} else if (xpp.getAttributeName(i).equals("subj")) {
									rec.subj = xpp.getAttributeValue(i);
								} else if (xpp.getAttributeName(i).equals("neg")) {
									rec.neg = xpp
										.getAttributeValue(i);
								} else if (xpp.getAttributeName(i).equals("verbs")) {
									rec.verbs = xpp
										.getAttributeValue(i);
								} else if (xpp.getAttributeName(i).equals("rus")) {
									rec.rus = xpp.getAttributeValue(i);
								}
							}
						
						}
						break; // конец тэга
					case XmlPullParser.END_TAG:
						if (xpp.getName().equals("entry") && 
							mode.contains("Комбинации"))
						{
							if (rec.verbs == null || rec.verbs.equals("")){
								MarkedString ms = MenuData.addMarkedString(rec.text, rec.rus);
								ms.setFlag(rec.flag);
							} else 
							{
								 String verbs[] = rec.verbs.split(",");

								 for (String verb : verbs){
									 if (mode.equals("Комбинации"))
										 MenuData.addMarkedString(rec.text, rec.subj, rec.neg, verb.trim());
									 else	
										 for (Pronoun p: Rules.getPronouns())
											 if (!p.getTranslation().equals("Вы(вежл.)"))
												 MenuData.addMarkedString(rec.neg, p, verb.trim());
								 }
							}	 
						} else if (xpp.getName().equals("entry")) {
							//Log.d("b",rec.text+" - "+rec.rus);
							MarkedString ms = MenuData.addMarkedString(rec.text, rec.rus);
							ms.setFlag(rec.flag);
						}
							
						rec.clear();
						break; // содержимое тэга
					case XmlPullParser.TEXT:
						if (mode.equals("Выражения") || mode.equals("Спряжения")) {
						}
						break;
					default:
						break;
				} // следующий элемент
				xpp.next();
			}

		} catch (XmlPullParserException e) {
			Toast.makeText(mContext,e.toString(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(mContext,e.toString(), Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(mContext,e.toString(), Toast.LENGTH_LONG).show();
		}
		/*
		for (int i = 0; i < MenuData.getGroupsSize(); i++){
			Log.d("", "<level0 title = \""+MenuData.getGroupTitle(i)+"\">\n");
			
			for (MenuChild m: MenuData.getMenuCildren(i)){
				Log.d("", "<level1 ");
				
				if (!m.getTitleStr().equals(""))
					Log.d("", " title = \""+ m.getTitleStr()+"\"");
				
				if (!m.getType().equals(""))
					Log.d("", " type = \""+ m.getType()+"\"");
					
				if (!(""+m.getHelpIndex()).equals(""))
					Log.d("", " helpindex = \""+ m.getHelpIndex()+"\"");
				

				if (!(""+m.getNote()).equals(""))
					Log.d("", " note = \""+ m.getNote()+"\"");
				
					
				if (!m.getTense().equals(""))
					Log.d("", " tense = \""+ m.getTense()+"\"");
				
				if (!m.getNeg().equals(""))
					Log.d("", " neg = \""+ m.getNeg()+"\"");
				
				Log.d("", ">\n");
					
				int j = MenuData.getMenuCildren(i).indexOf(m);
				for (MarkedString s: MenuData.getMarkedStrings(i, j)){
					Log.d("", "<entry");
						if (!s.getText().equals(""))
							Log.d("", " text = \""+ s.getText()+"\"");
						if (!s.getNeg().equals(""))
							Log.d("", " neg = \""+ s.getNeg()+"\"");
						//if (!s.getVerbs().equals(""))
							//os.write( " verbs = \""+ s.getVerbs()+"\"");
						if (!s.getRusText().equals(""))
							Log.d("", " rus = \""+ s.getRusText()+"\"");
						
						Log.d("", " flag = \""+ s.getFlag()+"\"");
					
						Log.d("", "></entry>\n");
				}
				
				Log.d("", "</level1>\n");
			}
			Log.d("", "</level0>\n");
		}
		*/
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
						
					//if (i==3 && j == 1)
						//Log.d("m","m222 "+s.getText());
					
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
