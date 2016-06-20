package com.vdolrm.lrmutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public abstract class BaseActivity extends AppCompatActivity {
	/** 记录处于前台的Activity */
	//private static Activity mForegroundActivity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        windowFeature();
        super.onCreate(savedInstanceState);

		BaseApplication.getInstance().addActivity(this);

		//RefWatcher refWatcher = TestApplication.getRefWatcher(this);
		//refWatcher.watch(this);

		initView();
		initToolBar();
		init();
		initEvent();

		bundleInOnCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		//WeakReference<Activity> weakReference = new WeakReference<Activity>(this);
		//mForegroundActivity = weakReference.get();

		//mForegroundActivity = this;
		super.onResume();
	}

	@Override
	protected void onPause() {
		//mForegroundActivity = null;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BaseApplication.getInstance().removeActivity(this);
	}

	//更改窗口样式 可以被重写
	public void windowFeature(){
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	public void bundleInOnCreate(Bundle savedInstanceState){};

	//public abstract void addActivity();
	
	public abstract void initView();
	
	public abstract void init();

	public void initToolBar() {
	}
	
	public abstract void initEvent();



	/** 获取当前处于前台的activity 
	*/
	/*public static Activity getForegroundActivity() {
		return mForegroundActivity;
	}*/



    //TODO 改到业务app自有根baseactivity里
	/*public void toastNetError(){
		UIUtils.showToastSafe("网络不给力，请稍后重试");
	}
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		ActivityAnimationUtil.ActivityCreate(this);
	}

	public void startActivityNoAnim(Intent intent){
		super.startActivity(intent);
	}

	public void startActivityBottomAnim(Intent intent){
		startActivityNoAnim(intent);
		ActivityAnimationUtil.ActivityCreateFromBottom(this);
	}*/
}
