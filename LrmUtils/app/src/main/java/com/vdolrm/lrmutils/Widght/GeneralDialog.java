package com.vdolrm.lrmutils.Widght;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public class GeneralDialog {
	private View mConvertView;
	private AlertDialog ad;
	private Window window;
	
	public GeneralDialog(Context context, int layoutId) {
		mConvertView = LayoutInflater.from(context).inflate(layoutId,null);
		ad=new AlertDialog.Builder(context).create();
		ad.show();
		window = ad.getWindow();
		window.setContentView(mConvertView);
	}
	
	 public View getConvertView(){
	     return mConvertView;
	 }
	 
	 public Window getWindow(){
		 return window;
	 }
 

	
	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		if(ad.isShowing()){
		ad.dismiss();
		}
	}
 
}