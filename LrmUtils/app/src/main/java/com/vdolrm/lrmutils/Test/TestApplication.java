package com.vdolrm.lrmutils.Test;

import com.vdolrm.lrmutils.BaseApplication;

/**
 * Created by Administrator on 2016/3/28.
 */
public class TestApplication extends BaseApplication {

//    public static RefWatcher getRefWatcher(Context context) {
//        TestApplication application = (TestApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }
//    private RefWatcher refWatcher;

    @Override
    public String getCrashPath() {
        return null;
    }

    @Override
    public Class<?> getFirstActivity() {
        return TestMainActivity.class;
    }

    @Override
    public void initOther() {
        //refWatcher = LeakCanary.install(this);
    }

    @Override
    public int iconForQuickProgram() {
        return 0;
    }

    @Override
    public void destroyOther() {

    }
}
