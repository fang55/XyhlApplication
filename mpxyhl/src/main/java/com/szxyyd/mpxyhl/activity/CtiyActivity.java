package com.szxyyd.mpxyhl.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.adapter.CityAdapter;
import com.szxyyd.mpxyhl.http.VolleyRequestUtil;
import com.szxyyd.mpxyhl.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhl.modle.City;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * 切换城市
 * Created by fq on 2016/7/6.
 */
public class CtiyActivity extends Activity{
    private ListView lv_city;
    private TextView tv_current;
    private CityAdapter adapter;
    private List<City> list;
    private String cityname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        initView();
        lodingCityData(null,1);
    }
    private void initView(){
        Button btn_back = (Button) findViewById(R.id.btn_back);
        tv_current = (TextView) findViewById(R.id.tv_current);
        lv_city = (ListView) findViewById(R.id.lv_city);
        lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                City city = list.get(position);
                cityname = city.getName().toString();
                tv_current.setText("当前城市："+cityname);
                String cityCode = city.getIid();
                lodingCityData(cityCode,2);
                returnCityData();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void returnCityData(){
        Intent intent = new Intent(CtiyActivity.this,HomePagerActivity.class);
        intent.putExtra("cityname",cityname);
        setResult(1, intent);
        finish();
    }
    //城市
    private void lodingCityData(String cityCode, final int index){
        String  url = null;
        switch (index){
            case 1:
                url = Constant.cityUrl;
                break;
            case 2:
                url = Constant.saveCityUrl +"&id="+Constant.cstId+"&city="+cityCode;
                break;
        }
        VolleyRequestUtil.newInstance().RequestGet(BaseApplication.getInstance(), url, "loding",
                new VolleyListenerInterface(BaseApplication.getInstance(), VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("ServiceAddressActivity", "addLocationData--result==" + result);
                        parserData(result,index);
                    }
                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }
    private void parserData(final String result, final int index) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(result);
                    String  jsonData = json.getString("city");
                    switch (index){
                        case 1:
                            Type listType = new TypeToken<LinkedList<City>>(){}.getType();
                            Gson gson = new Gson();
                            list = gson.fromJson(jsonData, listType);
                            adapter = new CityAdapter(CtiyActivity.this, list);
                            lv_city.setAdapter(adapter);
                            break;
                        case 2:

                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
