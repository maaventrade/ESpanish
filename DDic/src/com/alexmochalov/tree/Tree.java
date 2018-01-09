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

	private static ArrayList<Line> listDataHeader = new ArrayList<Line>();
	private static HashMap<Line, List<Line>> listDataChild = new HashMap<Line, List<Line>>();

	public static String getTranslation(int selectedGroupIndex, int selectedItemIndex)
	{
		return listDataChild.get(listDataHeader.get(selectedGroupIndex)).get(selectedItemIndex).getTranslation();
	}

	public static void clear() {
		listDataHeader = new ArrayList<Line>();
		listDataChild = new HashMap<Line, List<Line>>();
	}

	public static ArrayList<Line> getGroups() {
		return listDataHeader;
	}

	public static HashMap<Line, List<Line>> getChilds() {
		return listDataChild;
	}

	public static void addGroup(int index) {
		if (index < 0)
			index = 0;

		for (int i = 1; i < 999999; i++){
			if (!listDataHeader.contains("New"+i)){
				Line h = new Line("New"+i);
				listDataHeader.add(index,  h);
				listDataChild.put(h, new ArrayList<Line>());
				break;
			}
		}

	}

	/*
	 * public static int getIndex(PFile newRecord) { int index = -1;
	 * 
	 * for (PFile r : listDataHeader) { index++; if (r == newRecord) return
	 * index;
	 * 
	 * List<PFile> l = listDataChild.get(r); if (l != null) { for (PFile p : l)
	 * { index++; if (r == p) return index; } }
	 * 
	 * }
	 * 
	 * return index; }
	 * 
	 * 
	 * public static ArrayList<PFile> getList() {
	 * 
	 * ArrayList<PFile> list = new ArrayList<PFile>();
	 * 
	 * for (PFile r : listDataHeader) { boolean child = false; List<PFile> l =
	 * listDataChild.get(r); if (l != null) { if (l.size() == 0) list.add(r);
	 * else { for (PFile p : l) list.add(p); } } else { list.add(r); } }
	 * 
	 * return list; }
	 * 
	 * 
	 * public static PFile getGroup(int index) { return
	 * listDataHeader.get(index); }
	 * 
	 * public static PFile getItem(int groupPosition, int childPosition) { if
	 * (groupPosition == -1) return listDataHeader.get(childPosition); else {
	 * PFile group = listDataHeader.get(groupPosition); return
	 * listDataChild.get(group).get(childPosition); } }
	 * 
	 * public static void deleteRecord(PFile selectedRecord) { PFile parent =
	 * null;
	 * 
	 * listDataHeader.remove(selectedRecord);
	 * 
	 * if (listDataChild.get(selectedRecord) != null)
	 * listDataChild.remove(selectedRecord); else { for (Entry<PFile,
	 * List<PFile>> entry : listDataChild.entrySet()) for (PFile r :
	 * entry.getValue()) if (r == selectedRecord) { parent = entry.getKey();
	 * entry.getValue().remove(r); break; }
	 * 
	 * } }
	 * 
	 * public static PFile addChildRecord(PFile selectedRecord, File file) {
	 * PFile pfile = new PFile(file);
	 * 
	 * if (listDataChild.get(selectedRecord) == null) { ArrayList<PFile>
	 * newArray = new ArrayList<PFile>(); newArray.add(pfile);
	 * listDataChild.put(selectedRecord, newArray); } else {
	 * listDataChild.get(selectedRecord).add(pfile); }
	 * 
	 * return null; }
	 */

	/*
	 * public static PFile addRecord(PFile newRecord, String currentRecord) { if
	 * (newRecord == null) newRecord = new PFile("new String", false);
	 * 
	 * if (currentRecord == null) listDataHeader.add(newRecord); else if
	 * (listDataHeader.indexOf(currentRecord) >= 0) { if (currentRecord == null)
	 * { listDataHeader.add(newRecord); } else {
	 * listDataHeader.add(listDataHeader.indexOf(currentRecord) + 1, newRecord);
	 * } } else { for (Entry<PFile, List<PFile>> entry :
	 * listDataChild.entrySet()) if (entry.getValue().contains(currentRecord))
	 * entry.getValue().add( entry.getValue().indexOf(currentRecord) + 1,
	 * newRecord);
	 * 
	 * }
	 * 
	 * return newRecord; }
	 * 
	 * 
	 * public static PFile getGroup(PFile String) {
	 * 
	 * for (Entry<PFile, List<PFile>> entry : listDataChild.entrySet()) if
	 * (entry.getValue().contains(String)) return entry.getKey();
	 * 
	 * return String; }
	 */
	/*
	 * public static PFile pasteRecord(PFile copyRecord, String selectedRecord)
	 * { return null; }
	 * 
	 * private static Date date0 = new Date(0);
	 * 
	 * public static Object getDateOfTheLastFale() { return date0; }
	 * 
	 * 
	 * static class PFComparator implements Comparator<PFile> { public int
	 * compare(PFile fileA, PFile fileB) { if (fileA.isDirectory() && !
	 * fileB.isDirectory()) return -1; else if (!fileA.isDirectory() &&
	 * fileB.isDirectory()) return 1; else return
	 * fileA.getName().compareToIgnoreCase(fileB.getName()); } }
	 * 
	 * public static void sort(ArrayList<PFile> headers) { PFComparator fnc =
	 * new PFComparator(); Collections.sort(headers, fnc); }
	 */
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

			for (Line s : listDataHeader) {

				writer.write("<group name=\"" + s.getName() + "\"" + ">" + "\n");

				if (listDataChild.get(s) != null) {
					for (Line l : listDataChild.get(s)) {
						writer.write("<record name=\"" + l.getName() + "\""
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

		Line currentGroup = null;

		File file = new File(Utils.getAppFolder() + "/" + fileName);

		if (!file.exists()) {
			Line h = new Line("New1");
			listDataHeader.add(h);
			listDataChild.put(h, new ArrayList<Line>());
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
						currentGroup = new Line(parser.getAttributeValue(null, "name"));

						listDataHeader.add(currentGroup);
						listDataChild.put(currentGroup, new ArrayList<Line>());

					} else if (parser.getName().equals("record")) {

						String name = parser.getAttributeValue(null, "name");
						String transl = parser.getAttributeValue(null, "transl");
						if (transl == null || transl.equals("null"))
							transl = "";
						
						listDataChild.get(currentGroup).add(new Line(name, transl));

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
			Line h = new Line("New1");
			listDataHeader.add(h);
			listDataChild.put(h, new ArrayList<Line>());
		}

		return true;

		// listDataHeader.add("Root");
		// listDataChild.put("Root", new ArrayList<IndexEntry>() );
	}

	public static int addItem(int selectedGroupIndex, String name) {
		listDataChild.get(listDataHeader.get(selectedGroupIndex)).add(new Line(name)); 
		return listDataChild.get(listDataHeader.get(selectedGroupIndex)).size()-1;
	}

	public static int insertItem(int selectedGroupIndex, String name) {
		Line key = listDataHeader.get(selectedGroupIndex);
		Line newLine = new Line(name);
		Boolean find = false;
		int aa;
		
		for (int i = 0; i < listDataChild.get(key).size(); i++){
			aa =listDataChild.get(key).get(i).getText().compareTo(name);
			Log.d("a",""+aa+"  "+listDataChild.get(key).get(i).getText()+"  "+name);
		if (listDataChild.get(key).get(i).getText().compareTo(name) > 0){
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
		if (selectedItemIndex == -1)
			return listDataHeader.get(selectedGroupIndex).getName(); 
		else
			return listDataChild.get(listDataHeader.get(selectedGroupIndex)).get(selectedItemIndex).getName(); 

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
			listDataChild.get(listDataHeader.get(selectedGroupIndex)).get(selectedItemIndex).setName(name); 
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

	public static void delete(int selectedGroupIndex, int selectedItemIndex) {
		if (selectedGroupIndex >= 0 && selectedItemIndex >= 0){
			listDataChild.get(listDataHeader.get(selectedGroupIndex)).remove(selectedItemIndex);
		}		
	}
}
