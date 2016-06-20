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
    public void getAsync(String url, OSIHttpLoaderCallBack callBack) {
        call = OkHttpClientManager.getAsyn(url, callBack);
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
    public void postAsync(String url, OSIHttpLoaderCallBack callBack, Map<String, String> params) {

        OkHttpClientManager.postAsyn(url, callBack, params);
    }
}