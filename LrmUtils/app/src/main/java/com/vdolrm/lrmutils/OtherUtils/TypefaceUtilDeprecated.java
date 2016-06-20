package com.vdolrm.lrmutils.OtherUtils;


import android.content.Context;
import android.graphics.Typeface;


/**
 * 由于加入了t1是否为空的判断，所以只适用于整个app用了一个第三方字体的情况，否则请重写方法，把name写到各个方法里
 */
@Deprecated
public class TypefaceUtilDeprecated {

	private static Typeface t1,t2;
	private static Typeface t3,t4,t5;

	/**
	 * @param c
	 * @param name ex:lkssjht.TTF 需要放置在assets/fonts目录里
	 * @return
	 */
	public static Typeface getTypeFace(Context c,String name){
		//return null;
		if(t1==null)
			t1 = Typeface.createFromAsset(c.getAssets(), "fonts/"+name);
		return t1;
	}
	

}
