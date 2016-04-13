package com.vdolrm.lrmutils.Adapter.LVAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vdolrm.lrmutils.LogUtils.MyLog;

import java.util.List;

/**
 * adapter基类
 * @param <T>
 */
public abstract class BaseMyAdapter<T>  extends BaseAdapter {
  private List<T> mDatas;
  //private LayoutInflater mInflater;  
  private Context mContext;
  private int layoutId;
  
  public boolean isScrolling = false;// 判断是否正在滑动
  
  private int mCount = 0;
  private boolean isGridView = false;//判断是否为gridview的适配器
  
  public abstract void convert(BaseViewHolder vh,T item,int position);
  
  
  public BaseMyAdapter(Context ctx,List<T> lists,int layoutId){
	  this(ctx,lists,layoutId,false);//默认为不是gridview
  }
  
  public BaseMyAdapter(Context ctx,List<T> lists,int layoutId,boolean isGridView){
    this.mDatas=lists;
   // mInflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.mContext=ctx;
    this.layoutId=layoutId;
    this.isGridView = isGridView;

  }

  @Override
  public int getCount() {
    return mDatas.size();
  }
  
  @Override
  public T getItem(int position) {
    return mDatas.get(position);
  }
  
  @Override
  public long getItemId(int position) {
    return position;
  }
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    MyLog.d("BaseMyAdapter convertView=" + convertView);
	  BaseViewHolder vh = BaseViewHolder.getViewHolder(convertView, mContext, parent, layoutId, position);
    //解决gridview 重复调用position=0时的情况，（position等于其他时的重复调用不起作用）
    if(isGridView){//假如写在getView的起始位置，当list.size=1时会报莫名其妙的空指针错误
      if (position == 0){
        mCount++;
      }else{
        mCount = 0;
      }
      if (mCount > 1){
        return vh.getConvertView();
      }
    }
    convert(vh, mDatas.get(position),position);
    return vh.getConvertView();
  }
  
  
  /**判断滑动是否停止 用于滑动时调用，false为已经停止，true为正在滑动*/
  public void setScrolling(boolean b) {
		// TODO Auto-generated method stub
		this.isScrolling = b;
		if(b==false){
			notifyDataSetChanged();//b为true的时候不能让他刷新，否则就把之前已经显示出来的刷没了。为false的时候必须刷新，好让现在可见的item显示出来
		}
	}


}		
