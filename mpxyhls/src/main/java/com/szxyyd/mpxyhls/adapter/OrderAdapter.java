package com.szxyyd.mpxyhls.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.modle.OrderList;
import com.szxyyd.mpxyhls.utils.ExampleUtil;

import java.util.List;

/**
 * Created by jq on 2016/7/13.
 */
public class OrderAdapter extends BaseAdapter implements View.OnClickListener{
    private Context mContext;
    private List<OrderList> orderLists;
    private LayoutInflater inflater;
    private onSelectListener mListener;
    private int code = 0;
    private String getcode;
    public OrderAdapter(Context context,List<OrderList> lists){
        mContext = context;
        orderLists = lists;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return orderLists.size();
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
        OrderList order = orderLists.get(position);
        ViewHolder viewHolder;
        if(contentView == null){
            contentView = inflater.inflate(R.layout.adapter_order,null,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_orderId = (TextView) contentView.findViewById(R.id.tv_orderId);
            viewHolder.tv_orderState = (TextView) contentView.findViewById(R.id.tv_orderState);
            viewHolder.tv_orderName = (TextView) contentView.findViewById(R.id.tv_orderName);
            viewHolder.tv_orderAddr = (TextView) contentView.findViewById(R.id.tv_orderAddr);
            viewHolder.tv_orderTime = (TextView) contentView.findViewById(R.id.tv_orderTime);
            viewHolder.tv_cancle = (TextView) contentView.findViewById(R.id.tv_cancle);
            viewHolder.tv_go = (TextView) contentView.findViewById(R.id.tv_go);
            viewHolder.tv_cancle.setOnClickListener(this);
            viewHolder.tv_go.setOnClickListener(this);
            contentView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) contentView.getTag();
        }
        getcode = order.getStatus();
        if(getcode.equals("200")){
            viewHolder.tv_go.setText("接收");
            viewHolder.tv_cancle.setText("拒绝");
        }else {
            viewHolder.tv_go.setVisibility(View.GONE);
            viewHolder.tv_cancle.setVisibility(View.GONE);
        }
        viewHolder.tv_orderId.setText("订单编号："+order.getId()+"");
        viewHolder.tv_orderState.setText(order.getCodename());
        viewHolder.tv_orderName.setText(order.getNursename());
        viewHolder.tv_orderAddr.setText(order.getAddr());
        viewHolder.tv_orderTime.setText(ExampleUtil.showData(order.getAtarrival()));
        viewHolder.tv_cancle.setTag(position);
        viewHolder.tv_go.setTag(position);
        return contentView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancle:
                int index1 = Integer.parseInt(view.getTag().toString());
                code = 400;
                mListener.onSelect(index1,code);
                break;
            case R.id.tv_go:
                int index = Integer.parseInt(view.getTag().toString());
                code = 200;
                mListener.onSelect(index,code);
                break;
        }
    }
    static class ViewHolder{
        TextView tv_orderId;
        TextView tv_orderState;
        TextView tv_orderName;
        TextView tv_orderAddr;
        TextView tv_orderTime;
        TextView tv_cancle;
        TextView tv_go;
    }
    public void setclickListener(onSelectListener listener){
        mListener = listener;
    }
    public interface onSelectListener{
        void onSelect(int position,int code);
    }
}
