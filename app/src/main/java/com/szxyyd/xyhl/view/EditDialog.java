package com.szxyyd.xyhl.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.activity.BaseApplication;
import com.szxyyd.xyhl.activity.Constant;
import com.szxyyd.xyhl.adapter.RegionAdapter;
import com.szxyyd.xyhl.http.VolleyRequestUtil;
import com.szxyyd.xyhl.inf.VolleyListenerInterface;
import com.szxyyd.xyhl.modle.City;
import com.szxyyd.xyhl.modle.JsonBean;
import com.szxyyd.xyhl.modle.Reladdr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编辑地址
 * Created by jq on 2016/6/3.
 */
public class EditDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private TextView tv_sumbit;
    private TextView tv_addr_title;
    private EditText et_edit_name;
    private EditText et_edit_phone;
    private EditText et_edit_addr;
    private TextView et_edit_region;
    private CheckBox cb_rdiao;
    private Button btn_editback;
    private RelativeLayout rl_region; //所在地区
    private Reladdr reladdr;
    private String states;
    private Dialog alertDialog;
    private onFinshClickListener mListener;
    private int position;
    private int dwonIfdef = 0;
    private AlertDialog dialog = null;
    private LinearLayout ll_popup = null;
    private RadioGroup rgChannel = null;
    private RadioButton rb1 = null;
    private RadioButton rb2 = null;
    private RadioButton rb3 = null;
    private List<City> cityList;
    private int rbPostion = 0;
    private ListView listView = null;
    private RegionAdapter adapter = null;
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case Constant.SUCCEED:
                    cityList = (List<City>) msg.obj;
                    if(cityList.size() > 0 && cityList != null){
                        adapter = new RegionAdapter(mContext,cityList);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };
    public EditDialog(Context context, Reladdr reladdr, String states, int position) {
        super(context);
        mContext = context;
        this.reladdr = reladdr;
        this.states = states;
        this.position = position;
    }

    public void init() {
        alertDialog = new Dialog(mContext, R.style.dialog);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_edit_location);
        tv_addr_title = (TextView) window.findViewById(R.id.tv_addr_title);
        btn_editback = (Button) window.findViewById(R.id.btn_editback);
        tv_sumbit = (TextView) window.findViewById(R.id.tv_sumbit);
        rl_region = (RelativeLayout) window.findViewById(R.id.rl_region);
        if (states.equals("add")) {
            tv_sumbit.setText("提交");
            tv_addr_title.setText("添加服务地址");
        } else {
            tv_sumbit.setText("保存");
            tv_addr_title.setText("编辑服务地址");
        }
        et_edit_name = (EditText) window.findViewById(R.id.et_edit_name);
        et_edit_phone = (EditText) window.findViewById(R.id.et_edit_phone);
        et_edit_addr = (EditText) window.findViewById(R.id.et_edit_addr);
        et_edit_region = (TextView) window.findViewById(R.id.et_edit_region);
        cb_rdiao = (CheckBox) window.findViewById(R.id.cb_rdiao);
        if (reladdr != null) {
            et_edit_name.setText(reladdr.getName());
            et_edit_phone.setText(reladdr.getMobile());
            et_edit_addr.setText(reladdr.getAddr());
            if (reladdr.getIfdef().equals("是")) {
                cb_rdiao.setClickable(true);
            }
        }
        cb_rdiao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    dwonIfdef = 1;
                }
            }
        });
        tv_sumbit.setOnClickListener(this);
        btn_editback.setOnClickListener(this);
        rl_region.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sumbit:
                addLocationData();
                break;
            case R.id.btn_editback:
                alertDialog.cancel();
                break;
            case R.id.rl_region:
                showRegionView();
                break;
        }
    }
    /**
     * 弹出所在地区
     */
    /**
     * 显示选择所在地区
     */
    private void  showRegionView(){
        dialog = new AlertDialog.Builder(mContext).create();
        dialog.show();
        Window view = dialog.getWindow();
        view.setContentView(R.layout.item_popupwindows);
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
                    dialog.dismiss();
                    et_edit_region.setText(rb1.getText().toString() + rb2.getText().toString() + rb3.getText().toString());
                }
            }
        });
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              dialog.dismiss();
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
        VolleyRequestUtil.newInstance().GsonPostRequest(mContext,url,type ,map,new TypeToken<JsonBean>(){},
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
     * 添加服务地址
     */
    private void addLocationData() {
        //addr?a=addAddres&cstid(客户id)&name(客户名称)&mobile(客户手机号)&addr(详细地址)&ifdef(是否默认1 默认  0不是默认)
        String name = et_edit_name.getText().toString();
        String mobile = et_edit_phone.getText().toString();
        final String addr = et_edit_addr.getText().toString()+et_edit_region.getText().toString();
        String url = null;
        if(mobile.length() != 11){
            Toast.makeText(BaseApplication.getInstance(),"手机号码格式不正确",Toast.LENGTH_SHORT).show();
            return;
        }
        if (states.equals("add")) {
            url = Constant.addAddresUrl + "&cstid=" + Constant.cstId
                    + "&name=" + name + "&mobile=" + mobile + "&addr=" + addr + "&ifdef=" + dwonIfdef;
        } else {
            //id(服务地址id)
            url = Constant.saveAddresUrl + "&id=" + reladdr.getId()
                    + "&name=" + name + "&mobile=" + mobile + "&addr=" + addr + "&ifdef=" + dwonIfdef;
        }
        VolleyRequestUtil volley = new VolleyRequestUtil();
        volley.RequestGet(BaseApplication.getInstance(), url, "add",
                new VolleyListenerInterface(BaseApplication.getInstance(), VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("EditDialog", "EditDialog--result==" + result);
                        //如果保存默认地址，缓存到本地
                     //   if (dwonIfdef == 1) {
                            SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences(Constant.cstId+"defaddr", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("name", et_edit_name.getText().toString());
                            editor.putString("mobile", et_edit_phone.getText().toString());
                            editor.putString("addr", addr);
                            editor.putInt("ifdef", dwonIfdef);
                            editor.commit();
                            Intent intent = new Intent();  //Itent就是我们要发送的内容
                            intent.setAction(Constant.BROAD_ADD_ACTION);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                            mContext.sendBroadcast(intent);   //发送广播
                    //    }
                    }
                    @Override
                    public void onError(VolleyError error) {

                    }
                });
        alertDialog.cancel();
        if(mListener != null){
            mListener.onfinsh();
        }
    }
    public void setOnFinshClickListener(onFinshClickListener listener){
        mListener = listener;
    }
    public interface onFinshClickListener{
        void onfinsh();
    }
}
