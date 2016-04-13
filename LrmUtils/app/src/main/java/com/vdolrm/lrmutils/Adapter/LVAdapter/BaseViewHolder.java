package com.vdolrm.lrmutils.Adapter.LVAdapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * listview.adapter的辅助holder
 */
public class BaseViewHolder {
   private final SparseArray<View> mView;
   private View mConvertView;
   
  
   public BaseViewHolder(Context context, ViewGroup parent, int layoutId, int position){
     this.mView=new SparseArray<View>();
     mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
     mConvertView.setTag(this);
   };
    
/**
	  * 获得viewholder
	  * @param convertView
	  * @param context
	  * @param parent
	  * @param layoutId
	  * @param position
	  * @return
	  */
   public static BaseViewHolder getViewHolder(View convertView,Context context,ViewGroup parent, int layoutId, int position){
     if(null==convertView){
       return new BaseViewHolder(context, parent, layoutId, position);
     }
     return (BaseViewHolder) convertView.getTag();
   }
   

   /**
  * @param id mConvertView里子view的id
  * @return
  */
   public  <T extends View> T getView(int id){
     View view=mView.get(id);
     if(null==view){
       view=mConvertView.findViewById(id);
       mView.put(id,view);
     }
     return (T)view;
   }
   
   
   public View getConvertView(){
     return mConvertView;
   }


}
