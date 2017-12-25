package com.alexmochalov.tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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

import com.alexmochalov.dic.IndexEntry;
import com.alexmochalov.main.Utils;

import android.widget.Toast;


public class Tree {

	private static ArrayList<String> listDataHeader = new ArrayList<String>();
	private static HashMap<String, List<IndexEntry>> listDataChild = new HashMap<String, List<IndexEntry>>();

	public static void clear() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<IndexEntry>>();
	}
	
	public static void readFilesList() {
		listDataHeader.clear();
		listDataChild.clear();

		String currentGroup = null;
		
		File file = new File(Utils.getAppFolder());
		if(!file.exists()){                          
			file.mkdirs();                  
		}

		listDataHeader.add("Root");
		listDataChild.put("Root", new ArrayList<IndexEntry>() );
		
		/*
		File[] files = file.listFiles();
		
		for (int i = 0; i < files.length; i++)
		{
			String name = files[i].getName();
			
			if (files[i].isDirectory()){
				currentGroup = new PFile(files[i]);
				listDataHeader.add(currentGroup);
				listDataChild.put(currentGroup, new ArrayList<PFile>() );
				
				File[] filesSubdir = files[i].listFiles();
				for (int i1 = 0; i1 < filesSubdir.length; i1++)
				{
					name = filesSubdir[i1].getName();
					
					if (filesSubdir[i1].isDirectory()){
					} else {
						if (name.toLowerCase().endsWith(".xml")
								&& Programm.isProgramm(filesSubdir[i1])
								){
								listDataChild.get(currentGroup).add(new PFile(filesSubdir[i1]));
							}
					}
				}
				currentGroup = null;
				
			} else {
				if (name.toLowerCase().endsWith(".xml")
						&& Programm.isProgramm(files[i])
						){
						Date date  =  new Date(files[i].lastModified());
						if (date.compareTo(date0) > 0)
							date0 = date;
						listDataHeader.add(new PFile(files[i]));
						listDataChild.put(currentGroup, new ArrayList<PFile>() );
					}
			}
		}
		
		
		sort(listDataHeader);
		*/	
	}	
	
	public static ArrayList<String> getGroups() {
		return listDataHeader;
	}

	public static HashMap<String, List<IndexEntry>> getChilds() {
		return listDataChild;
	}

	public static void addGroup(int index) {
		if (index < 0) index = 0;
		
		listDataHeader.add(index, "New"); 
		listDataChild.put("New", new ArrayList<IndexEntry>());
	}
	
	
/*
	public static int getIndex(PFile newRecord) {
		int index = -1;

		for (PFile r : listDataHeader) {
			index++;
			if (r == newRecord)
				return index;

			List<PFile> l = listDataChild.get(r);
			if (l != null) {
				for (PFile p : l) {
					index++;
					if (r == p)
						return index;
				}
			}

		}

		return index;
	}


	public static ArrayList<PFile> getList() {

		ArrayList<PFile> list = new ArrayList<PFile>();

		for (PFile r : listDataHeader) {
			boolean child = false;
			List<PFile> l = listDataChild.get(r);
			if (l != null) {
				if (l.size() == 0)
					list.add(r);
				else {
					for (PFile p : l)
						list.add(p);
				}
			} else {
				list.add(r);
			}
		}

		return list;
	}


	public static PFile getGroup(int index) {
		return listDataHeader.get(index);
	}

	public static PFile getItem(int groupPosition, int childPosition) {
		if (groupPosition == -1)
			return listDataHeader.get(childPosition);
		else {
			PFile group = listDataHeader.get(groupPosition);
			return listDataChild.get(group).get(childPosition);
		}
	}

	public static void deleteRecord(PFile selectedRecord) {
		PFile parent = null;

		listDataHeader.remove(selectedRecord);

		if (listDataChild.get(selectedRecord) != null)
			listDataChild.remove(selectedRecord);
		else {
			for (Entry<PFile, List<PFile>> entry : listDataChild.entrySet())
				for (PFile r : entry.getValue())
					if (r == selectedRecord) {
						parent = entry.getKey();
						entry.getValue().remove(r);
						break;
					}

		}
	}

	public static PFile addChildRecord(PFile selectedRecord, File file) {
		PFile pfile = new PFile(file);

		if (listDataChild.get(selectedRecord) == null) {
			ArrayList<PFile> newArray = new ArrayList<PFile>();
			newArray.add(pfile);
			listDataChild.put(selectedRecord, newArray);
		} else {
			listDataChild.get(selectedRecord).add(pfile);
		}

		return null;
	}

	*/
	
	/*
	public static PFile addRecord(PFile newRecord, String currentRecord) {
		if (newRecord == null)
			newRecord = new PFile("new String", false);

		if (currentRecord == null)
			listDataHeader.add(newRecord);
		else if (listDataHeader.indexOf(currentRecord) >= 0) {
			if (currentRecord == null) {
				listDataHeader.add(newRecord);
			} else {
				listDataHeader.add(listDataHeader.indexOf(currentRecord) + 1,
						newRecord);
			}
		} else {
			for (Entry<PFile, List<PFile>> entry : listDataChild.entrySet())
				if (entry.getValue().contains(currentRecord))
					entry.getValue().add(
							entry.getValue().indexOf(currentRecord) + 1,
							newRecord);

		}

		return newRecord;
	}


	public static PFile getGroup(PFile String) {

		for (Entry<PFile, List<PFile>> entry : listDataChild.entrySet())
			if (entry.getValue().contains(String))
				return entry.getKey();

		return String;
	}

*/
/*
	public static PFile pasteRecord(PFile copyRecord, String selectedRecord) {
		return null;
	}

	private static Date date0 = new Date(0);

	public static Object getDateOfTheLastFale()
	{
		return date0;
	}


	static class PFComparator implements Comparator<PFile> {   
		public int compare(PFile fileA, PFile fileB) {
			if (fileA.isDirectory() && ! fileB.isDirectory())
				return -1;
			else if (!fileA.isDirectory() && fileB.isDirectory())
				return 1;
			else
			return fileA.getName().compareToIgnoreCase(fileB.getName());
		}
	}
	
	public static void sort(ArrayList<PFile> headers) {
		PFComparator fnc = new PFComparator();
        Collections.sort(headers, fnc);
	}
	*/
	
}
