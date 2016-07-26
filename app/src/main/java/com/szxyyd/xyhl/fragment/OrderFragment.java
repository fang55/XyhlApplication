package com.szxyyd.xyhl.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.activity.BaseApplication;
import com.szxyyd.xyhl.activity.ConfirmOrderActivity;
import com.szxyyd.xyhl.activity.Constant;
import com.szxyyd.xyhl.activity.DetailsActivity;
import com.szxyyd.xyhl.activity.MyActivity;
import com.szxyyd.xyhl.adapter.OrderAdapter;
import com.szxyyd.xyhl.http.VolleyRequestUtil;
import com.szxyyd.xyhl.inf.VolleyListenerInterface;
import com.szxyyd.xyhl.modle.Order;
import com.szxyyd.xyhl.view.CommentDialog;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 订单界面
 * @author fq
 */
public class OrderFragment extends Fragment {
	private View rootView;
	private GridView listview;
	private ImageView iv_line;
	private TextView tv_order1; //全部
	private TextView tv_order2; //待响应
	private TextView tv_order3; //待支付
	private TextView tv_order4; //待服务
	private TextView tv_order5; //服务中
	private TextView tv_order6; //待评价
	private OrderAdapter adapter;
	private MyActivity mActivity;
	private int offset = 0; // 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW; // 动画图片宽度
	private Animation animation;
	private AlertDialog alertDialog;
	private List<Order> list;
	private ProgressDialog proDialog;
	private int ordPosition = 0;
	private String ordCode = null;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case Constant.SUCCEED:
					 list = (List<Order>) msg.obj;
					if(list.size() != 0 ){
						adapter = new OrderAdapter(mActivity, list);
						listview.setAdapter(adapter);
					    adapter.setclickListener(new OrderAdapter.onSelectListener() {
						@Override
						public void onSelect(int position,int code,String getCode) {
							if(code == 900){//取消
								ordCode = getCode;
								if(getCode.equals("1100")){ //待评价
									showCommentDialog(list.get(position));
								}else{
									delectOrder();
									ordPosition = position;
								}
							}else{
								ordCode = getCode;
								if(getCode.equals("300")){
									showPayDialog(list.get(position));
								}else{
									submitData(position,code,getCode);
								}
							}
						}
					});
					    adapter.notifyDataSetChanged();
					}
					break;
				case Constant.SUBITM:
					if(ordCode == null){
						ordCode = "0";
					}
					getData(Integer.parseInt(ordCode));
					adapter.notifyDataSetChanged();
					listview.setAdapter(adapter);
					break;
				case Constant.FAILURE:
					if(proDialog != null){
						proDialog.dismiss();
					}
					list = new ArrayList<Order>();
					list.clear();
					adapter = new OrderAdapter(mActivity, list);
					listview.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					Toast.makeText(BaseApplication.getInstance(),"暂无数据",Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		};
	};
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MyActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_order, container, false);
		initView();
		initEvent();
		initImageView();
		getData(0);
		return rootView;
	}
	private void initView() {
		listview = (GridView) rootView.findViewById(R.id.listview);
		iv_line = (ImageView) rootView.findViewById(R.id.iv_line);
		tv_order1 = (TextView) rootView.findViewById(R.id.tv_order1);
		tv_order2 = (TextView) rootView.findViewById(R.id.tv_order2);
		tv_order3 = (TextView) rootView.findViewById(R.id.tv_order3);
		tv_order4 = (TextView) rootView.findViewById(R.id.tv_order4);
		tv_order5 = (TextView) rootView.findViewById(R.id.tv_order5);
		tv_order6 = (TextView) rootView.findViewById(R.id.tv_order6);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				Log.e("onItemClick","onItemClick");
				Order order = list.get(position);
				Log.e("onItemClick","order.getId()=="+order.getNurseid());
				String states = order.getStatus();
				Log.e("OrderFragment==","onItemClick--states=="+states);
				ordCode = states;
				ordPosition = position;
				if(states.equals("300")){  //待支付
					showPayDialog(order);
				}else if(states.equals("1100")){
					showCommentDialog(order);
				}else{
					showDetailsDialog(order);
				}
			}
		});
	}

	/**
	 * 评价界面
	 * @param order
     */
	private void showCommentDialog(Order order){
		CommentDialog dialog = new CommentDialog(mActivity,order,handler);
		dialog.init();
	}

	/**
	 * 订单详情界面
	 * @param order
     */
	public void showDetailsDialog(Order order){
		Intent intent = new Intent(mActivity, DetailsActivity.class);
		intent.putExtra("order",order);
		startActivityForResult(intent,  3);
	}

	/**
	 * 订单支付界面
	 * @param order
     */
	public void showPayDialog(Order order){
		Intent intent = new Intent(mActivity,ConfirmOrderActivity.class);
		intent.putExtra("order",order);
		startActivityForResult(intent,  2);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("OrderFragment","requestCode=="+requestCode);
		Log.e("OrderFragment","resultCode=="+resultCode);
			if (requestCode == 2) {
				if (data == null||"".equals(data)) {
					return;
				} else {
					ordCode = "300";
					Message message = new Message();
					message.what = Constant.SUBITM;
					handler.sendMessage(message);
				}
			} else if (requestCode == 3) {
				if (data == null||"".equals(data)) {
					return;
				} else {
					int states = data.getIntExtra("orderStates", 0);
					String getCode = data.getStringExtra("getCode");
					Log.e("onActivityResult","states=="+states);
					Log.e("onActivityResult","getCode=="+getCode);
					submitData(ordPosition, states, getCode);
				}
			}
	}

	private void initEvent(){
		tv_order1.setOnClickListener(new TitleOnClickListener(0));
		tv_order2.setOnClickListener(new TitleOnClickListener(1));
		tv_order3.setOnClickListener(new TitleOnClickListener(2));
		tv_order4.setOnClickListener(new TitleOnClickListener(3));
		tv_order5.setOnClickListener(new TitleOnClickListener(4));
		tv_order6.setOnClickListener(new TitleOnClickListener(5));
	}
	private void changeTextColor(TextView tv1,TextView tv2,TextView tv3,TextView tv4,TextView tv5,TextView tv6){
		tv1.setTextColor(mActivity.getResources().getColor(R.color.tv_login));
		tv2.setTextColor(Color.BLACK);
		tv3.setTextColor(Color.BLACK);
		tv4.setTextColor(Color.BLACK);
		tv5.setTextColor(Color.BLACK);
		tv6.setTextColor(Color.BLACK);
	}
	private void delectOrder(){
		alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog_cancle);
		TextView tv_dialog_title = (TextView) window.findViewById(R.id.tv_dialog_title);
		tv_dialog_title.setText("确定取消订单？");
		TextView tv_dialog_cancle = (TextView) window.findViewById(R.id.tv_dialog_cancle);
		tv_dialog_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				alertDialog.cancel();
			}
		});
		TextView tv_dialog_sure = (TextView) window.findViewById(R.id.tv_dialog_sure);
		tv_dialog_sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				submitData(ordPosition,900,ordCode);
				list.remove(ordPosition);
				adapter.notifyDataSetChanged();
				alertDialog.cancel();
			//	Toast.makeText(mActivity,"已取消",Toast.LENGTH_SHORT).show();
			}
		});
	}


	/**
	 * 头标点击监听
	 */
	public class TitleOnClickListener implements View.OnClickListener {
		private int mIndex = 0;
		public TitleOnClickListener(int index) {
			mIndex = index;
		}
		@Override
		public void onClick(View view) {
			switch (mIndex) {
				case 0:   // 全部订单
					getData(0);
					changeTextColor(tv_order1,tv_order2,tv_order3,tv_order4,tv_order5,tv_order6);
					break;
				case 1:  //待响应（200）
					getData(200);
					changeTextColor(tv_order2,tv_order1,tv_order3,tv_order4,tv_order5,tv_order6);
					break;
				case 2:  //待支付（300）
					getData(300);
					changeTextColor(tv_order3,tv_order1,tv_order2,tv_order4,tv_order5,tv_order6);
					break;
				case 3:  //待服务（400）
					getData(400);
					changeTextColor(tv_order4,tv_order1,tv_order3,tv_order2,tv_order5,tv_order6);
					break;
				case 4:  //服务中（800）
					getData(800);
					changeTextColor(tv_order5,tv_order3,tv_order4,tv_order1,tv_order2,tv_order6);
					break;
				case 5: //待评价（1100）
					getData(1100);
					changeTextColor(tv_order6,tv_order3,tv_order4,tv_order1,tv_order2,tv_order5);
					break;
				default:
					break;
			}
			changeImageView(mIndex);
		}
	}
	private void getData(int states){
		String url;
		Log.e("getData()", "Constant.cstId==" + Constant.cstId);
		if(Constant.cstId == null){
			return;
		}
		if(states == 0) {
			url = Constant.mktOrderListUrl + "&cstid=" + Constant.cstId;
		}else{
			url = Constant.mktOrderListUrl + "&cstid="+Constant.cstId+"&status="+states;
		}
		proDialog = ProgressDialog.show(mActivity, "", "加载中");
		VolleyRequestUtil volley = new VolleyRequestUtil();
		volley.RequestGet(getActivity(), url, "order",
				new VolleyListenerInterface(getActivity(),VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
					@Override
					public void onSuccess(String result) {
						Log.e("getData()", "result==" + result);
						if(proDialog != null){
							proDialog.cancel();
						}
						parserData(result,"get");
					}
					@Override
					public void onError(VolleyError error) {
						Log.e("VolleyError","error==="+error.toString());
						Message message = new Message();
						message.what = Constant.FAILURE;
						handler.sendMessage(message);
					}
				});
	}

	private void submitData(int position,int state,String getCode){
		Order order =  list.get(position);
		String orid = order.getId();
		//String cstpaysum = order.getCstpaysum();
		String url = null;
		if(getCode.equals("800")){
			url = Constant.odrCstUpdUrl + "&id="+orid+"&status="+"1100";
		}else{
			url = Constant.odrCstUpdUrl + "&id="+orid+"&status="+state;
		}
		VolleyRequestUtil volley = new VolleyRequestUtil();
		volley.RequestGet(getActivity(), url, "submit",
				new VolleyListenerInterface(getActivity(),VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
					@Override
					public void onSuccess(String result) {
						Log.e("submitData()", "submitData--result==" + result);
						parserData(result,"submit");
					}
					@Override
					public void onError(VolleyError error) {

					}
				});

	}

	private void parserData(String result,String type) {
		try {
			if(type.equals("submit")){
				Message message = new Message();
				message.what = Constant.SUBITM;
				handler.sendMessage(message);
			}else{
				JSONObject json = new JSONObject(result);
				String jsonData = json.getString("orderList");
				Log.e("OrderFragment","jsonData==="+jsonData);
				if("[]".equals(jsonData)){
					Message message = new Message();
					message.what = Constant.FAILURE;
					handler.sendMessage(message);
					return;
				}else{
					Type listType = new TypeToken<LinkedList<Order>>(){}.getType();
					Gson gson = new Gson();
					List<Order> list = gson.fromJson(jsonData, listType);
					Message message = new Message();
					message.what = Constant.SUCCEED;
					message.obj = list;
					handler.sendMessage(message);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 初始化动画
	 */
	private void initImageView() {
		bmpW = BitmapFactory.decodeResource(getResources(),
				R.drawable.title_line).getWidth();// 获取图片的宽度
		LinearLayout ll_title = (LinearLayout) rootView.findViewById(R.id.ll_title);
		int width = ll_title.getWidth();
		//Log.e("initImageView()", "width==" + width);
		int screeW = 920;
		int screeWidth = Constant.screenWidth /2;
	//	Log.e("initImageView()", "screeW==" + screeW);
	//	Log.e("initImageView()", "screeWidth==" + screeWidth);
		offset = (screeW / 6 - bmpW) / 2;// 计算偏移量
	//	Log.e("initImageView()", "offset==" + offset);
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		iv_line.setImageMatrix(matrix);// 设置动画
	}
	private void changeImageView(int index) {
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量
		int three = one * 3;  //页卡1 -> 页卡4偏移量
		int four = one * 4;  //页卡1 -> 页卡5偏移量
		int five = one * 5;  //页卡1 -> 页卡6偏移量
		Log.e("changeImageView()", "one==" +  one);
		Log.e("changeImageView()", "two==" + two);
		switch (index) {
		case 0:
			if (currIndex == 1) {
				animation = new TranslateAnimation(one, 0, 0, 0);
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, 0, 0, 0);
			}else if (currIndex == 3) {
				animation = new TranslateAnimation(three, 0, 0, 0);
			}else if (currIndex == 4) {
				animation = new TranslateAnimation(four, 0, 0, 0);
			}else if (currIndex == 5) {
				animation = new TranslateAnimation(five, 0, 0, 0);
			}else{
				animation = new TranslateAnimation(0, 0, 0, 0);
			}
			break;
		case 1:
			if (currIndex == 0) {
				animation = new TranslateAnimation(offset, one, 0, 0);
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, one, 0, 0);
			}else if(currIndex == 3){
				animation = new TranslateAnimation(three, one, 0, 0);
			}else if(currIndex == 4){
				animation = new TranslateAnimation(four, one, 0, 0);
			}else if(currIndex == 5){
				animation = new TranslateAnimation(five, one, 0, 0);
			}
			break;
		case 2:
			if (currIndex == 0) {
				animation = new TranslateAnimation(offset, two, 0, 0);
			} else if (currIndex == 1) {
				animation = new TranslateAnimation(one, two, 0, 0);
			}else if(currIndex == 3){
				animation = new TranslateAnimation(three, two, 0, 0);
			}else if(currIndex == 4){
				animation = new TranslateAnimation(four, two, 0, 0);
			}else if(currIndex == 5){
				animation = new TranslateAnimation(five, two, 0, 0);
			}
			break;
		case 3:
            if(currIndex == 0){
            	animation = new TranslateAnimation(offset, three, 0, 0);
            }else if(currIndex == 1){
            	animation = new TranslateAnimation(one, three, 0, 0);
            }else if(currIndex == 2){
            	animation = new TranslateAnimation(two, three, 0, 0);
            }else if(currIndex == 4){
				animation = new TranslateAnimation(four, three, 0, 0);
			}else if(currIndex == 5){
				animation = new TranslateAnimation(five, three, 0, 0);
			}
			break;
			case 4:
				if(currIndex == 0){
					animation = new TranslateAnimation(offset, four, 0, 0);
				}else if(currIndex == 1){
					animation = new TranslateAnimation(one, four, 0, 0);
				}else if(currIndex == 2){
					animation = new TranslateAnimation(two, four, 0, 0);
				}else if(currIndex == 3){
					animation = new TranslateAnimation(three, four, 0, 0);
				}else if(currIndex == 5){
					animation = new TranslateAnimation(five, four, 0, 0);
				}
				break;
			case 5:
				if(currIndex == 0){
					animation = new TranslateAnimation(offset, five, 0, 0);
				}else if(currIndex == 1){
					animation = new TranslateAnimation(one, five, 0, 0);
				}else if(currIndex == 2){
					animation = new TranslateAnimation(two, five, 0, 0);
				}else if(currIndex == 3){
					animation = new TranslateAnimation(three, five, 0, 0);
				}else if(currIndex == 4){
					animation = new TranslateAnimation(four, five, 0, 0);
				}
				break;
		default:
			break;
		}
		currIndex = index;
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(300);
		iv_line.startAnimation(animation);
	}

}
