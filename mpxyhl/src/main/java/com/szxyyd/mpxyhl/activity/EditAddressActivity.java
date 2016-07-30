package com.szxyyd.mpxyhl.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.adapter.RegionAdapter;
import com.szxyyd.mpxyhl.http.HttpMethods;
import com.szxyyd.mpxyhl.http.VolleyRequestUtil;
import com.szxyyd.mpxyhl.inter.CallOnResponsetListener;
import com.szxyyd.mpxyhl.inter.SubscriberOnNextListener;
import com.szxyyd.mpxyhl.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhl.modle.City;
import com.szxyyd.mpxyhl.modle.JsonBean;
import com.szxyyd.mpxyhl.modle.ProgressSubscriber;
import com.szxyyd.mpxyhl.modle.Reladdr;
import com.szxyyd.mpxyhl.utils.OkHttp3Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 添加、编辑服务地址
 * Created by jq on 2016/7/11.
 */
public class EditAddressActivity extends Activity implements View.OnClickListener{
    private TextView tv_title = null;
    private TextView tv_add = null;
    private EditText et_name = null;
    private EditText et_phone = null;
    private EditText et_addr = null;
    private ImageView iv_def = null;
    private RelativeLayout rl_region = null;
    private TextView tv_region = null;
    private RadioGroup rgChannel = null;
    private RadioButton rb1 = null;
    private RadioButton rb2 = null;
    private RadioButton rb3 = null;
    private int dwonIfdef = 0; //1：表示默认地址；
    private  Reladdr reladdr = null;
    private String type;
    private boolean isDwon = false;
    private PopupWindow pop = null;
    private LinearLayout ll_popup = null;
    private ListView listView = null;
    private RegionAdapter adapter = null;
    private List<City> cityList;
    private int rbPostion = 0;
    private View parentView;
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case Constant.SUCCEED:
                    cityList = (List<City>) msg.obj;
                    if(cityList.size() > 0 && cityList != null){
                        adapter = new RegionAdapter(EditAddressActivity.this,cityList);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.activity_editaddress, null);
        setContentView(parentView);
        reladdr = (Reladdr) getIntent().getSerializableExtra("reladdr");
        type = getIntent().getStringExtra("type");
        initView();
        showRegionView();
    }
    private void  initView(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.my_address));
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_add.setVisibility(View.VISIBLE);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        et_name = (EditText)findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_addr = (EditText) findViewById(R.id.et_addr);
        iv_def = (ImageView) findViewById(R.id.iv_def);
        rl_region = (RelativeLayout) findViewById(R.id.rl_region);
        tv_region = (TextView) findViewById(R.id.tv_region);
        if(type.equals("add")){
            tv_title.setText("添加收货地址");
            tv_add.setText("保存");
        }else{
            tv_title.setText("编辑收货地址");
            tv_add.setText("提交");
        }
        if(reladdr != null){
            et_name.setText(reladdr.getName());
            et_phone.setText(reladdr.getMobile());
            et_addr.setText(reladdr.getAddr());
            if(reladdr.getIfdef().equals("是")){
                dwonIfdef = 1;
                iv_def.setBackgroundResource(R.mipmap.def_seladdr);
            }
        }
        iv_def.setOnClickListener(this);
        tv_add.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        rl_region.setOnClickListener(this);
    }

    /**
     * 添加服务地址数据
     */
    private void addLocationData(String states) {
        //addr?a=addAddres&cstid(客户id)&name(客户名称)&mobile(客户手机号)&addr(详细地址)&ifdef(是否默认1 默认  0不是默认)
        String name = et_name.getText().toString();
        String mobile = et_phone.getText().toString();
        String region = tv_region.getText().toString();
        String addr = et_addr.getText().toString();
        if(mobile.length() != 11){
            Toast.makeText(BaseApplication.getInstance(),"手机号码格式不正确",Toast.LENGTH_SHORT).show();
            return;
        }
        if(name.length() == 0 && name == null){
            Toast.makeText(BaseApplication.getInstance(),"请填写联系人",Toast.LENGTH_SHORT).show();
            return;
        }
        if(region.length() == 0 && region == null){
            Toast.makeText(EditAddressActivity.this,"请选择所在地区",Toast.LENGTH_SHORT).show();
            return;
        }
        if(addr.length() == 0 && addr == null){
            Toast.makeText(EditAddressActivity.this,"请填写详细地址",Toast.LENGTH_SHORT).show();
            return;
        }
        final String place = region + addr;
        String url = null;
        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        map.put("mobile",mobile);
        map.put("addr",place);
        map.put("ifdef",String.valueOf(dwonIfdef));
        if (states.equals("add")) {
            HttpMethods.getInstance().submitAddAddresData("addAddres",Constant.cstId,map,new ProgressSubscriber<String>(getResultOnNext,this));
        } else{
            Log.e("EditAddressActivity", "addLocationData--reladdr.getId()==" + reladdr.getId());
            map.put("id",String.valueOf(reladdr.getId()));
            OkHttp3Utils.getInstance().requestPost(Constant.saveAddresUrl,map);
            OkHttp3Utils.getInstance().setOnResultListener(new CallOnResponsetListener() {
                @Override
                public void onSuccess(Call call, okhttp3.Response response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences preferences = getSharedPreferences(Constant.cstId+"defaddr", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("name", et_name.getText().toString());
                            editor.putString("mobile", et_phone.getText().toString());
                            editor.putString("addr", place);
                            editor.commit();
                            finish();
                        }
                    });
                }
            });
        }
    }
    private SubscriberOnNextListener getResultOnNext = new SubscriberOnNextListener<String>() {
        @Override
        public void onNext(String result) {
            Log.e("EditAddressActivity", "addLocationData--result==" + result);
            //如果保存默认地址，缓存到本地
            String region = tv_region.getText().toString();
            String addr = et_addr.getText().toString();
            SharedPreferences preferences = getSharedPreferences(Constant.cstId+"defaddr", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name", et_name.getText().toString());
            editor.putString("mobile", et_phone.getText().toString());
            editor.putString("addr", region+addr);
            editor.commit();
            finish();
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_add:
                if(tv_add.getText().toString().toString().equals("保存")){
                    addLocationData("add");
                }if(tv_add.getText().toString().toString().equals("提交")){
                    addLocationData("edit");
               }
                break;
            case R.id.iv_def:
                if(isDwon){
                    iv_def.setBackgroundResource(R.mipmap.def_selnotaddr);
                    dwonIfdef = 0;
                    isDwon = false;
                }else{
                    isDwon = true;
                    dwonIfdef = 1;
                    iv_def.setBackgroundResource(R.mipmap.def_seladdr);
                }
                break;
            case R.id.rl_region: //所在地区
                ll_popup.startAnimation(AnimationUtils.loadAnimation(EditAddressActivity.this,R.anim.activity_translate_in));
                pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
        }
    }
    /**
     * 加载城市
     */
    private void loadCityData(final String type,String id){
        Map<String, String> map = new HashMap<String, String>();
        String url = null;
        if(type.equals("county")){
            map.put("iid",id);
            url = Constant.findCountyUrl;
        }else if(type.equals("street")){
            map.put("iid",id);
            url = Constant.findStreetUrl;
        }else{
            url = Constant.findCityUrl;
        }
        VolleyRequestUtil.newInstance().GsonPostRequest(this,url,type ,map,new TypeToken<JsonBean>(){},
                new Response.Listener<JsonBean>() {
                    @Override
                    public void onResponse(JsonBean jsonBean) {
                        List<City> list = null;
                        if(type.equals("county")){
                            list = jsonBean.getCounty();
                        }else if(type.equals("street")){
                            list = jsonBean.getCounty();
                        }else{
                            list = jsonBean.getCity();
                            list.remove(0);
                        }
                        Message message = new Message();
                        message.what = Constant.SUCCEED;
                        message.obj = list;
                        mHandler.sendMessage(message);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
    }
    /**
     * 显示选择所在地区
     */
    private void  showRegionView(){
        pop = new PopupWindow(EditAddressActivity.this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        rgChannel=(RadioGroup)view.findViewById(R.id.rgChannel);
        rb1 = (RadioButton) view.findViewById(R.id.rb1);
        rb2 = (RadioButton) view.findViewById(R.id.rb2);
        rb3 = (RadioButton) view.findViewById(R.id.rb3);
        ImageView iv_cancle = (ImageView) view.findViewById(R.id.iv_cancle);
        listView = (ListView) view.findViewById(R.id.lv_region);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(rbPostion == 1){
                    rb1.setText(cityList.get(position).getName());
                    rb1.setTag(cityList.get(position).getIid());
                    rb2.setVisibility(View.VISIBLE);
                    rb2.setChecked(true);
                    rbPostion = 2;
                }else if(rbPostion == 2){
                    rb2.setTag(cityList.get(position).getIid());
                    rb2.setText(cityList.get(position).getName());
                    rb3.setVisibility(View.VISIBLE);
                    rb3.setChecked(true);
                    rbPostion = 3;
                }else if(rbPostion == 3){
                    rb3.setTag(cityList.get(position).getIid());
                    rb3.setText(cityList.get(position).getName());
                    pop.dismiss();
                    ll_popup.clearAnimation();
                    tv_region.setText(rb1.getText().toString() + rb2.getText().toString() + rb3.getText().toString());
                }
            }
        });
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
       rgChannel.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.rb1:
                                rbPostion = 1;
                                loadCityData("city",null);
                                break;
                            case R.id.rb2:
                                rbPostion = 2;
                                loadCityData("county",String.valueOf(rb1.getTag()));
                                break;
                            case R.id.rb3:
                                rbPostion = 3;
                                loadCityData("street",String.valueOf(rb2.getTag()));
                                break;
                        }
                    }
                });
        rb1.setChecked(true);
        pop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        pop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
    }
}
