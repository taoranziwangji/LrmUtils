package com.vdolrm.lrmutils.OtherUtils;

import android.content.ClipboardManager;
import android.content.Context;

import com.vdolrm.lrmutils.UIUtils.UIUtils;

/**剪切板工具类*/
public class ClipboardManagerUtil {

	/**
	 * @param c
	 * @param s
	 * @return 是否成功
	 */
	public static boolean copyToClipboard(Context c,final String s){
		try{
		//获取剪贴板管理服务
		final ClipboardManager cm =(ClipboardManager)c.getSystemService(Context.CLIPBOARD_SERVICE);
		//将文本数据复制到剪贴板
		if(android.os.Build.VERSION.SDK_INT>=11){
			UIUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					cm.setText(s);
				}
			});
			
			return true;
		}else{
			return false;
		}
	
	}catch(RuntimeException e){
		e.printStackTrace();
		return false;
	}
	}
}
