package com.vdolrm.lrmutils.Adapter.LVAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

/**
 * 可以折叠的listview的adapter
 * */
public abstract class BaseExpandableAdapter<T> extends BaseExpandableListAdapter {
    private T[] group;
    private T[][] child;
    private Context context;
    private int layoutId_child, layoutId_parent;
    
    public abstract void convertChild(BaseViewHolder vh,T childBean,int groupPosition,int childPosition);
    public abstract void convertParent(BaseViewHolder vh,T parentBean,int groupPosition, boolean isExpanded);
  
    public BaseExpandableAdapter(Context context,T[] group,T[][] child,int layoutId_child,int layoutId_parent){
    	this.context=context;
    	this.group=group;
    	this.child=child;
    	this.layoutId_child = layoutId_child;
    	this.layoutId_parent = layoutId_parent;
    	
    }
    
    @Override
	public T getChild(int groupPosition, int childPosition) {
		return child[groupPosition][childPosition];
	}
    
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean arg2, View convertView,
			ViewGroup arg4) {
		
		 BaseViewHolder vh = BaseViewHolder.getViewHolder(convertView, context, arg4, layoutId_child, childPosition);	  
		 convertChild(vh, getChild(groupPosition, childPosition),groupPosition,childPosition);
		 
		 return vh.getConvertView();
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return child[groupPosition].length;
	}
	
	@Override
	public T getGroup(int groupPosition) {
		return group[groupPosition];
	}
	
	@Override
	public int getGroupCount() {
		return group.length;
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup arg3) {
				
		 BaseViewHolder vh = BaseViewHolder.getViewHolder(convertView, context, arg3, layoutId_parent, groupPosition);	  
		 convertParent(vh, getGroup(groupPosition),groupPosition,isExpanded);
		
		 return vh.getConvertView();
	}

	
	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	// 子选项是否可以选择  
	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	
}
