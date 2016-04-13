package com.vdolrm.lrmutils.Adapter.RecyclerViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vdolrm.lrmutils.LogUtils.MyLog;
import com.vdolrm.lrmutils.OpenSourceUtils.img.OSBaseImageLoaderPresenter;

/**
 * Created by Administrator on 2016/3/29.
 */
public class OnRecyclerViewScrollImageStateListener extends RecyclerView.OnScrollListener {
    private final Context context;
    private final OSBaseImageLoaderPresenter osImageLoaderPresenter;

    public OnRecyclerViewScrollImageStateListener(Context context,OSBaseImageLoaderPresenter osImageLoaderPresenter) {
        this.context = context;
        this.osImageLoaderPresenter = osImageLoaderPresenter;
    }

    @Override
    public void onScrollStateChanged(RecyclerView view, int scrollState) {
        if(context==null || osImageLoaderPresenter==null){
            MyLog.e(MyLog.printBaseInfo()+" context or osImageLoaderPresenter is null");
            return;
        }
        if (scrollState == RecyclerView.SCROLL_STATE_IDLE || scrollState == RecyclerView.SCROLL_STATE_DRAGGING) {//不自动滚动时或者用手指让他滚动时resume
           // picasso.resumeTag(context);
            osImageLoaderPresenter.resumeTag(context);
        } else {
            //picasso.pauseTag(context);
            osImageLoaderPresenter.pauseTag(context);
        }
    }
}
