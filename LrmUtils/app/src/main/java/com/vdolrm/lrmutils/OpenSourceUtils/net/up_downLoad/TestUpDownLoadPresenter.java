package com.vdolrm.lrmutils.OpenSourceUtils.net.up_downLoad;

import com.vdolrm.lrmutils.OpenSourceUtils.net.up_downLoad.okhttp.TestUpDownLoadOkHttpImpl;

/**
 * Created by Administrator on 2016/4/5.
 */
public class TestUpDownLoadPresenter extends OSBaseUpDownLoadPresenter {
    @Override
    protected OSIUpDownLoad getOsiUpDownLoadImpl() {
        return new TestUpDownLoadOkHttpImpl();
    }
}
