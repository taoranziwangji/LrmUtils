package com.vdolrm.lrmutils.OtherUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * map与json相互转换工具类
 * Created by vdo on 17/1/25.
 */

public class MapJsonUtil {

    /**
     * map转json
     *
     * @param map
     * @return
     */
    public static JSONObject map2JsonObject(Map<String, String> map) {
        JSONObject jsonObject = new JSONObject();
        if (map == null)
            return jsonObject;

        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> pair : entrySet) {//遍历取出键值对，调用getkey()，getvalue()取出key和value。
            //System.out.println("key:"+pair.getKey());
            //System.out.println(pair.getValue()+"");
            try {
                jsonObject.putOpt(pair.getKey(), pair.getValue());
            } catch (JSONException | RuntimeException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    /**
     * json转map
     *
     * @param jsonObject
     * @return
     */
    public static HashMap<String, String> jsonObject2Map(JSONObject jsonObject) {
        HashMap<String, String> map = new HashMap();
        if (jsonObject == null)
            return map;

        Iterator<String> keyset = jsonObject.keys();
        List<String> list = new ArrayList<>();
        while (keyset.hasNext()) {
            list.add(keyset.next());
        }
        //Collections.sort(list);//由于是hashmap，排序也没用，要想有序使用TreeMap
        try {
            for (int i = 0; i < list.size(); i++) {
                //Log.d("lrm", (list.get(i) + "=" + jsonObject.get(list.get(i))));
                map.put(list.get(i), jsonObject.get(list.get(i)) + "");
            }

        } catch (JSONException | RuntimeException e) {
            e.printStackTrace();
        }

        return map;
    }
}
