package com.vdolrm.lrmutils.ImageUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.vdolrm.lrmutils.OtherUtils.Md5Util;
import com.vdolrm.lrmutils.FileUtils.StorageUtil;
import com.vdolrm.lrmutils.LogUtils.MyLog;


/**
 * 图片加载类 (from zhy)
 * http://blog.csdn.net/lmj623565791/article/details/41874561
 */
public class ImageLoader {
    private static ImageLoader mInstance;

    /**
     * 图片缓存的核心对象
     */
    private LruCache<String, Bitmap> mLruCache;
    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private static final int DEAFULT_THREAD_COUNT = 1;
    /**
     * 队列的调度方式
     */
    private CacheType mType = CacheType.LIFO;
    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTaskQueue;
    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;
    /**
     * UI线程中的Handler
     */
    private Handler mUIHandler;

    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    private Semaphore mSemaphoreThreadPool;

    private boolean isDiskCacheEnable = true;

    private static final String TAG = "ImageLoader";

    public enum CacheType {
        FIFO, LIFO
    }

    /**
     * @param threadCount 线程池里线程的数量，默认为1
     * @param type 图片加载方式，枚举 先进先出和后进先出，默认为后进先出，即后加入任务队列的优先执行(实现快速滑动时不至于从头加载)
     */
    private ImageLoader(int threadCount, CacheType type) {
        init(threadCount, type);
    }

    /**
     * 初始化
     *
     * @param threadCount
     * @param type 加载方式，枚举 先进先出和后进先出，默认为后进先出，即后加入任务队列的优先执行(实现快速滑动时不至于从头加载)
     */
    private void init(int threadCount, CacheType type) {
        initBackThread();

        // 获取我们应用的最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }

        };

        // 创建线程池
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<Runnable>();
        mType = type;
        mSemaphoreThreadPool = new Semaphore(threadCount);
    }

    /**
     * 初始化后台轮询线程
     */
    private void initBackThread() {
        // 后台轮询线程
        mPoolThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 线程池去取出一个任务进行执行
                        Runnable task = getTask();
                        if(task==null)
                            return;
                        mThreadPool.execute(task);
                        try {
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                        }
                    }
                };
                // 释放一个信号量
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            }

            ;
        };

        mPoolThread.start();
    }


    /**
     * @return 单例
     */
    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(DEAFULT_THREAD_COUNT, CacheType.LIFO);
                }
            }
        }
        return mInstance;
    }

    /**
     * @param threadCount 线程池里线程的数量，默认为1
     * @param type 图片加载方式，枚举 先进先出和后进先出，默认为后进先出，即后加入任务队列的优先执行(实现快速滑动时不至于从头加载)
     */
    public static ImageLoader getInstance(int threadCount, CacheType type) {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    /**
     * 根据path为imageview设置图片
     *
     * @param path      可以为本地路径或url，分别对应isFromNet参数为false或true
     * @param imageView
     */
    public void loadImage(final String path, final ImageView imageView,
                          final boolean isFromNet) {
        imageView.setTag(path);
        if (mUIHandler == null) {
            mUIHandler = new Handler() {
                public void handleMessage(Message msg) {
                    // 获取得到图片，为imageview回调设置图片
                    ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageview = holder.imageView;
                    String path = holder.path;
                    // 将path与getTag存储路径进行比较
                    if (imageview.getTag().toString().equals(path)) {
                        imageview.setImageBitmap(bm);
                    }
                }


            };
        }

        // 根据path在缓存中获取bitmap
        Bitmap bm = getBitmapFromLruCache(path);

        if (bm != null) {
            MyLog.d("find image :" + path + " in lruCache .");
            refreashBitmap(path, imageView, bm);
        } else {
            addTask(buildTask(path, imageView, isFromNet));
        }

    }

    /**
     * 根据传入的参数，新建一个任务
     *
     * @param path
     * @param imageView
     * @param isFromNet
     * @return
     */
    private Runnable buildTask(final String path, final ImageView imageView,
                               final boolean isFromNet) {
        return new Runnable() {
            @Override
            public void run() {
                Bitmap bm = null;
                if (isFromNet) {
                    File file = getDiskCacheDir(imageView.getContext(),
                            Md5Util.getMD5(path));
                    if (file.exists())// 如果在缓存文件中发现
                    {
                        MyLog.d("find image :" + path + " in disk cache .");
                        bm = loadImageFromLocal(file.getAbsolutePath(),
                                imageView);
                    } else {
                        if (isDiskCacheEnable)// 检测是否开启硬盘缓存
                        {
                            boolean downloadState = DownloadImgUtils
                                    .downloadImgByUrl(path, file);
                            if (downloadState)// 如果下载成功
                            {
                                MyLog.d(
                                        "download image :" + path
                                                + " to disk cache . path is "
                                                + file.getAbsolutePath());
                                bm = loadImageFromLocal(file.getAbsolutePath(),
                                        imageView);
                            }
                        } else
                        // 直接从网络加载
                        {
                            MyLog.d("load image :" + path + " to memory.");
                            bm = DownloadImgUtils.downloadImgByUrl(path,
                                    imageView);
                        }
                    }
                } else {
                    bm = loadImageFromLocal(path, imageView);
                }
                // 3、把图片加入到缓存
                addBitmapToLruCache(path, bm);
                refreashBitmap(path, imageView, bm);
                mSemaphoreThreadPool.release();
            }


        };
    }

    private Bitmap loadImageFromLocal(final String path,
                                      final ImageView imageView) {
        Bitmap bm;
        // 加载图片
        // 图片的压缩
        // 1、获得图片需要显示的大小
        ImageSizeUtil.ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
        // 2、压缩图片
        bm = ImageSizeUtil.decodeSampledBitmapFromPath(path, imageSize.width,
                imageSize.height);
        return bm;
    }

    /**
     * 从任务队列取出一个方法
     *
     * @return
     */
    private Runnable getTask() {
        if (mType == CacheType.FIFO) {
            return mTaskQueue.removeFirst();
        } else if (mType == CacheType.LIFO) {
            return mTaskQueue.removeLast();
        }
        return null;
    }


    private void refreashBitmap(final String path, final ImageView imageView,
                                Bitmap bm) {
        Message message = Message.obtain();
        ImgBeanHolder holder = new ImgBeanHolder();
        holder.bitmap = bm;
        holder.path = path;
        holder.imageView = imageView;
        message.obj = holder;
        mUIHandler.sendMessage(message);
    }

    /**
     * 将图片加入LruCache
     *
     * @param path
     * @param bm
     */
    protected void addBitmapToLruCache(String path, Bitmap bm) {
        if (getBitmapFromLruCache(path) == null) {
            if (bm != null)
                mLruCache.put(path, bm);
        }
    }



    private synchronized void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);
        // if(mPoolThreadHandler==null)wait();
        try {
            if (mPoolThreadHandler == null)
                mSemaphorePoolThreadHandler.acquire();
        } catch (InterruptedException e) {
        }
        mPoolThreadHandler.sendEmptyMessage(0x110);
    }

    /**
     * 获得缓存图片的地址
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = StorageUtil.getAppCachePath(context);

        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 根据path在缓存中获取bitmap
     *
     * @param key
     * @return
     */
    private Bitmap getBitmapFromLruCache(String key) {
        return mLruCache.get(key);
    }

    private class ImgBeanHolder {
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }
}
