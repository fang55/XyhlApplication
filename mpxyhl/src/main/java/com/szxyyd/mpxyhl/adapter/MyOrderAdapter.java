package com.szxyyd.mpxyhl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.activity.BaseApplication;
import com.szxyyd.mpxyhl.activity.Constant;
import com.szxyyd.mpxyhl.http.BitmapCache;
import com.szxyyd.mpxyhl.modle.Order;
import com.szxyyd.mpxyhl.utils.CommUtils;
import com.szxyyd.mpxyhl.view.RoundImageView;

import java.util.List;

/**
 * Created by jq on 2016/7/6.
 */
public class MyOrderAdapter extends BaseAdapter implements View.OnClickListener{
    private Context mContext;
    private LayoutInflater inflater;
    private onSelectListener mListener;
    private List<Order> mData;
    private ImageLoader mImageLoader;
    private String getCode;
    private RequestQueue mQueue;

    public MyOrderAdapter(Context context, List<Order> data) {
        mContext = context;
        mData = data;
        inflater = LayoutInflater.from(mContext);
        mQueue = BaseApplication.getRequestQueue();
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
    }
    @Override
    public int getCount() {
        return mData.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        Order order = mData.get(position);
        ViewHolder view ;
        if(contentView == null){
            contentView = inflater.inflate(R.layout.adapter_order, null, false);
            view = new ViewHolder();
            view.iv_teach = (RoundImageView) contentView.findViewById(R.id.iv_teach);
            view.tv_order_rank = (TextView) contentView.findViewById(R.id.tv_order_rank);
            view.tv_level = (TextView) contentView.findViewById(R.id.tv_level);
            view.tv_order_state = (TextView) contentView.findViewById(R.id.tv_order_state);
            view.tv_order_name = (TextView) contentView.findViewById(R.id.tv_order_name);
            view.tv_pay_money = (TextView) contentView.findViewById(R.id.tv_pay_money);
            view.tv_day = (TextView) contentView.findViewById(R.id.tv_day);
            view.tv_starttime = (TextView) contentView.findViewById(R.id.tv_starttime);
            view.btn_cancle = (TextView) contentView.findViewById(R.id.btn_cancle);
            view.btn_go = (TextView) contentView.findViewById(R.id.btn_go);
            view.btn_cancle.setOnClickListener(this);
            view.btn_go.setOnClickListener(this);
            contentView.setTag(view);
        }else{
            view = (ViewHolder) contentView.getTag();
        }
       getCode = order.getStatus();
        Log.e("MyOrderAdapter","getCode=="+getCode);
        if(getCode.equals("200")){
            view.btn_cancle.setVisibility(View.VISIBLE);
            view.btn_go.setVisibility(View.GONE);
            view.btn_cancle.setText("取消订单");
        }else if(getCode.equals("300")){
            view.btn_go.setVisibility(View.VISIBLE);
            view.btn_cancle.setVisibility(View.VISIBLE);
            view.btn_go.setText("去支付");
            view.btn_cancle.setText("取消订单");
        }else if(getCode.equals("400")){
            view.btn_go.setVisibility(View.VISIBLE);
            view.btn_cancle.setVisibility(View.GONE);
            view.btn_go.setText("开始服务");
        }else if(getCode.equals("1100")){
            view.btn_go.setVisibility(View.VISIBLE);
            view.btn_cancle.setVisibility(View.GONE);
            view.btn_go.setText("评价");
        }else if(getCode.equals("800")){
            view.btn_go.setVisibility(View.VISIBLE);
            view.btn_cancle.setVisibility(View.GONE);
            view.btn_go.setText("完成服务");
        }else if(getCode.equals("900")){
            view.btn_cancle.setVisibility(View.VISIBLE);
            view.btn_go.setVisibility(View.GONE);
            view.btn_cancle.setText("已取消");
        }else {
            view.btn_go.setVisibility(View.GONE);
            view.btn_cancle.setVisibility(View.GONE);
        }
       view.tv_order_rank.setText("订单编号："+order.getId());
        view.tv_order_state.setText(order.getCodename());
        view.tv_order_name.setText(order.getNursename());
        view.tv_starttime.setText(CommUtils.showData(order.getAtarrival()));
        view.tv_day.setText(CommUtils.showData(order.getAtsign()));
        view.tv_pay_money.setText(CommUtils.subStr(order.getCstpaysum())+".00");
        view.tv_level.setText(order.getOrdname());
        view.btn_cancle.setTag(position);
        view.btn_go.setTag(position);
        String imgUrl = order.getIcon();
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view.iv_teach, R.mipmap.teach, R.mipmap.teach);
        mImageLoader.get(Constant.nurseImage+imgUrl, listener);
        return contentView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancle: //取消
                int index = Integer.parseInt(view.getTag().toString());
                mListener.onSelect(index,900,getCode);
                break;
            case R.id.btn_go: //接受
                int index2 = Integer.parseInt(view.getTag().toString());
                mListener.onSelect(index2,800,getCode);
                break;
        }
    }

    private static class ViewHolder{
        RoundImageView iv_teach;
        TextView tv_order_rank;
        TextView tv_order_state;
        TextView tv_order_name;
        TextView tv_day;
        TextView tv_level;
        TextView tv_starttime;
        TextView tv_pay_money;
        TextView btn_cancle;
        TextView btn_go;
    }
    public void setclickListener(onSelectListener listener){
        mListener = listener;
    }
    public interface onSelectListener{
        void onSelect(int position,int code,String getCode);
    }
}
