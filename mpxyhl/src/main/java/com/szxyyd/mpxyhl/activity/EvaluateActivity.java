package com.szxyyd.mpxyhl.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.adapter.EvaluateAdapter;
import com.szxyyd.mpxyhl.http.VolleyRequestUtil;
import com.szxyyd.mpxyhl.modle.JsonBean;
import com.szxyyd.mpxyhl.modle.NurseList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评价
 * Created by jq on 2016/7/29.
 */
public class EvaluateActivity extends Activity{
    private ListView lv_evaluate;
    private  EvaluateAdapter adapter;
    private NurseList nurse;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.SUCCEED:
                    List<NurseList> nurseLists = (List<NurseList>) msg.obj;
                    if(nurseLists.size() > 0){
                        adapter = new EvaluateAdapter(EvaluateActivity.this,nurseLists);
                        lv_evaluate.setAdapter(adapter);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        nurse = (NurseList) getIntent().getSerializableExtra("nurse");
        initView();
        lodeEvaluateData();
    }
    private void initView(){
        ((TextView)findViewById(R.id.tv_title)).setText("评价");
        Button btn_back = (Button) findViewById(R.id.btn_back);
        lv_evaluate = (ListView) findViewById(R.id.lv_evaluate);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /**
     * 获取护理师评论列表
     */
    private void lodeEvaluateData(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("nurseid",nurse.getNursvrid());
        VolleyRequestUtil.newInstance().GsonPostRequest(this,Constant.nurseCmtAllUrl,"cmt" ,map,new TypeToken<JsonBean>(){},
                new Response.Listener<JsonBean>() {
                    @Override
                    public void onResponse(JsonBean jsonBean) {
                        List<NurseList> nurses = jsonBean.getNurse();
                        if(null != nurses){
                            Message message = new Message();
                            message.what = Constant.SUCCEED;
                            message.obj = nurses;
                            handler.sendMessage(message);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
    }

}
