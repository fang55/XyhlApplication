package com.szxyyd.mpxyhls.activity;

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
import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.http.VolleyRequestUtil;
import com.szxyyd.mpxyhls.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhls.utils.ExampleUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jq on 2016/7/12.
 */
public class ForgetPasswordActivity extends Activity implements View.OnClickListener{
    private LinearLayout ll_content = null;
    private TextView tv_title = null;
    private EditText et_psdPhone = null;
    private TextView tv_getverifiy = null;
    private String phone = null;
    private String password = null;
    private MyCount mc = null;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpsd);
        initView();
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = preferences.edit();
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
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        ll_content.addView(view);
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
    /**
     * 提交数据
     */
    private void submitData(final String type){
        phone = et_psdPhone.getText().toString().trim();
        if(phone == null){
            ExampleUtil.showToast(getString(R.string.tv_phone_null),this);
            return;
        }
        if(phone.length() < 11){
            ExampleUtil.showToast(getString(R.string.tv_phone_right),this);
            return;
        }
        String url = null;
        Map<String,String> map = new HashMap<String,String>();
        if(type.equals("getVer")){  //获取验证码
            mc = new MyCount(60000, 1000);
            mc.start();
            url = Constant.getVerifiUrl;
            map.put("mobile",phone);
        }else if(type.equals("register")){  //注册
            if(password == null){
                ExampleUtil.showToast("密码不能为空",this);
            }
            url = Constant.registerUrl;
            map.put("usr",phone);
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
            mc.cancel();
            editor.putString("verify", result);
            editor.commit();
        }else{
            editor.putString("phone", phone);
            editor.putString("password", password);
            editor.putString("userId", result);
            editor.commit();
            Toast.makeText(this,"提交成功",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.btn_back:
               finish();
               break;
           case R.id.tv_getverifiy:
               submitData("getVer");
               break;
           case R.id.btn_reset:
               if(phone == null){
                   ExampleUtil.showToast(getString(R.string.tv_phone_null),this);
               }else if(phone.length() < 11){
                   ExampleUtil.showToast(getString(R.string.tv_phone_right),this);
               }else{
                   showResetPsd();
               }
               break;
       }
    }
}
