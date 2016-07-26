package com.szxyyd.xyhl.activity;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.adapter.CityAdapter;
import com.szxyyd.xyhl.http.HttpUtils;
import com.szxyyd.xyhl.modle.City;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 城市
 * 
 * @author jq
 * 
 */
public class CityActivity extends Activity {
	private Button btn_back;
	private TextView btn_city;
	private TextView tv_title;
	private ListView lv_city;
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

	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("切换城市");
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btn_city = (TextView) findViewById(R.id.btn_city);
		lv_city = (ListView) findViewById(R.id.lv_city);
		lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				City city = list.get(position);
				cityname = city.getName().toString();
				btn_city.setText(cityname);
                String cityCode = city.getIid();
				lodingCityData(cityCode,2);
				returnCityData();
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}
	private void returnCityData(){
		Intent intent = new Intent(CityActivity.this,HomePageActivity.class);
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
		HttpUtils.getHttpData(url, new HttpUtils.callBackListener() {
			@Override
			public void onSucceed(String result) {
				Log.e("updateCity", "result==" + result);
				parserData(result,index);
			}
			@Override
			public void onFailure(String data) {

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
							adapter = new CityAdapter(CityActivity.this, list);
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
