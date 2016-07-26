package com.szxyyd.xyhl.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.http.VolleyRequestUtil;
import com.szxyyd.xyhl.inf.VolleyListenerInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 忘记密码
 * @author jq
 *
 */
public class ForgetPasswordActivity extends Activity implements OnClickListener{
	private TextView tv_title;
	private TextView tv_code;
	private EditText et_phone;
	private EditText et_code;
	private Button btn_back;
	private LinearLayout ll_content;
	private EditText et_newpsd;
	private EditText et_resetpsd;
	private String phone;
	private String resetPsd;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private String verify;
      @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_forgetpassword);
		  phone = getIntent().getStringExtra("phone");
    	initView();
    	initEvent();
		showPassword();
		  preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
		 editor = preferences.edit();
    }
      private void initView(){
		  tv_title = (TextView) findViewById(R.id.tv_title);
		  btn_back = (Button) findViewById(R.id.btn_back);
		  ll_content = (LinearLayout) findViewById(R.id.ll_content);
      }
      private void initEvent(){
    	  btn_back.setOnClickListener(this);
      }
	/**
	 * 忘记密码
	 */
	private void showPassword(){
		ll_content.removeAllViews();
		tv_title.setText(getString(R.string.tv_forget));
		View view = LayoutInflater.from(this).inflate(R.layout.view_forgetpsd, null, false);
		Button btn_next = (Button) view.findViewById(R.id.btn_next);
		et_phone = (EditText) view.findViewById(R.id.et_phone);
		if(phone != null){
			et_phone.setText(phone);
		}
		tv_code = (TextView) view.findViewById(R.id.tv_code);
		et_code = (EditText) view.findViewById(R.id.et_code);
		tv_code.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		ll_content.addView(view);
	}
	/**
	 * 重置密码
	 */
	private void showResetPsd(){
		ll_content.removeAllViews();
		tv_title.setText(getString(R.string.btn_reset));
		View view = LayoutInflater.from(this).inflate(R.layout.view_resetpsd, null, false);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		et_newpsd = (EditText) view.findViewById(R.id.et_newpsd);
		et_resetpsd = (EditText) view.findViewById(R.id.et_resetpsd);
		Button btn_del = (Button) view.findViewById(R.id.btn_del);
		btn_del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				et_newpsd.setText("");
			}
		});
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				lodeData();
			}
		});
		ll_content.addView(view);
	}
	/**
	 * 找回密码
	 */
	private void lodeData(){
		//cst?a=getPwd&id(用户id)&pwd(新密码)
		String newPsd = et_newpsd.getText().toString().trim();
		resetPsd = et_resetpsd.getText().toString().trim();
		if(!newPsd.equals(resetPsd)){
			showToast("两次输入密码不一致");
			return;
		}
		Log.e("ForgetPasswordActivity", "Constant.cstId=="+Constant.cstId);
		String url = Constant.getPwdUrl + "&id="+Constant.cstId +"&pwd="+resetPsd;
		VolleyRequestUtil volley = new VolleyRequestUtil();
		volley.RequestGet(this, url, "getPwd",
				new VolleyListenerInterface(this, VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
					@Override
					public void onSuccess(String result) {
						//result=={"cst":"yes"}
						Log.e("ForgetPasswordActivity", "result=="+result);
						parserData(result,"reset");
					}
					@Override
					public void onError(VolleyError error) {

					}
				});
	}
	private void parserData(String result,String type){
		JSONObject json;
		try {
			if(type.equals("verify")){
				verify = result;
				editor.putString("verify", result);
			}else if(type.equals("reset")){
				json = new JSONObject(result);
				String jsonData = json.getString("cst");
				Log.e("ForgetPasswordActivity", "jsonData=="+jsonData);
				if(jsonData != null){
					editor.putString("usr", phone);
					editor.putString("password", resetPsd);
					editor.commit();
				}
				Toast.makeText(ForgetPasswordActivity.this,"已修改成功",Toast.LENGTH_SHORT).show();
				finish();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void showToast(String str){
		Toast.makeText(ForgetPasswordActivity.this,str,Toast.LENGTH_SHORT).show();
	}
	/**
	 * 提交数据
	 */
	private void submitData(){
		phone = et_phone.getText().toString().trim();
		if(phone == null){
			showToast("手机号码不能为空");
			return;
		}
		if(phone.length() < 11){
			showToast("请输入正确的手机号码");
			return;
		}
		String  url = Constant.getVerifiUrl + "&mobile="+phone;;
		VolleyRequestUtil volley = new VolleyRequestUtil();
		volley.RequestGet(this, url, "verify",
				new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
					@Override
					public void onSuccess(String result) {
						Log.e("RegisterActivity", "submitData--verify=="+result);
						parserData(result,"verify");
					}
					@Override
					public void onError(VolleyError error) {

					}
				});
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_back:
				finish();
				break;
			case R.id.tv_code:  //获取验证码
				submitData();
				break;
			case R.id.btn_next:
                if(et_code.getText().toString()== null){
					showToast("验证码不能为空");
					return;
				}else if(!et_code.getText().toString().equals(verify)){
					Log.e("ForgetPassword", "onClick--verify=="+verify);
					showToast("验证码输入不正确");
					return;
				}else{
					showResetPsd();
				}
				break;
			default:
				break;
		}

	}
}
