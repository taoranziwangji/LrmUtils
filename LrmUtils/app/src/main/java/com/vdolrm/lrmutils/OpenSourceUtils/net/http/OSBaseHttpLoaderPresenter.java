package com.vdolrm.lrmutils.OpenSourceUtils.net.http;

import java.util.Map;

/**
 * Created by Administrator on 2016/4/1.
 */
public abstract class OSBaseHttpLoaderPresenter {
    protected abstract OSIHttpLoader getOsiHttpLoaderImpl();

    private OSIHttpLoader osiHttpLoader;

    public OSBaseHttpLoaderPresenter() {
        osiHttpLoader = getOsiHttpLoaderImpl();
    }


    /**
     * 加载
     *
     * @param url
     * @param objectCallBack
     */
    public void load(String url, OSIHttpLoaderCallBack objectCallBack) {
        if (osiHttpLoader != null)
            osiHttpLoader.getAsync(url, objectCallBack);
    }


    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        if (osiHttpLoader != null) {
            osiHttpLoader.cancelRequest();
        }
    }

    /**
     * post异步
     *
     * @param url
     * @param callBack
     * @param params
     */
    public void postAsync(String url, OSIHttpLoaderCallBack callBack, Map<String, String> params) {
        if (osiHttpLoader != null) {
            osiHttpLoader.postAsync(url, callBack, params);
        }
    }

    /**post异步 raw
     * @param url
     * @param callBack
     * @param json
     */
    public void postAsyncRaw(String url, OSIHttpLoaderCallBack callBack, String json) {
        if (osiHttpLoader != null) {
            osiHttpLoader.postAsyncRaw(url, callBack, json);
        }
    }

    /**delete异步
     * @param url
     * @param callBack
     * @param params
     */
    public void deleteAsync(String url, OSIHttpLoaderCallBack callBack, Map<String, String> params) {
        if (osiHttpLoader != null) {
            osiHttpLoader.deleteAsync(url, callBack, params);
        }
    }

    /**put异步
     * @param url
     * @param callBack
     * @param params
     */
    public void putAsync(String url, OSIHttpLoaderCallBack callBack, Map<String, String> params) {
        if (osiHttpLoader != null) {
            osiHttpLoader.putAsync(url, callBack, params);
        }
    }
}
