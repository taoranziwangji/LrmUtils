package com.vdolrm.lrmutils.Adapter.RecyclerViewAdapter;

import android.content.Context;

import com.vdolrm.lrmutils.LogUtils.MyLog;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2016/3/29.
 */
public class OnRecyclerViewScrollImageStateListener extends RecyclerView.OnScrollListener {
    private final Context context;
    //private final OSBaseImageLoaderPresenter osImageLoaderPresenter;

    public OnRecyclerViewScrollImageStateListener(Context context) {
        this.context = context;
        //this.osImageLoaderPresenter = osImageLoaderPresenter;
    }

    @Override
    public void onScrollStateChanged(RecyclerView view, int scrollState) {
        if(context==null ){
            MyLog.e(MyLog.printBaseInfo()+" context or osImageLoaderPresenter is null");
            return;
        }
        if (scrollState == RecyclerView.SCROLL_STATE_IDLE || scrollState == RecyclerView.SCROLL_STATE_DRAGGING) {//不自动滚动时或者用手指让他滚动时resume
            //osImageLoaderPresenter.resumeTag(context);
        } else {
            //osImageLoaderPresenter.pauseTag(context);
        }
    }
}
