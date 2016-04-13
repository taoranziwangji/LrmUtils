package com.vdolrm.lrmutils.OtherUtils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.vdolrm.lrmutils.LogUtils.MyLog;


public class PhoneUtil {
	
	/**获取imie码*/
	public static String getIMEI(Activity activity){
		try{
		TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		String IMEI = tm.getDeviceId();
		if(IMEI ==null || IMEI.equals("")){
			IMEI = "r_"+ System.currentTimeMillis();
		}
		return IMEI;
		}catch(RuntimeException e){
			e.printStackTrace();
		}
		return "r_"+ System.currentTimeMillis();
		
	}
	
	
	public static PhoneBean getPhoneState(Context c){
		TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		String yunyingshangname = tm.getNetworkOperatorName();//运营商姓名
		String phoneModel =  android.os.Build.MODEL;	//"手机型号:"
		String phonepinpai= android.os.Build.BRAND;//手机品牌
		String OSversion = android.os.Build.VERSION.RELEASE;// ",系统版本:" +
		String nettypeName = "";
			
		try{
			ConnectivityManager cm = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = cm.getActiveNetworkInfo();
				if(info != null){
				nettypeName = info.getTypeName();//联网类型=				
				}
		}catch(RuntimeException e){
			e.printStackTrace();
		}
		
		MyLog.d("lrm","运营商姓名yunyingshangname="+yunyingshangname);	
		MyLog.d("lrm", "联网类型nettypeName=" + nettypeName);
		
		MyLog.d("lrm","手机品牌phonepinpai="+phonepinpai);
		MyLog.d("lrm","手机型号phoneModel="+phoneModel);		
				
		MyLog.d("lrm","系统版本OSversion="+OSversion);
		
		PhoneBean bean = new PhoneBean();
		bean.setNettypeName(nettypeName);
		bean.setOSversion(OSversion);
		bean.setPhoneModel(phoneModel);
		bean.setPhonepinpai(phonepinpai);
		bean.setYunyingshangname(yunyingshangname);
		
		return bean;	
	}
	
	
	public static class PhoneBean{
		private String yunyingshangname;
		private String phoneModel;
		private String phonepinpai;
		private String OSversion;
		private String nettypeName;
		
		public String getYunyingshangname() {
			return yunyingshangname;
		}
		public void setYunyingshangname(String yunyingshangname) {
			this.yunyingshangname = yunyingshangname;
		}
		public String getPhoneModel() {
			return phoneModel;
		}
		public void setPhoneModel(String phoneModel) {
			this.phoneModel = phoneModel;
		}
		public String getPhonepinpai() {
			return phonepinpai;
		}
		public void setPhonepinpai(String phonepinpai) {
			this.phonepinpai = phonepinpai;
		}
		public String getOSversion() {
			return OSversion;
		}
		public void setOSversion(String oSversion) {
			OSversion = oSversion;
		}
		public String getNettypeName() {
			return nettypeName;
		}
		public void setNettypeName(String nettypeName) {
			this.nettypeName = nettypeName;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			String s = "yunyingshangname="+yunyingshangname+",phoneModel="+phoneModel+",phonepingtai="+phonepinpai+",OSversion="+OSversion+",nettypeName="+nettypeName;
			return s;
		}
		
		
	}
}
