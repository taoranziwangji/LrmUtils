package com.vdolrm.lrmutils.OpenSourceUtils.net.http;

/**
 * Created by Administrator on 2016/4/1.
 */
public interface OSIHttpLoader {
    void getAsync(String url,OSIHttpLoaderCallBack callBack);
    void cancelRequest();
}
