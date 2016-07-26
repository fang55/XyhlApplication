package com.szxyyd.mpxyhls.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szxyyd.mpxyhls.R;

/**
 * 设置
 * Created by jq on 2016/7/13.
 */
public class SetActivity extends Activity implements View.OnClickListener{
    private int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initView();
    }
    private void initView(){
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.text_set));
        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        RelativeLayout rl_info = (RelativeLayout) findViewById(R.id.rl_info);
        rl_info.setOnClickListener(this);
        RelativeLayout rl_revise = (RelativeLayout) findViewById(R.id.rl_revise);
        rl_revise.setOnClickListener(this);
        RelativeLayout rl_advice = (RelativeLayout) findViewById(R.id.rl_advice);
        rl_advice.setOnClickListener(this);
        RelativeLayout rl_about = (RelativeLayout) findViewById(R.id.rl_about);
        rl_about.setOnClickListener(this);
        RelativeLayout rl_check = (RelativeLayout) findViewById(R.id.rl_check);
        rl_check.setOnClickListener(this);
        RelativeLayout rl_protocol = (RelativeLayout) findViewById(R.id.rl_protocol);
        rl_protocol.setOnClickListener(this);
        Button btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.rl_info: // 个人资料
                type = Constant.SET_INFO;
                break;
            case R.id.rl_revise: // 修改密码
                type = Constant.SET_REVISE;
                break;
            case R.id.rl_advice: // 意见反馈
                type = Constant.SET_ADVICE;
                break;
            case R.id.rl_about: // 关于我们
                type = Constant.SET_ABOUT;
                break;
            case R.id.rl_check:  //检查版本
                type = Constant.SET_CHECK;
                break;
            case R.id.rl_protocol: // 服务协议
                type = Constant.SET_PROTOCOL;
                break;
        }
        Intent intent = new Intent(SetActivity.this,SetContentActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
    }
}
