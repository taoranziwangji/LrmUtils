package com.vdolrm.lrmutils.OpenSourceUtils.net.upload;

import com.vdolrm.lrmutils.OpenSourceUtils.net.download.OSIHttpDownloadCallBack;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/5.
 */
public interface OSIUpLoad {
    void upload(String url, OSIHttpDownloadCallBack callback, File file, String fileKey, Map<String,String> map);
}
