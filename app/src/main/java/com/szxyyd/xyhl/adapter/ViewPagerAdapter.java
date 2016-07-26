package com.szxyyd.xyhl.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by jq on 2016/6/26.
 */
public class ViewPagerAdapter extends PagerAdapter{
    private ImageView[] imageViews;
    public ViewPagerAdapter(ImageView[] imgIdArray) {
        imageViews = imgIdArray;
    }
    @Override
    public int getCount() {
        return imageViews.length;
    }
    @Override
    public boolean isViewFromObject(View arg0 , Object arg1) {
        return arg0 == arg1;
    }
    public void destroyItem(ViewGroup container, int position, Object object) {
     //   container.removeView(mList.get(position % mList.size()));
        container.removeView((View) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.removeView(imageViews[position]);
        container.addView(imageViews[position]);
        imageViews[position].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return imageViews[position];
    }
    }
