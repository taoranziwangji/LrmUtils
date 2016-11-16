package com.vdolrm.lrmutils.OpenSourceUtils.net.upload;

import com.vdolrm.lrmutils.OpenSourceUtils.net.download.OSIHttpDownloadCallBack;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/5.
 */
public abstract class OSBaseUpLoadPresenter {
    protected abstract OSIUpLoad getOsiUpLoadImpl();

    private OSIUpLoad osiHttpLoader;

    public OSBaseUpLoadPresenter() {
        osiHttpLoader = getOsiUpLoadImpl();
    }


    /**
     * 上传
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     */
    public void upload(String url, OSIHttpDownloadCallBack callback, File file, String fileKey, Map<String,String> map) {
        if (osiHttpLoader != null)
            osiHttpLoader.upload(url, callback, file, fileKey,map);
    }
}
