package com.vdolrm.lrmutils.OpenSourceUtils.net.upload;

import com.vdolrm.lrmutils.OpenSourceUtils.net.upload.okhttp.TestUpLoadOkHttpImpl;

/**
 * Created by Administrator on 2016/4/5.
 */
public class TestUpLoadPresenter extends OSBaseUpLoadPresenter {

    @Override
    protected OSIUpLoad getOsiUpLoadImpl() {
        return new TestUpLoadOkHttpImpl();
    }
}
