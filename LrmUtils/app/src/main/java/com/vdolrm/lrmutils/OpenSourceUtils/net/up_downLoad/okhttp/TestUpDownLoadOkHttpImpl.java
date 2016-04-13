package com.vdolrm.lrmutils.OpenSourceUtils.net.up_downLoad.okhttp;

import com.vdolrm.lrmutils.OpenSourceUtils.net.http.okhttp.OkHttpClientManager;
import com.vdolrm.lrmutils.OpenSourceUtils.net.up_downLoad.OSIHttpDownloadCallBack;
import com.vdolrm.lrmutils.OpenSourceUtils.net.up_downLoad.OSIUpDownLoad;

/**
 * Created by Administrator on 2016/4/5.
 */
public class TestUpDownLoadOkHttpImpl implements OSIUpDownLoad {
    @Override
    public void download(String url, String destDir, String fileName, OSIHttpDownloadCallBack callback) {
        OkHttpClientManager.downloadAsyn(url,destDir,fileName,callback);

    }
}
