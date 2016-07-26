package com.szxyyd.mpxyhl.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by jq on 2016/7/8.
 */
public class OrderPageFragmentAdapter extends FragmentPagerAdapter  {
    private List<Fragment> fragmentList;
    public OrderPageFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList){
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public long getItemId(int position) {
        // 获取当前数据的hashCode
        int hashCode = fragmentList.get(position).hashCode();
        return hashCode;
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
