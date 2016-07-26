package com.szxyyd.xyhl.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.http.HttpUtils;
import com.szxyyd.xyhl.http.VolleyRequestUtil;
import com.szxyyd.xyhl.inf.VolleyListenerInterface;

/**
 * 注册界面
 * @author fq
 *
 */
public class RegisterActivity extends Activity implements OnClickListener{
	private TextView tv_title;
	private TextView tv_cdtimer; //倒计时
	private Button btn_get; //获取验证
	private Button btn_back;
	private Button btn_register;
	private EditText et_phone;
	private EditText et_password;
	private EditText et_authcode;
	private MyCount mc;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private String phone;
	private String password;
	private String verify = null;
	private ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
    	initEvent();
		preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
		editor = preferences.edit();
	}
	private void initView(){
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getString(R.string.tv_register));
		tv_cdtimer = (TextView) findViewById(R.id.tv_cdtimer);
	  et_phone = (EditText) findViewById(R.id.et_phone);
	  et_password = (EditText) findViewById(R.id.et_password);
		et_authcode = (EditText) findViewById(R.id.et_getcode);
  	  btn_back = (Button) findViewById(R.id.btn_back);
  	  btn_register = (Button) findViewById(R.id.btn_register);
		btn_get = (Button) findViewById(R.id.btn_get);
    }
    private void initEvent(){
  	  btn_back.setOnClickListener(this);
  	  btn_register.setOnClickListener(this);
		btn_get.setOnClickListener(this);
    }
	/**
	 * 定义一个倒计时的内部类
	 */
	private class MyCount extends CountDownTimer{

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			tv_cdtimer.setText((millisUntilFinished / 1000) + "");
		}
		@Override
		public void onFinish() {
			tv_cdtimer.setText("0");
		}
	}
    private void showProgress(){
		progressDialog = ProgressDialog.show(RegisterActivity.this, "", "获取数据中...",true);
	}
	private void cancleProgress(){
		progressDialog.cancel();
	}
	private void showToast(String str){
		Toast.makeText(RegisterActivity.this,str,Toast.LENGTH_SHORT).show();
	}
	/**
	* 提交数据
	*/
	private void submitData(final String type){
		phone = et_phone.getText().toString().trim();
		password = et_password.getText().toString().trim();
		if(phone == null){
			showToast("手机号码不能为空");
			return;
		}
		if(phone.length() < 11){
			showToast("请输入正确的手机号码");
			return;
		}
		String url = null;
		if(type.equals("getVer")){  //获取验证码
			mc = new MyCount(60000, 1000);
			mc.start();
			btn_get.setVisibility(View.GONE);
			tv_cdtimer.setVisibility(View.VISIBLE);
			url = Constant.getVerifiUrl + "&mobile="+phone;
		}else if(type.equals("register")){  //注册
			if(password == null){
				showToast("密码不能为空");
			}
			url = Constant.registerUrl + "&usr="+phone+"&pwd="+password;
		}
		VolleyRequestUtil volley = new VolleyRequestUtil();
		volley.RequestGet(this, url, type,
				new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
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
		  verify = result;
		  editor.putString("verify", result);
		  editor.commit();
	  }else{
		  editor.putString("usr", phone);
		  editor.putString("password", password);
		  editor.commit();
		  Toast.makeText(this,"提交成功",Toast.LENGTH_SHORT).show();
		  finish();
	  }
}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_back:
				finish();
				break;
			case R.id.btn_get:
				submitData("getVer");
				break;
			case R.id.btn_register:  //80001912
				/*if(TextUtils.isEmpty(et_authcode.getText().toString())){
					Toast.makeText(RegisterActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
					return;
				}
				if(!verify.equals(et_authcode.getText().toString())){ //输入的验证码不正确
					Log.e("RegisterActivity", "onClick--verify=="+verify);
					showToast("输入的验证码不正确");
					return;
				}*/
				submitData("register");
				break;
			default:
				break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mc != null){
			mc.cancel();
		}
	}
}
