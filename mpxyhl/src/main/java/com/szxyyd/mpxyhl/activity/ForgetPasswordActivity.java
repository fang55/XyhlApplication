package com.szxyyd.mpxyhl.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.http.VolleyRequestUtil;
import com.szxyyd.mpxyhl.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhl.utils.ExampleUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 忘記密碼
 * Created by jq on 2016/7/4.
 */
public class ForgetPasswordActivity extends Activity implements View.OnClickListener{
    private LinearLayout ll_content = null;
    private TextView tv_title  = null;
    private EditText et_psdPhone  = null;
    private EditText et_code  = null;
    private EditText et_newpsd  = null;
    private EditText et_resetpsd  = null;
    private TextView tv_getverifiy  = null;
    private Button btn_del  = null;
    private MyCount mc  = null;
    private String phone  = null;
    private String password  = null;
    private String getverifiy  = null;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpsd);
        initView();
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = preferences.edit();
        Constant.usrId = preferences.getString("usrId", "");
    }
    private void initView(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.tv_forget));
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        showPassword();
    }
    /**
     * 忘记密码界面
     */
    private void showPassword(){
        ll_content.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.view_forgetpsd, null, false);
        Button btn_reset = (Button) view.findViewById(R.id.btn_reset);
        et_psdPhone = (EditText) view.findViewById(R.id.et_psdPhone);
        et_code = (EditText) view.findViewById(R.id.et_code);
        tv_getverifiy = (TextView) view.findViewById(R.id.tv_getverifiy);
        btn_reset.setOnClickListener(this);
        tv_getverifiy.setOnClickListener(this);
        ll_content.addView(view);
    }
    /**
     * 重置密码界面
     */
    private void showResetPsd(){
        ll_content.removeAllViews();
        tv_title.setText(getString(R.string.btn_set));
        View view = LayoutInflater.from(this).inflate(R.layout.view_resetpsd, null, false);
        et_newpsd = (EditText) view.findViewById(R.id.et_newpsd);
        et_resetpsd = (EditText) view.findViewById(R.id.et_resetpsd);
        btn_del = (Button) view.findViewById(R.id.btn_del);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        ll_content.addView(view);
    }
    /**
     * 提交数据
     */
    private void submitData(final String type){
        String url = null;
        Map<String,String> map = new HashMap<String,String>();
        if(type.equals("getVer")){  //获取验证码
            mc = new MyCount(60000, 1000);
            mc.start();
            url = Constant.getVerifiUrl;
            map.put("mobile",phone);
        }else if(type.equals("pwd")){  //新密码
            String newpsd = et_newpsd.getText().toString();
            String resetpsd = et_resetpsd.getText().toString();
            if(newpsd == null){
                ExampleUtil.showToast("密码不能为空",this);
                return;
            }
            if(!newpsd.equals(resetpsd)){
                ExampleUtil.showToast("输入的密码不一致",this);
                return;
            }
            if(Constant.usrId == null){
                ExampleUtil.showToast("用户不存在，先注册",this);
                return;
            }
            url = Constant.getPwdUrl;
            map.put("id",Constant.usrId);
            map.put("pwd",password);
        }
        VolleyRequestUtil.newInstance().RequestPost(this,url,type,map,new VolleyListenerInterface(this,VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onSuccess(String result) {
                Log.e("RegisterActivity", "result=="+result);
                parserData(result,type);
            }
            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    /**
     * 解析返回的数据
     * @param result
     */
    private void parserData(String result,String type){
        if(type.equals("getVer")){
            getverifiy = result;
            mc.cancel();
        }else{
            editor.putString("phone", phone);
            editor.putString("password", password);
            editor.putString("userId", result);
            editor.commit();
            Toast.makeText(this,"提交成功",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    /**
     * 定义一个倒计时的内部类
     */
    private class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_getverifiy.setText((millisUntilFinished / 1000) + "");
        }
        @Override
        public void onFinish() {
            tv_getverifiy.setText("0");
        }
    }
    @Override
    public void onClick(View view) {
        phone = et_psdPhone.getText().toString().trim();
        switch (view.getId()){
            case R.id.btn_back:
              finish();
                break;
            case R.id.tv_getverifiy:
                if(phone.length() == 0){
                    ExampleUtil.showToast(getString(R.string.tv_phone_null),this);
                    return;
                }else{
                    submitData("getVer");
                }
                break;
            case R.id.btn_reset:
                if(phone.length() == 0){
                    ExampleUtil.showToast(getString(R.string.tv_phone_null),this);
                    return;
                }
                if(phone.length() < 11){
                    ExampleUtil.showToast(getString(R.string.tv_phone_right),this);
                    return;
                }
                if(!et_code.getText().toString().equals(getverifiy)){
                    ExampleUtil.showToast("请输入正确验证码",this);
                    return;
                }
                showResetPsd();
                break;
            case R.id.btn_del:
                et_newpsd.setText("");
                break;
            case R.id.btn_ok:
                submitData("pwd");
                break;
        }
    }
}
