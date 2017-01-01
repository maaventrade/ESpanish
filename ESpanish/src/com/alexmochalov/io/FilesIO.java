package com.alexmochalov.io;
import android.util.*;
import com.alexmochalov.menu.*;
import com.alexmochalov.root.*;
import java.io.*;
import java.util.*;

public class FilesIO
{


	public static void saveMenu(ArrayList<MenuGroup> menuData, 
		ArrayList<ArrayList<ArrayList<MarkedString>>> textData,
		ArrayList<Scheme> schemes)
	{
		File file = new File(Utils.APP_FOLDER+"/menu_it.xml");
		try
		{
			FileOutputStream fos = new FileOutputStream(file, false);
		
		OutputStreamWriter os = new OutputStreamWriter(fos);

		//outputStreamWriter.write(name + " "+ text.replace("\n", ";")+"\n");
		//outputStreamWriter.close();
		
		os.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		os.write( "<data version = \"1\">\n");

		for (int i = 0; i < menuData.size(); i++){
			os.write( "<level0 title = \""+menuData.get(i).getTitle()+"\">\n");
			for (MenuChild m: menuData.get(i).getMChildren()){
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
				int j = menuData.get(i).getMChildren().indexOf(m);
				for (MarkedString s: textData.get(i).get(j)){
					//Log.d("d", s.getVerbs());
						
						os.write( "<entry");
						if (!s.getText().equals(""))
							os.write( " text = \""+ s.getText()+"\"");
						if (!s.getNeg().equals(""))
							os.write( " neg = \""+ s.getNeg()+"\"");
						if (!s.getVerbs().equals(""))
							os.write( " verbs = \""+ s.getVerbs()+"\"");
					
					os.write( "></entry>\n");
				}
				
				os.write( "</level1>\n");
			}
			os.write( "</level0>\n");
		}
	
			os.write( "<schemes>\n");
		for (Scheme s: schemes){
			os.write( "<scheme title = \""+s.getTitle()+"\"");
			os.write( "<text = \""+s.getText()+"\">\n");
			
			for (String str: s.getStrings())
				os.write( str.
							replace("<","&lt;").
				replace(">","&gt;")+"\n");
			
		}
		os.write( "</schemes>\n");
		os.write( "</data>");
		
		os.close();
		}
		catch (FileNotFoundException e)
		{}
		catch (IOException e)
		{}
	}
}
