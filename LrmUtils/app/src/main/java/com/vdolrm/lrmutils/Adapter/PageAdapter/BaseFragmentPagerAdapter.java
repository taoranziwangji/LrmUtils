package com.vdolrm.lrmutils.Adapter.PageAdapter;



import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * FragmentPagerAdapter类内的每一个生成的 Fragment 都将保存在内存之中，因此适用于那些相对静态的页，数量也比较少的那种；
 * 如果需要处理有很多页，并且数据动态性较大、占用内存较多的情况，应该使用FragmentStatePagerAdapter
 * Created by Administrator on 2016/3/29.
 */
public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<String> list_title;
    private List<Fragment> list_fragment;

    public BaseFragmentPagerAdapter(FragmentManager fm, List<String> list_title, List<Fragment> list_fragment) {
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
