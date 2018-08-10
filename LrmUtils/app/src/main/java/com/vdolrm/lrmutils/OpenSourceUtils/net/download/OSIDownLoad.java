package com.vdolrm.lrmutils.OpenSourceUtils.net.download;

/**
 * Created by Administrator on 2016/4/5.
 */
public interface OSIDownLoad {
    void download(String flag, String url, String destDir, String fileName, OSIHttpDownloadCallBack callback);
}
