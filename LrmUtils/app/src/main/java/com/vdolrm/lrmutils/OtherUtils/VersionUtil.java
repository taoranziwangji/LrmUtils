package com.vdolrm.lrmutils.OtherUtils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.vdolrm.lrmutils.R;


public class VersionUtil {

/**	获取android的版本*/
	public static int getAndroidVerson(){
		return android.os.Build.VERSION.SDK_INT;
	}
	
	
	/**
	 * 获取versionname*/
	public static String getVersion(Context context)//获取版本号
	{
		if(context==null){
			return "";
		}
		try {
			PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return context.getString(R.string.vdo_version_unknown);
		}
	}
	
	
	/**
	 * 获取ersioncode*/
	public static int getVersionCode(Context context)//获取版本号(内部识别号)
	{
		if(context==null){
			return 0;
		}
		try {
			PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
}
