package com.vdolrm.lrmutils.OpenSourceUtils.net.upload.okhttp;

import com.vdolrm.lrmutils.LogUtils.MyLog;
import com.vdolrm.lrmutils.OpenSourceUtils.net.download.OSIHttpDownloadCallBack;
import com.vdolrm.lrmutils.OpenSourceUtils.net.http.okhttp.OkHttpClientManager;
import com.vdolrm.lrmutils.OpenSourceUtils.net.upload.OSIUpLoad;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/5.
 */
public class TestUpLoadOkHttpImpl implements OSIUpLoad {

    @Override
    public void upload(String url, OSIHttpDownloadCallBack callback, File file, String fileKey, Map<String, String> map) {
        try {
            if (map != null) {
                Collection<String> keyset = map.keySet();
                List<String> list = new ArrayList<>(keyset);
                OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    OkHttpClientManager.Param p = new OkHttpClientManager.Param(list.get(i), map.get(list.get(i)));
                    params[i] = p;
                }

                OkHttpClientManager.postAsyn(url, callback, file, fileKey, params);
            } else {
                OkHttpClientManager.postAsyn(url, callback, file, fileKey);
            }
        } catch (IOException e) {
            e.printStackTrace();
            MyLog.e("上传失败" + MyLog.printSimpleBaseInfo());
        }
    }
}
