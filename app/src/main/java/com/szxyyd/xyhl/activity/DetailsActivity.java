package com.szxyyd.xyhl.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.fragment.OrderFragment;
import com.szxyyd.xyhl.http.BitmapCache;
import com.szxyyd.xyhl.modle.Order;
import com.szxyyd.xyhl.utils.AMapUtil;
import com.szxyyd.xyhl.utils.CommUtils;

/**
 * 订单详情
 * Created by jq on 2016/6/15.
 */
public class DetailsActivity extends Activity implements View.OnClickListener{
    private TextView tv_title;
    private Button btn_back;
    private Button btn_start;
    private RelativeLayout rl_addr;
    private ImageView iv_theach;
    private Order order;
    private int orderStates;
    private String addressName;
    private String getCode;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    private ProgressDialog progDialog = null;
   /* private GeocodeSearch geocoderSearch;
    private AMap aMap;
    private MapView mapView;
    private Marker geoMarker;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_order_details);
        order = (Order) getIntent().getSerializableExtra("order");
        mQueue = BaseApplication.getRequestQueue();
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
       /* mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写*/
        initView();
        getLatlon("深圳白石洲");

    }
    private void initView(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("订单详情");
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_start = (Button) findViewById(R.id.btn_start);
        rl_addr = (RelativeLayout) findViewById(R.id.rl_addr);
        iv_theach = (ImageView) findViewById(R.id.iv_theach);
        ImageView iv_next = (ImageView)findViewById(R.id.iv_next);
        iv_next.setVisibility(View.GONE);
        rl_addr.setVisibility(View.VISIBLE);
        btn_back.setOnClickListener(this);
        btn_start.setOnClickListener(this);
        initData();
/*
        if (aMap == null) {
            aMap = mapView.getMap();
            geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);*/
        progDialog = new ProgressDialog(this);
    }
    private void initData(){
        getCode = order.getStatus();
        ((TextView)findViewById(R.id.tv_addr_name)).setText(order.getNursename());//地址姓名
        ((TextView)findViewById(R.id.tv_addr_phone)).setText(order.getMobile());//地址号码
        ((TextView)findViewById(R.id.tv_addr)).setText(order.getAddr());//地址
        ((TextView)findViewById(R.id.tv_order_number)).setText("");//订单编号
        ((TextView)findViewById(R.id.tv_combo)).setText(order.getOrdname());//宝宝洗护师A套餐服务
        ((TextView)findViewById(R.id.tv_states)).setText(order.getCodename()); //状态
        ((TextView)findViewById(R.id.tv_name)).setText(order.getNursename()); //预约护理师：
        ((TextView)findViewById(R.id.tv_num)).setText(order.getNum()+"个");//服务人员
        ((TextView)findViewById(R.id.tv_serdate)).setText(CommUtils.showData(order.getAtarrival()));//服务日期：
        ((TextView)findViewById(R.id.tv_startdate)).setText(CommUtils.showData(order.getAtarrival()));//下单日期
        ((TextView)findViewById(R.id.tv_money)).setText(CommUtils.subStr(order.getCstpaysum()));//价格

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv_theach, 0, R.drawable.teach);
        mImageLoader.get(Constant.nurseImage + order.getIcon(), listener);

        changeBtnStates(order.getStatus());
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
    //    mapView.onResume();
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
    //    mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    //    mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  mapView.onDestroy();
    }

    /**
     * 显示进度条对话框
     */
    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在获取地址");
        progDialog.show();
    }

    /**
     * 隐藏进度条对话框
     */
    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
    /**
     * 响应地理编码
     */
    public void getLatlon( String name) {
     //   showDialog();
        GeocodeQuery query = new GeocodeQuery(name, "深圳");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
    //    geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }
    /**
     * 根据状态
     * @param states
     */
    private void changeBtnStates(String states){
           if(states.equals("200")){ //待响应
               btn_start.setText("取消订单");
               orderStates = 900;
               getCode = "200";
           }else if(states.equals("400")){ //待服务
               btn_start.setText("开始服务");
               orderStates = 800;
               getCode = "400";
           }else if(states.equals("800")){ //完成服务
               btn_start.setText("完成服务");
               orderStates = 800;
               getCode = "800";
           }else{
               btn_start.setVisibility(View.GONE);
           }
    }
    @Override
    public void onClick(View view) {
      switch (view.getId()){
          case R.id.btn_back:
              finish();
              break;
          case R.id.btn_start:
              Intent intent = new Intent(DetailsActivity.this,OrderFragment.class);
              intent.putExtra("orderStates",orderStates);
              intent.putExtra("getCode",getCode);
              setResult(3, intent);
              break;
      }
    }
  /*  @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
      //  dismissDialog();
        if (rCode == 1000) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
                geoMarker.setPosition(AMapUtil.convertToLatLng(address.getLatLonPoint()));
                addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
                        + address.getFormatAddress();
             //   Toast.makeText(DetailsActivity.this,"addressName=="+addressName,Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DetailsActivity.this,"没有数据",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(DetailsActivity.this,"rCode=="+rCode,Toast.LENGTH_SHORT).show();
        }
    }*/
}
