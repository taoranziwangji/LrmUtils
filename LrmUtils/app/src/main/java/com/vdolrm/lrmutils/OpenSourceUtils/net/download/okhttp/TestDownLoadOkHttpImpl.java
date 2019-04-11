package com.vdolrm.lrmutils.OpenSourceUtils.net.download.okhttp;

import com.vdolrm.lrmutils.OpenSourceUtils.net.http.okhttp.OkHttpClientManager;
import com.vdolrm.lrmutils.OpenSourceUtils.net.download.OSIHttpDownloadCallBack;
import com.vdolrm.lrmutils.OpenSourceUtils.net.download.OSIDownLoad;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/5.
 */
public class TestDownLoadOkHttpImpl implements OSIDownLoad {

    private Call call;

    @Override
    public void download(String flag, String url, String destDir, String fileName, OSIHttpDownloadCallBack callback) {
        call = OkHttpClientManager.downloadAsyn(flag, url,destDir,fileName,callback);

    }

    @Override
    public void cancelRequest() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }
}
