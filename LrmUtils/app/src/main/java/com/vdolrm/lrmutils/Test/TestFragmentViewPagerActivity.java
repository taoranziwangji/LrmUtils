package com.vdolrm.lrmutils.Test;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.vdolrm.lrmutils.Adapter.PageAdapter.BaseFragmentPagerAdapter;
import com.vdolrm.lrmutils.BaseActivity;
import com.vdolrm.lrmutils.BaseFragment;
import com.vdolrm.lrmutils.LogUtils.MyLog;
import com.vdolrm.lrmutils.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestFragmentViewPagerActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BaseFragmentPagerAdapter fragmentPagerAdapter;
    private List<String> list_title = Arrays.asList(new String[]{"titel1","title2","title3"});
    private List<Fragment> list_fragment = new ArrayList<Fragment>();

    private void assignViews() {
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
    public void initEvent() {
        for(int i=0;i<list_title.size();i++){
            TestFragment testFragment = TestFragment.newInstance("t1");
            list_fragment.add(testFragment);
        }

        fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(),list_title,list_fragment);
        viewPager.setAdapter(fragmentPagerAdapter);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

    }


    public static class TestFragment extends BaseFragment{
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



        @Override
        public View createLoadedView(boolean reLoading) {
            return null;
        }

        @Override
        public View createLoadingView() {
            return null;
        }

        @Override
        public View createEmptyView() {
            return null;
        }

        @Override
        public View createErrorView() {
            return null;
        }

        @Override
        public View createNetErrorView() {
            return null;
        }

        @Override
        public void lazyLoad() {

        }
    }
}
