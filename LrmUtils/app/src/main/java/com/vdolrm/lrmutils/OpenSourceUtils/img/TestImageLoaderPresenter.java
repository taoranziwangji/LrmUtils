package com.vdolrm.lrmutils.OpenSourceUtils.img;

import android.content.Context;

import com.vdolrm.lrmutils.OpenSourceUtils.img.picasso.TestPicassoImpl;

import java.lang.ref.WeakReference;

/**
 * 作为对图片处理的总控制类，不应该在业务app中直接使用，而且名字也不该带有具体框架的名字，而应该由业务app仿照此类创建一个新的，便于修改不同框架的回调实现类 {@link #getOsiImageLoaderImpl}
 * Created by Administrator on 2016/3/29.
 */
public class TestImageLoaderPresenter extends OSBaseImageLoaderPresenter {
    private static OSBaseImageLoaderPresenter instance;

    private TestImageLoaderPresenter(Context context) {
        super(context);
    }

    public static OSBaseImageLoaderPresenter getInstance(Context context) {
        WeakReference<Context> weakContext = new WeakReference<Context>(context);
        if (instance == null) {
            synchronized (TestImageLoaderPresenter.class) {
                if (instance == null) {
                    instance = new TestImageLoaderPresenter(weakContext.get());
                }
            }
        }
        return instance;

    }

    @Override
    public OSIImageLoader getOsiImageLoaderImpl(Context context) {
        return new TestPicassoImpl(context);
    }

    @Override
    public boolean isCreateASingleInstance() {
        return true;
    }
}
