package com.szxyyd.xyhl.activity;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.http.HttpUtils;
import com.szxyyd.xyhl.http.VolleyRequestUtil;
import com.szxyyd.xyhl.inf.VolleyListenerInterface;
import com.szxyyd.xyhl.modle.Cst;
import com.szxyyd.xyhl.modle.User;
import com.szxyyd.xyhl.utils.ExampleUtil;
import com.szxyyd.xyhl.view.CommentDialog;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 登录界面
 *
 * @author fq
 * @deta 2016/5/17
 */
public class LoginActivity extends Activity implements OnClickListener {
    private Button btn_login;  //登录
    private Button btn_weixin;
    private Button btn_friends;
    private Button btn_qq;
    private TextView tv_forget_password; //忘记密码
    private TextView tv_register; //注册
    private EditText et_phone;  //手机
    private EditText et_password; //密码
    private TextView tv_title;
    private Button btn_back;
    private String psd;  //已存在密码
    private ProgressDialog proDialog;
    public static boolean isForeground = false;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCEED:
                    setAlias();
                    Intent hpIntent = new Intent(LoginActivity.this, HomePageActivity.class);
                    startActivity(hpIntent);
                    finish();
                    break;
                case Constant.TUISONG:
                    Log.e("LoginActivity", "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    break;
            }
            Log.e("LoginActivity", "Unhandled msg - " + msg.what);
        }

        ;
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
        registerMessageReceiver();
    }
    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                Log.e("LoginActivity","MessageReceiver--messge==="+messge);
                Log.e("LoginActivity","MessageReceiver--extras==="+extras);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }
    private void setCostomMsg(String msg){

         Toast.makeText(LoginActivity.this,"msg=="+msg,Toast.LENGTH_SHORT).show();

    }
    /**
     * 初始化
     */
    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_weixin = (Button) findViewById(R.id.btn_weixin);
        btn_friends = (Button) findViewById(R.id.btn_microblog);
        btn_qq = (Button) findViewById(R.id.btn_qq);
        tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);
        tv_register = (TextView) findViewById(R.id.tv_register);
    }

    /**
     * 点击事件
     */
    private void initEvent() {
        btn_back.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_weixin.setOnClickListener(this);
        btn_friends.setOnClickListener(this);
        btn_qq.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String usr = preferences.getString("usr", "");
        String password = preferences.getString("password", "");
      //  Constant.cstId = preferences.getString("cstId", "");
        Log.e("LoginActivity", "onResume--Constant.cstId==" + Constant.cstId);
        Log.e("LoginActivity", "onResume--usr==" + usr);
        if (!TextUtils.isEmpty(usr)) {
            et_phone.setText(usr);
        }
        if (!TextUtils.isEmpty(password)) {
            et_password.setText(password);
            psd = password;
        }
    }
    private void showToast(String str) {
        Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
    }
    private void submitLoginData(final String type) {
        String phone = et_phone.getText().toString().trim();
        String passWord = et_password.getText().toString().trim();
        if (phone == null) {
            showToast("手机号码不能为空");
            return;
        }
        if (phone.length() < 11) {
            showToast("请输入正确的手机号码");
            return;
        }
        String url = null;
        if (type.equals("findUsr")) { //验证用户
            url = Constant.findUsrUrl + "&usr=" + phone;
        } else if (type.equals("login")) { //登录用户
            if (passWord == null) {
                showToast("密码不能为空");
                return;
            }
            proDialog = ProgressDialog.show(LoginActivity.this, "", "正在登录.....");
            url = Constant.loginUrl + "&usr=" + phone + "&pwd=" + passWord;
        }
        VolleyRequestUtil volley = new VolleyRequestUtil();
        volley.RequestGet(this, url, type,
                new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("LoginActivity", "lodeUser--result==" + result);
                        if(proDialog != null){
                            proDialog.cancel();
                        }
                        parserUserData(result, type);
                    }
                    @Override
                    public void onError(VolleyError error) {
                    }
                });
    }


    /**
     * 如果用户不存在，就去注册
     *
     * @param result
     */
    private void parserUserData(String result, String type) {
        try {
                JSONObject json = new JSONObject(result);
            SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
                if (type.equals("findUsr")) {
                    String jsonData = json.getString("usr");
                    Type listType = new TypeToken<LinkedList<User>>() {}.getType();
                    Gson gson = new Gson();
                    List<User> list = gson.fromJson(jsonData, listType);
                    for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                        User user = (User) iterator.next();
                        Constant.usr = user.getUser();
                        Constant.usrId = user.getId();
                        editor.putString("icon", user.getIcon());
                        editor.putString("userid", user.getId());
                        editor.putString("nickname", user.getNickname());
                        editor.commit();
                    }
                    if (list.size() == 0) {
                        showToast("用户不存在,请注册");
                    } else {
                        submitLoginData("login");
                    }
                } else if (type.equals("login")) {
                    error(result,editor);
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void error(String result,SharedPreferences.Editor editor){
        try {
            JSONObject json = new JSONObject(result);
            if(json.isNull("type")){
                String jsonCst = json.getString("cst");
                Type cstType = new TypeToken<LinkedList<Cst>>() {}.getType();
                Gson gsonCst = new Gson();
                List<Cst> listCst = gsonCst.fromJson(jsonCst, cstType);
                for (Iterator iterator = listCst.iterator(); iterator.hasNext(); ) {
                    Cst cst = (Cst) iterator.next();
                    Constant.cstId = cst.getId();
                    Constant.cityId = cst.getCity();
                    Constant.cityName = cst.getCityName();
                }
                editor.putString("cstId", Constant.cstId);
                editor.putString("usr", et_phone.getText().toString());
                editor.putString("password", et_password.getText().toString());
                editor.commit();
                Message message = new Message();
                message.what = Constant.SUCCEED;
                handler.sendMessage(message);
            }else{
                showToast(json.getString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_login:
                submitLoginData("findUsr");
                break;
            case R.id.tv_forget_password:
                Intent fpIntent = new Intent(this, ForgetPasswordActivity.class);
                fpIntent.putExtra("phone", et_phone.getText().toString().trim());
                startActivity(fpIntent);
                break;
            case R.id.tv_register:
                Intent rsIntent = new Intent(this, RegisterActivity.class);
                startActivity(rsIntent);
                break;
            case R.id.btn_weixin:

                break;
            case R.id.btn_microblog:

                break;
            case R.id.btn_qq:

                break;
            default:
                break;
        }
    }
    /*极光推送别名*/
    private void setAlias() {
        String alias = Constant.usrId ;
        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(LoginActivity.this,"错误", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            Toast.makeText(LoginActivity.this,"错误", Toast.LENGTH_SHORT).show();
            return;
        }
        // 调用 Handler 来异步设置别名
        handler.sendMessage(handler.obtainMessage(Constant.TUISONG, alias));
    }
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.e("LoginActivity", logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.e("LoginActivity", logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    handler.sendMessageDelayed(handler.obtainMessage(Constant.TUISONG, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("LoginActivity", logs);
            }
            ExampleUtil.showToast(logs, getApplicationContext());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
    }
}
