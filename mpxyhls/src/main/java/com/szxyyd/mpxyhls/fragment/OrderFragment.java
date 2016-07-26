package com.szxyyd.mpxyhls.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.activity.Constant;
import com.szxyyd.mpxyhls.adapter.OrderAdapter;
import com.szxyyd.mpxyhls.http.VolleyRequestUtil;
import com.szxyyd.mpxyhls.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhls.modle.JsonBean;
import com.szxyyd.mpxyhls.modle.OrderList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单
 * Created by jq on 2016/7/13.
 */
public class OrderFragment extends Fragment{
    private View rootView ;
    private RadioGroup rgChannel;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private LinearLayout ll_notorder;
    private GridView gv_order;
    private OrderAdapter adapter;
    private List<OrderList> listData;
    private ProgressDialog proDialog;
    private int ordCode;
    private int ORDER_STATES ;
    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCEED:
                    listData = (List<OrderList>) msg.obj;
                    Log.e("MyOrderActivity","listData.size()=="+listData.size());
                    if(listData.size() != 0 && listData != null) {
                        ll_notorder.setVisibility(View.GONE);
                        adapter = new OrderAdapter(getActivity(), listData);
                    }else {
                        listData.clear();
                        adapter = new OrderAdapter(getActivity(), listData);
                        ll_notorder.setVisibility(View.VISIBLE);
                    //    Toast.makeText(getActivity(),"暂无数据",Toast.LENGTH_SHORT).show();
                    }
                    gv_order.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setclickListener(new OrderAdapter.onSelectListener() {
                        @Override
                        public void onSelect(int position, int code) {
                            OrderList orderList = listData.get(position);
                            ordCode = Integer.parseInt(orderList.getStatus());
                         //   Log.e("MyOrderActivity","ordCode=="+ordCode);
                            submitRefuseData(orderList,code);
                        }
                    });
                    break;
                case Constant.FAILURE:

                    break;
                case Constant.SUBITM:
                    if(ordCode == 0){
                        ordCode = 0;
                    }
                    Log.e("OrderFragment", "handler---ordCode=="+ordCode);
                    lodeOrderData(ordCode);
                    adapter.notifyDataSetChanged();
                    gv_order.setAdapter(adapter);
                    break;
                default:
                    break;
            }

        };
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_order,container,false);
        initView();
        return rootView;
    }
    private void initView(){
        ll_notorder = (LinearLayout) rootView.findViewById(R.id.ll_notorder);
        gv_order = (GridView) rootView.findViewById(R.id.gv_order);
        rgChannel = (RadioGroup) rootView.findViewById(R.id.rgChannel);
        rb1 = (RadioButton) rootView.findViewById(R.id.rb1);
        rb2 = (RadioButton) rootView.findViewById(R.id.rb2);
        rb3 = (RadioButton) rootView.findViewById(R.id.rb3);
        rb4 = (RadioButton) rootView.findViewById(R.id.rb4);
        rgChannel.setOnCheckedChangeListener(new rgOnCheckedChangeListener());
        rb2.setChecked(true);

    }
    class rgOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            switch (checkedId) {
                case R.id.rb1:
                    ORDER_STATES = 0;
                    changeTextColor(rb1,rb2,rb3,rb4);
                    break;
                case R.id.rb2:
                    ORDER_STATES = 200;
                    changeTextColor(rb2,rb1,rb3,rb4);
                    break;
                case R.id.rb3:
                    ORDER_STATES = 400;
                    changeTextColor(rb3,rb2,rb1,rb4);
                    break;
                case R.id.rb4:
                    ORDER_STATES = 800;
                    changeTextColor(rb4,rb2,rb3,rb1);
                    break;
            }
            lodeOrderData(ORDER_STATES);
        }
    }
    private void changeTextColor(RadioButton tv1,RadioButton tv2,RadioButton tv3,RadioButton tv4){
        tv1.setTextColor(getResources().getColor(R.color.color_bule));
        tv2.setTextColor(getResources().getColor(R.color.color_six_six));
        tv3.setTextColor(getResources().getColor(R.color.color_six_six));
        tv4.setTextColor(getResources().getColor(R.color.color_six_six));
    }
    /**
     * 加载数据
     */
    private void lodeOrderData(int index){
        proDialog = ProgressDialog.show(getActivity(), "", "加载中");
        String nurseid = Constant.nurId;
        Map<String,String> map = new HashMap<>();
        if(index == 0){
            map.put("nurseid",nurseid);
        }else{
            map.put("nurseid",nurseid);
            map.put("status",String.valueOf(index));
        }
        VolleyRequestUtil.newInstance().GsonPostRequest(getActivity(),Constant.orderListUrl,"order" ,map,new TypeToken<JsonBean>(){},
                new Response.Listener<JsonBean>() {
                    @Override
                    public void onResponse(JsonBean jsonBean) {
                        if(proDialog!= null){
                            proDialog.cancel();
                        }
                        List<OrderList> list = jsonBean.getOrderList();
                        Message message = new Message();
                        message.what = Constant.SUCCEED;
                        message.obj = list;
                        handler.sendMessage(message);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
    }
    /**
     * 提交
     */
    private void submitRefuseData(OrderList orderList,int code){
        String odrid = orderList.getId();
        String nurseid = orderList.getNurseid();  //1294046
        //  String nurseid = "1294180"; 1294026
        Log.e("OrderFragment", "submitRefuseData---odrid=="+odrid);
        Log.e("OrderFragment", "submitRefuseData---nurseid=="+nurseid);
        String note = orderList.getNote();
        String url = Constant.odrespUpdUrl +"&odrid="+odrid +"&nurseid="+nurseid
                +"&status="+code
                +"&note="+note;
        Log.e("OrderFragment", "submitRefuseData---url=="+url);
        VolleyRequestUtil.newInstance().RequestGet(getActivity(), url, "submit",
                new VolleyListenerInterface(getActivity(),VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("OrderFragment", "submitRefuseData---result=="+result);
                        Message message = new Message();
                        message.what = Constant.SUBITM;
                        handler.sendMessage(message);
                    }
                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }
}
