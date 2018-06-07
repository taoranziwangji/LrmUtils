package com.vdolrm.lrmutils.OpenSourceUtils.net.http;

import java.util.Map;

/**
 * Created by Administrator on 2016/4/1.
 */
public interface OSIHttpLoader {
    void getAsync(String flag, String url,OSIHttpLoaderCallBack callBack, Map<String, String> headers);
    void cancelRequest();

    void postAsync(String flag, String url, OSIHttpLoaderCallBack callBack, Map<String, String> params, Map<String, String> headers);
    void postAsyncRaw(String flag, String url, OSIHttpLoaderCallBack callBack, String json, Map<String, String> headers);
    void deleteAsync(String flag, String url, OSIHttpLoaderCallBack callBack, Map<String, String> params, Map<String, String> headers);
    void putAsync(String flag, String url, OSIHttpLoaderCallBack callBack, Map<String, String> params, Map<String, String> headers);
}
