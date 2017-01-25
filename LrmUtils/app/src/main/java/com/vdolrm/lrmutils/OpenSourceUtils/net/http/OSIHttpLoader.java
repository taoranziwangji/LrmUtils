package com.vdolrm.lrmutils.OpenSourceUtils.net.http;

import java.util.Map;

/**
 * Created by Administrator on 2016/4/1.
 */
public interface OSIHttpLoader {
    void getAsync(String url,OSIHttpLoaderCallBack callBack);
    void cancelRequest();

    void postAsync(String url, OSIHttpLoaderCallBack callBack, Map<String, String> params);
    void postAsyncRaw(String url, OSIHttpLoaderCallBack callBack, String json);
    void deleteAsync(String url, OSIHttpLoaderCallBack callBack, Map<String, String> params);
}
