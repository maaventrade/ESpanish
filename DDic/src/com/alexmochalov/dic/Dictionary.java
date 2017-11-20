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

import com.alexmochalov.main.Utils;

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
	static Boolean parsing = false;

	public static EventCallback eventCallback;

	private static String mDictionaryName;

	static String info;

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

		if(!file.exists()){
			if (eventCallback != null)
				eventCallback.loadingFinishedCallBack(file.exists());
			return;
		}
		
		//Log.d("a","Start Loading "+dictionary_name);
		//Log.d("a",index_file_name);
		
		
		loadIndexAsinc();
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

			Utils.setInformation(info);

			//seekBarVertical.setMax(getStringsSize());
			Toast.makeText(mContext,
						   "Cancelled", Toast.LENGTH_LONG) // mContext.getResources().getString(R.string.loading_cancelled)
				.show();
			eventCallback.loadingFinishedCallBack(true); 	
		}

		@Override    
		protected void onPostExecute(Void result) {      
			super.onPostExecute(result);
			progressDialog.hide();
			progressDialog.dismiss();

			Utils.setDictionaryName(mDictionaryName);

			Utils.setInformation(info);

			if (eventCallback != null)
				eventCallback.loadingFinishedCallBack(true);
			//invalidate();    
			//seekBarVertical.setMax(getStringsSize());
		}

		protected void loadAsinc(ArrayList<IndexEntry> ens) {
			parsing = true;
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
				info = "File <"+mDictionaryName+"> loaded. "+ens.size()+" entries.";
				reader.close();
			} catch (IOException t) {
				info = "Error loading <"+mDictionaryName+"> loaded. "+t.toString();
				//Log.d("s", info);
			}
			parsing = false;
			//Log.d("s", "OK");
		}

	}	

	public static IndexEntry find(String string) {
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
		BufferedInputStream bis;
		try {
			if (Utils.isInternalDictionary())
				bis = new BufferedInputStream(mContext.getResources().openRawResource(Utils.getInternalDictionaryID()));
			else
				bis = new BufferedInputStream(new FileInputStream(Utils.getDictionaryName())); //PATH

			//bis.skip(1000);
			//byte[] buffer = new byte[500];

			bis.skip(indexEntry.getPos());
			byte[] buffer = new byte[indexEntry.getLength()];

			int bytesRead = bis.read(buffer);

			String s = new String(buffer, 0, indexEntry.getLength()-1);
//			String s = new String(buffer, 0, 500);

			bis.close();

			return s;
		} catch (IOException t) {
			Toast.makeText(mContext,
						   "Error:" + t.toString(), Toast.LENGTH_LONG)
				.show();
			Utils.setInformation(""+t);
			return "";
		}

	}

	static final int BUFFER_SYZE = 2048;
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
			Utils.setInformation(info);
			Toast.makeText(mContext,
						   "Cancelled", Toast.LENGTH_LONG) // mContext.getResources().getString(R.string.loading_cancelled)
				.show();
		}

		@Override    
		protected void onPostExecute(Void result) {      
			super.onPostExecute(result);
			progressDialog.hide();
			progressDialog.dismiss();
			Utils.setInformation(info);
			Log.d("","eventCallback --->>>>> "+eventCallback);
			if (eventCallback != null){
				eventCallback.indexingFinishedCallBack(mDictionaryName);
			}
		}
	}	

	public static boolean createIndex() {
		ArrayList<IndexEntry> indexEntries = new ArrayList<IndexEntry>();

		BufferedInputStream bis;
		BufferedOutputStream bos;


		String chunk;
		String chunk0 = "";

		byte[] buffer = new byte[BUFFER_SYZE];
		int bytesRead = 0;
		
		
		
		try {
			
			if (Utils.isInternalDictionary())
				bis = new BufferedInputStream(mContext.getResources().openRawResource(Utils.getInternalDictionaryID()));
			else
				bis = new BufferedInputStream(new FileInputStream(mDictionaryName));

		/*		
			 int c[] = {0,0,0};

			InputStream fis = mContext.getResources().openRawResource(Utils.getInternalDictionaryID());

			 if (fis.available() >= 2){
			 c[0] = fis.read();
			 c[1] = fis.read();
			 }
			 if (fis.available() >= 3)
			 c[2] = fis.read();

			BufferedReader reader;
			 
			
			 if (c[0] == 255 && c[1] == 254 )
			 reader = new BufferedReader(fis, "UTF-16");
			 else if (c[0] == 239 && c[1] == 187 && c[2] == 191 )
			 reader = new BufferedReader(new InputStreamReader(openFileInputStream(name), "UTF-8"));
			 else
			 reader = new BufferedReader(new InputStreamReader(openFileInputStream(name), "windows-1251"));

			 reader = new BufferedReader(new InputStreamReader(openFileInputStream(name), "UTF-8"));
*/
			 
				
				
				
				
			bos = new BufferedOutputStream(new FileOutputStream(Utils.getIndexPath()));

			int state = 0;
			int start = 0;
			int end = 0;
			int pos = 0;

			int prevTextEnd = 0;

			while ((bytesRead = bis.read(buffer)) != -1) {
				start = 0;
				end = 0;

				for (int i = 0; i < BUFFER_SYZE; i++){
					switch (state) {
						case 0:
							if (buffer[i] == '<')
							{	
								prevTextEnd = pos-1;
								state = 1;
							}	
							break;
						case 1:
							if (buffer[i] == 'k')
								state = 2;
							else 
								state = 0;
							break;
						case 2:
							if (buffer[i] == '>'){
								state = 3;
								start = i+1;
							}	
							break;
						case 3:
							if (buffer[i] != '<'){
							} else {
								end = i;
								state = 4;
							}
							break;
						case 4:
							if (buffer[i] == 'k')
								state = 5;
							break;
						case 5:
							if (buffer[i] == '>'){
								chunk = new String(buffer, start, end-start);
								if ((chunk0+chunk).trim().length() > 0
									&&  !chunk.startsWith("  ")){
									indexEntries.add(new IndexEntry((chunk0+chunk).trim(), pos+1, prevTextEnd));
									chunk0 = "";
								}
								state = 0;
							}
							else
								state = 0;
							break;
						default:	
							Log.d("", "state ??? "+state);
					}
					pos++;
				}
				if (state == 3){
				    chunk0 = new String(buffer, start, BUFFER_SYZE-start);
				    if (chunk0.startsWith("  ")){
				    	chunk0 = "";
				    	state = 0;
				    }
				}    
				else
					chunk0 = "";	
			}		


			int ind = 0;
			for (IndexEntry indexEntry: indexEntries){
				if (ind < indexEntries.size()-1)
					indexEntry.setLength(indexEntries.get(ind+1).getLength() - indexEntry.getPos() + 2);
				else	
					indexEntry.setLength(pos - indexEntry.getPos());
				ind++;
			}

			EntryComparator ec = new EntryComparator();
			java.util.Collections.sort(indexEntries, ec);			

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
			Utils.setInformation(""+t);
			return "";
		}
	}

	private boolean isSeparator(char charAt) {
		Pattern p = Pattern.compile(";,.!?");
		return ";,.!?".contains(""+charAt);		
	}

}

