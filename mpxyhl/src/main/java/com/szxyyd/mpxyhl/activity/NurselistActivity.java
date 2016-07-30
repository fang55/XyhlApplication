package com.szxyyd.mpxyhl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.adapter.NurseAdapter;
import com.szxyyd.mpxyhl.http.HttpMethods;
import com.szxyyd.mpxyhl.inter.SubscriberOnNextListener;
import com.szxyyd.mpxyhl.modle.City;
import com.szxyyd.mpxyhl.modle.JsonBean;
import com.szxyyd.mpxyhl.modle.NurseList;
import com.szxyyd.mpxyhl.modle.ProgressSubscriber;
import com.szxyyd.mpxyhl.view.NurseListPopupWindow;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jq on 2016/7/5.
 */
public class NurselistActivity extends Activity implements View.OnClickListener{
    private GridView gv_nurse = null;
    private LinearLayout ll2 = null; //年龄
    private LinearLayout ll3 = null; //籍贯
    private LinearLayout ll4 = null; //排序
    private TextView tv2 = null;
    private TextView tv3 = null;
    private TextView tv4 = null;
    private String[] data2 = new String[]{"20岁以下","20~29","30~39","40以上"};
    private String[] dataTag2 = new String[]{"1","2","3","4"};
    private String[] data3 = null;
    private String[] dataTag3 = null;
    private String[] data4 = new String[]{"服务次数","服务评分","距离"};
    private String[] dataTag4 = new String[]{"1","2","3"};
    private NurseAdapter adapter = null;
    private List<NurseList> list = null;
    private Map<String,String> map = new HashMap<>();
    private NurseListPopupWindow popupWindow = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurselis);
        initView();
        lodeOrigoData();
        map.put("city","440300");
        map.put("svrid",String.valueOf(Constant.svrId));
        map.put("lvl",Constant.lvlId);
        lodeData(map);
        BaseApplication.getInstance().addActivity(this);
    }
    private void initView(){
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("最佳匹配");
        LinearLayout ll_city = (LinearLayout) findViewById(R.id.ll_city);
        ll_city.setVisibility(View.VISIBLE);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        gv_nurse = (GridView) findViewById(R.id.gv_nurse);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll3 = (LinearLayout) findViewById(R.id.ll3);
        ll4 = (LinearLayout) findViewById(R.id.ll4);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        gv_nurse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NurseList nurse = list.get(position);
                Intent intent = new Intent(NurselistActivity.this,NurseDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("nurse", nurse);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btn_back.setOnClickListener(this);
        ll2.setOnClickListener(this);
        ll3.setOnClickListener(this);
        ll4.setOnClickListener(this);
    }
    SubscriberOnNextListener getOrigoOnNext =  new SubscriberOnNextListener<JsonBean>() {
        @Override
        public void onNext(JsonBean jsonBean) {
            List<City> listCity = jsonBean.getOrigo();
            if(listCity.size() != 0 && listCity != null){
                data3 = new String[listCity.size()];
                dataTag3 = new String[listCity.size()];
                for(int i = 0; i < listCity.size(); i++){
                    data3[i] = listCity.get(i).getName();
                    dataTag3[i] = listCity.get(i).getIid();
                }
            }
        }
    };
   private void lodeOrigoData(){
       HttpMethods.getInstance().getOrigoData("findOrigo",new ProgressSubscriber<JsonBean>(getOrigoOnNext,this));
   }
    SubscriberOnNextListener getNurserOnNext = new SubscriberOnNextListener<JsonBean>() {
        @Override
        public void onNext(JsonBean jsonBean) {
            list = jsonBean.getNurse();
            if(list.size() != 0 ){
                adapter = new NurseAdapter(NurselistActivity.this,list);
                gv_nurse.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else{
              if(adapter != null){
                  list.clear();
                    adapter = new NurseAdapter(NurselistActivity.this,list);
                    gv_nurse.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                Toast.makeText(NurselistActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
            }
        }
    };
    /**
     * 根据条件查找数据
     * @param param
     */
    private void lodeData(Map<String, String> param){
        StringBuffer sb = new StringBuffer();
        for (String key : param.keySet()) {
            sb.append(key).append(param.get(key));
        }
        String result = sb.toString();
        Log.e("NurselistActivity","result=="+result);
        HttpMethods.getInstance().getNurseListData("nurList",param,new ProgressSubscriber<JsonBean>(getNurserOnNext,this));
    }
    private void showPopupWindow(View rootView,String[] data,final int index){
        popupWindow = new NurseListPopupWindow(this,data);
        if(popupWindow.isShowing()){
            popupWindow.dismiss();
        }
        popupWindow.setonPopuItemListener(new NurseListPopupWindow.OnPopuItemClickListener() {
            @Override
            public void onSure(int position) {
                showTextContent(position,index);
            }
        });
        popupWindow.initView(rootView);
    }
    private void showTextContent(int position,int index){
        String tag = null;
        switch (index){
            case 2:
                tv2.setText(data2[position]);
                tag = dataTag2[position];
                map.put("age",tag);
                break;
            case 3:
                tv3.setText(data3[position]);
                tag = dataTag3[position];
                map.put("origo",tag);
                break;
            case 4:
                tv4.setText(data4[position]);
                tag = dataTag4[position];
                map.put("sort",tag);
                break;
        }
        lodeData(map);
    }
    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.btn_back:
               finish();
               break;
           case R.id.ll2:
               showPopupWindow(ll2,data2,2);
               break;
           case R.id.ll3:
               showPopupWindow(ll3,data3,3);
               break;
           case R.id.ll4:
               showPopupWindow(ll4,data4,4);
               break;
       }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
