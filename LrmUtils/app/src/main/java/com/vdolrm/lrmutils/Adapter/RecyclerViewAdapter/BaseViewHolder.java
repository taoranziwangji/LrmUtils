package com.vdolrm.lrmutils.Adapter.RecyclerViewAdapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2016/3/28.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private View itemRootView;
   // public TextView tv_item;
   // public ImageView imv_item;
    private int position;

    public BaseViewHolder(View itemView,View itemRootView,OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        super(itemView);
       // tv_item = (TextView) itemView.findViewById(R.id.tv_item);
      //  imv_item = (ImageView)itemView.findViewById(R.id.imv_item);
     //   rootView = (ViewGroup)itemView.findViewById(R.id.root_item);
        this.itemRootView = itemRootView;
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        itemRootView.setOnClickListener(this);
        itemRootView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (null != onRecyclerViewItemClickListener) {
            onRecyclerViewItemClickListener.onItemClick(position);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (null != onRecyclerViewItemClickListener) {
            return onRecyclerViewItemClickListener.onItemLongClick(position);
        }
        return true;
    }


    public int getPos() {
        return position;
    }

    public void setPos(int position) {
        this.position = position;
    }

    public View getItemRootView() {
        return itemRootView;
    }

    public void setItemRootView(View itemRootView) {
        this.itemRootView = itemRootView;
    }
}
