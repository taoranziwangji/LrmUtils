package com.vdolrm.lrmutils.OtherUtils;


public class RandomUtil {

	/**
	 * n表示几位
	 * */
	public static String getRandom(int n){
		// 获取随机数
		/*char[] chars = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E',
		'F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W',
		'X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
		'p','q','r','s','t','u','v','w','x','y','z'};*/
		char[] chars = {'9','0','1','2','3','4','5','6','7','8','9',};//第一个9永远取不到
		
			String res = "";
			for(int i = 0; i < n ; i ++) {
			    int id = (int) Math.ceil(Math.random() * 10);//此方法返回最小的（最接近负无穷大）浮点值大于或相等于参数，并相等于一个整数。
			    res += chars[id];
			}
			return res;
		
	}
	
	/**
	 * n表示几位
	 * */
	public static String getRandom2(int n){
		// 获取随机数
		char[] chars = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E',
		'F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W',
		'X','Y','Z'};
	//	char[] chars = {'9','0','1','2','3','4','5','6','7','8','9',};//第一个9永远取不到
		
			String res = "";
			for(int i = 0; i < n ; i ++) {
			    int id = (int) Math.ceil(Math.random() * 35);//此方法返回最小的（最接近负无穷大）浮点值大于或相等于参数，并相等于一个整数。
			    res += chars[id];
			}
			return res;
		
	}
	
}
