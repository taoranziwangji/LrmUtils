package com.vdolrm.lrmutils.OpenSourceUtils.net.http.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.vdolrm.lrmutils.NetUtils.NetCheckUtil;
import com.vdolrm.lrmutils.OpenSourceUtils.net.download.OSIHttpDownloadCallBack;
import com.vdolrm.lrmutils.OpenSourceUtils.net.http.OSIHttpLoaderCallBack;
import com.vdolrm.lrmutils.OtherUtils.StringUtils;
import com.vdolrm.lrmutils.UIUtils.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
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

    private static int CONNECT_TIMEOUT = 0;
    private static int READ_TIMEOUT = 0;
    private static int WRITE_TIMEOUT = 0;
    private static boolean retryOnConnectionFailure = false;


    private static final String TAG = "OkHttpClientManager";
    public static final MediaType JSON=MediaType.parse("application/json;charset=utf-8");

    //lrm test cache
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (NetCheckUtil.isNetworkConnected(UIUtils.getContext())) {
                int maxAge = 60; // read from cache for 1 minute
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    //下述四个方法需要在初始化时调用
    public static void setConnectionTimeOut(int t){
        CONNECT_TIMEOUT = t;
    }

    public static void setReadTimeOut(int t){
        READ_TIMEOUT = t;
    }

    public static void setWriteTimeOut(int t){
        WRITE_TIMEOUT = t;
    }

    public static void setRetryOnConnectionFailure(boolean b){
        retryOnConnectionFailure = b;
    }


    private OkHttpClientManager() {
        //mOkHttpClient = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(retryOnConnectionFailure);
        if(CONNECT_TIMEOUT > 0) builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS); //连接超时
        if(READ_TIMEOUT > 0) builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS); //读取超时
        if(WRITE_TIMEOUT > 0) builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS); //写超时
        mOkHttpClient = builder.build();
        //根据业务服务器的配置来定，笨鸟服务器返回的接口没让缓存
