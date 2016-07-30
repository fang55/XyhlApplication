package com.szxyyd.mpxyhl.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.http.VolleyRequestUtil;
import com.szxyyd.mpxyhl.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhl.modle.JsonBean;
import com.szxyyd.mpxyhl.modle.PriceLvl;
import com.szxyyd.mpxyhl.modle.Reladdr;
import com.szxyyd.mpxyhl.modle.SelectBtn;
import com.szxyyd.mpxyhl.utils.CommUtils;
import com.szxyyd.mpxyhl.view.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jq on 2016/7/5.
 */
public class HealthNurseActivity extends Activity implements View.OnClickListener{
    private GridLayout ll_level = null;  //服务星级
    private GridLayout gl = null; //服务选项
    private GridLayout gl_people = null; //服务人员
    private TextView tv_content = null; // 服务内容
    private LinearLayout ll_notaddr = null; //未有服务地址
    private RelativeLayout rl_time = null; //服务时间
    private RelativeLayout rl_location = null; // 服务地址
    private RelativeLayout rl_addr = null;
    private TextView tv_addr_name = null;
    private TextView tv_addr_phone = null;
    private TextView tv_addr = null;
    private TextView tv_money = null; //价钱
    public TextView tv_date = null; //服务时间
    public EditText et_remark = null; //备忘信息
    private List<PriceLvl> listPriceLvl = null;
    public  int svrid;
    private String orderTitle;
    private int baseMoney;
    private String levelData = null;  //级别选择
    private String levelId = null;   //级别选择Id
    public String orderTime;
    public List<String> nameData = new ArrayList<>(); //个性化的多选
    private  SelectBtn selectBtn = new SelectBtn();
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case Constant.SERVICE_LEVEL:
                    listPriceLvl = (List<PriceLvl>) msg.obj;
                    if(listPriceLvl.size() != 0 && listPriceLvl != null) {
                     //   ll_level.setVisibility(View.VISIBLE);
                        showPriceLvl(listPriceLvl);
                    }
                    break;
                case Constant.SERVICE_OPTION_LEVEL:
                    List<PriceLvl> list = (List<PriceLvl>) msg.obj;
                    showLevelOption(list);
                    break;
                case Constant.SERVICE_PEOPLE:
                    List<PriceLvl> list2 = (List<PriceLvl>) msg.obj;
                    showServicePeople(list2);
                    break;
                case  Constant.SERVICE_DEFADDR: //加载默认地址
                    List<Reladdr> deflist = (List<Reladdr>) msg.obj;
                    Log.e("mHandler===","deflist.size()=="+deflist.size());
                    if(deflist.size() == 0){
                        ll_notaddr.setVisibility(View.VISIBLE);
                    }else{
                        rl_addr.setVisibility(View.VISIBLE);
                        ll_notaddr.setVisibility(View.GONE);
                        String name = deflist.get(0).getName().toString();
                        String phone = deflist.get(0).getMobile().toString();
                        String addr = deflist.get(0).getAddr().toString();
                        tv_addr_name.setText(name);
                        tv_addr_phone.setText(phone);
                        tv_addr.setText(addr);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        svrid = getIntent().getIntExtra("svrid",-1);
        orderTitle = getIntent().getStringExtra("title");
        initView();
        loadSvrLevelData();
        loadLevelOptionData();
        loadServiceData();
        BaseApplication.getInstance().addActivity(this);

    }
    private void initView(){
        ((TextView)findViewById(R.id.tv_title)).setText(orderTitle);
        ll_level = (GridLayout) findViewById(R.id.ll_level);
     //   rg_level = (RadioGroup) findViewById(R.id.rg_level);
        gl = (GridLayout) findViewById(R.id.gl);
        gl_people = (GridLayout) findViewById(R.id.gl_people);
        tv_content = (TextView) findViewById(R.id.tv_content);
        Button btn_next = (Button) findViewById(R.id.btn_next);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        tv_money= (TextView) findViewById(R.id.tv_money);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_date.setText(CommUtils.showToDay());
        et_remark = (EditText)findViewById(R.id.et_remark);
        rl_time = (RelativeLayout) findViewById(R.id.rl_time);
        ll_notaddr = (LinearLayout) findViewById(R.id.ll_notaddr);
        rl_location = (RelativeLayout) findViewById(R.id.rl_location);
        rl_addr = (RelativeLayout) findViewById(R.id.rl_addr);
        tv_addr_name = (TextView) findViewById(R.id.tv_addr_name);
        tv_addr_phone = (TextView) findViewById(R.id.tv_addr_phone);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        btn_next.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        rl_location.setOnClickListener(this);
        rl_time.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showSharedPreferencesfAddr();
    }
    /**
     * 本地获取地址
     */
    private void showSharedPreferencesfAddr(){
        SharedPreferences preferences = getSharedPreferences(Constant.cstId+"defaddr", Context.MODE_PRIVATE);
        String name = preferences.getString("name", "");
        String phone = preferences.getString("mobile", "");
        String addr = preferences.getString("addr", "");
       /* Log.e("showSharedPreferencesfAddr","name==="+name);
        Log.e("showSharedPreferencesfAddr","phone==="+phone);
        Log.e("showSharedPreferencesfAddr","addr==="+addr);*/
        if(name.length() > 0 ){
            rl_addr.setVisibility(View.VISIBLE);

            tv_addr_name.setText(name);
            tv_addr_phone.setText(phone);
            tv_addr.setText(addr);
        }else{
            loadDefAddrData();
        }
    }
    /**
     * 加载默认地址
     */
    private void loadDefAddrData(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("cstid",Constant.cstId);
        VolleyRequestUtil.newInstance().GsonPostRequest(this,Constant.defAdressUrl,"defadress" ,map,new TypeToken<JsonBean>(){},
                new Response.Listener<JsonBean>() {
                    @Override
                    public void onResponse(JsonBean jsonBean) {
                        List<Reladdr> list = jsonBean.getReladdr();
                        Message message = new Message();
                        message.what = Constant.SERVICE_DEFADDR;
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
     * 获取级别
     */
    private void loadSvrLevelData(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("svrid",String.valueOf(svrid));
        map.put("city","440300");
        VolleyRequestUtil.newInstance().GsonPostRequest(this,Constant.getPriceAndLvUrl,"priceandlv" ,map,new TypeToken<JsonBean>(){},
                new Response.Listener<JsonBean>() {
                    @Override
                    public void onResponse(JsonBean jsonBean) {
                        List<PriceLvl> levels = jsonBean.getYxvPriceLvl();
                        Message message = new Message();
                        message.what = Constant.SERVICE_LEVEL;
                        message.obj = levels;
                        mHandler.sendMessage(message);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
    }
    /**
     * 获取级别选项数据
     */
    private void loadLevelOptionData(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("svrid",String.valueOf(svrid));
        VolleyRequestUtil.newInstance().GsonPostRequest(this,Constant.funUrl,"option" ,map,new TypeToken<JsonBean>(){},
                new Response.Listener<JsonBean>() {
                    @Override
                    public void onResponse(JsonBean jsonBean) {
                        List<PriceLvl> svrFun = jsonBean.getSvrFun();
                        Message message = new Message();
                        message.what = Constant.SERVICE_OPTION_LEVEL;
                        message.obj = svrFun;
                        mHandler.sendMessage(message);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
    }
    /**
     * 获取服务人员数据
     */
    private void loadServiceData(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("svrid",String.valueOf(svrid));
        VolleyRequestUtil.newInstance().GsonPostRequest(this,Constant.nurseServiceUrl,"service" ,map,new TypeToken<JsonBean>(){},
                new Response.Listener<JsonBean>() {
                    @Override
                    public void onResponse(JsonBean jsonBean) {
                        List<PriceLvl> svrCal = jsonBean.getSvrCal();
                        Message message = new Message();
                        message.what = Constant.SERVICE_PEOPLE;
                        message.obj = svrCal;
                        mHandler.sendMessage(message);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
    }
    /**
     * 改变价格选项
    */
    private void loadGetPriceData(){
       /* Map<String, String> map = new HashMap<String, String>();
        map.put("svrid",String.valueOf(svrid));
        map.put("lvl",levelId);
        map.put("city","440300");
        map.put("id",getIdxsList());*/
        Log.e("HealthNurseActivity","id=="+getIdxsList());
        Log.e("HealthNurseActivity","svrid=="+String.valueOf(svrid));
        Log.e("HealthNurseActivity","lvl=="+levelId);
        Log.e("HealthNurseActivity","city=="+Constant.cityId);
        String url = Constant.getPriceUrl + "&svrid=" + String.valueOf(svrid) + "&lvl=" + levelId
                +"&city="+"440300"+getIdxsList();
        VolleyRequestUtil.newInstance().RequestGet(this, url, "price",new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
            @Override
            public void onSuccess(String result) {
                Log.e("HealthNurseActivity","result=="+result);
                tv_money.setText(result);
            }
            @Override
            public void onError(VolleyError error) {
            }
        });
    }
    private void showPriceLvl(List<PriceLvl> list) {
        ll_level.removeAllViews();
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 0, 0);
        Button btn = null;
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_level, null);
            btn = (Button) view.findViewById(R.id.btn_level1);
            btn.setTag(i+1);
            btn.setText(list.get(i).getName());
            if(Integer.parseInt(btn.getTag().toString()) == 1) {
                btn.setBackgroundResource(R.drawable.shape_blue);
                selectBtn.setId(1);
                selectBtn.setView(btn);
                btn.setTextColor(getResources().getColor(R.color.color_bule));
            }
            btn.setOnClickListener(new btnListener(btn,list));
            ll_level.addView(view);
        }
        showSelectData(list, 0);
    }
    /*
   * 创建一个按钮监听器类
   */
    class btnListener implements View.OnClickListener {
        //定义一个 Button 类型的变量
        private Button btn;
        private List<PriceLvl> list;
        private btnListener(Button btn,List<PriceLvl> list) {
            this.btn = btn;//将引用变量传递给实体变量
            this.list = list;
        }
        @Override
        public void onClick(View view) {
            int tag = Integer.parseInt(btn.getTag().toString());
           if(selectBtn.getId() != tag){
               if(selectBtn.getView() != null){
                   selectBtn.getView().setBackgroundResource(R.drawable.shape_gray);
                   selectBtn.getView().setTextColor(getResources().getColor(R.color.color_six_three));
               }
               selectBtn.setId(tag);
               selectBtn.setView(btn);
               btn.setBackgroundResource(R.drawable.shape_blue);
               btn.setTextColor(getResources().getColor(R.color.color_bule));
               showSelectData(list, (tag-1));
           }
        }
    }
    /**
     * 显示选择星级数据
     */
    private void showSelectData(List<PriceLvl> list,int index){
        tv_content.setText(list.get(index).getContent());
        tv_money.setText(list.get(index).getPrice()+"");
        levelData = list.get(index).getName();
        baseMoney = list.get(index).getPrice();
        Constant.lvlTitle = list.get(index).getName();
        Constant.lvlId = list.get(index).getLvl();
        levelId = list.get(index).getLvl();
    }
    /**
     * 显示级别选项数据
     * @param data
     */
    private void showLevelOption(final List<PriceLvl> data){
        gl.removeAllViews();
        for(int i = 0;i<data.size();i++){
            View view = getLayoutInflater().inflate(R.layout.include_border,null,false);
            CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
            cb.setText(data.get(i).getName().toString());
            cb.setTag(data.get(i).getIdx());
            cb.setTextColor(this.getResources().getColor(R.color.color_six_a));
            cb.setChecked(true);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if(isChecked){
                        Constant.listLevel.add(buttonView.getTag().toString());
                    }else{
                        Constant.listLevel.remove(buttonView.getTag().toString());
                    }

                }
            });
            gl.addView(view);
        }
    }
    private String getIdxsList(){
        String result = "";
        for(int i = 0; i < Constant.listpople.size();i++){
            StringBuilder sb = new StringBuilder();
            result += sb.append("&idx=").append(Constant.listpople.get(i)).toString();
            Log.e("getIdxsList", "result=="+result);
        }
        return result;
    }
    /**
     * 显示服务人员的数据
     */
    private void showServicePeople(final List<PriceLvl> data){
        gl_people.removeAllViews();
       // PriceLvl svrCal;
        for(int i = 0;i<data.size();i++){
            View view = getLayoutInflater().inflate(R.layout.include_border, null);
           final CheckBox cb  = (CheckBox) view.findViewById(R.id.cb);
           //  svrCal = data.get(i);
            cb.setText(data.get(i).getName().toString());
            cb.setTag(data.get(i).getId());
            cb.setTextColor(this.getResources().getColor(R.color.color_six_a));
            Constant.listpople.clear();
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if(isChecked){
                        Constant.listpople.add(buttonView.getTag().toString());
                        nameData.add(cb.getText().toString());
                        loadGetPriceData();
                    }else{
                        Constant.listpople.remove(buttonView.getTag().toString());
                        nameData.remove(cb.getText().toString());
                        tv_money.setText(baseMoney+"");
                    }
                }
            });
            gl_people.addView(view);
        }
    }
    /**
     * 缓存服务内容
     */
    public  void sharedSerContent(){
        SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("sercontent", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("level", orderTitle+ " "+levelData);
        StringBuilder sb = new StringBuilder();
        for(String str : nameData){
            sb.append(" ").append(str).toString();
        }
        String result = sb.toString();
        editor.putString("serpeople", result);
        if(orderTime == null){
            editor.putString("sertime", tv_date.getText().toString());
        }else{
            editor.putString("sertime", orderTime);
        }
        editor.putString("note",  et_remark.getText().toString());
        editor.putString("price", tv_money.getText().toString());
        editor.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_next:
                sharedSerContent();
                Intent intent = new Intent(HealthNurseActivity.this,NurselistActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_location:
                Intent intentAddress = new Intent(HealthNurseActivity.this,ServiceAddressActivity.class);
                startActivity(intentAddress);
                break;
            case R.id.rl_time:
                Calendar c = Calendar.getInstance();
                 new TimePickerDialog(HealthNurseActivity.this, 0, new TimePickerDialog.OnDateSetListener() {
                     @Override
                     public void onDateSet( int startYear, int startMonthOfYear, int startDayOfMonth,
                                           int hourOfDay,int minute) {
                         tv_date.setText(startYear + "-" + startMonthOfYear + "-" + startDayOfMonth+" " +hourOfDay + ":" + hourOfDay);
                     }
                 },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE),true).show();
                break;
        }

    }
}
