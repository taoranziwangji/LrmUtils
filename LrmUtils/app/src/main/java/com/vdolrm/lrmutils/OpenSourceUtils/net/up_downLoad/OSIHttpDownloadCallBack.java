package com.vdolrm.lrmutils.OpenSourceUtils.net.up_downLoad;

import com.vdolrm.lrmutils.OpenSourceUtils.net.http.OSIHttpLoaderCallBack;

/**
 * 传入泛型String,onSuccess返回的形参为下载成功的绝对路径
 * Created by Administrator on 2016/4/5.
 */
public abstract class OSIHttpDownloadCallBack<T> extends OSIHttpLoaderCallBack<T> {

    public abstract void onLoading(long total, long current, boolean isUploading);

    @Override
    public final void onErrorGsonException(String response, Exception e) {
        //下载时不会被调用到，写成final
    }

}
