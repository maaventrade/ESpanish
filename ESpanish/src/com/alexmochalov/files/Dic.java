package com.alexmochalov.files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alexmochalov.main.Utils;

public class Dic {
	private static ArrayList<IndexEntry> indexEntries = new ArrayList<IndexEntry>();
	
	
	private static ProgressDialog progressDialog;
	private static MyTaskIndexing myTaskIndexing;
	private static Context mContext; 

	public static boolean fileExists(String fileName){
		File file = new File(Utils.APP_FOLDER + "/" + fileName);
		return file.exists();
	}

	public static void unzip(Context context, String fileName, int id) {
		try {
			int BUFFER_SIZE = 1024;
			byte[] buffer = new byte[BUFFER_SIZE];
			int size;
			
			String nameUnzipped = Utils.APP_FOLDER + "/" + fileName;
			
			//FileInputStream fin = new FileInputStream(fileName);

			//InputStream openFileInputStream(String name){
			//	if (fromRresource)
			
			InputStream fin = context.getResources().openRawResource(id);
			ZipInputStream zin = new ZipInputStream(fin);
			
			ZipEntry ze = null;
			while ((ze = zin.getNextEntry()) != null) {
				//if (isCancelled()) return;
				if (ze.isDirectory()) {
				} else {
					FileOutputStream fout = new FileOutputStream(nameUnzipped);
					BufferedOutputStream bout = new BufferedOutputStream(fout, BUFFER_SIZE);
					
					while ( (size = zin.read(buffer, 0, BUFFER_SIZE)) != -1 ) {
                        fout.write(buffer, 0, size);
                    }
					
					zin.closeEntry();
					bout.flush();
					bout.close(); 
				}
			}
			zin.close();
		} catch (Exception t) {
			;
		}
	}

	public static boolean createIndexAsinc(Context context, String fileName) {
		mContext = context;
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("Indexing dictionary...");
		progressDialog.setMessage(fileName);
		progressDialog.show();
		
		myTaskIndexing = new MyTaskIndexing();    
		myTaskIndexing.execute(fileName);
		return true;
	}	
		
	static class MyTaskIndexing extends AsyncTask<String, Integer, Void> {

		@Override
		protected Void doInBackground(String... params) {
			createIndex(params[0]);
			return null;
		}
		
		@Override    
		protected void onCancelled(Void result) {      
			super.onCancelled(result);
			progressDialog.hide();
			progressDialog.dismiss();
			Toast.makeText(mContext,
					"Cancelled", Toast.LENGTH_LONG) // mContext.getResources().getString(R.string.loading_cancelled)
					.show();
		}
		
		@Override    
		protected void onPostExecute(Void result) {      
			super.onPostExecute(result);
			progressDialog.hide();
			progressDialog.dismiss();
			//Log.d("","eventCallback --->>>>> "+eventCallback);
			//if (eventCallback != null)
				//Log.d("","INDEXING FINISHED");
//				private String mDictionaryName;
//			private String mIndexFileName;
				//eventCallback.indexingFinishedCallBack(mDictionaryName, mIndexFileName);
		}
	}	
	
	static final class EntryComparator implements Comparator<IndexEntry> {
		  public int compare(IndexEntry e1, IndexEntry e2) {
		    return e1.text.compareToIgnoreCase(e2.text);
		  }
	}
	
	static final int BUFFER_SYZE = 2048;
	public static void createIndex(String fileName) {
		BufferedInputStream bis;
		BufferedOutputStream bos;
		
		String chunk;
		String chunk0 = "";
		byte[] buffer = new byte[BUFFER_SYZE];
		int bytesRead = 0;
		
		String fileNameIndex = fileName.replace(".xdxf", ".index");
		
		try {
			bis = new BufferedInputStream(new FileInputStream(Utils.APP_FOLDER+"/"+fileName));
			bos = new BufferedOutputStream(new FileOutputStream(Utils.APP_FOLDER+"/"+fileNameIndex));
			
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
						    if ((chunk0+chunk).length() > 0
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
			for (IndexEntry IndexEntry: indexEntries){
				if (ind < indexEntries.size()-1)
					IndexEntry.length = indexEntries.get(ind+1).length - IndexEntry.pos + 2;
				else	
					IndexEntry.length = pos - IndexEntry.pos;
				ind++;
			}
			
			EntryComparator ec = new EntryComparator();
			java.util.Collections.sort(indexEntries, ec);			
			
			for (IndexEntry IndexEntry: indexEntries){
				bos.write(IndexEntry.text.getBytes());
				bos.write((char)(0x9));
				bos.write(String.format("%x", IndexEntry.pos).getBytes());
				bos.write((char)(0x9));
				bos.write(String.format("%x", IndexEntry.length).getBytes());
				bos.write((char)(0xa));
			}
			
			bos.flush();
			bos.close();
		}
		catch (IOException e)
		{}

		
		
	}

	public static void loadIndex(Context context, String fileNameIndex) {
		BufferedReader reader;
		
		indexEntries.clear();
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(Utils.APP_FOLDER+"/"+fileNameIndex)));

			String line = reader.readLine();
			while (line != null){
				indexEntries.add(new IndexEntry(line));
				line = reader.readLine();
			    //if (isCancelled()){
				//	reader.close();
			    //	break;
			    //}
			}
			reader.close();
		} catch (IOException t) {
		}
		Log.d("OK", "OK");
	}

	public static ArrayList<IndexEntry> getEntries() {
		return indexEntries;
	}

	public static String getTranslation(IndexEntry indexEntry) {
		BufferedInputStream bis;
		try {
			bis = new BufferedInputStream(new FileInputStream(Utils.APP_FOLDER+"/"+"it_ru.xdxf"));

			//bis.skip(1000);
			//byte[] buffer = new byte[500];
			
			bis.skip(indexEntry.pos);
			byte[] buffer = new byte[indexEntry.length];

			int bytesRead = bis.read(buffer);

			String s = new String(buffer, 0, indexEntry.length-1);
//			String s = new String(buffer, 0, 500);

			bis.close();

			return s;
		} catch (IOException t) {
			Toast.makeText(mContext,
						   "Error:" + t.toString(), Toast.LENGTH_LONG)
				.show();
			return "";
		}
	}

	public static String getTranslation(String text) {
		for (IndexEntry i:indexEntries)
			if(i.getText().equals(text))
				return(i.getTranslation());
		return "";
	}	
	
}