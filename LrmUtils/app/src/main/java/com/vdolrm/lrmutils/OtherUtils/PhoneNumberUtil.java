package com.vdolrm.lrmutils.OtherUtils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberUtil {

	/**判断是否是手机号码*/
		public static boolean isMobileNum(String mobiles) {
	       /* 182识别不出
	        * 
	        * Pattern p = Pattern
	                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
	        Matcher m = p.matcher(mobiles);
	        System.out.println(m.matches() + "---");
	        return m.matches();*/
			
			if(mobiles==null){
				return false;
			}
			
			/*if(mobiles.startsWith("1")){
				return true;
			}else{
				return false;
			}*/
			
			Pattern p = Pattern.compile("^[1][34578][0-9]{9}$");
			Matcher m = p.matcher(mobiles);
			System.out.println(m.matches() + "---");
	        return m.matches();

	    }
}
