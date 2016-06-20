package com.vdolrm.lrmutils.Test;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vdolrm.lrmutils.Adapter.PageAdapter.BaseFragmentPagerAdapter;
import com.vdolrm.lrmutils.Adapter.RecyclerViewAdapter.BaseMyAdapter;
import com.vdolrm.lrmutils.Adapter.RecyclerViewAdapter.BaseViewHolder;
import com.vdolrm.lrmutils.Adapter.RecyclerViewAdapter.OnRecyclerViewItemClickListener;
import com.vdolrm.lrmutils.BaseActivity;
import com.vdolrm.lrmutils.LogUtils.MyLog;
import com.vdolrm.lrmutils.R;
import com.vdolrm.lrmutils.UIUtils.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * recyclerview的adapter内的布局 假如textview设置了singline则可能导致viewpager不能滑动
 */
public class TestFragmentViewPagerActivity extends BaseActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BaseFragmentPagerAdapter fragmentPagerAdapter;
    private List<String> list_title = Arrays.asList(new String[]{"titel1", "title2", "title3"});
    private List<Fragment> list_fragment = new ArrayList<Fragment>();

    private void assignViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }


    @Override
    public void initView() {
        setContentView(R.layout.activity_test_fragment_view_pager);
    }

    @Override
    public void init() {
        assignViews();
    }

    @Override
    public void initToolBar() {
        super.initToolBar();
        if(toolbar!=null) {
            toolbar.setTitle("问题列表");
            setSupportActionBar(toolbar);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setHomeAsUpIndicator(android.R.drawable.arrow_down_float);//替换原来系统默认的返回箭头
            actionBar.setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标
        }
    }

    @Override
    public void initEvent() {
        for (int i = 0; i < list_title.size(); i++) {
            TestFragment testFragment = TestFragment.newInstance("t1");
            list_fragment.add(testFragment);
        }

        fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), list_title, list_fragment);
        viewPager.setAdapter(fragmentPagerAdapter);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

    }


    public static class TestFragment extends Fragment {
        private static final String KEY_CONTENT = "BaseFragment";
        private String mContent = "???";

        public static TestFragment newInstance(String content) {
            TestFragment fragment = new TestFragment();
            fragment.mContent = content;

            Bundle b = new Bundle();
            b.putString(KEY_CONTENT, content);
            fragment.setArguments(b);
            MyLog.d("创建fragment " + content);
            return fragment;
        }


        private RecyclerView recyclerView;
        private MyAdapter adapter;
        private List<String> list = new ArrayList<>();


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View v = inflater.inflate(R.layout.fragment_recyclerview,null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            v.setLayoutParams(lp);
            recyclerView = (RecyclerView) v.findViewById(R.id.mRecyclerView);
            lazyLoad();
            return v;
        }





        public void lazyLoad() {
            MyLog.d("lazyLoad() called with: " + "");

            for(int i=0;i<20;i++){
                list.add("hellohellohellohellohellohello"+i);
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            adapter = new MyAdapter(getContext(),list);
            recyclerView.setAdapter(adapter);


            adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    MyLog.d("onItemClick() called with: " + "position = [" + position + "]");
                }

                @Override
                public boolean onItemLongClick(int position) {
                    MyLog.d("onItemLongClick() called with: " + "position = [" + position + "]");
                    return false;
                }
            });

        }
    }

    private static class MyAdapter extends BaseMyAdapter<String, MyHolder> {

        public MyAdapter(Context c, List list) {
            super(c, list);
        }

        @Override
        public View getItemView(ViewGroup parent) {
            View v =  UIUtils.inflate(R.layout.item_testrecyclerviewadapte2r);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            v.setLayoutParams(lp);
            return v;
        }

        @Override
        public void onBind(int position, String bean, View itemView, MyHolder holder) {
            holder.tv_test.setText(bean);
        }

        @Override
        public MyHolder getHolder(View itemView, View itemRootView, OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
            return new MyHolder(itemView, itemRootView, onRecyclerViewItemClickListener);
        }
    }

    private static class MyHolder extends BaseViewHolder {

        public TextView tv_test;

        public MyHolder(View itemView, View itemRootView, OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
            super(itemView, itemRootView, onRecyclerViewItemClickListener);

            tv_test = (TextView) itemRootView.findViewById(R.id.tv_test);
        }
    }
}
