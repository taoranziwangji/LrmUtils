package com.vdolrm.lrmutils.Adapter.LVAdapter;

import android.content.Context;

import java.util.List;

/**可以局部刷新的listview的adapter*/
public abstract class BasePartRefreshMyAdapter<T> extends BaseMyAdapter<T> {
	/*private List<T> mDatas;
	  private LayoutInflater mInflater;  
	  private Context mContext; 
	  private int layoutId;*/

	public BasePartRefreshMyAdapter(Context ctx, List<T> lists, int layoutId) {
		super(ctx, lists, layoutId);
		// TODO Auto-generated constructor stub
		 /*this.mDatas=lists;
		    mInflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    this.mContext=ctx;
		    this.layoutId=layoutId;*/

	}
	
	private UpdateCallback<T> updateCallback;
	public UpdateCallback<T> getUpdateCallback() {
		return updateCallback;
	}
	public void setUpdateCallback(UpdateCallback<T> updateCallback) {
		this.updateCallback = updateCallback;
	}
	
	
	public void startListenProgress(T bean,int pos){
		
		if(null != updateCallback){//局部更新
			updateCallback.startProgress(bean,pos);
		}
	}
	
	
	
	
	
	
	public interface UpdateCallback<T> {
		public void startProgress(T model, int position);
	}

}
