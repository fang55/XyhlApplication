package com.szxyyd.mpxyhls.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.adapter.ArchivesFragmentPagerAdapter;
import com.szxyyd.mpxyhls.modle.Nurse;

/**
 * 我的档案
 * Created by jq on 2016/7/13.
 */
public class ArchivesActivity extends FragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private RadioGroup rgChannel;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private ViewPager viewPager;
    private ArchivesFragmentPagerAdapter mAdapter;
    public Nurse nurse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archives);
        nurse = (Nurse) getIntent().getSerializableExtra("nurse");
        initView();
    }
    private void initView(){
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.my_file));
        Button btn_back = (Button) findViewById(R.id.btn_back);
        rgChannel = (RadioGroup) findViewById(R.id.rgChannel);
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        rb3 = (RadioButton) findViewById(R.id.rb3);
        rb4 = (RadioButton) findViewById(R.id.rb4);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mAdapter = new ArchivesFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(Constant.PAGE_ONE);
        viewPager.setOnPageChangeListener(this);
        rgChannel.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.rb1:
                                viewPager.setCurrentItem(Constant.PAGE_ONE);
                                changeTextColor(rb1,rb2,rb3,rb4);
                                break;
                            case R.id.rb2:
                                viewPager.setCurrentItem(Constant.PAGE_TWO);
                                changeTextColor(rb2,rb1,rb3,rb4);
                                break;
                            case R.id.rb3:
                                viewPager.setCurrentItem(Constant.PAGE_THREE);
                                changeTextColor(rb3,rb2,rb1,rb4);
                                break;
                            case R.id.rb4:
                                viewPager.setCurrentItem(Constant.PAGE_FOUR);
                                changeTextColor(rb4,rb2,rb3,rb1);
                                break;
                        }
                    }
                });
        rb1.setChecked(true);
        btn_back.setOnClickListener(this);
    }
    private void changeTextColor(RadioButton tv1,RadioButton tv2,RadioButton tv3,RadioButton tv4){
        tv1.setTextColor(getResources().getColor(R.color.color_bule));
        tv2.setTextColor(getResources().getColor(R.color.color_six_six));
        tv3.setTextColor(getResources().getColor(R.color.color_six_six));
        tv4.setTextColor(getResources().getColor(R.color.color_six_six));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
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
                    rb1.setChecked(true);
                    break;
                case Constant.PAGE_TWO:
                    rb2.setChecked(true);
                    break;
                case Constant.PAGE_THREE:
                    rb3.setChecked(true);
                    break;
                case Constant.PAGE_FOUR:
                    rb4.setChecked(true);
                    break;
            }
        }
    }

}
