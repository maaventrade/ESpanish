package com.alexmochalov.dic;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.alexmochalov.ddic.R;
import com.alexmochalov.main.Utils;
import com.alexmochalov.tree.Line;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import android.text.*;


public final class Dictionary{

	private static Context mContext;
	private static ArrayList<IndexEntry> indexEntries = new ArrayList<IndexEntry>();

	static MyTaskLoading myTaskLoading;
	static MyTaskIndexing myTaskIndexing;

	static ProgressDialog progressDialog;
	static Boolean mIsLoaded = false;

	public static EventCallback eventCallback;

	private static String mDictionaryName;

	private static String info;

	public interface EventCallback { 
		void loadingFinishedCallBack(boolean result); 
		void indexingFinishedCallBack(String dictionary_name); 
	}

	public static void setParams(Context context) {
		mContext = context;
	}
	
	public static void loadIndex(String dictionary_name, boolean onResume) {
		mDictionaryName = dictionary_name;
		
		if (onResume && indexEntries.size() > 0){
			if (eventCallback != null)
				eventCallback.loadingFinishedCallBack(true);
			return;
		}

		File file = new File(Utils.getIndexPath());

		if(!file.exists()){ ///// ?? 
			if (eventCallback != null)
				eventCallback.loadingFinishedCallBack(file.exists());
			return;
		}
		
		Log.d("ab","Start Loading "+dictionary_name);
		
		
		loadIndexAsinc();
	}

	public static int getSize(){
		return indexEntries.size();
	}
	
	private static void loadIndexAsinc() {
		progressDialog = new ProgressDialog(mContext);
		progressDialog.setTitle("Loading dictionary...");
		progressDialog.setMessage(Utils.getIndexName());
		progressDialog.show();

		myTaskLoading = new MyTaskLoading();    
		myTaskLoading.execute(indexEntries);
	}

	static class MyTaskLoading extends AsyncTask<ArrayList<IndexEntry>, Integer, Void> {    
		@Override 
		protected void onPreExecute() {      
			super.onPreExecute();      
		    //progressBar.setVisibility(View.VISIBLE);
		}    
		@Override    
		protected Void doInBackground(ArrayList<IndexEntry>... values) {
			loadAsinc(values[0]);
			return null; 

		}

		@Override    
		protected void onCancelled(Void result) {    
			super.onCancelled(result);
			progressDialog.hide();
			progressDialog.dismiss();

			mIsLoaded = false;
			Utils.addInformation("onCancelled");

			//seekBarVertical.setMax(getStringsSize());
			Toast.makeText(mContext,
						   "Cancelled", Toast.LENGTH_LONG) // mContext.getResources().getString(R.string.loading_cancelled)
				.show();
			eventCallback.loadingFinishedCallBack(true); 	
		}

		@Override    
		protected void onPostExecute(Void result) {  
			mIsLoaded = false;
			super.onPostExecute(result);
			progressDialog.hide();
			progressDialog.dismiss();

			Utils.setDictionaryName(mDictionaryName);

			Utils.addInformation("onPostExecute: eventCallback = "+eventCallback);

			if (eventCallback != null)
				eventCallback.loadingFinishedCallBack(true);
			//invalidate();    
			//seekBarVertical.setMax(getStringsSize());
		}

