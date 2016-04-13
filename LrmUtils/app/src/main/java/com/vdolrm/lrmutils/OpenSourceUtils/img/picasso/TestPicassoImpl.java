package com.vdolrm.lrmutils.OpenSourceUtils.img.picasso;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vdolrm.lrmutils.R;

import java.lang.ref.WeakReference;

/**
 * 业务app应该仿照此类写一个新的作为图片加载的不同框架的实现类（因为错误图片使用的ic_launcher）
 * Created by Administrator on 2016/3/29.
 */
public class TestPicassoImpl extends OSBaseIImageLoaderPicassoImpl {
    //private Context context;
    private WeakReference<Context> weakContext;
    public TestPicassoImpl(Context context) {
        super(context);
        weakContext = new WeakReference<Context>(context);
    }

    //举个例子而已，实际跟父类没区别，此处可更改为业务app的自己的逻辑
    @Override
    public void load(String url, ImageView imageView) {
        Context context = weakContext.get();
        Picasso.with(weakContext.get())
                .load(url)
                .fit()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .tag(context)
                .into(imageView);
    }
}
