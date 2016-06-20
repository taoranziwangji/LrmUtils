package com.vdolrm.lrmutils.OpenSourceUtils.net.download;

import com.vdolrm.lrmutils.OpenSourceUtils.net.download.okhttp.TestDownLoadOkHttpImpl;

/**
 * Created by Administrator on 2016/4/5.
 */
public class TestDownLoadPresenter extends OSBaseDownLoadPresenter {
    @Override
    protected OSIDownLoad getOsiDownLoadImpl() {
        return new TestDownLoadOkHttpImpl();
    }
}