//        int cacheSize = 10 * 1024 * 1024; // 10 MiB
//        Cache cache = new Cache(new File(StorageUtil.getAppCachePath(UIUtils.getContext()) + "/httpCache"), cacheSize);
//        mOkHttpClient = new OkHttpClient.Builder().cache(cache).build();

        //mOkHttpClient.networkInterceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);//exception

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
    private Response _getAsyn(String url, String userAgent) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url);
        if(StringUtils.isNotEmpty(userAgent)){
            builder.removeHeader("User-Agent").addHeader("User-Agent", userAgent);
        }
        final Request request = builder.build();

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
    private String _getAsString(String url, String userAgent) throws IOException {
        Response execute = _getAsyn(url, userAgent);
        return execute.body().string();
    }


    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    private Call _getAsyn(String flag, String url, final OSIHttpLoaderCallBack callback, String userAgent, Map<String, String> headers) {
        /*final Request request = new Request.Builder()
                .url(url)
            //  .cacheControl(new CacheControl.Builder()//根据业务服务器的配置来定，笨鸟服务器返回的接口没让缓存
            //  .maxAge(1, TimeUnit.DAYS)
            //  .maxStale(1, TimeUnit.DAYS)
                .build();*/

        Param[] headersArr = map2Params(headers);
        Request request = buildGetRequest(url,userAgent,headersArr);

        return deliveryResult(flag, callback, request);
    }


    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return
     */
    private Response _post(String url, Map<String, String> params, String userAgent, Param... headers) throws IOException {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr, userAgent, headers);
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
    private String _postAsString(String url, Map<String, String> params, String userAgent, Param... headers) throws IOException {
        Response response = _post(url, params, userAgent, headers);
        return response.body().string();
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(final String flag, String url, final OSIHttpLoaderCallBack callback, Map<String, String> params, String userAgent, Map<String, String> headers) {
        Param[] paramsArr = map2Params(params);
        Param[] headersArr = map2Params(headers);
        Request request = buildPostRequest(url, paramsArr, userAgent, headersArr);
        deliveryResult(flag, callback, request);
    }

    /**
     * 异步的post请求 raw
     *
     * @param url
     * @param callback
     * @param json
     */
    private void _postAsynRaw(final String flag, String url, final OSIHttpLoaderCallBack callback, String json, String userAgent, Map<String, String> headers) {
        Param[] headersArr = map2Params(headers);
        Request request = buildPostRequestRaw(url, json, userAgent, headersArr);
        deliveryResult(flag, callback, request);
    }

    /**
     * 异步的delete请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _deleteAsyn(final String flag, String url, final OSIHttpLoaderCallBack callback, Map<String, String> params, String userAgent, Map<String, String> headers) {
        Param[] paramsArr = map2Params(params);
        Param[] headersArr = map2Params(headers);
        Request request = buildDeleteRequest(url, paramsArr, userAgent, headersArr);
        deliveryResult(flag, callback, request);
    }

    /**
     * 异步的put请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _putAsyn(final String flag, String url, final OSIHttpLoaderCallBack callback, Map<String, String> params, String userAgent, Map<String, String> headers) {
        Param[] paramsArr = map2Params(params);
        Param[] headersArr = map2Params(headers);
        Request request = buildPutRequest(url, paramsArr, userAgent, headersArr);
        deliveryResult(flag, callback, request);
    }

    /**
     * 同步基于post的文件上传
     *
     * @param params
     * @return
     */
    private Response _post(String url, File[] files, String[] fileKeys, Map<String, String> params) throws IOException {
        Param[] paramsArr = map2Params(params);
        Request request = buildMultipartFormRequest(url, files, fileKeys, paramsArr);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _post(String url, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _post(String url, File file, String fileKey, Map<String, String> params) throws IOException {
        Param[] paramsArr = map2Params(params);
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, paramsArr);
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
    private void _postAsyn(final String flag, String url, OSIHttpLoaderCallBack callback, File[] files, String[] fileKeys, Map<String, String> params) throws IOException {
        Param[] paramsArr = map2Params(params);
        Request request = buildMultipartFormRequest(url, files, fileKeys, paramsArr);
        deliveryResult(flag, callback, request);
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
    private void _postAsyn(final String flag, String url, OSIHttpLoaderCallBack callback, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        deliveryResult(flag, callback, request);
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
    private void _postAsyn(final String flag, String url, OSIHttpLoaderCallBack callback, File file, String fileKey, Map<String, String> params) throws IOException {
        Param[] paramsArr = map2Params(params);
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, paramsArr);
        deliveryResult(flag, callback, request);
    }

    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     */
    private Call _downloadAsyn(final String flag, final String url, final String destFileDir, final OSIHttpDownloadCallBack callback) {
        return _downloadAsyn(flag, url, destFileDir, getFileName(url), callback);
    }

    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     * @param fileName    文件名
     */
    private Call _downloadAsyn(final String flag, final String url, final String destFileDir, final String fileName, final OSIHttpDownloadCallBack callback) {

        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                sendFailedStringCallback(flag, request, e, callback);
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
                    sendSuccessOSIHttpLoaderCallBack(flag, file.getAbsolutePath(), file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(flag, response.request(), e, callback);
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

        return call;

    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }


    //*************对外公布的方法************


    /**
     * 同步的Get请求
     *
     * @param url
     * @return Response
     */
    public static Response getAsyn(String url, String userAgent) throws IOException {
        return getInstance()._getAsyn(url, userAgent);
    }


    /**
     * 同步的Get请求
     *
     * @param url
     * @return 字符串
     */
    public static String getAsString(String url, String userAgent) throws IOException {
        return getInstance()._getAsString(url, userAgent);
    }

    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    public static Call getAsyn(String flag, String url, OSIHttpLoaderCallBack callback, String userAgent, Map<String, String> headers) {
        return getInstance()._getAsyn(flag, url, callback, userAgent, headers);
    }

    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return
     */
    public static Response post(String url, Map<String, String> params, String userAgent, Param... headers) throws IOException {
        return getInstance()._post(url, params, userAgent, headers);
    }

    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return 字符串
     */
    public static String postAsString(String url, Map<String, String> params, String userAgent, Param... headers) throws IOException {
        return getInstance()._postAsString(url, params, userAgent, headers);
    }


    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    public static void postAsyn(final String flag, String url, final OSIHttpLoaderCallBack callback, Map<String, String> params, String userAgent, Map<String, String> headers) {
        getInstance()._postAsyn(flag, url, callback, params, userAgent, headers);
    }

    /**
     * 异步的post请求raw
     *
     * @param url
     * @param callback
     * @param json
     */
    public static void postAsynRaw(final String flag, String url, final OSIHttpLoaderCallBack callback, String json, String userAgent, Map<String, String> headers) {
        getInstance()._postAsynRaw(flag, url, callback, json, userAgent, headers);
    }

    /**
     * 异步的delete请求
     *
     * @param url
     * @param callback
     * @param params
     */
    public static void deleteAsyn(final String flag, String url, final OSIHttpLoaderCallBack callback, Map<String, String> params, String userAgent, Map<String, String> headers) {
        getInstance()._deleteAsyn(flag, url, callback, params, userAgent, headers);
    }

    /**
     * 异步的put请求
     *
     * @param url
     * @param callback
     * @param params
     */
    public static void putAsyn(final String flag, String url, final OSIHttpLoaderCallBack callback, Map<String, String> params, String userAgent, Map<String, String> headers) {
        getInstance()._putAsyn(flag, url, callback, params, userAgent, headers);
    }


    /**
     * 同步基于post的文件上传
     *
     * @param params
     * @return
     */
    public static Response post(String url, File[] files, String[] fileKeys, Map<String, String> params) throws IOException {
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
    public static Response post(String url, File file, String fileKey, Map<String, String> params) throws IOException {
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
    public static void postAsyn(final String flag, String url, OSIHttpLoaderCallBack callback, File[] files, String[] fileKeys, Map<String, String> params) throws IOException {
        getInstance()._postAsyn(flag, url, callback, files, fileKeys, params);
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
    public static void postAsyn(final String flag, String url, OSIHttpLoaderCallBack callback, File file, String fileKey) throws IOException {
        getInstance()._postAsyn(flag, url, callback, file, fileKey);
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
    public static void postAsyn(final String flag, String url, OSIHttpLoaderCallBack callback, File file, String fileKey, Map<String, String> params) throws IOException {
        getInstance()._postAsyn(flag, url, callback, file, fileKey, params);
    }


    /**
     * 异步下载文件
     *
     * @param url
     * @param destDir  本地文件存储的文件夹
     * @param callback
     */
    public static Call downloadAsyn(final String flag, String url, String destDir, OSIHttpDownloadCallBack callback) {
        return getInstance()._downloadAsyn(flag, url, destDir, callback);
    }

    /**
     * 异步下载文件
     *
     * @param url
     * @param destDir  本地文件存储的文件夹
     * @param callback
     * @param fileName
     */
    public static Call downloadAsyn(final String flag, String url, String destDir, String fileName, OSIHttpDownloadCallBack callback) {
        return getInstance()._downloadAsyn(flag, url, destDir, fileName, callback);
    }

    /**
     * 获取下载文件的长度
     *
     * @param downloadUrl
     * @return
     */
    public static long getFileLength(String downloadUrl) {
        return getInstance()._getFileLength(downloadUrl);
    }

    private long _getFileLength(String downloadUrl){
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.body().close();
                return contentLength == 0 ? -1 : contentLength;
            }
        } catch (IOException|RuntimeException e) {
            e.printStackTrace();
        }
        return -1;
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

    private Call deliveryResult(final String flag, final OSIHttpLoaderCallBack callback, final Request request) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {

                sendFailedStringCallback(flag, request, e, callback);
            }

            @Override
            public void onResponse(Call call, final Response response) {
                String strResponse = null;

                try {
                    final String string = response.body().string();
                    strResponse = string;
                    if (callback.getType() == String.class) {
                        sendSuccessOSIHttpLoaderCallBack(flag, string, string, callback);
                    } else {
                        Object o = mGson.fromJson(string, callback.getType());
                        sendSuccessOSIHttpLoaderCallBack(flag, string, o, callback);
                    }


                } catch (IOException e) {
                    sendFailedStringCallback(flag, response.request(), e, callback);
                } catch (com.google.gson.JsonParseException e)//Json解析的错误
                {
                    // sendFailedStringCallback(response.request(), e, callback);
                    sendFailedStringByErrorGsonCallback(flag, response.request(), strResponse, e, callback);
                }

            }
        });

        return call;
    }

    private void sendFailedStringCallback(final String flag, final Request request, final String msg, final OSIHttpLoaderCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(flag, msg);
            }
        });
    }

    private void sendFailedStringByErrorGsonCallback(final String flag, final Request request, final String strResponse, final Exception e, final OSIHttpLoaderCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onErrorGsonException(flag, strResponse, e);
            }
        });
    }

    private void sendFailedStringCallback(final String flag, final Request request, final Exception e, final OSIHttpLoaderCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onException(flag, e);
            }
        });
    }

    private void sendSuccessOSIHttpLoaderCallBack(final String flag, final String strResponse, final Object object, final OSIHttpLoaderCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(flag, strResponse, object);
                }
            }
        });
    }

    private void sendLoadingOSIHttpLoaderCallBack(final long total, final long current, final boolean isUploading, final OSIHttpDownloadCallBack callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onLoading(total, current, isUploading);
                }
            }
        });
    }

    private Request buildGetRequest(String url, String userAgent, Param[] headers) {

        Request.Builder requestBuilder = new Request.Builder()
                .url(url);
        if(StringUtils.isNotEmpty(userAgent)){
            requestBuilder.removeHeader("User-Agent").addHeader("User-Agent", userAgent);
        }
        if(headers!=null && headers.length > 0){
            for(Param header : headers){
                requestBuilder.addHeader(header.key, header.value);
            }
        }
        return requestBuilder.build();
    }

    private Request buildPostRequest(String url, Param[] params, String userAgent, Param[] headers) {
        if (params == null) {
            params = new Param[0];
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();

        Request.Builder builder2 = new Request.Builder()
                .url(url);
        if(StringUtils.isNotEmpty(userAgent)){
            builder2.removeHeader("User-Agent").addHeader("User-Agent", userAgent);
        }
        Request.Builder requestBuilder = builder2.post(requestBody);

        if(headers!=null && headers.length > 0){
            for(Param header : headers){
                requestBuilder.addHeader(header.key, header.value);
            }
        }
        return requestBuilder.build();
    }

    private Request buildPostRequestRaw(String url, String json, String userAgent, Param[] headers) {

        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request.Builder builder = new Request.Builder()
                .url(url);
        if(StringUtils.isNotEmpty(userAgent)){
            builder.removeHeader("User-Agent").addHeader("User-Agent", userAgent);
        }
        Request.Builder requestBuilder = builder.post(requestBody);

        if(headers!=null && headers.length > 0){
            for(Param header : headers){
                requestBuilder.addHeader(header.key, header.value);
            }
        }

        return requestBuilder.build();
    }

    private Request buildDeleteRequest(String url, Param[] params, String userAgent, Param[] headers) {
        if (params == null) {
            params = new Param[0];
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        Request.Builder builder2 = new Request.Builder()
                .url(url);
        if(StringUtils.isNotEmpty(userAgent)){
            builder2.removeHeader("User-Agent").addHeader("User-Agent", userAgent);
        }
        Request.Builder requestBuilder = builder2.delete(requestBody);

        if(headers!=null && headers.length > 0){
            for(Param header : headers){
                requestBuilder.addHeader(header.key, header.value);
            }
        }

        return requestBuilder.build();
    }

    private Request buildPutRequest(String url, Param[] params, String userAgent, Param[] headers) {
        if (params == null) {
            params = new Param[0];
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        Request.Builder builder2 = new Request.Builder()
                .url(url);
        if(StringUtils.isNotEmpty(userAgent)){
            builder2.removeHeader("User-Agent").addHeader("User-Agent", userAgent);
        }
        Request.Builder requestBuilder = builder2.put(requestBody);

        if(headers!=null && headers.length > 0){
            for(Param header : headers){
                requestBuilder.addHeader(header.key, header.value);
            }
        }

        return requestBuilder.build();

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
