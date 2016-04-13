package com.vdolrm.lrmutils.OpenSourceUtils.net.http;

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
     * @param url
     * @param objectCallBack
     */
    public void load(String url, OSIHttpLoaderCallBack objectCallBack) {
        if (osiHttpLoader != null)
            osiHttpLoader.getAsync(url,objectCallBack);
    }


    /**
     * 取消网络请求
     */
    public void cancelRequest(){
        if(osiHttpLoader!=null){
            osiHttpLoader.cancelRequest();
        }
    }
}
