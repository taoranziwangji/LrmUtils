package com.vdolrm.lrmutils.Adapter.RecyclerViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Administrator on 2016/3/28.
 */
public abstract class BaseMyAdapter<T,TT extends BaseViewHolder> extends RecyclerView.Adapter {

    public abstract android.view.View getItemView(ViewGroup parent);
    public abstract void onBind(int position, T bean, android.view.View itemView, TT holder);
    public abstract TT getHolder(View itemView, View itemRootView, OnRecyclerViewItemClickListener onRecyclerViewItemClickListener);

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    private List<T> list;
    private WeakReference<Context> context;

    public BaseMyAdapter(Context c,List<T> list) {
        this.list = list;
        context = new WeakReference<Context>(c);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //MyLog.d("onCreateViewHolder parent="+parent+",viewType="+viewType);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_test_item_picasso, null);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        view.setLayoutParams(lp);
        android.view.View view = getItemView(parent);
        //return new BaseViewHolder(view,view.getRootView(),onRecyclerViewItemClickListener);

        BaseViewHolder holder = getHolder(view,view.getRootView(),onRecyclerViewItemClickListener);
        return holder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //MyLog.d("onBindViewHolder holder="+holder+",position="+position);

        TT holder2 = (TT)holder;
        holder2.setPos(position);
        T bean = list.get(position);

        onBind(position,bean,holder2.getItemRootView(),holder2);

    }



    @Override
    public int getItemCount() {
        return list.size();
    }



    

}