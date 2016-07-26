package com.szxyyd.xyhl.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.adapter.ServiceLocationAdapter;
import com.szxyyd.xyhl.http.BitmapCache;
import com.szxyyd.xyhl.http.VolleyRequestUtil;
import com.szxyyd.xyhl.inf.VolleyListenerInterface;
import com.szxyyd.xyhl.modle.NurseList;
import com.szxyyd.xyhl.modle.Reladdr;
import com.szxyyd.xyhl.pay.PayTools;
import com.szxyyd.xyhl.view.MenuPopupWindow;
import com.szxyyd.xyhl.view.RoundImageView;
import com.szxyyd.xyhl.view.ShowTimeDialog;
import java.util.List;

/**
 * 预约护理师
 * @author jq
 *
 */
public class OrderNurseActivity extends Activity implements OnClickListener{
	private Button btn_back;
	private TextView tv_title;
	private RoundImageView circle_people;
	private Button btn_sureorder;
	private Button btn_navigation;
	private TextView order_name;
	private TextView order_technology; //擅长
	private TextView tv_introduce; //服务介绍
	private TextView tv_addr_name; //姓名
	private TextView tv_addr; //地址
	private TextView tv_money; //价钱
	private TextView tv_remark;
	private TextView tv_bodynum;
	private TextView tv_date;
	private RelativeLayout rl_location; //选择服务地址
	private RelativeLayout rl_addr;
	private LinearLayout ll_notaddr;
	private NurseList nurse;
	private RequestQueue mQueue;
	private ImageLoader mImageLoader;
	private String starlevel;
	private String bodynum;
	private String date;
	private String remark;
	private String money;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		nurse = (NurseList) getIntent().getSerializableExtra("nurse");
		initView();
		initEvent();
		showOrderContent();
		mQueue = BaseApplication.getRequestQueue();
		mImageLoader = new ImageLoader(mQueue, new BitmapCache());
		showImae();
		BaseApplication.getInstance().addActivity(this);
	}
	private void initView(){
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getString(R.string.tv_order_teacher));
		order_name = (TextView) findViewById(R.id.order_name);
		order_name.setText(nurse.getName());
		((TextView) findViewById(R.id.order_health)).setText(nurse.getSvrid());
		order_technology = (TextView) findViewById(R.id.order_technology);
		order_technology.setText("擅长："+nurse.getSpcty());
		tv_introduce = (TextView)findViewById(R.id.tv_introduce);
		tv_addr_name = (TextView)findViewById(R.id.tv_addr_name);
		tv_addr = (TextView)findViewById(R.id.tv_addr);
		tv_money = (TextView)findViewById(R.id.tv_money);
		tv_remark =  (TextView)findViewById(R.id.tv_remark);
		tv_bodynum = (TextView) findViewById(R.id.tv_bodynum);
		tv_date = (TextView) findViewById(R.id.tv_date);
		rl_location = (RelativeLayout) findViewById(R.id.rl_location);
		ImageView iv_next = (ImageView) findViewById(R.id.iv_next);
		iv_next.setVisibility(View.GONE);
		btn_sureorder = (Button) findViewById(R.id.btn_sureorder);
		ll_notaddr = (LinearLayout) findViewById(R.id.ll_notaddr);
		rl_addr = (RelativeLayout) findViewById(R.id.rl_addr);
		circle_people = (RoundImageView) findViewById(R.id.circle_people);
		btn_navigation = (Button) findViewById(R.id.btn_navigation);
	}
	private void initEvent(){
		btn_sureorder.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_navigation.setOnClickListener(this);
	}
	private void showImae() {
		ImageLoader.ImageListener listener = ImageLoader.getImageListener(circle_people, 0, R.drawable.teach);
		mImageLoader.get(Constant.nurseImage + nurse.getIcon(), listener);
	}
	private void showOrderContent(){
		SharedPreferences preferences = getSharedPreferences("sercontent", Context.MODE_PRIVATE);
		starlevel = preferences.getString("level", "");
		bodynum = preferences.getString("serpeople", "");
		date = preferences.getString("sertime", "");
		remark = preferences.getString("note", "");
		money = preferences.getString("price","");
		((TextView) findViewById(R.id.tv_starlevel)).setText(starlevel);
		 tv_bodynum.setText(bodynum);
		if(TextUtils.isEmpty(bodynum)){
			bodynum = "未选择";
			tv_bodynum.setText(bodynum);
		}else{
			tv_bodynum.setText(bodynum+ "");
		}
		tv_date.setText(date);
		if(TextUtils.isEmpty(remark)){
			remark = "未说明";
			tv_remark.setText(remark);
		}else{
			tv_remark.setText(remark+ "");
		}
		tv_money.setText(money);
		String name = preferences.getString("name", "");
		String phone = preferences.getString("mobile", "");
		String addr = preferences.getString("addr", "");
		if (name.length() > 0) {
			ll_notaddr.setVisibility(View.GONE);
			rl_addr.setVisibility(View.VISIBLE);
			tv_addr_name.setText(name);
			((TextView) findViewById(R.id.tv_addr_phone)).setText(phone);
			tv_addr.setText(addr);
		}
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_back:
                finish();
				break;
			case R.id.btn_sureorder:
				showOrderDialog();
				break;
			case R.id.btn_navigation:
				MenuPopupWindow	popupWindow = new MenuPopupWindow(OrderNurseActivity.this);
				popupWindow.initPopupWindow(btn_navigation,124,372);
				popupWindow.setOutsideTouchable(true);
				break;
			default:
				break;
		}
	}
	/**
	 * 提交订单
	 */
	private void submitData(){
		int lvl = Constant.svrId;//级别id
		String cstid = Constant.cstId;
		String name = order_name.getText().toString();
		String addr = tv_addr.getText().toString();//地址
		String nurseid = nurse.getNursvrid();
		String calids = null;//数组[1,2,3,4]
		String idxs = null;  //数组
		String note = tv_remark.getText().toString();  //备忘信息
		String cstpaysum = tv_money.getText().toString(); //价格
		String atarrival =tv_date.getText().toString().trim(); //时间
		String url = Constant.orderUrl+"&svrid="+lvl+"&lvl="+lvl+"&cstid="+cstid+"&name="+name
				+"&addr="+addr+"&nurseid="+nurseid+getIdxsList()+ getCalidsList()
				+"&note="+note
				+"&cstpaysum="+cstpaysum
				+"&atarrival="+atarrival;
		VolleyRequestUtil volley = new VolleyRequestUtil();
		volley.RequestGet(this, url, "order",
				new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
					@Override
					public void onSuccess(String result) {
						Log.e("getData()", "result==" + result);
						//parserData(result,"order");
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(OrderNurseActivity.this,"已提交",Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(OrderNurseActivity.this,MyActivity.class);
								startActivity(intent);
								BaseApplication.getInstance().exit();
							}
						});
					}
					@Override
					public void onError(VolleyError error) {
					}
				});
	}
	private String getIdxsList(){
		String result = "";
		for(int i = 0; i < Constant.listLevel.size();i++){
			StringBuilder sb = new StringBuilder();
			result += sb.append("&idxs=").append(Constant.listLevel.get(i)).toString();
			Log.e("getIdxsList", "result=="+result);
		}
		return result;
	}
	private String getCalidsList(){
		String result = "";
		for(int i = 0; i < Constant.listpople.size();i++){
			StringBuilder sb = new StringBuilder();
			result += sb.append("&calids=").append(Constant.listpople.get(i)).toString();
			Log.e("getCalidsList", "result=="+result);
		}
		return result;
	}

	/**
	 * 确定支付对话框
     */
	private void showOrderDialog(){
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.view_sure_order);
		((TextView)window.findViewById(R.id.tv_leveinfo)).setText(starlevel);
		((TextView)window.findViewById(R.id.tv_sure_bodynum)).setText(bodynum);
		((TextView)window.findViewById(R.id.tv_sure_date)).setText(date);
		((TextView)window.findViewById(R.id.tv_sure_remark)).setText(remark);
		((TextView)window.findViewById(R.id.tv_sure_money)).setText(money);
		TextView tv_dialog_cancle = (TextView) window.findViewById(R.id.tv_sure_cancle);
		tv_dialog_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				alertDialog.cancel();
			}
		});
		TextView tv_dialog_sure = (TextView) window.findViewById(R.id.tv_sure_sure);
		tv_dialog_sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				submitData();
				alertDialog.cancel();
			}
		});
	}
}
