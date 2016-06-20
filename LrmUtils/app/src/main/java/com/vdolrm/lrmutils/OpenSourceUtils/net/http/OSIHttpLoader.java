package com.vdolrm.lrmutils.OpenSourceUtils.net.http;

import java.util.Map;

/**
 * Created by Administrator on 2016/4/1.
 */
public interface OSIHttpLoader {
    void getAsync(String url,OSIHttpLoaderCallBack callBack);
    void cancelRequest();

    void postAsync(String url, OSIHttpLoaderCallBack callBack, Map<String, String> params);
}
