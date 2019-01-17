package com.vdolrm.lrmutils.OpenSourceUtils.net.http.okhttp;

import com.vdolrm.lrmutils.OpenSourceUtils.net.http.OSIHttpLoader;
import com.vdolrm.lrmutils.OpenSourceUtils.net.http.OSIHttpLoaderCallBack;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/1.
 */
public class TestOkHttpImpl implements OSIHttpLoader {

    private Call call;

    @Override
    public void getAsync(String flag, String url, OSIHttpLoaderCallBack callBack, String userAgent, Map<String, String> headers) {
        call = OkHttpClientManager.getAsyn(flag, url, callBack, userAgent, headers);
    }

    @Override
    public void cancelRequest() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }

    }

    /**
     * post异步
     * @param url
     * @param callBack
     * @param params
     */
    @Override
    public void postAsync(String flag, String url, OSIHttpLoaderCallBack callBack, Map<String, String> params, String userAgent, Map<String, String> headers) {

        OkHttpClientManager.postAsyn(flag, url, callBack, params, userAgent, headers);
    }

    /**
     * @param url
     * @param callBack
     * @param json
     */
    @Override
    public void postAsyncRaw(String flag, String url, OSIHttpLoaderCallBack callBack, String json, String userAgent, Map<String, String> headers) {
        OkHttpClientManager.postAsynRaw(flag, url, callBack, json, userAgent, headers);
    }

    @Override
    public void deleteAsync(String flag, String url, OSIHttpLoaderCallBack callBack, Map<String, String> params, String userAgent, Map<String, String> headers) {
        OkHttpClientManager.deleteAsyn(flag, url, callBack, params, userAgent, headers);
    }

    @Override
    public void putAsync(String flag, String url, OSIHttpLoaderCallBack callBack, Map<String, String> params, String userAgent, Map<String, String> headers) {
        OkHttpClientManager.putAsyn(flag, url, callBack, params, userAgent, headers);
    }
}