		protected void loadAsinc(ArrayList<IndexEntry> ens) {
			mIsLoaded = true;
			ens.clear();

			BufferedReader reader;

			ens.clear();
			//Log.d("s","LOAD "+mIndexFileName);
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getIndexPath())));

				String line = reader.readLine();
				while (line != null){
					IndexEntry indexEntry = new IndexEntry(line);
					if (indexEntry.getText().length() > 0)
						ens.add(indexEntry);
					line = reader.readLine();
				    if (isCancelled()){
						reader.close();
				    	break;
				    }
				}
				Utils.addInformation("loadAsinc: File <"+mDictionaryName+"> loaded. "+ens.size()+" entries.");
				reader.close();
			} catch (IOException t) {
				Utils.addInformation("loadAsinc: Error loading <"+mDictionaryName+"> loaded. "+t.toString());
			}
			mIsLoaded = false;
		}

	}	
	
	public static boolean isLoaded(){
		return mIsLoaded;
	}

	public static IndexEntry find(String string) {
		
		if (string.length() == 0) return null;
		
		ArrayList<IndexEntry> results = new ArrayList<IndexEntry>();
		string = string.toLowerCase();

		String[] strings = Utils.getStringForms(string);
//
		int n = 0;
		for (IndexEntry indexEntry: indexEntries){
			if (indexEntry.getText() == null)
				continue;

			if (string.charAt(0) > indexEntry.getText().charAt(0))
				continue;
			if (string.charAt(0) < indexEntry.getText().charAt(0)){
				//Log.d("z", string.charAt(0) +" - "+ indexEntry.getText().charAt(0));
				break;
			}

			String text = indexEntry.getText().replace("-", "");
			n++;
			if (string.equals(text)){

				return indexEntry;
			}

			if (strings.length == 1){

				if (strings[0].equals(text)){

					results.add(indexEntry);
				}
			}

			else if (strings[1].equals(text))
				return indexEntry;
			else if (string.startsWith(text) && string.length() - text.length() > 2)
				results.add(indexEntry);
			else if (strings[0].startsWith(text))
				results.add(indexEntry);
			else if (strings[2].equals(text))
				results.add(indexEntry);
		}

		Log.d("z", "n "+n);
		if (results.size() > 0)
			return results.get(results.size()-1);
		else return null;
	}

	/**
	 * Reads bytes from the Dictionary fileget
	 * @param indexEntry - gives the position and length of translation in the file   
	 * @return the translation as String
	 */
	public static String readTranslation(IndexEntry indexEntry)
	{
		BufferedReader bis;

		try {
			
			if (Utils.isInternalDictionary())
				bis = new BufferedReader(new InputStreamReader(mContext.getResources().openRawResource(Utils.getInternalDictionaryID())));
			else
				bis = new BufferedReader(new InputStreamReader(
											 new FileInputStream(mDictionaryName)));
			
			char[] buffer = new char[indexEntry.getLength()+1];
			
			bis.skip(indexEntry.getPos());
			bis.read(buffer, 0, indexEntry.getLength());
			
			String s = new String(buffer);
			
			for (int i = 0; i < 30; i++){
				s = s.replaceFirst(i+"\\.", "<br>"+i+".");
			}
			
			//for (int i = 0; i < indexEntry.getLength(); i++)
			//	s = s + (char)bis.read();
			
			bis.close();

			return s;
		} catch (IOException t) {
			Toast.makeText(mContext,
						   "Error:" + t.toString(), Toast.LENGTH_LONG)
				.show();
			Utils.addInformation(""+t);
			return "";
		}

	}

