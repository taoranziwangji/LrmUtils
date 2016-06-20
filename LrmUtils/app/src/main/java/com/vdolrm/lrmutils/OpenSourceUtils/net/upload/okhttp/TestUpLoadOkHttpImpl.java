package com.vdolrm.lrmutils.OpenSourceUtils.net.upload.okhttp;

import com.vdolrm.lrmutils.LogUtils.MyLog;
import com.vdolrm.lrmutils.OpenSourceUtils.net.http.okhttp.OkHttpClientManager;
import com.vdolrm.lrmutils.OpenSourceUtils.net.download.OSIHttpDownloadCallBack;
import com.vdolrm.lrmutils.OpenSourceUtils.net.upload.OSIUpLoad;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/4/5.
 */
public class TestUpLoadOkHttpImpl implements OSIUpLoad {

    @Override
    public void upload(String url, OSIHttpDownloadCallBack callback, File file, String fileKey) {
        try {
            OkHttpClientManager.postAsyn(url,callback,file,fileKey);
        } catch (IOException e) {
            e.printStackTrace();
            MyLog.e("上传失败"+ MyLog.printSimpleBaseInfo());
        }
    }
}
