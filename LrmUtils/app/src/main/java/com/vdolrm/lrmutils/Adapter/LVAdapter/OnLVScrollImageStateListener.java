package com.vdolrm.lrmutils.Adapter.LVAdapter;

import android.content.Context;
import android.widget.AbsListView;

import com.vdolrm.lrmutils.LogUtils.MyLog;

/**
 * Created by Administrator on 2016/3/29.
 */
public class OnLVScrollImageStateListener implements AbsListView.OnScrollListener {
    private final Context context;
    //private final OSBaseImageLoaderPresenter osImageLoaderPresenter;

    public OnLVScrollImageStateListener(Context context) {
        this.context = context;
        //this.osImageLoaderPresenter = osImageLoaderPresenter;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(context==null){
            MyLog.e(MyLog.printBaseInfo() + " context or osImageLoaderPresenter is null");
            return;
        }
        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {//不自动滚动时或者用手指让他滚动时resume
            //osImageLoaderPresenter.resumeTag(context);
        } else {
            //osImageLoaderPresenter.pauseTag(context);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        // Do nothing.
    }
}
