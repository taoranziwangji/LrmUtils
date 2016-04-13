package com.vdolrm.lrmutils.OpenSourceUtils.img.picasso;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vdolrm.lrmutils.LogUtils.MyLog;
import com.vdolrm.lrmutils.OpenSourceUtils.img.OSBaseImageLoaderPresenter;
import com.vdolrm.lrmutils.OpenSourceUtils.img.OSIImageLoader;
import com.vdolrm.lrmutils.R;

import java.lang.ref.WeakReference;

/**
 * picasso加载图片的简易实现，假如业务app需要自定义样式或设置错误图片、空图片等可以继承此类重写load方法，并创建 {@link OSBaseImageLoaderPresenter ｝的子类返回
 * Created by Administrator on 2016/3/29.
 */
public class OSBaseIImageLoaderPicassoImpl implements OSIImageLoader {
    private WeakReference<Context> weakContext;
    private WeakReference<ImageView> weakImageView;

    public OSBaseIImageLoaderPicassoImpl(Context context) {
        weakContext = new WeakReference<Context>(context);
    }

    @Override
    public void load(String url, ImageView imageView) {

        weakImageView = new WeakReference<ImageView>(imageView);

        if(weakContext!=null && weakContext.get()!=null) {
            try {
                Picasso.with(weakContext.get())
                        .load(url)
                        .fit()
                        .placeholder(R.mipmap.ic_launcher) //
                        .error(R.mipmap.ic_launcher) //
                        .tag(weakContext.get())
                        .into(weakImageView.get());
            } catch (RuntimeException e) {
                e.printStackTrace();
                MyLog.e(MyLog.printBaseInfo() + ",e=" + e.getMessage());
            }
        }
    }

    @Override
    public void resumeTag(Object tag) {
        if (weakContext != null && weakContext.get() != null && tag!=null)
            Picasso.with(weakContext.get()).resumeTag(tag);
    }

    @Override
    public void pauseTag(Object tag) {
        if (weakContext != null && weakContext.get() != null && tag!=null)
            Picasso.with(weakContext.get()).pauseTag(tag);
    }

    @Override
    public void cancelTag(Object tag) {
        if (weakContext != null && weakContext.get() != null && tag!=null) {
            Picasso.with(weakContext.get()).cancelTag(tag);
        }
    }
}
