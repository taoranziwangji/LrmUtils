package com.vdolrm.lrmutils.Adapter.PageAdapter;



import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * 该 PagerAdapter 的实现将只保留当前页面，
 * 当页面离开视线后，就会被消除，释放其资源；而在页面需要显示时，生成新的页面(就像 ListView 的实现一样)。
 * 这么实现的好处就是当拥有大量的页面时，不必在内存中占用大量的内存。
 * Created by Administrator on 2016/3/29.
 */
public class BaseFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    private List<String> list_title;
    private List<Fragment> list_fragment;

    public BaseFragmentStatePagerAdapter(FragmentManager fm, List<String> list_title, List<Fragment> list_fragment) {
        super(fm);
        this.list_title = list_title;
        this.list_fragment = list_fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_fragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(list_title==null || list_title.size()==0){
            return super.getPageTitle(position);
        }

        return list_title.get(position);
    }

    /**重写getItemPosition方法返回POSITION_NONE解决调用pageradapter.notifydatasetchanged不更新数据的问题
     * http://blog.sina.com.cn/s/blog_671d2e4f01015rgn.html
     * http://www.cnblogs.com/lianghui66/p/3607091.html
     * */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
