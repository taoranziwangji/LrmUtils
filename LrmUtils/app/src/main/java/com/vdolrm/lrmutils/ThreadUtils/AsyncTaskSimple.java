package com.vdolrm.lrmutils.ThreadUtils;

import android.os.AsyncTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * if(VersionUtil.getAndroidVerson()  > 11) {
	asyncTask.executeOnExecutor(FULL_TASK_EXECUTOR);   //AsyncTask默认最大只支持同时有5个AsyncTask同时执行，想要不受限制需要创建线程池。详情参见：http://blog.csdn.net/hitlion2008/article/details/7983449
}else{
	asyncTask.execute();
} 
 * @param <DATA> doinbackgroud形参类型 
 * @param <T> 返回值类型*/
public abstract class AsyncTaskSimple<DATA,T> extends AsyncTask<DATA, Void, T> {
	
	 private static ExecutorService FULL_TASK_EXECUTOR;
		
		static {  
	       // SINGLE_TASK_EXECUTOR = (ExecutorService) Executors.newSingleThreadExecutor();  
	       // LIMITED_TASK_EXECUTOR = (ExecutorService) Executors.newFixedThreadPool(7);  
	        FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();
	    }; 
	    
	public AsyncTaskSimple(){
		if(FULL_TASK_EXECUTOR==null){
			FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();
		}
		
	}
	
	
	public static ExecutorService getExecutorServiceInstance(){
		if(FULL_TASK_EXECUTOR==null){
			FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();
		}
		
		return FULL_TASK_EXECUTOR;
	}

	@Override
	protected final T doInBackground(DATA... params) {
		// TODO Auto-generated method stub
		return doInMyBackground(params);
	}

	@Override
	protected final void onPostExecute(T result) {
		// TODO Auto-generated method stub
		onMyPostExecute(result);
	}
	
	




	@Override
	protected final void onCancelled() {
		// TODO Auto-generated method stub
		onCanceled();
		super.onCancelled();
	}


	public abstract T doInMyBackground(DATA... params);
	public abstract void onMyPostExecute(T result);
	public abstract void onCanceled();


	


	
	
	
}
