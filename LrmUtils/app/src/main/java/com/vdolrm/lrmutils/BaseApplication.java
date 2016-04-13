package com.vdolrm.lrmutils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.widget.Toast;


import com.vdolrm.lrmutils.LogUtils.MyLog;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseApplication extends Application {
    /**
     * 全局Context，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了
     */
    private static BaseApplication mInstance;
    /**
     * 主线程ID
     */
    private static int mMainThreadId = -1;
    /**
     * 主线程ID
     */
    private static Thread mMainThread;
    /**
     * 主线程Handler
     */
    private static Handler mMainThreadHandler;
    /**
     * 主线程Looper
     */
    private static Looper mMainLooper;

    private List<Activity> activitys = null;//必须写成static的 ，否则调用addactivity时会报空！可能是又新建了一个

    /**子类application的对象*/
    //public abstract BaseApplication getapplication();

    /**
     * 崩溃时系统日志保存的路径 UIUtils.getRootPath()+"/xxx/CrashInfos
     */
    public abstract String getCrashPath();

    /**
     * 程序的第一个页面，用于崩溃重启时的页面跳转
     */
    public abstract Class<?> getFirstActivity();

    public abstract void initOther();

    /**
     * 快捷方式的图标 默认为0即不创建，假如需要创建则需传图片的resourceId
     * 要在AndroidManifest.xml中写<uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />权限
     */
    public abstract int iconForQuickProgram();
    /*public  int  iconForQuickProgram(){
		return R.drawable.ic_launcher;
	};*/

    /**
     * 执行退出时的其他的一些操作 如果有的话
     */
    public abstract void destroyOther();

    @Override
    public void onCreate() {
        MyLog.d("application onCreate");
        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mMainThreadHandler = new Handler();
        mMainLooper = getMainLooper();
        mInstance = this;
        if (activitys == null) {
            activitys = new LinkedList<Activity>();
        }
        super.onCreate();


        if (iconForQuickProgram() != 0) {
            try {
                //创建桌面快捷方式
                SharedPreferences shortcutpref = this.getSharedPreferences(Contant.isCreateDesk, Context.MODE_PRIVATE);
                boolean iscreated = shortcutpref.getBoolean("iscreated", false);
                if (!iscreated) {
                    try {
                        createDeskShortCut(iconForQuickProgram());
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }

            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        initOther();

    }

    public static synchronized BaseApplication getInstance() {
        if (mInstance == null) {
            //mInstance = getApplication();//20151119可能会死循环
            //mInstance = new BaseApplication();//不用这么写，肯定有值，直接返回就行(application本身就是单例)
        }
        return mInstance;
    }

    /**
     * 获取主线程ID
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 获取主线�?
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 获取主线程的looper
     */
    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }


    /**
     * 添加Activity到容器中
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activitys == null) {
            activitys = new LinkedList<Activity>();
        }

        if (activitys.size() > 0) {
            if (!activitys.contains(activity)) {
                activitys.add(activity);
                MyLog.d("addActivity add " + activity);
            }
        } else {
            activitys.add(activity);
            MyLog.d("addActivity add " + activity);
        }

    }

    public void removeActivity(Activity activity){
        if(activitys!=null && activitys.size()>0 && activitys.contains(activity)){
            activitys.remove(activity);
            MyLog.d("removeActivity "+activity);
        }
    }



    private void exit_abstract(Context context) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                if (activity != null) {
                    MyLog.d("finish " + activity);
                    activity.finish();
                }
            }
        }

        activitys.clear();
        activitys = null;
    }


    /**
     * 遍历所有Activity并finish
     * @param context
     */
    public void exit(Context context) {
        if (activitys != null && activitys.size() > 0) {
            MyLog.d("exit activity.size= " + activitys.size());
            exit_abstract(context);
        }

        destroyOther();
    }


    /* ** 
     * 创建快捷方式 
     */
    private void createDeskShortCut(int resourceId) {
        // 创建快捷方式的Intent  
        Intent shortcutIntent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建  
        shortcutIntent.putExtra("duplicate", false);
        // 需要显示的名称  
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                getString(R.string.app_name));

        // 快捷图片  
        Parcelable icon = Intent.ShortcutIconResource.fromContext(
                getApplicationContext(), resourceId);

        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

        Intent intent = new Intent(getApplicationContext(),
                getFirstActivity());
        // 下面两个属性是为了当应用程序卸载时桌面 上的快捷方式会删除  
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        // 点击快捷图片，运行的程序主入口  
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 发送广播。OK  
        sendBroadcast(shortcutIntent);

        // 在配置文件中声明已经创建了快捷方式  
        this.getSharedPreferences(Contant.isCreateDesk, Context.MODE_PRIVATE)
                .edit().putBoolean("iscreated", true).commit();

        // 2.3.3系统创建快捷方式不提示  
        if (android.os.Build.VERSION.SDK.equals("10")) {
            Toast.makeText(
                    this,
                    "已创建" + this.getResources().getString(
                            R.string.app_name) + "的快捷方式",
                    Toast.LENGTH_LONG).show();
        }

        String handSetInfo = "手机型号:" + android.os.Build.MODEL + ",SDK版本:"
                + android.os.Build.VERSION.SDK + ",系统版本:"
                + android.os.Build.VERSION.RELEASE;
        MyLog.e("lrm", handSetInfo);
    }
}
