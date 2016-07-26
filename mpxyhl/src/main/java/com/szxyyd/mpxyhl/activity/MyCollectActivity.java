package com.szxyyd.mpxyhl.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.adapter.CollectAdapter;
import com.szxyyd.mpxyhl.adapter.NurseAdapter;
import com.szxyyd.mpxyhl.http.VolleyRequestUtil;
import com.szxyyd.mpxyhl.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhl.modle.JsonBean;
import com.szxyyd.mpxyhl.modle.NurseList;
import com.szxyyd.mpxyhl.view.PopupDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 我的收藏
 * Created by fq on 2016/7/6.
 */
public class MyCollectActivity extends Activity implements AdapterView.OnItemLongClickListener{
    private GridView gv_collect;
    private List<NurseList> listNurse;
    private CollectAdapter adapter;
    private ProgressDialog proDialog;
    private int collectPotion;
    private boolean isShowDelete=false;
    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCEED:
                    listNurse = (List<NurseList>) msg.obj;
                    if(listNurse.size() != 0 ) {
                        adapter = new CollectAdapter(MyCollectActivity.this,listNurse);
                        adapter.setonSelectDelectListener(new CollectAdapter.onSelectDelectListener() {
                            @Override
                            public void onDelect(int position) {
                                collectPotion = position;
                                showDelectDialog();
                            }
                        });
                        gv_collect.setAdapter(adapter);
                    }else{
                        Toast.makeText(MyCollectActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
                        if(adapter != null){
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case Constant.SUBITM:
                    Toast.makeText(MyCollectActivity.this,"已删除",Toast.LENGTH_SHORT).show();
                    listNurse.remove(collectPotion);
                    gv_collect.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        initView();
        lodeCollectData();
    }
    private void initView(){
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.text_collect));
        Button btn_back = (Button) findViewById(R.id.btn_back);
        gv_collect = (GridView) findViewById(R.id.gv_collect);
        gv_collect.setOnItemLongClickListener(this);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /**
     * 弹出询问是否删除地址
     */
    private void showDelectDialog(){
        PopupDialog dialog = new PopupDialog(MyCollectActivity.this,"collect");
        dialog.setonSelectListener(new PopupDialog.onSelectClickListener() {
            @Override
            public void onSure() {
                submitCancleData();
            }
        });
        dialog.initView();
    }
    /**
     * 提交取消收藏
     * collectId 收藏id
     */
    private void submitCancleData(){
        proDialog = ProgressDialog.show(MyCollectActivity.this, "", "加载中");
        int collectId =  listNurse.get(collectPotion).getFvrid();
        String url = Constant.cstFvrnurDelUrl+"&id="+collectId;
        VolleyRequestUtil.newInstance().RequestGet(this, url, "subcollect",
                new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(String result) {
                        proDialog.dismiss();
                        Message message = new Message();
                        message.what = Constant.SUBITM;
                        handler.sendMessage(message);
                    }
                    @Override
                    public void onError(VolleyError error) {

                    }
                });

    }
    /**
     * 获取收藏列表
     * cstId 用户id
     *
     */
    private void lodeCollectData(){
        proDialog = ProgressDialog.show(MyCollectActivity.this, "", "加载中");
        Map<String, String> param = new HashMap<>();
        param.put("cstid",Constant.cstId);
        VolleyRequestUtil.newInstance().GsonPostRequest(this,Constant.cstFvrnurListUrl,"fvr" ,param,new TypeToken<JsonBean>(){},
                new Response.Listener<JsonBean>() {
                    @Override
                    public void onResponse(JsonBean jsonBean) {
                        if(proDialog != null){
                            proDialog.cancel();
                        }
                        List<NurseList> nurseLists = jsonBean.getNurse();
                        Message message = new Message();
                        message.what = Constant.SUCCEED;
                        message.obj = nurseLists;
                        handler.sendMessage(message);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (isShowDelete) {
            isShowDelete = false;
        } else {
            isShowDelete = true;
        }
        adapter.setIsShowDelete(isShowDelete);
        return true;
    }
}
