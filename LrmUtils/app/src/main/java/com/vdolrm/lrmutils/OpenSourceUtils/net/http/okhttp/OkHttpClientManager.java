package com.vdolrm.lrmutils.OpenSourceUtils.net.http.okhttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.vdolrm.lrmutils.OpenSourceUtils.net.up_downLoad.OSIHttpDownloadCallBack;
import com.vdolrm.lrmutils.OpenSourceUtils.net.http.OSIHttpLoaderCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 洪洋大神原来二次封装的OKHTTP框架，新版封装采用了链式结构，那还不如用retrofit,所以这只列出这个老版，新版可以使用retrofit
 * 详情：http://blog.csdn.net/lmj623565791/article/details/47911083
 * 缺少下载、上传时的进度回调(http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0904/3416.html)，下载、上传时的暂停,进度保存，断点续传,OkHttp缓存，取消网络请求等功能
 * Created by zhy on 15/8/17.
 */
public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;


    private static final String TAG = "OkHttpClientManager";

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        //mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    private static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return Response
     */
    private Response _getAsyn(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return 字符串
     */
    private String _getAsString(String url) throws IOException {
        Response execute = _getAsyn(url);
        return execute.body().string();
    }


    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    private Call _getAsyn(String url, final OSIHttpLoaderCallBack callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        return deliveryResult(callback, request);
    }


    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return
     */
    private Response _post(String url, Param... params) throws IOException {
        Request request = buildPostRequest(url, params);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }


    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return 字符串
     */
    private String _postAsString(String url, Param... params) throws IOException {
        Response response = _post(url, params);
        return response.body().string();
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final OSIHttpLoaderCallBack callback, Param... params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final OSIHttpLoaderCallBack callback, Map<String, String> params) {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(callback, request);
    }

    /**
     * 同步基于post的文件上传
     *
     * @param params
     * @return
     */
    private Response _post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _post(String url, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _post(String url, File file, String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 异步基于post的文件上传
     *
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @throws IOException
     */
    private void _postAsyn(String url, OSIHttpLoaderCallBack callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件不带参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @throws IOException
     */
    private void _postAsyn(String url, OSIHttpLoaderCallBack callback, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件且携带其他form参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @param params
     * @throws IOException
     */
    private void _postAsyn(String url, OSIHttpLoaderCallBack callback, File file, String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     */
    private void _downloadAsyn(final String url, final String destFileDir, final OSIHttpDownloadCallBack callback) {
        _downloadAsyn(url, destFileDir, getFileName(url), callback);
    }

    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     * @param fileName 文件名
     */
    private void _downloadAsyn(final String url, final String destFileDir, final String fileName, final OSIHttpDownloadCallBack callback) {

            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(final Call call, final IOException e) {
                    sendFailedStringCallback(request, e, callback);
                }

                @Override
                public void onResponse(Call call, Response response) {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;//一次读取多少，最大为2046字节
                    FileOutputStream fos = null;
                    try {
                        long contentLength = response.body().contentLength();

                        is = response.body().byteStream();
                        File file = new File(destFileDir, fileName);
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sendLoadingOSIHttpLoaderCallBack(contentLength, file.length(), false, callback);
                            //MyLog.d("下载文件总大小"+contentLength+",当前文件大小 " + file.length() );//第一个为当前文件大小
                        }
                        fos.flush();
                        //如果下载文件成功，第一个参数为文件的绝对路径
                        sendSuccessOSIHttpLoaderCallBack(file.getAbsolutePath(), callback);
                    } catch (IOException e) {
                        sendFailedStringCallback(response.request(), e, callback);
                    } finally {
                        try {
                            if (is != null) is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (fos != null) fos.close();
                        } catch (IOException e) {
                        }
                    }

                }
            });

    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 加载图片
     *
     * @param view
     * @param url
     * @throws IOException
     */
    private void _displayImage(final ImageView view, final String url, final int errorResId) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setErrorResId(view, errorResId);
            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream is = null;
                try {
                    is = response.body().byteStream();
                    OkHttpClientManagerImageUtils.ImageSize actualImageSize = OkHttpClientManagerImageUtils.getImageSize(is);
                    OkHttpClientManagerImageUtils.ImageSize imageViewSize = OkHttpClientManagerImageUtils.getImageViewSize(view);
                    int inSampleSize = OkHttpClientManagerImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
                    try {
                        is.reset();
                    } catch (IOException e) {
                        response = _getAsyn(url);
                        is = response.body().byteStream();
                    }

                    BitmapFactory.Options ops = new BitmapFactory.Options();
                    ops.inJustDecodeBounds = false;
                    ops.inSampleSize = inSampleSize;
                    final Bitmap bm = BitmapFactory.decodeStream(is, null, ops);
                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            view.setImageBitmap(bm);
                        }
                    });
                } catch (Exception e) {
                    setErrorResId(view, errorResId);

                } finally {
                    if (is != null) try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private void setErrorResId(final ImageView view, final int errorResId) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                view.setImageResource(errorResId);
            }
        });
    }


    //*************对外公布的方法************


    /**
     * 同步的Get请求
     *
     * @param url
     * @return Response
     */
    public static Response getAsyn(String url) throws IOException {
        return getInstance()._getAsyn(url);
    }


    /**
     * 同步的Get请求
     *
     * @param url
     * @return 字符串
     */
    public static String getAsString(String url) throws IOException {
        return getInstance()._getAsString(url);
    }

    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    public static Call getAsyn(String url, OSIHttpLoaderCallBack callback) {
        return getInstance()._getAsyn(url, callback);
    }

    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return
     */
    public static Response post(String url, Param... params) throws IOException {
        return getInstance()._post(url, params);
    }

    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return 字符串
     */
    public static String postAsString(String url, Param... params) throws IOException {
        return getInstance()._postAsString(url, params);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    public static void postAsyn(String url, final OSIHttpLoaderCallBack callback, Param... params) {
        getInstance()._postAsyn(url, callback, params);
    }


    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    public static void postAsyn(String url, final OSIHttpLoaderCallBack callback, Map<String, String> params) {
        getInstance()._postAsyn(url, callback, params);
    }


    /**
     * 同步基于post的文件上传
     *
     * @param params
     * @return
     */
    public static Response post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        return getInstance()._post(url, files, fileKeys, params);
    }

    /**
     * 同步基于post的单个文件上传，不携带参数
     *
     * @return
     */
    public static Response post(String url, File file, String fileKey) throws IOException {
        return getInstance()._post(url, file, fileKey);
    }

    /**
     * 同步基于post的单个文件上传，携带参数
     *
     * @param params
     * @return
     */
    public static Response post(String url, File file, String fileKey, Param... params) throws IOException {
        return getInstance()._post(url, file, fileKey, params);
    }

    /**
     * 异步基于post的文件上传
     *
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @throws IOException
     */
    public static void postAsyn(String url, OSIHttpLoaderCallBack callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        getInstance()._postAsyn(url, callback, files, fileKeys, params);
    }


    /**
     * 异步基于post的文件上传，单文件不带参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @throws IOException
     */
    public static void postAsyn(String url, OSIHttpLoaderCallBack callback, File file, String fileKey) throws IOException {
        getInstance()._postAsyn(url, callback, file, fileKey);
    }


    /**
     * 异步基于post的文件上传，单文件且携带其他form参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @param params
     * @throws IOException
     */
    public static void postAsyn(String url, OSIHttpLoaderCallBack callback, File file, String fileKey, Param... params) throws IOException {
        getInstance()._postAsyn(url, callback, file, fileKey, params);
    }

    /**
     * 加载图片
     *
     * @param view
     * @param url
     * @throws IOException
     */
    public static void displayImage(final ImageView view, String url, int errorResId) throws IOException {
        getInstance()._displayImage(view, url, errorResId);
    }


    /**
     * 加载图片
     *
     * @param view
     * @param url
     * @throws IOException
     */
    public static void displayImage(final ImageView view, String url) {
        getInstance()._displayImage(view, url, -1);
    }

    /**
     * 异步下载文件
     *
     * @param url
     * @param destDir  本地文件存储的文件夹
     * @param callback
     */
    public static void downloadAsyn(String url, String destDir, OSIHttpDownloadCallBack callback) {
        getInstance()._downloadAsyn(url, destDir, callback);
    }

    /**
     * 异步下载文件
     *
     * @param url
     * @param destDir  本地文件存储的文件夹
     * @param callback
     * @param fileName
     */
    public static void downloadAsyn(String url, String destDir, String fileName, OSIHttpDownloadCallBack callback) {
        getInstance()._downloadAsyn(url, destDir, fileName, callback);
    }

    //****************************


    private Request buildMultipartFormRequest(String url, File[] files,
                                              String[] fileKeys, Param[] params) {
        params = validateParam(params);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
               /* try {
                    long contentLength = fileBody.contentLength();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                //TODO 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private static final String SESSION_KEY = "Set-Cookie";
    private static final String mSessionKey = "JSESSIONID";

    private Map<String, String> mSessions = new HashMap<String, String>();

    private Call deliveryResult(final OSIHttpLoaderCallBack callback, final Request request) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {

                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Call call, final Response response) {
                String strResponse = null;

                try {
                    final String string = response.body().string();
                    strResponse = string;
                    if (callback.getType() == String.class) {
                        sendSuccessOSIHttpLoaderCallBack(string, callback);
                    } else {
                        Object o = mGson.fromJson(string, callback.getType());
                        sendSuccessOSIHttpLoaderCallBack(o, callback);
                    }


                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } catch (com.google.gson.JsonParseException e)//Json解析的错误
                {
                   // sendFailedStringCallback(response.request(), e, callback);
                    sendFailedStringByErrorGsonCallback(response.request(),strResponse,e,callback);
                }

            }
        });

        return call;
    }

    private void sendFailedStringCallback(final Request request,final String msg, final OSIHttpLoaderCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(msg);
            }
        });
    }

    private void sendFailedStringByErrorGsonCallback(final Request request,final String strResponse,final Exception e, final OSIHttpLoaderCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onErrorGsonException(strResponse,e);
            }
        });
    }

    private void sendFailedStringCallback(final Request request,final Exception e, final OSIHttpLoaderCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onException(e);
            }
        });
    }

    private void sendSuccessOSIHttpLoaderCallBack(final Object object, final OSIHttpLoaderCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }

    private void sendLoadingOSIHttpLoaderCallBack(final long total,final long current,final boolean isUploading, final OSIHttpDownloadCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onLoading(total,current,isUploading);
                }
            }
        });
    }

    private Request buildPostRequest(String url, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }


    

    public static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }


}
