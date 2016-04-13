package com.vdolrm.lrmutils.OtherUtils;

import android.content.Context;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SoftKeyBoardUtil {

	/**
	 * 退出界面时或点击edittext的外围隐藏掉软键盘*/
	public static void hideTheKeyBorad(EditText edt){
		if( edt==null){
			return ;
		}
		try{
			InputMethodManager imm = (InputMethodManager) edt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			 //edt.setCursorVisible(false);//失去光标
			if(imm!=null){
				imm.hideSoftInputFromWindow(edt.getWindowToken(), 0); //强制隐藏键盘  
			}
		}catch(RuntimeException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 进入界面时自动弹出软键盘*/
	public static void showTheKeyBorad(final EditText edt){
		if(edt==null){
			return;
		}
		try{
		(new Handler()).postDelayed(new Runnable() {
			@Override
			public void run() {
                InputMethodManager imm = (InputMethodManager)edt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
               // edt.setCursorVisible(true);//得到光标
                if(imm!=null){
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
	        }
	    }, 500);
		}catch(RuntimeException e){
			e.printStackTrace();
		}
	}
}
