package com.vdolrm.lrmutils.UIUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vdolrm.lrmutils.BaseApplication;
import com.vdolrm.lrmutils.LogUtils.MyLog;


public class UIUtils {
	
	private static Toast toast;

	public static Context getContext() {
		return BaseApplication.getInstance();
	}

	public static Thread getMainThread() {
		return BaseApplication.getMainThread();
	}

	public static long getMainThreadId() {
		return BaseApplication.getMainThreadId();
	}

	/** dip转换px */
	public static int dip2px(int dip) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		MyLog.d("density=" + scale);
		return (int) (dip * scale + 0.5f);
	}

	/** pxz转换dip */
	public static int px2dip(int px) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		MyLog.d("density="+scale);
		return (int) (px / scale + 0.5f);
	}

	/** 获取主线程的handler */
	public static Handler getHandler() {
		return BaseApplication.getMainThreadHandler();
	}

	/** 延时在主线程执行runnable */
	public static boolean postDelayed(Runnable runnable, long delayMillis) {
		return getHandler().postDelayed(runnable, delayMillis);
	}

	/** 在主线程执行runnable */
	public static boolean post(Runnable runnable) {
		return getHandler().post(runnable);
	}

	/** 从主线程looper里面移除runnable */
	public static void removeCallbacks(Runnable runnable) {
		getHandler().removeCallbacks(runnable);
	}

	/**
	 * 废弃，配合butterknife使用时会莫名其妙崩溃，用原生的LayoutInflater.from(context).inflate(R.layout.xx,null);替代
	 * @param resId
	 * @return
	 */
	@Deprecated
	public static View inflate(int resId){
		return inflate(resId,null);
	}

	/**
	 * 废弃，配合butterknife使用时会莫名其妙崩溃，用原生的LayoutInflater.from(context).inflate(R.layout.xx,null);替代
	 * @param resId
	 * @return
	 */
	@Deprecated
	public static View inflate(int resId,ViewGroup root){
		return inflate(resId, root,false);
	}

	/**
	 * 废弃，配合butterknife使用时会莫名其妙崩溃，用原生的LayoutInflater.from(context).inflate(R.layout.xx,null);替代
	 * @param resId
	 * @return
	 */
	@Deprecated
	public static View inflate(int resId,ViewGroup root,boolean attachToRoot){
		return LayoutInflater.from(getContext()).inflate(resId,root,attachToRoot);
	}

	/** 获取资源 */
	public static Resources getResources() {
		return getContext().getResources();
	}

	/** 获取文字 */
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/** 获取文字数组 */
	public static String[] getStringArray(int resId) {
		return getResources().getStringArray(resId);
	}

	/** 获取dimen（px） */
	public static int getDimens(int resId) {
		return getResources().getDimensionPixelSize(resId);
	}

	/** 获取drawable */
	public static Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}

	/** 获取颜色 */
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/** 获取颜色选择�? */
	public static ColorStateList getColorStateList(int resId) {
		return getResources().getColorStateList(resId);
	}
	//判断当前的线程是不是在主线程 
	public static boolean isRunInMainThread() {
		return android.os.Process.myTid() == getMainThreadId();
	}
    
	public static void runInMainThread(Runnable runnable) {
		if (isRunInMainThread()) {
			runnable.run();
		} else {
			post(runnable);
		}
	}

	public static void startActivity(Intent intent){
		//Activity activity = BaseFloorActivity.getForegroundActivity();
		Activity activity = null;
		if(activity != null){
			activity.startActivity(intent);
		}else{
			//MyLog.e(MyLog.printBaseInfo()+" activity is null");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getContext().startActivity(intent);
		}
	}

	/** 对toast的简易封装。线程安全，可以在非UI线程调用。 */
	public static void showToastSafe(final int resId) {
		showToastSafe(getString(resId));
	}

	/** 对toast的简易封装。线程安全，可以在非UI线程调用。 */
	public static void showToastSafe(final String str) {
		if (isRunInMainThread()) {
			showToast(str);
		} else {
			post(new Runnable() {
				@Override
				public void run() {
					showToast(str);
				}
			});
		}
	}
	

	private static void showToast(String str) {
		Activity frontActivity = BaseApplication.getInstance().getStackTopActivity();
		if (frontActivity != null) {
			//Toast.makeText(frontActivity, str, Toast.LENGTH_LONG).show();

			//改成了只显示一次的toast
			if (toast == null) {
				toast = Toast.makeText(frontActivity, str, Toast.LENGTH_SHORT);
			} else {
				toast.setText(str);
			}
			toast.show();
		}else{
			//MyLog.e(MyLog.printBaseInfo()+" activity is null");
			try{
				Toast.makeText(BaseApplication.getInstance(), str, Toast.LENGTH_LONG).show();
			}catch(RuntimeException e){
				e.printStackTrace();
			}
		}
	}
	
	/*public static String getRootPath(){
		return Environment.getExternalStorageDirectory().getAbsolutePath().toString();
	}*/
	
	
	/**添加下划线*/
	public static void setUnderLine(TextView tv){
		if(tv==null)return;
		tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
	}
	
	
	/**跳转到系统webview*/
	public static void gotoWebView(String url,String error){
		try{
			Intent intent= new Intent();
		    intent.setAction("android.intent.action.VIEW");    
		    Uri content_url = Uri.parse(url);
		    intent.setData(content_url);  
		    startActivity(intent);
		}catch(RuntimeException e){
			e.printStackTrace();
			showToastSafe(error);
		}
	}
	
	
	/**获取屏幕的宽*/
	public static int getScreenWidth(Activity a){
		if(a==null)return 0;
		DisplayMetrics metrics=new DisplayMetrics();
		a.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int widthPixels=metrics.widthPixels;
		//int heightPixels=metrics.heightPixels;
		return widthPixels;
	}
	
	
	/**获取屏幕的高*/
	public static int getScreenHeight(Activity a){
		if(a==null)return 0;
		DisplayMetrics metrics=new DisplayMetrics();
		a.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		//int widthPixels=metrics.widthPixels;
		int heightPixels=metrics.heightPixels;
		return heightPixels;
	}
	
	
	/**
	 * 获取当前activity的类名
	 */
	public static String getClassName(Activity a){
		String s = a.getClass().getCanonicalName();
		return s;
	}

	/**
	 * 获取包名
	 */
	public static String getPackageName(Context context) {
		if (context != null)
			return context.getPackageName();
		return "";
	}
}


