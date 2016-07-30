package com.szxyyd.mpxyhl.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.fragment.MyOrderFragment;
import com.szxyyd.mpxyhl.http.BitmapCache;
import com.szxyyd.mpxyhl.http.HttpMethods;
import com.szxyyd.mpxyhl.http.VolleyRequestUtil;
import com.szxyyd.mpxyhl.inter.SubscriberOnNextListener;
import com.szxyyd.mpxyhl.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhl.modle.NurseList;
import com.szxyyd.mpxyhl.modle.ProgressSubscriber;
import com.szxyyd.mpxyhl.view.RoundImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * 预约护理师
 * Created by fq on 2016/7/5.
 */
public class OrderNurseActivity extends Activity implements View.OnClickListener{
    private RoundImageView circle_people = null;
    private TextView tv_introduce = null; //服务介绍
    private LinearLayout ll_notaddr = null; //未有服务地址
    private RelativeLayout rl_addr = null;
    private TextView tv_addr_name = null;
    private TextView tv_addr_phone = null;
    private TextView tv_addr = null;
    private SharedPreferences preferences;
    private NurseList nurse = null;
    private String starlevel = null;
    private String needText = null;
    private String date = null;
    private String remarkText = null;
    private String money = null;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nurse = (NurseList) getIntent().getSerializableExtra("nurse");
        setContentView(R.layout.activity_order);
        mQueue = BaseApplication.getRequestQueue();
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
        initView();
        showDetailData();
        showSharedPreferencesfAddr();
        BaseApplication.getInstance().addActivity(this);
    }
    private void initView(){
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("预约护理师");
        circle_people = (RoundImageView) findViewById(R.id.circle_people);
        ((TextView) findViewById(R.id.order_name)).setText(nurse.getName());
        ((TextView) findViewById(R.id.order_health)).setText(nurse.getSvrid());
        TextView order_technology = (TextView) findViewById(R.id.order_technology);
        order_technology.setText("擅长："+nurse.getSpcty());
        ImageView iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_next.setVisibility(View.GONE);
        ll_notaddr = (LinearLayout) findViewById(R.id.ll_notaddr);
        rl_addr = (RelativeLayout) findViewById(R.id.rl_addr);
        tv_addr_name = (TextView) findViewById(R.id.tv_addr_name);
        tv_addr_phone = (TextView) findViewById(R.id.tv_addr_phone);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        Button btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setText("确定预约");
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(circle_people, 0, R.mipmap.teach);
        mImageLoader.get(Constant.nurseImage + nurse.getIcon(), listener);
        btn_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }
    /**
     * 本地获取地址
     */
    private void showSharedPreferencesfAddr(){
        SharedPreferences preferences = getSharedPreferences(Constant.cstId+"defaddr", Context.MODE_PRIVATE);
        String name = preferences.getString("name", "");
        String phone = preferences.getString("mobile", "");
        String addr = preferences.getString("addr", "");
        if(name.length() > 0){
            ll_notaddr.setVisibility(View.GONE);
            rl_addr.setVisibility(View.VISIBLE);
            tv_addr_name.setText(name);
            tv_addr_phone.setText(phone);
            tv_addr.setText(addr);
        }
    }
    private void showDetailData(){
        preferences = getSharedPreferences("sercontent", Context.MODE_PRIVATE);
        starlevel = preferences.getString("level", "");
        String bodynum = preferences.getString("serpeople", "");
        date = preferences.getString("sertime", "");
        String remark = preferences.getString("note", "");
        money = preferences.getString("price","");
        ((TextView) findViewById(R.id.tv_starlevel)).setText(starlevel);
        if(TextUtils.isEmpty(bodynum)){
            needText = "未选择";
        }else{
            needText = bodynum;
        }
        if(TextUtils.isEmpty(remark)){
            remarkText = "未说明";
        }else{
            remarkText = remark;
        }
        ((TextView) findViewById(R.id.tv_bodynum)).setText(needText);
        ((TextView) findViewById(R.id.tv_date)).setText(date);
        ((TextView) findViewById(R.id.tv_remark)).setText(remarkText);
        ((TextView) findViewById(R.id.tv_money)).setText(money);
    }
    /**
     * 确定预约对话框
     */
    private void showOrderDialog(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.view_sure_order);
        ((TextView)window.findViewById(R.id.tv_leveinfo)).setText(starlevel);
        ((TextView)window.findViewById(R.id.tv_sure_bodynum)).setText(needText);
        ((TextView)window.findViewById(R.id.tv_sure_date)).setText(date);
        ((TextView)window.findViewById(R.id.tv_sure_remark)).setText(remarkText);
        ((TextView)window.findViewById(R.id.tv_sure_money)).setText(money);
        TextView tv_dialog_cancle = (TextView) window.findViewById(R.id.tv_sure_cancle);
        tv_dialog_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        TextView tv_dialog_sure = (TextView) window.findViewById(R.id.tv_sure_sure);
        tv_dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
                alertDialog.cancel();
            }
        });
    }
    SubscriberOnNextListener getResultOnNext = new SubscriberOnNextListener<String>() {
        @Override
        public void onNext(String result) {
            Toast.makeText(OrderNurseActivity.this,"已提交"+result,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(OrderNurseActivity.this,HomePagerActivity.class);
            startActivity(intent);
            BaseApplication.getInstance().exit();
        }
    };
    /**
     * 提交订单
     */
    private void submitData(){
        Map<String,String> map = new HashMap<>();
        map.put("svrid",String.valueOf(Constant.svrId)); //服务类别
        map.put("lvl",String.valueOf(Constant.svrId)); //服务级别
        map.put("cstid", Constant.cstId); //客户id
        map.put("name",tv_addr_name.getText().toString()); //客户名称
        map.put("addr",tv_addr.getText().toString()); //客户地址
        map.put("nurseid",nurse.getNursvrid()); //护理师编号
        map.put("calids",getIdxsList()); //订单计算项
        map.put("idxs",getCalidsList()); //订单功能项
        map.put("note",remarkText); //备注
        map.put("cstpaysum",money); //客户支付金额
        map.put("atarrival",date); //服务时间
        HttpMethods.getInstance().submitAddOrderData("add",map,new ProgressSubscriber<String>(getResultOnNext,this));
    }
    private String getIdxsList(){
        String result = "";
        for(int i = 0; i < Constant.listLevel.size();i++){
            StringBuilder sb = new StringBuilder();
            result += sb.append("&idxs=").append(Constant.listLevel.get(i)).toString();
          //  Log.e("getIdxsList", "result=="+result);
        }
        return result;
    }
    private String getCalidsList(){
        String result = "";
        for(int i = 0; i < Constant.listpople.size();i++){
            StringBuilder sb = new StringBuilder();
            result += sb.append("&calids=").append(Constant.listpople.get(i)).toString();
          //  Log.e("getCalidsList", "result=="+result);
        }
        return result;
    }
    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.btn_back:
                 finish();
                 break;
             case R.id.btn_next:
                 showOrderDialog();
                 break;
         }
    }
}
