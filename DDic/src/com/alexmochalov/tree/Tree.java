package com.alexmochalov.tree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.alexmochalov.ddic.R;
import com.alexmochalov.dic.Dictionary;
import com.alexmochalov.dic.IndexEntry;
import com.alexmochalov.main.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Tree {

	private static boolean errorLoading = false; 

	private static ArrayList<LineGroup> listDataHeader = new ArrayList<LineGroup>();
	private static HashMap<LineGroup, List<LineItem>> listDataChild = new HashMap<LineGroup, List<LineItem>>();
	private static Line copyLine = null;

	public static String getGrouName(int selectedGroupIndex)
	{
		// TODO: Implement this method
		return null;
	}
/*
	public static void clearText()
	{
		for (Line s : listDataHeader) {
			if (listDataChild.get(s) != null) {
				for (Line l : listDataChild.get(s)) {
					l.setName("") ;
				}
			}
		}
	}
*/
	public static int paste(int selectedGroupIndex)
	{
		/*Line key = listDataHeader.get(selectedGroupIndex);
		Line newLine = new Line(copyLine);
		Boolean find = false;
		
		String name = copyLine.getNameEng();
		

		for (int i = 0; i < listDataChild.get(key).size(); i++){
			if (listDataChild.get(key).get(i).getText().compareTo(name) > 0){
				listDataChild.get(key).add(i, newLine);
				find = true;
				break;
			}}
		if (!find)
			listDataChild.get(key).add(newLine); 
		return listDataChild.get(key).indexOf(newLine);
		*/
		return 0;
	}
	
	
	public static void copyItem(int selectedGroupIndex, int selectedItemIndex)
	{
		/*if (selectedGroupIndex >= 0 && selectedItemIndex >= 0){
			
			copyLine = new Line( listDataChild.get(listDataHeader.get(selectedGroupIndex)).get(selectedItemIndex).getText(),
								listDataChild.get(listDataHeader.get(selectedGroupIndex)).get(selectedItemIndex).getTranslation());
		}		
		*/
	}

	public static String getTranslation(int selectedGroupIndex, int selectedItemIndex)
	{
		if (selectedGroupIndex < 0) return "";
		else return listDataChild.get(listDataHeader.get(selectedGroupIndex)).get(selectedItemIndex).getTranslation();
	}

	public static void clear() {
		listDataHeader.clear(); // = new ArrayList<Line>();
		listDataChild.clear();  // = new HashMap<Line, List<Line>>();
	}

	public static ArrayList<LineGroup> getGroups() {
		return listDataHeader;
	}

	public static HashMap<LineGroup, List<LineItem>> getChilds() {
		return listDataChild;
	}

	public static void addGroup(int index, String name) {
		if (index < 0)
			index = 0;
/*
		Line h = new Line(name);
		listDataHeader.add(index,  h);
		listDataChild.put(h, new ArrayList<Line>());
*/
	}

	public static File save(Context mContext, String fileName) {

		if (errorLoading) return null;

		File file = new File(Utils.getAppFolder());
		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(Utils.getAppFolder(), fileName);
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(
												   new FileOutputStream(file), "UTF-8"));

			writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n");
			writer.write("<body type = \"tree\">" + "\n");

			for (LineGroup s : listDataHeader) {
				writer.write("<group name=\"" + s.getName() + "\"" + ">" + "\n");

				if (listDataChild.get(s) != null) {
					for (LineItem l : listDataChild.get(s)) {
						writer.write("<record nameEng=\"" + l.getNameEng() + "\""
								 + " nameIt=\"" + l.getNameIt() + "\""
								 + " transl=\"" + l.getTranslation() + "\""
									 + ">\n");
						writer.write("</record>" + "\n");
					}
				}
				writer.write("</group>" + "\n");
			}

			writer.write("</body>" + "\n");
			writer.close();

			// Toast.makeText(mContext,
			// mContext.getResources().getString(R.string.file_saved)+" "+fileName,
			// Toast.LENGTH_LONG)
			// .show();
		} catch (IOException e) {
			// Utils.setInformation(context.getResources().getString(R.string.error_save_file)+" "+e);
			Toast.makeText(
				mContext,
				mContext.getResources().getString(
					R.string.error_saving_file)
				+ " " + e, Toast.LENGTH_LONG).show();
			return null;
		}
		return file;
	}

	public static boolean loadXML(Activity mContext, String fileName) {
		clear();

		LineGroup currentGroup = null;

		File file = new File(Utils.getAppFolder() + "/" + fileName);

		if (!file.exists()) {
			/*Line h = new Line("New1");
			listDataHeader.add(h);
			listDataChild.put(h, new ArrayList<Line>());*/
			return true;
		}

		try {

			fileName = Utils.getAppFolder() + "/" + fileName;

			BufferedReader reader;
			BufferedReader rd = new BufferedReader(new InputStreamReader(
													   new FileInputStream(fileName)));

			String line = rd.readLine();

			rd.close();

			if (line.toLowerCase().contains("windows-1251"))
				reader = new BufferedReader(new InputStreamReader(
												new FileInputStream(fileName), "windows-1251")); // Cp1252
			else if (line.toLowerCase().contains("utf-8"))
				reader = new BufferedReader(new InputStreamReader(
												new FileInputStream(fileName), "UTF-8"));
			else if (line.toLowerCase().contains("utf-16"))
				reader = new BufferedReader(new InputStreamReader(
												new FileInputStream(fileName), "utf-16"));
			else
				reader = new BufferedReader(new InputStreamReader(
												new FileInputStream(fileName)));

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();

			parser.setInput(reader);

			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				// Log.d("a", ""+eventType);
				if (eventType == XmlPullParser.START_DOCUMENT)
					;
				else if (eventType == XmlPullParser.END_TAG)
					;
				else if (eventType == XmlPullParser.START_TAG) {

					if (parser.getName() == null)
						;
					else if (parser.getName().equals("group")) {
						currentGroup = new LineGroup(parser.getAttributeValue(null, "name"));

						listDataHeader.add(currentGroup);
						listDataChild.put(currentGroup, new ArrayList<LineItem>());

					} else if (parser.getName().equals("record")) {

						String nameEng = parser.getAttributeValue(null, "nameEng");
						String nameIt = parser.getAttributeValue(null, "nameIt");
						
						String transl = parser.getAttributeValue(null, "transl");
						if (transl == null || transl.equals("null"))
							transl = "";
						
						listDataChild.get(currentGroup).add(new LineItem(nameEng, nameIt, transl));

					}
				}
				try {
					eventType = parser.next();
				} catch (XmlPullParserException e) {
					errorLoading = true;
					Toast.makeText(
						mContext,
						mContext.getResources().getString(
							R.string.error_load_xml)
						+ ". !!! " + e.toString(), Toast.LENGTH_LONG)
						.show();
					return false;
				}
			}

		} catch (Throwable t) {
			errorLoading = true;
			Toast.makeText(
				mContext,
				mContext.getResources().getString(R.string.error_load_xml)
				+ ". ???  " + t.toString(), Toast.LENGTH_LONG).show();
			return false;
		}

		if (listDataHeader.size() == 0) {
			LineGroup h = new LineGroup("New1");
			listDataHeader.add(h);
			listDataChild.put(h, new ArrayList<LineItem>());
		}

		return true;

		// listDataHeader.add("Root");
		// listDataChild.put("Root", new ArrayList<IndexEntry>() );
	}

	
	
	
	public static int addItem(int selectedGroupIndex, String name) {
		listDataChild.get(listDataHeader.get(selectedGroupIndex)).add(new LineItem(name)); 
		return listDataChild.get(listDataHeader.get(selectedGroupIndex)).size()-1;
	}

	public static int insertItem(int selectedGroupIndex, String name, String translation) {
		LineGroup key = listDataHeader.get(selectedGroupIndex);
		LineItem newLine;

		if (Utils.isEnglish())
			newLine = new LineItem(name, "", translation);
		else
			newLine = new LineItem("", name , translation);
		
		Boolean find = false;
		
		for (int i = 0; i < listDataChild.get(key).size(); i++){
			if (listDataChild.get(key).get(i).getTranslation().compareTo(translation) > 0){
				listDataChild.get(key).add(i, newLine);
				find = true;
				break;
		}}
		if (!find)
			listDataChild.get(key).add(newLine); 
		return listDataChild.get(key).indexOf(newLine);
	}
	
	public static String getName(int selectedGroupIndex,
								 int selectedItemIndex) {
		//group = -1
		if (selectedGroupIndex == -1) 
			return "";
		else if (selectedItemIndex == -1)
			return listDataHeader.get(selectedGroupIndex).getName(); 
		else
			return listDataChild.get(listDataHeader.get(selectedGroupIndex)).get(selectedItemIndex).getText(); 

	}
	
	public static Line getLine(int selectedGroupIndex,
								 int selectedItemIndex) {
		if (selectedItemIndex == -1)
			return listDataHeader.get(selectedGroupIndex); 
		else
			return listDataChild.get(listDataHeader.get(selectedGroupIndex)).get(selectedItemIndex); 

	}

	public static void setName(int selectedGroupIndex, int selectedItemIndex,
							   String name) {

		listDataChild.get(listDataHeader.get(selectedGroupIndex));

		if (selectedItemIndex == -1)
			listDataHeader.get(selectedGroupIndex).setName(name); 
		else
			listDataChild.get(listDataHeader.get(selectedGroupIndex)).get(selectedItemIndex).setName1(name); 
	}

	public static void copy(Activity mContext, String from, String to) {
		InputStream in = null;
	    OutputStream out = null;
	    try {

	        in = new FileInputStream(Utils.getAppFolder() + "/" +  from);        
	        out = new FileOutputStream(Utils.getAppFolder() + "/" +  to);

	        byte[] buffer = new byte[1024];
	        int read;
	        while ((read = in.read(buffer)) != -1) {
	            out.write(buffer, 0, read);
	        }
	        in.close();
	        in = null;

			// write the output file (You have now copied the file)
			out.flush();
	        out.close();
	        out = null;        

	    }  catch (FileNotFoundException fnfe1) {
	        Log.e("tag", fnfe1.getMessage());
	    }
		catch (Exception e) {
	        Log.e("tag", e.getMessage());
	    }
	}

	public static boolean delete(int selectedGroupIndex, int selectedItemIndex) {
		if (selectedGroupIndex >= 0 && selectedItemIndex >= 0){
			listDataChild.get(listDataHeader.get(selectedGroupIndex)).remove(selectedItemIndex);
		} else if (selectedGroupIndex >= 0){
			if (listDataChild.get(listDataHeader.get(selectedGroupIndex)).size() == 0){
				listDataChild.remove(listDataHeader.get(selectedGroupIndex));
				listDataHeader.remove(selectedGroupIndex);
			} else {
				return false;
			}
				
		}
		return true;
	}

	public static List<LineItem> getItems(int selectedGroupIndex){
		return listDataChild.get(listDataHeader.get(selectedGroupIndex));
}

	public int get1(){
		return 1;
	}

	public static void setTranslation(int selectedGroupIndex,
			int selectedItemIndex, String translation) {
		
		listDataChild.get(listDataHeader.get(selectedGroupIndex));

		if (selectedItemIndex == -1)
			listDataHeader.get(selectedGroupIndex).setName(translation); 
		else
			listDataChild.get(listDataHeader.get(selectedGroupIndex)).get(selectedItemIndex).setTranslation(translation); 
	}
	public static LineItem getSelectedItem(int selectedGroupIndex,
			int selectedItemIndex) {
		
		if (selectedItemIndex == -1)
			return null; 
		else {
			int i = 1;
				return listDataChild.get(listDataHeader.get(selectedGroupIndex)).get(selectedItemIndex);
			}
	}
}
