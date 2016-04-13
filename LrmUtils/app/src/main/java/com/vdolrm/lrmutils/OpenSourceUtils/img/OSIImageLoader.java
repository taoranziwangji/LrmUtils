package com.vdolrm.lrmutils.OpenSourceUtils.img;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/3/29.
 */
public interface OSIImageLoader {
    void load(String url,ImageView imageView);
    void resumeTag(Object tag);
    void pauseTag(Object tag);
    void cancelTag(Object tag);
}
