package com.vdolrm.lrmutils.OpenSourceUtils.net.http;


import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016/4/1.
 */
public abstract class OSIHttpLoaderCallBack<T> {

    private Type mType;

    public OSIHttpLoaderCallBack() {
        mType = getSuperclassTypeParameter(getClass());
    }


    /**
     * @return 泛型的类型
     */
    public Type getType(){
        if(mType==null){
            mType = getSuperclassTypeParameter(getClass());
        }
        return mType;
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public abstract void onException(Exception e);

    /**
     * 由于gson解析错误引起的异常
     * @param response  解析之前的数据流
     * @param e
     */
    public abstract void onErrorGsonException(String response,Exception e);

    public abstract void onError(String errorMsg);

    /**
     * @param response 泛型必须传，假如不需gson解析的话可以传String
     *                 使用子类OSIHttpDownloadCallBack时response为下载成功后的绝对路径
     */
    public abstract void onResponse(T response);
}
