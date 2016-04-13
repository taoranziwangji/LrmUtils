package com.vdolrm.lrmutils.Widght;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.vdolrm.lrmutils.LogUtils.MyLog;
import com.vdolrm.lrmutils.R;
import com.vdolrm.lrmutils.UIUtils.UIUtils;


/**
 * fragment加载数据的逻辑工具类 包括加载中、数据为空、加载错误、加载成功等页面
 *
 * */
public abstract class LoadingPage extends FrameLayout {
	private static final int STATE_UNLOADED = 0;//未知状态
	public static final int STATE_LOADING = 1;//加载状态
	public static final int STATE_ERROR = 3;//加载完毕，但是出错状态
	public static final int STATE_EMPTY = 4;//加载完毕，但是没有数据状态
	public static final int STATE_NETERROR = 5;//网络错误
	public static final int STATE_SUCCEED = 6;//加载成功

	private View mLoadingView;//加载时显示的View
	private View mErrorView;//加载出错显示的View
	private View mEmptyView;//加载没有数据显示的View
	private View mNetErrorView;//网络错误的View
	private View mSucceedView;//加载成功显示的View

	private int mState;//当前的状态，显示需要根据该状态判断
	
	public ClickListener clickListener = new ClickListener();
	
	public abstract View createLoadedView(boolean reLoading);

	public LoadingPage(Context context) {
		super(context);
		init();
	}

	private void init() {
		//setBackgroundColor(UIUtils.getColor(R.color.bg_page));//设置背景
		mState = STATE_UNLOADED;//初始化状态

		//创建对应的View，并添加到布局中
		mLoadingView = createLoadingView();
		if (null != mLoadingView) {
			addView(mLoadingView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}

		mErrorView = createErrorView();
		if (null != mErrorView) {
			addView(mErrorView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}

		mEmptyView = createEmptyView();
		if (null != mEmptyView) {
			addView(mEmptyView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		
		mNetErrorView = createNetErrorView();
		if (null != mNetErrorView) {
			addView(mNetErrorView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		
		// 只有数据成功返回了，才知道成功的View该如何显示，因为该View的显示依赖加载完毕的数据
		mSucceedView = createLoadedView(false);
		if(mSucceedView!=null){
			addView(mSucceedView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
				
		//显示对应的View
		showPageSafe();
	}

	/** 线程安全的方法 */
	private void showPageSafe() {
		UIUtils.runInMainThread(new Runnable() {
			@Override
			public void run() {
				showPage();
			}
		});
	}

	/** 显示对应的View */
	private void showPage() {
		MyLog.d("^^^^^^^^^^mState=" + mState);
		if (null != mLoadingView) {
			mLoadingView.setVisibility(mState == STATE_UNLOADED || mState == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
			//MyLog.d("^^^^^mLoadingView ="+mLoadingView.getVisibility());
		}
		if (null != mErrorView) {
			mErrorView.setVisibility(mState == STATE_ERROR ? View.VISIBLE : View.INVISIBLE);//invisible也可以换乘gone,但性能不好，因为再visiable时需要再次测量onmeasure 影响性能
			//MyLog.d("^^^^^mErrorView ="+mErrorView.getVisibility());
		}
		if (null != mEmptyView) {
			mEmptyView.setVisibility(mState == STATE_EMPTY ? View.VISIBLE : View.INVISIBLE);
			//MyLog.d("^^^^^mEmptyView ="+mEmptyView.getVisibility());
		}
		
		if(mNetErrorView!=null){
			mNetErrorView.setVisibility(mState == STATE_NETERROR ? View.VISIBLE : View.INVISIBLE);
			//MyLog.d("^^^^^mNetErrorView ="+mNetErrorView.getVisibility());
		}

		if (null != mSucceedView) {
			mSucceedView.setVisibility(mState == STATE_SUCCEED ? View.VISIBLE : View.INVISIBLE);
			//MyLog.d("^^^^^mSucceedView ="+mSucceedView.getVisibility());
		}
	}

	/** 恢复状态 */
	private void reset() {
		mState = STATE_UNLOADED;
		showPageSafe();
	}

	/** 是否需要恢复状态 */
	/*protected boolean needReset() {
		return mState == STATE_ERROR || mState == STATE_EMPTY;
	}*/

	/** 有外部调用，会根据状态判断是否引发load */
	public synchronized void show(int state) {
		MyLog.d("^传递进来的state="+state);
		mState = state;//add
		
		if (mState == STATE_UNLOADED) {
			mState = STATE_LOADING;
			
		}
		showPageSafe();
	}

	public View createLoadingView() {
		return UIUtils.inflate(R.layout.vdo_loading_page_loading);
	}

	public View createEmptyView() {
		Drawable drawable = initErrorDrawable();
		int resid = initErrorDrawableResId();
		MyLog.d("createEmptyView drawable="+drawable+",resid="+resid);
		View view = UIUtils.inflate(R.layout.vdo_loading_page_empty);
		ImageView imv_icon = (ImageView)view.findViewById(R.id.page_iv);
		if(resid!=-1){
			imv_icon.setImageResource(resid);
		}else if(drawable!=null){
			imv_icon.setImageDrawable(drawable);
		}
		view.findViewById(R.id.rel_empty_btn).setOnClickListener(clickListener);
		return view;
	}

	public View createErrorView() {
		Drawable drawable = initErrorDrawable();
		int resid = initErrorDrawableResId();
		MyLog.d("createErrorView drawable="+drawable+",resid="+resid);
		View view = UIUtils.inflate(R.layout.vdo_loading_page_error);
		ImageView imv_icon = (ImageView)view.findViewById(R.id.page_iv);
		if(resid!=-1){
			imv_icon.setImageResource(resid);
		}else if(drawable!=null){
			imv_icon.setImageDrawable(drawable);
		}
		view.findViewById(R.id.rel_error_btn).setOnClickListener(clickListener);
		return view;
	}

	
	public View createNetErrorView() {
		Drawable drawable = initErrorDrawable();
		int resid = initErrorDrawableResId();
		MyLog.d("createNetErrorView drawable="+drawable+",resid="+resid);
		View view = UIUtils.inflate(R.layout.vdo_loading_page_neterror);
		ImageView imv_icon = (ImageView)view.findViewById(R.id.page_iv);
		if(resid!=-1){
			imv_icon.setImageResource(resid);
		}else if(drawable!=null){
			imv_icon.setImageDrawable(drawable);
		}
		view.findViewById(R.id.rel_neterror_btn).setOnClickListener(clickListener);
		return view;
	}


	class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			MyLog.d("click");
			//show(STATE_ERROR);
			reset();
			createLoadedView(true);
		}
		
	}

	/**加载出错时或者为空时或者网络错误时显示的图片（暂没分开获取），先获取resid，假如为空在获取drawable,假如再为空则使用ic_launter*/
	public Drawable initErrorDrawable(){
		return null;
	}

	/**加载出错时或者为空时或者网络错误时显示的图片（暂没分开获取），先获取resid，假如为空在获取drawable,假如再为空则使用ic_launter*/
	public int initErrorDrawableResId(){
		return -1;
	}
	
}
