package com.szxyyd.mpxyhl.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.activity.ConfirmOrderActivity;
import com.szxyyd.mpxyhl.activity.Constant;
import com.szxyyd.mpxyhl.activity.HomePagerActivity;
import com.szxyyd.mpxyhl.activity.OrderCommentActivity;
import com.szxyyd.mpxyhl.activity.OrderDetailsActivity;
import com.szxyyd.mpxyhl.adapter.MyOrderAdapter;
import com.szxyyd.mpxyhl.http.HttpMethods;
import com.szxyyd.mpxyhl.inter.CallOnResponsetListener;
import com.szxyyd.mpxyhl.inter.SubscriberOnNextListener;
import com.szxyyd.mpxyhl.modle.JsonBean;
import com.szxyyd.mpxyhl.modle.Order;
import com.szxyyd.mpxyhl.modle.ProgressSubscriber;
import com.szxyyd.mpxyhl.utils.OkHttp3Utils;
import com.szxyyd.mpxyhl.view.PopupDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 我的订单
 * Created by fq on 2016/7/6.
 */
public class MyOrderFragment extends Fragment {
    private View rootView;
    private RadioGroup rgChannel = null;
    public  RadioButton rb1 = null;
    private RadioButton rb2 = null;
    private RadioButton rb3 = null;
    private RadioButton rb4 = null;
    private RadioButton rb5 = null;
    private RadioButton rb6 = null;
    private GridView gv_order = null;
    private List<Order> list = null;
    private MyOrderAdapter adapter = null;
    private String ordCode = "0";
    private LinearLayout ll_notorder = null;
    public  int ORDER_STATES  ;
    private HomePagerActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (HomePagerActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_myorder,container,false);
        initView();
        return rootView;
    }
    private void initView(){
        ll_notorder = (LinearLayout) rootView.findViewById(R.id.ll_notorder);
        gv_order = (GridView) rootView.findViewById(R.id.gv_order);
        gv_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Order order = list.get(position);
                String states = order.getStatus();
                ordCode = states;
                showDetailsView(states,order);
            }
        });
        rgChannel=(RadioGroup)rootView.findViewById(R.id.rgChannel);
        rb1 = (RadioButton) rootView.findViewById(R.id.rb1);
        rb2 = (RadioButton)rootView.findViewById(R.id.rb2);
        rb3 = (RadioButton) rootView.findViewById(R.id.rb3);
        rb4 = (RadioButton) rootView.findViewById(R.id.rb4);
        rb5 = (RadioButton) rootView.findViewById(R.id.rb5);
        rb6 = (RadioButton) rootView.findViewById(R.id.rb6);
        rgChannel.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.rb1:
                                ORDER_STATES = 0;
                                changeTextColor(rb1,rb2,rb3,rb4,rb5,rb6);
                                break;
                            case R.id.rb2:
                                ORDER_STATES = 200;
                                changeTextColor(rb2,rb1,rb3,rb4,rb5,rb6);
                                break;
                            case R.id.rb3:
                                ORDER_STATES = 300;
                                changeTextColor(rb3,rb2,rb1,rb4,rb5,rb6);
                                break;
                            case R.id.rb4:
                                ORDER_STATES = 400;
                                changeTextColor(rb4,rb2,rb3,rb1,rb5,rb6);
                                break;
                            case R.id.rb5:
                                ORDER_STATES = 800;
                                changeTextColor(rb5,rb2,rb3,rb4,rb1,rb6);
                                break;
                            case R.id.rb6:
                                ORDER_STATES = 1100;
                                changeTextColor(rb6,rb2,rb3,rb4,rb5,rb1);
                                break;
                        }
                        getData(ORDER_STATES);
                    }
                });
        rb2.setChecked(true);
    }
    private void changeTextColor(RadioButton tv1,RadioButton tv2,RadioButton tv3,RadioButton tv4,RadioButton tv5,RadioButton tv6){
        tv1.setTextColor(getResources().getColor(R.color.color_bule));
        tv2.setTextColor(getResources().getColor(R.color.color_six_six));
        tv3.setTextColor(getResources().getColor(R.color.color_six_six));
        tv4.setTextColor(getResources().getColor(R.color.color_six_six));
        tv5.setTextColor(getResources().getColor(R.color.color_six_six));
        tv6.setTextColor(getResources().getColor(R.color.color_six_six));
    }
    /**
     * 根据状态提交数据
     * @param getCode
     */
    private void sumbitTyoeData(String getCode,Order order){
        if(getCode.equals("300")){ //待支付
            showDetailsView(ordCode,order);
        }else if(getCode.equals("400") ){ //待服务
            ordCode = "400";
            submitData(order,"800");
        }else if(getCode.equals("800")){ //服务中
            ordCode = "800";
            submitData(order,"1100");
        }else if(getCode.equals("1100")){ //待评价
            ordCode = "1100";
            showDetailsView(ordCode,order);
        }
    }
    /**
     * 根据状态不同进入详情、支付、评价界面
     * @param states
     */
   private void showDetailsView(String states,Order order){
       if(states.equals("300")){  //待支付
           Intent intentConfirm = new Intent(getActivity(),ConfirmOrderActivity.class);
           intentConfirm.putExtra("order",  order);
           startActivityForResult(intentConfirm,  1);
       }else if(states.equals("1100")){ //评价
           Intent intentComment = new Intent(getActivity(),OrderCommentActivity.class);
           intentComment.putExtra("order", order);
           startActivityForResult(intentComment,  2);
       }else{ //订单详情
           Intent intentDetails = new Intent(getActivity(),OrderDetailsActivity.class);
           intentDetails.putExtra("order", order);
           startActivityForResult(intentDetails,  3);
       }
   }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null||"".equals(data)) {
            return;
        } else {
            getData(Integer.parseInt(ordCode));
        }
    }
    /**
     * 取消订单
     * @param order
     */
    private void cancleOrder(final Order order){
        PopupDialog dialog = new PopupDialog(getActivity(),"order");
        dialog.initView();
        dialog.setonSelectListener(new PopupDialog.onSelectClickListener() {
            @Override
            public void onSure() {
                submitData(order,"900");
            }
        });
    }
    /**
     * 提交订单
     * @param order
     * @param status
     */
    private void submitData(Order order,String status){
        String orid = order.getId();
        Map<String,String> map = new HashMap<>();
        map.put("id",orid);
        map.put("status",status);
        OkHttp3Utils.getInstance().requestPost(Constant.odrCstUpdUrl,map);
        OkHttp3Utils.getInstance().setOnResultListener(new CallOnResponsetListener() {
            @Override
            public void onSuccess(Call call, Response response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getData(Integer.parseInt(ordCode));
                    }
                });
            }
        });
    }
    /**
     * 根据状态获取订单
     * @param states
     */
    public void getData(int states){
        if(states == 0) {
            HttpMethods.getInstance().getOrderListData("mktOrderList",Constant.cstId,new ProgressSubscriber<JsonBean>(getOrderOnNext,getActivity()));
        }else{
            HttpMethods.getInstance().getOrderListData("mktOrderList",Constant.cstId,states,new ProgressSubscriber<JsonBean>(getOrderOnNext,getActivity()));
        }
    }
    private SubscriberOnNextListener getOrderOnNext = new SubscriberOnNextListener<JsonBean>() {
        @Override
        public void onNext(JsonBean jsonBean) {
            list = jsonBean.getOrderList();
            if(list.size() != 0) {
                ll_notorder.setVisibility(View.GONE);
                adapter = new MyOrderAdapter(getActivity(),list);
                gv_order.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.setclickListener(new MyOrderAdapter.onSelectListener() {
                    @Override
                    public void onSelect(int position,int code,String getCode) {
                        Order order = list.get(position);
                        ordCode = getCode;
                        if(code == 900){//取消按钮状态
                            cancleOrder(order);
                        }else{ //接受按钮状态
                            sumbitTyoeData(getCode,order);
                        }
                    }
                });
            }else{
                ll_notorder.setVisibility(View.VISIBLE);
            }
        }
    };
}
