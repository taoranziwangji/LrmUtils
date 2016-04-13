package com.vdolrm.lrmutils.NetUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetCheckUtil {
	public static boolean isNetworkConnected(Context context) {
	     if (context != null) {  
	         ConnectivityManager mConnectivityManager = (ConnectivityManager) context
	                 .getSystemService(Context.CONNECTIVITY_SERVICE);
	         NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
	         if (mNetworkInfo != null) {  
	              return mNetworkInfo.isAvailable();  
	          }  
	      }  
	     return false;  
	 }
	
	/**获取联网类型 返回字符串*/
	public static String getNetTypeName(Context context){
		String nettypeName = "";
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info != null){
			nettypeName = info.getTypeName();//联网类型=				
		}
		return nettypeName==null?"":nettypeName;
	}
	
	/**获取联网类型 返回整形*/
	public static int getNetType(Context context){
		int type = -1;
	
		ConnectivityManager connectMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo info = connectMgr.getActiveNetworkInfo();
		 if(info!=null){
			type = info.getType();//ConnectivityManager.TYPE_WIFI
			//int subtype = info.getSubtype();
		 }
		return type;
	}
}
