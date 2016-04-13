package com.vdolrm.lrmutils.OpenSourceUtils.net.http;

import com.vdolrm.lrmutils.OpenSourceUtils.net.http.okhttp.TestOkHttpImpl;

/**
 * 不要写成单例，因为TestOkHttpImpl会维护一个全局的call作为取消网络请求的对象，而多并发的请求应该有多个call对象来维持
 * Created by Administrator on 2016/4/1.
 */
public class TestHttpLoaderPresenter extends OSBaseHttpLoaderPresenter {

    @Override
    protected OSIHttpLoader getOsiHttpLoaderImpl() {
        return new TestOkHttpImpl();
    }

}
