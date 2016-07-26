package com.szxyyd.xyhl.adapter;

import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.activity.BaseApplication;
import com.szxyyd.xyhl.activity.ConfirmOrderActivity;
import com.szxyyd.xyhl.activity.Constant;
import com.szxyyd.xyhl.http.BitmapCache;
import com.szxyyd.xyhl.inf.UpdateData;
import com.szxyyd.xyhl.modle.Order;
import com.szxyyd.xyhl.utils.CommUtils;
import com.szxyyd.xyhl.view.RoundImageView;

public class OrderAdapter extends BaseAdapter implements OnClickListener{
	private Context mContext;
	private LayoutInflater infater;
	private List<Order> mData;
	private int selectposition;
	private String getCode;
	private onSelectListener mListener;
	private ImageLoader mImageLoader;
	private RequestQueue mQueue ;
   public OrderAdapter(Context context,List<Order> data){
	   mContext = context;
	   mData = data;
	   infater = LayoutInflater.from(mContext);
	   mQueue = BaseApplication.getRequestQueue();
	   mImageLoader = new ImageLoader(mQueue, new BitmapCache());
   }
	@Override
	public int getCount() {
		return mData.size()==0 ? 0 :mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		Order order = mData.get(position);
		ViewHolder view ;
		if(contentView == null){
			contentView = infater.inflate(R.layout.order_item, null, false);
			view = new ViewHolder();
			view.iv_teach = (RoundImageView) contentView.findViewById(R.id.iv_teach);
			view.tv_order_id = (TextView) contentView.findViewById(R.id.tv_order_id);
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
		Log.e("getView==","getCode=="+getCode);
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
			view.btn_cancle.setVisibility(View.VISIBLE);
			view.btn_go.setVisibility(View.GONE);
			view.btn_cancle.setText("评价");
		}else if(getCode.equals("800")){
			view.btn_go.setVisibility(View.VISIBLE);
			view.btn_cancle.setVisibility(View.GONE);
			view.btn_go.setText("完成服务");
		}else if(getCode.equals("900")){
			view.btn_cancle.setVisibility(View.VISIBLE);
			view.btn_go.setVisibility(View.GONE);
			view.btn_cancle.setText("已取消");
		}else if(getCode.equals("500") || getCode.equals("600") || getCode.equals("700")){
			view.btn_go.setVisibility(View.GONE);
			view.btn_cancle.setVisibility(View.GONE);
		}
		view.tv_order_id.setText(order.getId());
		view.tv_order_state.setText(order.getCodename());
		view.tv_order_name.setText(order.getNursename());
		view.tv_starttime.setText(CommUtils.showData(order.getAtarrival()));
		view.tv_day.setText(CommUtils.showData(order.getAtsign()));
		view.tv_pay_money.setText(CommUtils.subStr(order.getCstpaysum())+".00");
		view.tv_level.setText(order.getOrdname());
		view.btn_cancle.setTag(position);
		view.btn_go.setTag(position);
		selectposition = position;
		String imgUrl = order.getIcon();
		//Log.e("getView","imgUrl=="+imgUrl);
		ImageLoader.ImageListener listener = ImageLoader.getImageListener(view.iv_teach, R.drawable.teach, R.drawable.teach);
		mImageLoader.get(Constant.nurseImage+imgUrl, listener);
		return contentView;
	}
   private static class ViewHolder{
	   RoundImageView iv_teach;
	   TextView tv_order_id;
	   TextView tv_order_state;
	   TextView tv_order_name;
	   TextView tv_day;
	   TextView tv_level;
	   TextView tv_starttime;
	   TextView tv_pay_money;
	   TextView btn_cancle;
	   TextView btn_go;
   }
@Override
public void onClick(View view) {
	switch (view.getId()){
		case R.id.btn_cancle: //取消
			int index = Integer.parseInt(view.getTag().toString());
			mListener.onSelect(index,900,getCode);
			break;
		case R.id.btn_go: //接受
			Log.e("btn_go", "selectposition=="+selectposition);
			Log.e("btn_go", "getCode=="+getCode);
			int index2 = Integer.parseInt(view.getTag().toString());
			mListener.onSelect(index2,800,getCode);
			break;
	}
}
	public void setclickListener(onSelectListener listener){
		mListener = listener;
	}
     public interface onSelectListener{
		void onSelect(int position,int code,String getCode);
	}
}
