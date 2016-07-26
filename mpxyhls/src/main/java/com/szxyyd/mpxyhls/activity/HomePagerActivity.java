package com.szxyyd.mpxyhls.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.adapter.HomeFragmentPagerAdapter;

/**
 * Created by jq on 2016/7/12.
 */
public class HomePagerActivity extends FragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private TextView tv_title;
    private TextView tv_home;
    private TextView tv_order;
    private TextView tv_my;
    private Button btn_set;
    private ViewPager viewPager;
    private HomeFragmentPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepager);
        initView();
    }
    private void initView(){
        btn_set = (Button) findViewById(R.id.btn_set);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_order = (TextView) findViewById(R.id.tv_order);
        tv_my = (TextView) findViewById(R.id.tv_my);
        LinearLayout ll_home = (LinearLayout) findViewById(R.id.ll_home);
        LinearLayout ll_order = (LinearLayout) findViewById(R.id.ll_order);
        LinearLayout ll_my = (LinearLayout) findViewById(R.id.ll_my);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mAdapter = new HomeFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(Constant.PAGE_ONE);
        viewPager.setOnPageChangeListener(this);
        changeTextColor(tv_home, tv_order, tv_my);
        tv_title.setText("心悦护理师");
        ll_home.setOnClickListener(this);
        ll_order.setOnClickListener(this);
        ll_my.setOnClickListener(this);
        btn_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.ll_home:
                 viewPager.setCurrentItem(Constant.PAGE_ONE);
                 break;
             case R.id.ll_order:
                 viewPager.setCurrentItem(Constant.PAGE_TWO);
                 break;
             case R.id.ll_my:
                 viewPager.setCurrentItem(Constant.PAGE_THREE);
                 btn_set.setVisibility(View.VISIBLE);
                 break;
             case R.id.btn_set:
                 Intent intent = new Intent(HomePagerActivity.this,SetActivity.class);
                 startActivity(intent);
                 break;
         }
    }
    /**
     * 点击改变字体颜色
     */
    private void changeTextColor(TextView tv1,TextView tv2,TextView tv3){
        tv1.setTextColor(this.getResources().getColor(R.color.color_bule));
        tv2.setTextColor(this.getResources().getColor(R.color.color_six_nine));
        tv3.setTextColor(this.getResources().getColor(R.color.color_six_nine));
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            switch (viewPager.getCurrentItem()) {
                case Constant.PAGE_ONE:
                    tv_title.setText("心悦护理师");
                    changeTextColor(tv_home, tv_order, tv_my);
                    break;
                case Constant.PAGE_TWO:
                    tv_title.setText("我的订单");
                    changeTextColor(tv_order, tv_home, tv_my);
                    break;
                case Constant.PAGE_THREE:
                    tv_title.setText("个人中心");
                    changeTextColor(tv_my, tv_order, tv_home);
                    break;

            }
        }
    }
}
