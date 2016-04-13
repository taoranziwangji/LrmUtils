package com.vdolrm.lrmutils.OpenSourceUtils.img;

import android.content.Context;
import android.widget.ImageView;

import com.vdolrm.lrmutils.LogUtils.MyLog;
import com.vdolrm.lrmutils.OpenSourceUtils.img.picasso.OSBaseIImageLoaderPicassoImpl;

import java.lang.ref.WeakReference;

/**
 * 由于单例基类不便于继承，所以子类必须自己定义单例方法！！！
 * 图片加载统一控制器基类，可以调用不同的OSIImageLoader接口的实现类，方便更换不同的图片加载框架，
 * Created by Administrator on 2016/3/29.
 */
public abstract class OSBaseImageLoaderPresenter {

    /**
     * @param context
     * @return 返回OSIImageLoader接口的具体实例，即各个图片加载框架的具体实现方法的实现类,ex:{@link OSBaseIImageLoaderPicassoImpl}
     */
    public abstract OSIImageLoader getOsiImageLoaderImpl(Context context);

    /**
     * @return 子类是否创建了单例方法，false时会抛出异常
     */
    public abstract boolean isCreateASingleInstance();

    private OSIImageLoader osiImageLoader;

    public OSBaseImageLoaderPresenter(Context context) {
        if(isCreateASingleInstance()){
            osiImageLoader = getOsiImageLoaderImpl(new WeakReference<Context>(context).get());
        }else{
            MyLog.e(MyLog.printSimpleBaseInfo()+"子类必须创建自己的单例方法！！");
            throw new IllegalStateException("子类必须创建自己的单例方法！！");

        }

    }


    /**
     * 加载图片
     *
     * @param url
     * @param imageView
     */
    public void load(String url, ImageView imageView) {
        if (osiImageLoader != null)
            osiImageLoader.load(url, imageView);
    }


    /**
     * 设置listview、recyclerView恢复滚动时的标签
     *
     * @param tag 一般情况传context
     */
    public void resumeTag(Object tag) {
        if (osiImageLoader != null)
            osiImageLoader.resumeTag(tag);
    }

    /**
     * 设置listview、recyclerView暂停滚动时的标签
     *
     * @param tag 一般情况传context
     */
    public void pauseTag(Object tag) {
        if (osiImageLoader != null)
            osiImageLoader.pauseTag(tag);
    }

    /**
     * 设置listview、recyclerView页面销毁时的标签
     *
     * @param tag 一般情况传context
     */
    public void cancelTag(Object tag) {
        if (osiImageLoader != null)
            osiImageLoader.cancelTag(tag);
    }


}
