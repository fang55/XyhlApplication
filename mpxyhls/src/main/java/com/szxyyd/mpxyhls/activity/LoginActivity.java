package com.szxyyd.mpxyhls.activity;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.http.VolleyRequestUtil;
import com.szxyyd.mpxyhls.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhls.modle.Nurse;
import com.szxyyd.mpxyhls.modle.User;
import com.szxyyd.mpxyhls.utils.ExampleUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by jq on 2016/7/12.
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    private EditText et_phone;
    private EditText et_password;
    private ProgressDialog proDialog;
    private DbUtils dbUtils;
    public static boolean isForeground = false;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCEED:
                    setAlias();
                    List<Nurse> list = (List<Nurse>) msg.obj;
                    try {
                        if(null != list){
                            Nurse nurse = list.get(0);
                            dbUtils.dropTable(Nurse.class);
                            dbUtils.save(nurse);
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    Intent hpIntent = new Intent(LoginActivity.this, HomePagerActivity.class);
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
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbUtils = BaseApplication.getdbUtils();
        registerMessageReceiver();
        initView();
    }
    private void initView(){
        TextView tv_register = (TextView) findViewById(R.id.tv_register);
        TextView tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_password = (EditText) findViewById(R.id.et_password);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        Button btn_login = (Button) findViewById(R.id.btn_go);
        tv_register.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String usr = preferences.getString("phone", "");
        String password = preferences.getString("password", "");
        //  Constant.cstId = preferences.getString("cstId", "");
        Log.e("LoginActivity", "onResume--usr==" + usr);
        if (!TextUtils.isEmpty(usr)) {
            et_phone.setText(usr);
        }
        if (!TextUtils.isEmpty(password)) {
            et_password.setText(password);
        }
        JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
    /**
     * 验证用户是否存在
     */
    private void submitFindData() {
        String phone = et_phone.getText().toString().trim();
        if (phone == null) {
            ExampleUtil.showToast(getString(R.string.tv_mobile_null),this);
            return;
        }
        if (phone.length() < 11) {
            ExampleUtil.showToast(getString(R.string.tv_mobile_right),this);
            return;
        }
     //   proDialog = ProgressDialog.show(LoginActivity.this, "", "正在登录.....");
        String url = Constant.findUsrUrl + "&usr=" + phone;
        VolleyRequestUtil volley = new VolleyRequestUtil();
        volley.RequestGet(this, url, "find",
                new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("LoginActivity", "submitLoginData---result=="+result);
                        parserData(result,"findUsr");
                    }
                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }
    /**
     * 登录接口
     */
    private void submitLoginData(){
        String phone = et_phone.getText().toString().trim();
        String passWord = et_password.getText().toString().trim();
        if (passWord == null) {
            ExampleUtil.showToast(getString(R.string.tv_psd_right),this);
            return;
        }
        String url = Constant.loginUrl +"&usr="+phone+"&pwd="+passWord;
        VolleyRequestUtil volley = new VolleyRequestUtil();
        volley.RequestGet(this, url, "login",
                new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("LoginActivity", "submitLoginData---result=="+result);
                    //    proDialog.dismiss();
                        parserData(result,"login");
                    }
                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }
    private void parserData(String result, String type) {
        try {
            JSONObject json = new JSONObject(result);
            if (type.equals("findUsr")) {
                String jsonData = json.getString("usr");
                Type listType = new TypeToken<LinkedList<User>>() {}.getType();
                Gson gson = new Gson();
                List<User> list = gson.fromJson(jsonData, listType);
                for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                    User user = (User) iterator.next();
                    Constant.nickname = user.getNickname();
                    Constant.usrId = user.getId();
                    Log.e("LoginActivity", "mpls--Constant.usrId=="+Constant.usrId);
                }
                if (list.size() == 0) {
                 //   proDialog.dismiss();
                    ExampleUtil.showToast(getString(R.string.tv_user_null),this);
                } else {
                    submitLoginData();
                }
            }else{
                decideData(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断用户账号、密码是否正确
     */
    private void decideData(String result){
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        try {
            JSONObject json = new JSONObject(result);
            if(json.isNull("type")){
                String jsonCst = json.getString("nur");
                Type nurType = new TypeToken<LinkedList<Nurse>>() {}.getType();
                Gson gsonNur = new Gson();
                List<Nurse> listCst = gsonNur.fromJson(jsonCst, nurType);
                editor.putString("phone", et_phone.getText().toString());
                editor.putString("password", et_password.getText().toString());
                editor.commit();
                Message message = new Message();
                message.what = Constant.SUCCEED;
                message.obj = listCst;
                handler.sendMessage(message);
            }else{
                ExampleUtil.showToast(json.getString("msg"),this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_register:
                Intent intentRegister = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.tv_forget_password:
                Intent intentForget = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intentForget);
                break;
            case R.id.btn_go:
                submitFindData();
                break;
        }
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
                //   setCostomMsg(showMsg.toString());
            }
        }
    }
    private void setCostomMsg(String msg){
        Toast.makeText(LoginActivity.this,"msg=="+msg,Toast.LENGTH_SHORT).show();

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
           // ExampleUtil.showToast(logs, getApplicationContext());
       //     ExampleUtil.showToast(alias, getApplicationContext());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
    }
}
