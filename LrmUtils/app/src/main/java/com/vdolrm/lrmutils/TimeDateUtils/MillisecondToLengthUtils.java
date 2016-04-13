package com.vdolrm.lrmutils.TimeDateUtils;

public class MillisecondToLengthUtils {

	/** 根据毫秒数 转换成分钟数和秒数 ex:10000 ---00:10
	 * time:毫秒
	 * 返回值为min:sec(min最大无限制)
     * **/
	public static String getShortStrTime(int time){
		int min = 0;
		int sec = 0;
		String strTime = "";
		time /= 1000;
		if(time < 60){
			min = 0;
			sec = time;
		}else{
			min = time / 60;
			sec = time % 60;
		}
		if(min < 10){
			strTime += "0" + min + ":" ;
		}else{
			strTime += min + ":" ;
		}
		if(sec<10){
			strTime += "0" + sec ;
		}else{
			strTime += sec ;
		}
		
		return strTime;
	}
	
	
	/** 根据毫秒数 转换成小时数分钟数和秒数 ex:10000 ---00:10
	 * time:毫秒
	 * max:99:59:59
	 * 返回值为hour:min:sec
     * **/
	public static String getStrTime(int time) {
        String timeStr = null;
        int hour = 0;  
        int minute = 0;  
        int second = 0;  
        time /= 1000;
        if (time <= 0)  
            return "00:00";  
        else {  
            minute = time / 60;  
            if (minute < 60) {  
                second = time % 60;  
                timeStr = unitFormat(minute) + ":" + unitFormat(second);  
            } else {  
                hour = minute / 60;  
                if (hour > 99)  
                    return "99:59:59";  
                minute = minute % 60;  
                second = time - hour * 3600 - minute * 60;  
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
            }  
        }  
        return timeStr;  
    }  
  
    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)  
            retStr = "0" + Integer.toString(i);
        else  
            retStr = "" + i;  
        return retStr;  
    }  
	
	
}
