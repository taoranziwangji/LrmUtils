package com.vdolrm.lrmutils.ObserverUtils;



import com.vdolrm.lrmutils.LogUtils.MyLog;

import java.util.ArrayList;
import java.util.List;

/**观察者模式实质上就是维护一个list和一个接口*/
public class Observable {
	
	private Observable() {//私有化构造方法 防止new出来 必须使用getInstance()方法获取实例
		
	}
	
	private static Observable instance;
	/** 用于记录观察者，当信息发送了改变，需要通知他们 */
	private List<ObserverListener> mObservers = new ArrayList<ObserverListener>();
	
	public static synchronized Observable getInstance() {//带锁 单例
		MyLog.d("观察者初始化 instance=" + instance);
		if (instance == null) {
			instance = new Observable();
		}
		return instance;
	}
	
	
	
	/** 添加观察者 （谁要监听谁注册）*/
	public void registerObserver(ObserverListener observer) {
		MyLog.d("添加观察者"+observer);
		 if (observer == null) {
	            throw new NullPointerException("observer == null");
	        }
		synchronized (mObservers) {//同步锁 防止add的时候有其他操作
			if (!mObservers.contains(observer)) {
				mObservers.add(observer);
			}
		}
	}

	/** 移除观察者 （谁要监听谁注册）*/
	public void unRegisterObserver(ObserverListener observer) {
		 if (observer == null) {
	            throw new NullPointerException("observer == null");
	        }
		synchronized (mObservers) {
			if (mObservers.contains(observer)) {
				mObservers.remove(observer);
			}
		}
	}
	
	/**清空观察者 */
	public void clearObserver(ObserverListener observer) {
		synchronized (mObservers) {
			mObservers.clear();
		}
	}
	
	/**获取观察者的数量*/
	 public int countObservers() {
	        return mObservers.size();
	    }
	

	/** 当状态发送改变的时候回调 （由被监听者调用）
	 * @param <T>*/
	public <T> void notifyStateChanged(T info) {
		synchronized (mObservers) {
			for (ObserverListener observer : mObservers) {
				MyLog.d("观察者更新消息"+info);
				observer.onUpdate(info);
			}
		}
	}
	
	
	
	
	public interface ObserverListener {
		/**得到被监听者状态改变的通知*/
		public <T> void onUpdate(T info);

	}
	
}
