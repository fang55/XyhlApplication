package com.szxyyd.mpxyhl.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szxyyd.mpxyhl.R;

/**
 * 设置
 * Created by jq on 2016/7/6.
 */
public class SetActivity extends Activity implements View.OnClickListener{
    private int type;
    //我的模块页面的常量
    private int SET_INFO = 1;  //个人资料
    private int SET_REVISE = 2; //修改密码
    private int SET_ABOUT= 4; //关于我们
    private int SET_CHECK = 5; //检查版本
    private int SET_PROTOCOL = 6; //服务协议

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
        RelativeLayout rl_about = (RelativeLayout) findViewById(R.id.rl_about);
        rl_about.setOnClickListener(this);
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
            case R.id.btn_exit:
                System.exit(0);
                break;
            case R.id.rl_info: // 个人资料
                type = SET_INFO;
                break;
            case R.id.rl_revise: // 修改密码
                type = SET_REVISE;
                break;
            case R.id.rl_about: // 关于我们
                type = SET_ABOUT;
                break;
            case R.id.rl_protocol: // 服务协议
                type = SET_PROTOCOL;
                break;
        }
        Intent intent = new Intent(SetActivity.this,SetContentActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
    }
}
