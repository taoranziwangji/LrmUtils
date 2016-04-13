package com.vdolrm.lrmutils.FileUtils;


import android.app.ActivityManager;
import android.content.Context;

import com.vdolrm.lrmutils.UIUtils.UIUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class StorageUtil {

    /**
     * 获得本程序最大的可用内存
     */
    public static long getMemoryMax(){
        return Runtime.getRuntime().maxMemory();
    }




    /**
     * 获取存储空间的根目录，有SD卡则为外部存储空间，否则为内部存储空间
     */
    public static String getAppCachePath(Context context) {
        String path = "";
        if (isExitsSdcard())
            path = getExternalCachePath(context);
        else
            path = getInnerCachePath(context);
        return path;
    }


    /**
     * 获取外部系统缓存目录，可以随着卸载而自动删除
     * ex:/storage/emulated/0/Android/data/packagename/cache
     */
    private static String getExternalCachePath(Context context) {
        return context.getExternalCacheDir().getPath();
    }

    /**
     * 获取内部系统缓存目录，可以随着卸载而自动删除
     * ex: /data/packagename/cache
     */
    private static String getInnerCachePath(Context context) {
        String path = context.getCacheDir().getPath();
        return path;
    }


    /**
     * 判断Sdcard是否存在
     */
    private static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }


    /**
     * 获取存储空间的根目录，为外部存储空间
     * ex:/storage/emulated/0
     */
    public static String getExternalRootPath() {
        String path = android.os.Environment.getExternalStorageDirectory().getPath();
        return path;
    }

    /**
     * 获取/data/packagename/下自定义名字name的目录,有的话会直接返回，没有的话会创建（但文件夹会带app_前缀）
     * ex:/data/data/packagename/app_mydir
     */
    public static String getInnerCustomCachePath(Context context, String name) {
        String path = context.getDir(name, Context.MODE_PRIVATE).toString();
        return path;
    }


    /**
     * @return 获取文件目录分隔符“/”
     */
    public static String getItalicLine(){
        return File.separator;
    }





}
