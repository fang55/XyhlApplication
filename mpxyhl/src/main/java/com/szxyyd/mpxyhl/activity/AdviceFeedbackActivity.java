package com.szxyyd.mpxyhl.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.szxyyd.mpxyhl.R;

/**
 * 意见反馈
 * Created by jq on 2016/7/21.
 */
public class AdviceFeedbackActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        initView();
    }
    private void initView(){
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("反馈");
    }
}
