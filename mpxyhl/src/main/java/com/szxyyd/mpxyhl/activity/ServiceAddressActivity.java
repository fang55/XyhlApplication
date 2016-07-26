package com.szxyyd.mpxyhl.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.adapter.ServiceAddressAdapter;
import com.szxyyd.mpxyhl.http.HttpMethods;
import com.szxyyd.mpxyhl.http.VolleyRequestUtil;
import com.szxyyd.mpxyhl.inter.SubscriberOnNextListener;
import com.szxyyd.mpxyhl.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhl.modle.JsonBean;
import com.szxyyd.mpxyhl.modle.ProgressSubscriber;
import com.szxyyd.mpxyhl.modle.Reladdr;
import com.szxyyd.mpxyhl.view.PopupDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务地址
 * Created by fq on 2016/7/6.
 */
public class ServiceAddressActivity extends FragmentActivity implements View.OnClickListener{
    private TextView tv_title;
    private TextView tv_add;
    private GridView gv_address;
    private ServiceAddressAdapter addressAdapter;
    private List<Reladdr> listAddr;
    private Reladdr reladdr = null;
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case Constant.SERVICE_ADDR_LIST:
                    listAddr = (List<Reladdr>) msg.obj;
                    addressAdapter = new ServiceAddressAdapter(ServiceAddressActivity.this,listAddr);
                    addressAdapter.setSelectListener(new ServiceAddressAdapter.selectOnclickListener() {
                        @Override
                        public void selectDelect(int position, String type) {
                            reladdr = listAddr.get(position);
                            if(type.equals("edit")){  //编辑
                                Intent intent = new Intent(ServiceAddressActivity.this,EditAddressActivity.class);
                                intent.putExtra("type","edit");
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("reladdr",reladdr);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }else if(type.equals("delect")){  //删除
                                showDelectDialog();
                            }else{
                                delectAddrData(reladdr.getId(),"checked");
                            }
                        }
                    });
                    gv_address.setAdapter(addressAdapter);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceaddress);
        initView();
    }
    private void  initView(){
        gv_address = (GridView)findViewById(R.id.gv_address);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.my_address));
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_add.setVisibility(View.VISIBLE);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        gv_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SharedPreferences preferences = getSharedPreferences(Constant.cstId+"defaddr", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("name", listAddr.get(position).getName());
                editor.putString("mobile", listAddr.get(position).getMobile());
                editor.putString("addr", listAddr.get(position).getAddr());
                editor.commit();
                finish();
            }
        });
        tv_add.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadAddrListData();
    }

    SubscriberOnNextListener getAddrListOnNext = new SubscriberOnNextListener<JsonBean>() {
        @Override
        public void onNext(JsonBean jsonBean) {
            listAddr = jsonBean.getReladdr();
            addressAdapter = new ServiceAddressAdapter(ServiceAddressActivity.this,listAddr);
            addressAdapter.setSelectListener(new ServiceAddressAdapter.selectOnclickListener() {
                @Override
                public void selectDelect(int position, String type) {
                    reladdr = listAddr.get(position);
                    if(type.equals("edit")){  //编辑
                        Intent intent = new Intent(ServiceAddressActivity.this,EditAddressActivity.class);
                        intent.putExtra("type","edit");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("reladdr",reladdr);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else if(type.equals("delect")){  //删除
                        showDelectDialog();
                    }else{
                        delectAddrData(reladdr.getId(),"checked");
                    }
                }
            });
            gv_address.setAdapter(addressAdapter);
        }
    };
    /**
     * 获取服务地址列表
     */
    private void loadAddrListData(){
       HttpMethods.getInstance().getAddressListData("addressList",Constant.cstId,new ProgressSubscriber<JsonBean>(getAddrListOnNext,this));
    }

    /**
     * 弹出询问是否删除地址
     */
    private void showDelectDialog(){
        PopupDialog dialog = new PopupDialog(ServiceAddressActivity.this,"addr");
        dialog.setonSelectListener(new PopupDialog.onSelectClickListener() {
            @Override
            public void onSure() {
                delectAddrData(reladdr.getId(),"delect");
            }
        });
        dialog.initView();
    }


    /**
     * delect：删除
     * checked：设置默认地址
     */
    private void delectAddrData(int id,final String type){
        String url = null;
        Map<String, String> map = new HashMap<String, String>();
        if(type.equals("delect")){
            url = Constant.delAddresUrl;
            map.put("id",String.valueOf(id));
        }else if(type.equals("checked")){
            url = Constant.saveAddressByIdUrl;
            map.put("id",String.valueOf(id));
            map.put("cstid",Constant.cstId);
        }
        VolleyRequestUtil.newInstance().RequestPost(BaseApplication.getInstance(), url, type,map,
                new VolleyListenerInterface(BaseApplication.getInstance(), VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("ServiceAddressActivity", "delectAddrData--result==" + result);
                        parserData(result,type);
                    }
                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }

    private void parserData(String result, String type) {
        if(type.equals("delect")){
            Toast.makeText(ServiceAddressActivity.this,"已删除",Toast.LENGTH_SHORT).show();
        }else{  //设为默认地址
            Log.e("parserData---Constant.cstId===",Constant.cstId);
            SharedPreferences preferences = getSharedPreferences(Constant.cstId+"defaddr", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name", reladdr.getName());
            editor.putString("mobile", reladdr.getMobile());
            editor.putString("addr", reladdr.getAddr());
            editor.commit();
        }
        loadAddrListData();
    }


    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.btn_back:
              finish();
               break;
           case R.id.tv_add:
               Intent intent = new Intent(ServiceAddressActivity.this,EditAddressActivity.class);
               intent.putExtra("type","add");
               Bundle bundle = new Bundle();
               bundle.putSerializable("reladdr",reladdr);
               intent.putExtras(bundle);
               startActivity(intent);
               break;
       }
    }
}
