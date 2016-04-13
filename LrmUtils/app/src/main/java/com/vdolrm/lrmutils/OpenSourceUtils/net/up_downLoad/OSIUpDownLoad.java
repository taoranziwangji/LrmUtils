package com.vdolrm.lrmutils.OpenSourceUtils.net.up_downLoad;

/**
 * Created by Administrator on 2016/4/5.
 */
public interface OSIUpDownLoad {
    void download(String url, String destDir, String fileName, OSIHttpDownloadCallBack callback);
}
