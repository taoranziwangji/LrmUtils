package com.vdolrm.lrmutils;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vdolrm.lrmutils.LogUtils.MyLog;
import com.vdolrm.lrmutils.ThreadUtils.ThreadManager;
import com.vdolrm.lrmutils.UIUtils.UIUtils;
import com.vdolrm.lrmutils.UIUtils.ViewUtils;
import com.vdolrm.lrmutils.Widght.LoadingPage;


public abstract class BaseFragment extends Fragment {
	
	public static final int STATE_SUCCESS = 0;
	public static final int STATE_ERROR = 1;
	public static final int STATE_EMPTY = 2;

	//private View v;
		
	private LoadingPage mContentView;

	/** 加载完成的View */
	public abstract View createLoadedView(boolean reLoading);
	//public abstract int onLoadState();//假如不能得到的话考虑用callback
	public abstract View createLoadingView();
	public abstract View createEmptyView();
	public abstract View createErrorView();
	public abstract View createNetErrorView();
	
	
	private View loadingView;//加载时显示的View
	private View errorView;//加载出错显示的View
	private View emptyView;//加载没有数据显示的View
	private View successView;
	private View netErrorView;
	
	public BaseFragment(){
		loadingView = createLoadingView();
		errorView = createErrorView();
		emptyView = createEmptyView();
		netErrorView = createNetErrorView();
	}
	
	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.d("~~~~~~BaseFragment v=" + mContentView);//v为要缓存的那页的view，比如当前显示第一页 它为第二页的（不管子类是否写|| isVisible ==false ）
		//每次ViewPager要展示该页面时，均会调用该方法获取显示的View
		/*if (v == null) {//为null时，创建一个
			//v = createLoadedView();
		}else{//不为null时，需要把自身从父布局中移除，因为ViewPager会再次添加
			//因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除(return时会自动再添加一个),否则会报：java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
			MyLog.d("remove View");
			ViewUtils.removeSelfFromParent(v);
		}*/
		
		if (mContentView == null) {
			mContentView = new LoadingPage(UIUtils.getContext()) {
				@Override
				public View createNetErrorView() {
					// TODO Auto-generated method stub
					if(netErrorView!=null){
						return netErrorView;
					}
					return super.createNetErrorView();
				}

				@Override
				public View createLoadingView() {
					// TODO Auto-generated method stub
					if(loadingView!=null){
						return loadingView;
					}
					return super.createLoadingView();
				}

				@Override
				public View createEmptyView() {
					// TODO Auto-generated method stub
					if(emptyView!=null){
						return emptyView;
					}
					return super.createEmptyView();
				}

				@Override
				public View createErrorView() {
					// TODO Auto-generated method stub
					if(errorView!=null){
						return errorView;
					}
					return super.createErrorView();
				}

				@Override
				public View createLoadedView(boolean reLoading) {
					return BaseFragment.this.createLoadedView(reLoading);
				}

				@Override
				public Drawable initErrorDrawable() {
					return initBaseFragmentErrorDrawable();
				}

				@Override
				public int initErrorDrawableResId() {
					return initBaseFragmentErrorDrawableResId();
				}
			};
		} else {
			MyLog.d("remove View");
			ViewUtils.removeSelfFromParent(mContentView);
		}
		MyLog.d("#BaseFragment页 执行完onCreateView方法");
		return mContentView;
	}

	
	/** 当显示的时候，加载该页面 */
	public void show(final int state) {
		
		MyLog.d("#BaseFragment页 show方法 mContentView="+mContentView);
		showTask task = new showTask(state);
		ThreadManager.getLongPool().execute(task);
		
	}
	
	
	class showTask implements Runnable {
		int state = -1;
		public showTask(int state){
			this.state = state;
		}
		@Override
		public void run() {
			try {
				Thread.sleep(100);//这个方法和上面的onCreateView方法经测是同时执行（精确到毫秒数都一样，但show方法会比onCreateView的return时先执行 所以给他睡0.1s）
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (mContentView != null) {
				mContentView.show(state);
			}
		}
	}
	

	/** Fragment当前状态是否可见 */
    public boolean isVisible;
     
    /** 
     * 延迟加载
     * 子类必须重写此方法
     */
    public abstract void lazyLoad();
    
  //注释：setUserVisibleHint每次fragment显示与隐藏都会调用，他的调用顺序由于onCreate，所以需要一些常量进行判断
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
         
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
     
     
    /**
     * 可见
     */
    private void onVisible() {
        lazyLoad();     
    }
     
     
    /**
     * 不可见
     */
    private void onInvisible() {
         
         
    }


	/*业务类基类自己实现
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		ActivityAnimationUtil.ActivityCreate(this.getActivity());
	}

	public void startActivityNoAnim(Intent intent){
		super.startActivity(intent);
	}

	public void startActivityBottomAnim(Intent intent){
		startActivityNoAnim(intent);
		ActivityAnimationUtil.ActivityCreateFromBottom(this.getActivity());
	}*/


	/**加载出错时或者为空时或者网络错误时显示的图片（暂没分开获取），先获取resid，假如为空在获取drawable,假如再为空则使用ic_launter*/
	public Drawable initBaseFragmentErrorDrawable(){
		return null;
	}

	/**加载出错时或者为空时或者网络错误时显示的图片（暂没分开获取），先获取resid，假如为空在获取drawable,假如再为空则使用ic_launter*/
	public int initBaseFragmentErrorDrawableResId(){
		return -1;
	}
}

