package com.vdolrm.lrmutils.OtherUtils;

/**
 * Created by Administrator on 2015/12/10.
 */
public class StringUtils {
    public static boolean isEmpty(String s){
        return (s == null) || (s.length() == 0);
    }

    public static boolean isNotEmpty(String s)
    {
        return !isEmpty(s);
    }
}
