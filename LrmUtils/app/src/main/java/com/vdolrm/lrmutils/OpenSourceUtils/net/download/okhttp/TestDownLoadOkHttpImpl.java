package com.vdolrm.lrmutils.OpenSourceUtils.net.download.okhttp;

import com.vdolrm.lrmutils.OpenSourceUtils.net.http.okhttp.OkHttpClientManager;
import com.vdolrm.lrmutils.OpenSourceUtils.net.download.OSIHttpDownloadCallBack;
import com.vdolrm.lrmutils.OpenSourceUtils.net.download.OSIDownLoad;

/**
 * Created by Administrator on 2016/4/5.
 */
public class TestDownLoadOkHttpImpl implements OSIDownLoad {
    @Override
    public void download(String flag, String url, String destDir, String fileName, OSIHttpDownloadCallBack callback) {
        OkHttpClientManager.downloadAsyn(flag, url,destDir,fileName,callback);

    }
}
