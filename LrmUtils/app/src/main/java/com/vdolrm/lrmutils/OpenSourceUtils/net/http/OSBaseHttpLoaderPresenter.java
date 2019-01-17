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
    public void getAsync(String flag, String url, OSIHttpLoaderCallBack objectCallBack, String userAgent, Map<String, String> headers) {
        if (osiHttpLoader != null)
            osiHttpLoader.getAsync(flag, url, objectCallBack, userAgent, headers);
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
    public void postAsync(String flag, String url, OSIHttpLoaderCallBack callBack, Map<String, String> params, String userAgent, Map<String, String> headers) {
        if (osiHttpLoader != null) {
            osiHttpLoader.postAsync(flag, url, callBack, params, userAgent, headers);
        }
    }

    /**post异步 raw
     * @param url
     * @param callBack
     * @param json
     */
    public void postAsyncRaw(String flag, String url, OSIHttpLoaderCallBack callBack, String json, String userAgent, Map<String, String> headers) {
        if (osiHttpLoader != null) {
            osiHttpLoader.postAsyncRaw(flag, url, callBack, json, userAgent, headers);
        }
    }

    /**delete异步
     * @param url
     * @param callBack
     * @param params
     */
    public void deleteAsync(String flag, String url, OSIHttpLoaderCallBack callBack, Map<String, String> params, String userAgent, Map<String, String> headers) {
        if (osiHttpLoader != null) {
            osiHttpLoader.deleteAsync(flag, url, callBack, params, userAgent, headers);
        }
    }

    /**put异步
     * @param url
     * @param callBack
     * @param params
     */
    public void putAsync(String flag, String url, OSIHttpLoaderCallBack callBack, Map<String, String> params, String userAgent, Map<String, String> headers) {
        if (osiHttpLoader != null) {
            osiHttpLoader.putAsync(flag, url, callBack, params, userAgent, headers);
        }
    }
}
