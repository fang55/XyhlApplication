package com.szxyyd.mpxyhl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.fragment.MyOrderFragment;
import com.szxyyd.mpxyhl.http.VolleyRequestUtil;
import com.szxyyd.mpxyhl.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhl.modle.Order;
import com.szxyyd.mpxyhl.pay.PayTools;
import com.szxyyd.mpxyhl.utils.CommUtils;

/**
 * 支付界面
 * Created by fq on 2016/7/7.
 */
public class ConfirmOrderActivity extends Activity implements View.OnClickListener{
    private CheckBox cb_weixin;
    private CheckBox rb_pay;
    private Button btn_confirm;
    private RelativeLayout rl_addr;
    private Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        order = (Order) getIntent().getSerializableExtra("order");
        initView();
    }
    private void initView(){
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("订单支付");
        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_confirm = (Button) findViewById(R.id.btn_next);
        btn_confirm.setText("确定支付");
        btn_confirm.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        cb_weixin = (CheckBox) findViewById(R.id.cb_weixin);
        rl_addr = (RelativeLayout) findViewById(R.id.rl_addr);
        rl_addr.setVisibility(View.VISIBLE);
        ImageView iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_next.setVisibility(View.GONE);
        cb_weixin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    rb_pay.setClickable(false);
                }else{
                    rb_pay.setClickable(true);
                }
            }
        });
        rb_pay = (CheckBox) findViewById(R.id.rb_pay);
        rb_pay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cb_weixin.setClickable(false);
                }else{
                    cb_weixin.setClickable(true);
                }
            }
        });
        initData();
    }
    private void initData(){
        ((TextView)findViewById(R.id.tv_addr_name)).setText(order.getNursename());
        ((TextView)findViewById(R.id.tv_addr_phone)).setText(order.getMobile());
        ((TextView)findViewById(R.id.tv_addr)).setText(order.getAddr());
        ((TextView)findViewById(R.id.tv_order_name)).setText(order.getNursename());
        ((TextView)findViewById(R.id.tv_order_money)).setText(CommUtils.subStr(order.getCstpaysum())+".00");
    }
    /**
     * 提交支付
     */
    private void submitData(){
        //nur?a=nurseCmt	id (订单id) cstpaysum (支付金额)
        String id = order.getId();
        String cstpaysum = order.getCstpaysum();
     //   String url = Constant.odrPayUrl + "&id="+id+"&cstpaysum="+cstpaysum;
        String url = Constant.aliPayUrl + "&id="+id;
        VolleyRequestUtil.newInstance().RequestGet(this, url, "pay",
                new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(final String result) {
                        Log.e("ConfirmOrderActivity", "ConfirmOrderActivity--result=="+result);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                confirmPay(result);
                            }
                        });
                    }
                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }
    /**
     * 确定支付
     */
    private void confirmPay(String sign){
        PayTools payTools = PayTools.getInstance(this);
        //String orderId = getOutTradeNo();
        //String sign = "body=\"测试\"&out_trade_no=\"22223333113354\"&payment_type=\"1\"&subject=\"护理师测试\"&total_fee=\"0.01\"＆service=\"mobile.securitypay.pay\"&partner=\"2088911930206812\"&_input_charset=\"utf-8\"&sign_type=\"MD5\"&seller_id=\"3278795454@qq.com\"&notify_url=\"http://183.232.35.71:8080/xyhl/ardpay\"&return_url=\"m.alipay.com\"&sign=\"df2e5c3e8ce1f10fba4856ab6630b5d6\"";
        //	String sign = "_input_charset=\"utf-8\"&body=\"测试\"&notify_url=\"http://183.232.35.71:8080/xyhl/ardpay\"&out_trade_no=\"12345678901020\"&partner=\"2088911930206812\"&payment_type=\"1\"&return_url=\"m.alipay.com\"&seller_id=\"3278795454@qq.com\"&service=\"create_direct_pay_by_user\"&subject=\"护理师测试\"&total_fee=\"0.01\"&sign=\"6a7a1c0daa96e9e700ac46cca4882805\"&sign_type=\"MD5\"";
        //	String sign = "_input_charset=utf-8&body=你个大石&notify_url=http://183.232.35.71:8080/xyhl/ardpay&out_trade_no=a201607084526112&partner=2088911930206812&payment_type=1&return_url=m.alipay.com&seller_id=3278795454@qq.com&service=alipay.wap.create.direct.pay.by.user&show_url=http://183.232.35.71:8080/xyhl/ardpay&subject=测试订单1&total_fee=0.01&sign_type=MD5&sign=0a28d0c8e6244f3ef0ca30edb61759f2\n";
        payTools.payByZFB(sign);
        payTools.setOnPayResultLinstener(new PayTools.OnPayResultLinstener() {
            @Override
            public void paySuccessfullResult(int payType, String message) {
                Log.e("ConfirmOrderActivity","payType=="+payType);
                Log.e("ConfirmOrderActivity","message=="+message);
                Intent intent = new Intent(ConfirmOrderActivity.this,MyOrderFragment.class);
                setResult(1, intent);
                finish();
            }
            @Override
            public void faildPayResult(int payType, String message) {

            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
               finish();
                break;
            case R.id.btn_next:
                submitData();
                break;
        }
    }
}
