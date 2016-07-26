package com.szxyyd.mpxyhls.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.adapter.ContentAdapter;
import com.szxyyd.mpxyhls.fragment.BasicMessageFragment;
import com.szxyyd.mpxyhls.http.VolleyRequestUtil;
import com.szxyyd.mpxyhls.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhls.modle.Info;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 基本信息内容选择：籍贯、星座
 * Created by jq on 2016/7/13.
 */
public class BasicContentActivity extends Activity{
    private String type;
    private TextView tv_conTitle;
    private ListView lv_content;
    private ContentAdapter adapter;
    private List<Info> dataList;
    private List<Info> zodiacList;  //星座
    private List<Info> origoList;  //籍贯
    private List<Info> eduList;  //学历
    private List<Info> anmsignList; //属相
    private String name;
    private String id;
    private ProgressDialog proDialog;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCEED:
                    showTypeContent();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        type = getIntent().getStringExtra("type");
        initView();
        lodeMessage();
    }
    private void initView(){
        tv_conTitle = (TextView) findViewById(R.id.tv_conTitle);
        LinearLayout ll_back = (LinearLayout) findViewById(R.id.ll_back);
        lv_content = (ListView) findViewById(R.id.lv_content);
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                name = dataList.get(position).getName();
                String id = dataList.get(position).getIid();
                Toast.makeText(BasicContentActivity.this,"id=="+id,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Constant.BROAD_LIST_ACTION);
                intent.putExtra("name",name);
                intent.putExtra("id",id);
                sendBroadcast(intent);
                finish();
            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /**
     * 根据类型不同显示内容
     *
     */
    private void showTypeContent(){
        switch (type){
            case "籍贯":
                tv_conTitle.setText("籍贯");
                dataList = origoList;
                break;
            case "属相":
                tv_conTitle.setText("属相");
                dataList = anmsignList;
                break;
            case "星座":
                tv_conTitle.setText("星座");
                dataList = zodiacList;
                break;
            case "学历":
                tv_conTitle.setText("学历");
                dataList = eduList;
                break;
        }
        adapter = new ContentAdapter(this,dataList);
        lv_content.setAdapter(adapter);
    }
    /**
     获取基本信息
     */
    private void lodeMessage(){
        proDialog = ProgressDialog.show(this, "", "正在加载.....");
        VolleyRequestUtil.newInstance().RequestGet(this, Constant.getDetailsUrl,"detail",new VolleyListenerInterface(this,VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onSuccess(String result) {
                //   Toast.makeText(BaseApplication.getInstance(),"已提交",Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "lodeMessage---result=="+result);
                proDialog.dismiss();
                parserData(result);
            }
            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    private void parserData(String result){
        try {
            JSONObject json = new JSONObject(result);
            String jsonZodiac = json.getString("zodiacList");
            Type zodiacType = new TypeToken<LinkedList<Info>>() {}.getType();
            Gson zodiacgson = new Gson();
            zodiacList = zodiacgson.fromJson(jsonZodiac, zodiacType);

            String jsonOrigo = json.getString("origoList");
            Type origoType = new TypeToken<LinkedList<Info>>() {}.getType();
            Gson origogson = new Gson();
            origoList = origogson.fromJson(jsonOrigo, origoType);

            String jsonEdu = json.getString("eduList");
            Type eduType = new TypeToken<LinkedList<Info>>() {}.getType();
            Gson edugson = new Gson();
            eduList = edugson.fromJson(jsonEdu, eduType);

            String jsonAnmsign= json.getString("anmsignList");
            Type anmsignType = new TypeToken<LinkedList<Info>>() {}.getType();
            Gson anmsigngson = new Gson();
            anmsignList = anmsigngson.fromJson(jsonAnmsign, anmsignType);

            Message message = new Message();
            message.what = Constant.SUCCEED;
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
