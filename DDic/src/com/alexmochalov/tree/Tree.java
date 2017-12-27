package com.alexmochalov.tree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
	
	private static ArrayList<Header> listDataHeader = new ArrayList<Header>();
	private static HashMap<Header, List<Child>> listDataChild = new HashMap<Header, List<Child>>();

	public static void clear() {
		listDataHeader = new ArrayList<Header>();
		listDataChild = new HashMap<Header, List<Child>>();
	}

	public static ArrayList<Header> getGroups() {
		return listDataHeader;
	}

	public static HashMap<Header, List<Child>> getChilds() {
		return listDataChild;
	}

	public static void addGroup(int index) {
		if (index < 0)
			index = 0;

		for (int i = 1; i < 999999; i++){
			if (!listDataHeader.contains("New"+i)){
				Header h = new Header("New"+i);
				listDataHeader.add(index,  h);
				listDataChild.put(h, new ArrayList<Child>());
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

			for (Header s : listDataHeader) {

				writer.write("<group name=\"" + s.getName() + "\"" + ">" + "\n");

				if (listDataChild.get(s) != null) {
					for (Child l : listDataChild.get(s)) {
						writer.write("<record name=\"" + l.getName() + "\""
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

		Header currentGroup = null;

		File file = new File(Utils.getAppFolder() + "/" + fileName);

		if (!file.exists()) {
			Header h = new Header("New1");
			listDataHeader.add(h);
			listDataChild.put(h, new ArrayList<Child>());
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
						currentGroup = new Header(parser.getAttributeValue(null, "name"));

						listDataHeader.add(currentGroup);
						listDataChild.put(currentGroup, new ArrayList<Child>());

					} else if (parser.getName().equals("record")) {

						String name = parser.getAttributeValue(null, "name");

						listDataChild.get(currentGroup).add(new Child(name));

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
			Header h = new Header("New1");
			listDataHeader.add(h);
			listDataChild.put(h, new ArrayList<Child>());
		}

		return true;

		// listDataHeader.add("Root");
		// listDataChild.put("Root", new ArrayList<IndexEntry>() );
	}

	public static void addItem(String key) {
		//listDataChild.get(key).add(Dictionary.find("Head"));	
	}

	public static String getName(int selectedGroupIndex,
			int selectedItemIndex) {
		if (selectedItemIndex == -1)
			return listDataHeader.get(selectedGroupIndex).getName(); 
		else
			return listDataChild.get(listDataHeader.get(selectedGroupIndex)).get(selectedItemIndex).getName(); 
			
	}

	public static void setName(int selectedGroupIndex, int selectedItemIndex,
			String name) {
			listDataHeader.get(selectedGroupIndex).setName(name); 
//		if (selectedItemIndex == -1)
//			listDataHeader. .put(selectedGroupIndex, string); 
//		else
//			return listDataChild.get(listDataHeader.get(selectedGroupIndex)).get(selectedItemIndex).getText(); 
	}

}
