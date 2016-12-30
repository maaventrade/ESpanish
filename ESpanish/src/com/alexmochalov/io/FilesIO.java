package com.alexmochalov.io;
import com.alexmochalov.menu.*;
import com.alexmochalov.root.*;
import java.io.*;
import java.util.*;

public class FilesIO
{


	public static void saveMenu(ArrayList<MenuData.MenuGroup> menuData, ArrayList<ArrayList<ArrayList<MarkedString>>> textData)
	{
		File file = new File(Utils.APP_FOLDER+"/params1.txt");
		FileOutputStream fos = new FileOutputStream(file, true);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);

		outputStreamWriter.write(name + " "+ text.replace("\n", ";")+"\n");
		outputStreamWriter.close();

		String listString = "";

		listString = listString + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		listString = listString + "<data version = \"1\">\n";

		for (int i = 0; i < menuData.size(); i++){
			for (int j = 0; j < menuData.get(i).mChildren.size(); j++){

				for (MarkedString s: textData.get(i).get(j)){

					listString = listString + "<mark group = \""+ i 
						+"\" child = \"" + j 
						+"\" index = \"" + textData.get(i).get(j).indexOf(s) 
						+ "\" value = \"" + s.mFlag  
						+ "\"></mark>\n"; 
				}
			}
		}

		listString = listString + "</data>";
		editor.putString("MARKS", listString);

		Utils.writeFile("MARKS", listString);
		
	}
}
