package com.alexmochalov.test;

import com.alexmochalov.main.Utils;
import com.alexmochalov.tree.LineItem;

public class TestItem {

		private String mText;
		private String mTranslation;
		private int mCount;
		
		public TestItem(String name, String transl) {
			mText = name;
			mTranslation = transl;
			mCount = 0;
		}
		
		public TestItem(LineItem selectedItem) {
			mText = selectedItem.getText();
			mTranslation = selectedItem.getTranslation();
		}

		public void setCount(int p0)
		{
			mCount = 0;
		}

		public String getTranslation() {
			return mTranslation;
		}
		
		public String getText() {
			return mText;
		}
		
		//public void incCount() {
			//mCount++;
		//}
		
		public int getCount() {
			return mCount;
		}

	public boolean test(String string, int direction) {
		
		if (direction == 1 && string.equals(mText)){
			mCount = mCount | 1;
			return true;
		} else if (direction == 2 && string.equals(mTranslation)){
			mCount = mCount | 2;
			return true;
		}
		return false;
			//return
			//	direction == 1 && string.equals(mText) ||
				//direction == 2 && string.equals(mTranslation);							
		}

	}

	 