//	
	public static boolean createIndexAsinc(String dictionary_name) {

		String index_file_name;
		index_file_name= Utils.getAppFolder() +dictionary_name.replace(".xdxf", ".index");

		mDictionaryName = dictionary_name;

		progressDialog = new ProgressDialog(mContext);
		progressDialog.setTitle("Indexing dictionary...");
		progressDialog.setMessage(index_file_name);
		progressDialog.show();

		myTaskIndexing = new MyTaskIndexing();    
		myTaskIndexing.execute(index_file_name);
		return true;
	}	

	static class MyTaskIndexing extends AsyncTask<String, Integer, Void> {

		@Override
		protected Void doInBackground(String... params) {
			createIndex();
			return null;
		}

		@Override    
		protected void onCancelled(Void result) {      
			super.onCancelled(result);
			progressDialog.hide();
			progressDialog.dismiss();
			Utils.addInformation(info);
			Toast.makeText(mContext,
						   "Cancelled", Toast.LENGTH_LONG) // mContext.getResources().getString(R.string.loading_cancelled)
				.show();
		}

		@Override    
		protected void onPostExecute(Void result) {      
			super.onPostExecute(result);
			progressDialog.hide();
			progressDialog.dismiss();
			Utils.addInformation(info);
			Log.d("","eventCallback --->>>>> "+eventCallback);
			if (eventCallback != null){
				eventCallback.indexingFinishedCallBack(mDictionaryName);
			}
		}
	}	

	public static boolean createIndex() {
		ArrayList<IndexEntry> indexEntries = new ArrayList<IndexEntry>();

		
		
		
		try {

			BufferedReader bis;
			BufferedOutputStream bos;
			
			if (Utils.isInternalDictionary())
				bis = new BufferedReader(new InputStreamReader(mContext.getResources().openRawResource(Utils.getInternalDictionaryID())));
			else
				bis = new BufferedReader(new InputStreamReader(
						   new FileInputStream(mDictionaryName)));
				
			bos = new BufferedOutputStream(new FileOutputStream(Utils.getIndexPath()));
			
			char chr;
			int code;
			String str = "";
			int state = 0;
			
			int pos = 0;
			int posStart = 0;

			while ((code = bis.read()) != -1) {
					chr = (char)code;
					pos++;
					switch (state) {
						case 0:
							if (chr == '<')
							{	
								state = 1;
							}
							break;
						case 1:
							if (chr == 'k')
								state = 2;
							else 
								state = 0;
							break;
						case 2:
							if (chr == '>'){
								state = 3;
								posStart = pos;
							} else 
								state = 0;
							break;
						case 3:
							if (chr != '<'){
								str = str + chr;
							} else {
								state = 4;
							}
							break;
						case 4:
							if (chr == 'k')
								state = 5;
							break;
						case 5:
							if (chr == '>'){
								
								if (indexEntries.size() == 551 ){
									int uuu = 1;
									Log.d("", ""+uuu);
								}
								
								if (str.length() > 0
									){
									indexEntries.add(new IndexEntry(str, posStart));
									str = "";
								}
								state = 0;
							}
							else
								state = 0;
							break;
						default:	
							Log.d("", "state ??? "+state);
					}
					
					/*
				if (state == 3){
				    chunk0 = new String(buffer, start, BUFFER_SYZE-start);
				    if (chunk0.startsWith("  ")){
				    	chunk0 = "";
				    	state = 0;
				    }
				}    
				else
					chunk0 = "";	*/
			}		


			int ind = 0;
			for (IndexEntry indexEntry: indexEntries){
				if (ind < indexEntries.size()-1)
					indexEntry.setLength(indexEntries.get(ind+1).getPos() - indexEntries.get(ind).getPos());
				else	
					indexEntry.setLength(pos - indexEntry.getPos());
				ind++;
			}

			//EntryComparator ec = new EntryComparator();
			//java.util.Collections.sort(indexEntries, ec);			

			for (IndexEntry indexEntry: indexEntries){
				bos.write(indexEntry.getText().getBytes());
				bos.write((char)(0x9));
				bos.write(String.format("%x", indexEntry.getPos()).getBytes());
				bos.write((char)(0x9));
				bos.write(String.format("%x", indexEntry.getLength()).getBytes());
				bos.write((char)(0xa));
			}

			bos.flush();
			bos.close();

			//Utils.dictionary_name = name;
			//Utils.dictionary_index = index;
			Log.d("a","ind end");
			return true;
		} catch (IOException t) {
			info = "Error indexing "+t;
			Log.d("a","ind error "+t);
			return false;
		}
		
	}

	
	static final class EntryComparator implements Comparator<IndexEntry> {
		public int compare(IndexEntry e1, IndexEntry e2) {
		    return e1.getText().compareToIgnoreCase(e2.getText());
		}
	}


	public void remove(int line, int pos) {
		// TODO Auto-generated method stub

	}

	public CharSequence getFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	public static int getCount() {
		return indexEntries.size();
	}

	public String[] getDictionaryAsStrings() {
		String[] strings = new String[indexEntries.size()];

		int n = 0;
		for (IndexEntry i: indexEntries)
			strings[n++] = i.getText();

		return strings;
	}

	public static ArrayList<IndexEntry> getIndexEntries(){
		//int n = indexEntries.size();
		return indexEntries;
	}

	public static String getDictionaryInfo() {
		try {
			BufferedReader reader;
			if (Utils.isInternalDictionary())
				reader = new BufferedReader(new InputStreamReader(mContext.getResources().openRawResource(Utils.getInternalDictionaryID())));
			else
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getDictionaryName()))); //PATH

			String line = reader.readLine();
			int i = 0;
			while (i < 20 && line != null){
				int j = line.indexOf("<full_name>");
				if (j >= 0){
					reader.close();
					int k = line.indexOf("</full_name>");
					if (k >=0)
						return line.substring(j+11,k);
					else
						return line.substring(j+11);
				}
				line = reader.readLine();
			}
			reader.close();
			return "";
		} catch (IOException t) {
			Utils.addInformation(""+t);
			return "";
		}
	}

	private boolean isSeparator(char charAt) {
		Pattern p = Pattern.compile(";,.!?");
		return ";,.!?".contains(""+charAt);		
	}

}

