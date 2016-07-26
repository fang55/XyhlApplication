package com.szxyyd.xyhl.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.adapter.InfiniteLoopViewPagerAdapter;
import com.szxyyd.xyhl.adapter.ServiceLocationAdapter;
import com.szxyyd.xyhl.adapter.ViewPagerAdapter;
import com.szxyyd.xyhl.http.VolleyRequestUtil;
import com.szxyyd.xyhl.inf.VolleyListenerInterface;
import com.szxyyd.xyhl.modle.JsonBean;
import com.szxyyd.xyhl.modle.Level;
import com.szxyyd.xyhl.modle.PriceLvl;
import com.szxyyd.xyhl.modle.Reladdr;
import com.szxyyd.xyhl.modle.SelectBtn;
import com.szxyyd.xyhl.modle.SvrCal;
import com.szxyyd.xyhl.utils.CommUtils;
import com.szxyyd.xyhl.view.EditDialog;
import com.szxyyd.xyhl.view.InfiniteLoopViewPager;
import com.szxyyd.xyhl.view.ShowTimeDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 健康护理师
 */
public class HealthNurseActivity extends Activity implements OnClickListener{
	private LinearLayout ll_level;  //服务星级
	private GridLayout gl; //服务选项
	private GridLayout gl_people; //服务人员
	private TextView tv_content; // 服务内容
	private TextView tv_money; //价钱
	private LinearLayout ll_notaddr; //未有服务地址
	private RelativeLayout rl_time; //服务时间
	private RelativeLayout rl_location; // 服务地址
	private RelativeLayout rl_addr;
	private TextView tv_addr_name;
	private TextView tv_addr_phone;
	private TextView tv_addr;
	public TextView tv_date; //服务时间
	public EditText et_remark; //备忘信息
	private ListView lvAddr;
	private InfiniteLoopViewPager viewPager;
	private int[] imageViewIds;
	private ImageView[] imageViews;
	private InfiniteLoopViewPagerAdapter pagerAdapter;
	private int sleepTime = 3000;
	public boolean isRun = false;
	public boolean isDown = false;
	private LinearLayout mBottonLayout;
	private ImageView imgCur;
	public  int svrid;
	private String orderTitle;
	public String orderTime;
	private List<PriceLvl> listPriceLvl;
	private List<Reladdr> listAddr;
	private ServiceLocationAdapter adapter;
	private AlertDialog alertDialog;
	private ReceiveBroadCast receiveBroadCast;  //广播实例
	public List<String> nameData = new ArrayList<>(); //个性化的多选
	private int baseMoney;
	private String levelData ;  //级别选择
	private String levelId ;   //级别选择Id
	private DbUtils dbUtils;
	private  SelectBtn selectBtn = new SelectBtn();
	private List<String> serviceList = new ArrayList<String>();
	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
				case 0:
					viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
					if (isRun && !isDown) {
						this.sendEmptyMessageDelayed(0, sleepTime);
					}
					break;
				case 1:
					if (isRun && !isDown) {
						this.sendEmptyMessageDelayed(0, sleepTime);
					}
					break;
				case Constant.SERVICE_LEVEL:
					listPriceLvl = (List<PriceLvl>) msg.obj;
					if(listPriceLvl.size() != 0 && listPriceLvl != null) {
						ll_level.setVisibility(View.VISIBLE);
						showPriceLvl(listPriceLvl);
                        try {
							dbUtils.dropTable(PriceLvl.class);
                            dbUtils.saveAll(listPriceLvl);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
					}
					break;
				case Constant.SERVICE_OPTION_LEVEL:
					List<Level> list = (List<Level>) msg.obj;
					showLevelOption(list);
					break;
				case Constant.SERVICE_PEOPLE:
					List<SvrCal> list2 = (List<SvrCal>) msg.obj;
					showServicePeople(list2);
					break;
				case  Constant.SERVICE_DEFADDR:
					List<Reladdr> deflist = (List<Reladdr>) msg.obj;
					if(deflist.size() == 0){
						ll_notaddr.setVisibility(View.VISIBLE);
					}else{
						rl_addr.setVisibility(View.VISIBLE);
						ll_notaddr.setVisibility(View.GONE);
						String name = deflist.get(0).getName().toString();
						String phone = deflist.get(0).getMobile().toString();
						String addr = deflist.get(0).getAddr().toString();
						tv_addr_name.setText(name);
						tv_addr_phone.setText(phone);
						tv_addr.setText(addr);
					}
					break;
				case Constant.SERVICE_ADDR_LIST:
					listAddr = (List<Reladdr>) msg.obj;
					showAddressList();
					break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		svrid = getIntent().getIntExtra("svrid",-1);
		orderTitle = getIntent().getStringExtra("title");
		initView();
		initViewPagerData();
		loadSvrLevelData();
		loadLevelOptionData();
		loadServiceData();
		showSharedPreferencesfAddr();
		// 注册广播接收
		receiveBroadCast = new ReceiveBroadCast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.BROAD_ADD_ACTION);    //只有持有相同的action的接受者才能接收此广播
		registerReceiver(receiveBroadCast, filter);
		dbUtils = BaseApplication.getdbUtils();
		BaseApplication.getInstance().addActivity(this);
	}
   private void initView(){
	   ((TextView)findViewById(R.id.tv_title)).setText(orderTitle);
	   ll_level = (LinearLayout) findViewById(R.id.ll_level);
	   gl = (GridLayout) findViewById(R.id.gl);
	   gl_people = (GridLayout) findViewById(R.id.gl_people);
	   tv_content = (TextView) findViewById(R.id.tv_content);
	   tv_money= (TextView) findViewById(R.id.tv_money);
	   et_remark = (EditText)findViewById(R.id.et_remark);
	   tv_date = (TextView) findViewById(R.id.tv_date);
	   tv_date.setText(CommUtils.showToDay());
	   tv_addr_name = (TextView)findViewById(R.id.tv_addr_name);
	   tv_addr_phone = (TextView)findViewById(R.id.tv_addr_phone);
	   tv_addr = (TextView)findViewById(R.id.tv_addr);
	   rl_time = (RelativeLayout) findViewById(R.id.rl_time);
	   ll_notaddr = (LinearLayout) findViewById(R.id.ll_notaddr);
	   rl_location = (RelativeLayout) findViewById(R.id.rl_location);
	   rl_addr = (RelativeLayout) findViewById(R.id.rl_addr);
	   Button btn_back = (Button) findViewById(R.id.btn_back);
	   Button btn_next = (Button) findViewById(R.id.btn_next);
	   btn_back.setOnClickListener(this);
	   btn_next.setOnClickListener(this);
	   rl_location.setOnClickListener(this);
	   rl_time.setOnClickListener(this);
   }
	/**
	 * 显示ViewPager的内容
	 */
	private void initViewPagerData(){
		imageViewIds = new int[] { R.drawable.def_heaimage1,R.drawable.def_heaimage1,R.drawable.def_heaimage1,R.drawable.def_heaimage1 };
		imageViews = new ImageView[imageViewIds.length];
		for (int i = 0; i < imageViewIds.length; i++) {
			imageViews[i] = new ImageView(this);
			imageViews[i].setImageResource(imageViewIds[i]);
		}
		viewPager = (InfiniteLoopViewPager) findViewById(R.id.viewPager);
		mBottonLayout = (LinearLayout) findViewById(R.id.ll_group);
		pagerAdapter = new InfiniteLoopViewPagerAdapter(new ViewPagerAdapter(imageViews));
		viewPager.setInfinateAdapter(this, mHandler, pagerAdapter);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		setFaceCurPage(0);
	}
	/**
	 * 更新当前页码
	 */
	public void setFaceCurPage(int page) {
		mBottonLayout.removeAllViews();
		page=page%imageViews.length;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(10, 0, 0, 0);
		for (int i = 0; i < imageViews.length; i++) {
			imgCur = new ImageView(HealthNurseActivity.this);
			imgCur.setBackgroundResource(R.drawable.group_select);
			if (page != i) {
				imgCur.setBackgroundResource(R.drawable.group_not);
			}
			mBottonLayout.addView(imgCur,lp);
		}
	}
	/**
	 * 本地获取地址
	 */
	private void showSharedPreferencesfAddr(){
		Log.e("showSharedPreferencesfAddr","Constant.cstId=="+Constant.cstId);
		SharedPreferences preferences = getSharedPreferences(Constant.cstId+"defaddr", Context.MODE_PRIVATE);
		String name = preferences.getString("name", "");
		String phone = preferences.getString("mobile", "");
		String addr = preferences.getString("addr", "");
		if(name.length() > 0){
			ll_notaddr.setVisibility(View.GONE);
			rl_addr.setVisibility(View.VISIBLE);
			tv_addr_name.setText(name);
			tv_addr_phone.setText(phone);
			tv_addr.setText(addr);
		}else{
			loadDefAddrData();
		}
	}
	class ReceiveBroadCast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			showSharedPreferencesfAddr();
		}
	}
	/**
	 * 获取级别
	 */
	private void loadSvrLevelData(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("svrid",String.valueOf(svrid));
		map.put("city","440300");
		VolleyRequestUtil.newInstance().GsonPostRequest(this,Constant.getPriceAndLvUrl,"price" ,map,new TypeToken<JsonBean>(){},
				new Response.Listener<JsonBean>() {
					@Override
					public void onResponse(JsonBean jsonBean) {
						List<PriceLvl> levels = jsonBean.getYxvPriceLvl();
						Message message = new Message();
						message.what = Constant.SERVICE_LEVEL;
						message.obj = levels;
						mHandler.sendMessage(message);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
	}
	/**
	 * 获取级别选项数据
	 */
	private void loadLevelOptionData(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("svrid",String.valueOf(svrid));
		VolleyRequestUtil.newInstance().GsonPostRequest(this,Constant.funUrl,"svrlevel" ,map,new TypeToken<JsonBean>(){},
				new Response.Listener<JsonBean>() {
					@Override
					public void onResponse(JsonBean jsonBean) {
						List<Level> svrFun = jsonBean.getSvrFun();
						Message message = new Message();
						message.what = Constant.SERVICE_OPTION_LEVEL;
						message.obj = svrFun;
						mHandler.sendMessage(message);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
	}
	/**
	 * 获取服务人员数据
	 */
	private void loadServiceData(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("svrid",String.valueOf(svrid));
		VolleyRequestUtil.newInstance().GsonPostRequest(this,Constant.nurseServiceUrl,"service" ,map,new TypeToken<JsonBean>(){},
				new Response.Listener<JsonBean>() {
					@Override
					public void onResponse(JsonBean jsonBean) {
						List<SvrCal> svrCal = jsonBean.getSvrCal();
						Message message = new Message();
						message.what = Constant.SERVICE_PEOPLE;
						message.obj = svrCal;
						mHandler.sendMessage(message);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
	}
	private String getIdxsList(){
		String result = "";
		for(int i = 0; i < serviceList.size();i++){
			StringBuilder sb = new StringBuilder();
			result += sb.append("&idx=").append(serviceList.get(i)).toString();
			Log.e("getIdxsList", "result=="+result);
		}
		return result;
	}
	/**
	 * 改变价格选项
	 */
	private void loadGetPriceData(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("idx",getIdxsList());
		map.put("svrid",String.valueOf(svrid));
		map.put("lvl",levelId);
		map.put("city",Constant.cityId);
		Log.e("HealthNurseActivity","id=="+getIdxsList());
		Log.e("HealthNurseActivity","svrid=="+String.valueOf(svrid));
		Log.e("HealthNurseActivity","lvl=="+levelId);
		Log.e("HealthNurseActivity","city=="+Constant.cityId);
		String url = Constant.getPriceUrl + "&svrid=" + String.valueOf(svrid) + "&lvl=" + levelId
				+"&city="+"440300"+getIdxsList();
		VolleyRequestUtil.RequestPost(this, url, "price", null, new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
			@Override
			public void onSuccess(String result) {
				Log.e("HealthNurseActivity","result=="+result);
                  tv_money.setText(result);
			}
			@Override
			public void onError(VolleyError error) {
			}
		});
	}
	/**
	 * 加载默认地址
	 */
	private void loadDefAddrData(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("cstid",Constant.cstId);
		VolleyRequestUtil.newInstance().GsonPostRequest(this,Constant.defAdressUrl,"defadress" ,map,new TypeToken<JsonBean>(){},
				new Response.Listener<JsonBean>() {
					@Override
					public void onResponse(JsonBean jsonBean) {
						List<Reladdr> list = jsonBean.getReladdr();
						Message message = new Message();
						message.what = Constant.SERVICE_DEFADDR;
						message.obj = list;
						mHandler.sendMessage(message);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
	}
	/**
	 * 获取服务地址列表
	 */
	private void loadAddrListData(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("cstid",Constant.cstId);
		VolleyRequestUtil.newInstance().GsonPostRequest(this,Constant.locationUrl,"adresslist" ,map,new TypeToken<JsonBean>(){},
				new Response.Listener<JsonBean>() {
					@Override
					public void onResponse(JsonBean jsonBean) {
						List<Reladdr> list = jsonBean.getReladdr();
						Message message = new Message();
						message.what = Constant.SERVICE_ADDR_LIST;
						message.obj = list;
						mHandler.sendMessage(message);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
	}
	/**
	 *删除地址
	 */
	private void delectAddrData(int id,String type){
		String url = null;
		Map<String, String> map = new HashMap<String, String>();
		if(type.equals("delect")){
			url = Constant.delAddresUrl;
			map.put("id",String.valueOf(id));
		}else if(type.equals("checked")){
			url = Constant.saveAddressByIdUrl;
			map.put("id",String.valueOf(id));
			map.put("cstid",Constant.cstId);
		}
		VolleyRequestUtil.newInstance().GsonPostRequest(this,url,"sumbit" ,map,new TypeToken<JsonBean>(){},
				new Response.Listener<JsonBean>() {
					@Override
					public void onResponse(JsonBean jsonBean) {

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
	}
	/**
	 * 显示服务地址列表
	 */
	private void showAddressList(){
		if(listAddr.size()!= 0 && listAddr != null){
			adapter = new ServiceLocationAdapter(this, listAddr, new ServiceLocationAdapter.selectOnclickListener() {
				@Override
				public void selectDelect(int position,String type) {
					int id = listAddr.get(position).getId();
					Reladdr reladdr = listAddr.get(position);
					if(type.equals("edit")){  //编辑
						EditDialog dialog = new EditDialog(HealthNurseActivity.this,reladdr,"edit",position);
						dialog.init();
						dialog.setOnFinshClickListener(new EditDialog.onFinshClickListener() {
							@Override
							public void onfinsh() {
								//  loadAddrListData("list","0");
							}
						});
						alertDialog.cancel();
					}else if(type.equals("checked")){  //修改默认地址
						delectAddrData(id,"checked");
					}
					else{
						delectAddrData(id,"delect");
						listAddr.remove(position);
						Toast.makeText(BaseApplication.getInstance(),"已删除",Toast.LENGTH_SHORT).show();
						alertDialog.cancel();
					}

				}
			});
			lvAddr.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}
	/**
	 * 显示选择星级数据
	 */
	private void showSelectData(List<PriceLvl> list,int index){
				tv_content.setText(list.get(index).getContent());
		        tv_money.setText(list.get(index).getPrice()+"");
				levelData = list.get(index).getName();
				baseMoney = list.get(index).getPrice();
				Constant.lvlTitle = list.get(index).getName();
				Constant.lvlId = list.get(index).getLvl();
				levelId = list.get(index).getLvl();
	}
	private void showPriceLvl(List<PriceLvl> list){
		ll_level.removeAllViews();
		Button btn = null;
		for(int i = 0;i<list.size();i++){
			View view = LayoutInflater.from(this).inflate(R.layout.view_level, null);
			btn = (Button) view.findViewById(R.id.btn_level1);
			btn.setTag(i+1);
			btn.setText(list.get(i).getName());
			if(Integer.parseInt(btn.getTag().toString()) == 1) {
				btn.setBackgroundResource(R.drawable.border_blue);
				btn.setTextColor(getResources().getColor(R.color.tv_login));
				selectBtn.setId(1);
				selectBtn.setView(btn);
			}
			btn.setOnClickListener(new btnListener(btn,list));
			ll_level.addView(view);
		}
		showSelectData(list,0);
	}
	class btnListener implements View.OnClickListener {
		//定义一个 Button 类型的变量
		private Button btn;
		private List<PriceLvl> list;
		private btnListener(Button btn,List<PriceLvl> list) {
			this.btn = btn;//将引用变量传递给实体变量
			this.list = list;
		}
		@Override
		public void onClick(View view) {
			int tag = Integer.parseInt(btn.getTag().toString());
			if(selectBtn.getId() != tag){
				if(selectBtn.getView() != null){
					selectBtn.getView().setBackgroundResource(R.drawable.border_gray);
					selectBtn.getView().setTextColor(getResources().getColor(R.color.line));
				}
				selectBtn.setId(tag);
				selectBtn.setView(btn);
				btn.setBackgroundResource(R.drawable.border_blue);
				btn.setTextColor(getResources().getColor(R.color.tv_login));
				showSelectData(list, (tag-1));
			}
		}
	}
	/**
	 * 添加服务时间
	 */
	private void addSerTime(){
		ShowTimeDialog dialog = new ShowTimeDialog(this);
		dialog.setSelectOnclick(new ShowTimeDialog.onClick() {
			@Override
			public void dwonDate(int year, int month, String day,String time) {
				tv_date.setText(year+"-"+month+"-"+day+" "+time);
				orderTime = (year+"-"+month+"-"+day);
			}

		});
		dialog.init();
	}
	/**
	 * 切换地址
	 *
	 */
	private void showLocationDialog(){
		loadAddrListData();
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_order_location, null);
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(view);
		Button btn_back = (Button) window.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				alertDialog.cancel();
			}
		});
		lvAddr = (ListView) view.findViewById(R.id.lv_order);
		lvAddr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				rl_addr.setVisibility(View.VISIBLE);
				ll_notaddr.setVisibility(View.GONE);
				String name = listAddr.get(position).getName().toString();
				String phone = listAddr.get(position).getMobile().toString();
				String addr = listAddr.get(position).getAddr().toString();
				tv_addr_name.setText(name);
				tv_addr_phone.setText(phone);
				tv_addr.setText(addr);
				alertDialog.cancel();
			}
		});
		Button btn_add = (Button) view.findViewById(R.id.btn_add);
		btn_add.setOnClickListener(new View.OnClickListener() { //添加服务地址

			@Override
			public void onClick(View view) {
				addSerAddr();
				alertDialog.cancel();
			}
		});
	}
	/**
	 * 添加服务地址
	 */
	private void addSerAddr() {
		EditDialog dialog = new EditDialog(this, null, "add",0);
		dialog.init();
		dialog.setOnFinshClickListener(new EditDialog.onFinshClickListener() {
			@Override
			public void onfinsh() {

			}
		});
	}
	/**
	 * 显示级别选项数据
	 * @param data
	 */
	private void showLevelOption(final List<Level> data){
		gl.removeAllViews();
		for(int i = 0;i<data.size();i++){
			View view = getLayoutInflater().inflate(R.layout.include_border,null,false);
			CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
			cb.setText(data.get(i).getName().toString());
			cb.setTag(data.get(i).getIdx());
			cb.setTextColor(this.getResources().getColor(R.color.line));
			cb.setChecked(true);
			cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
											 boolean isChecked) {
					if(isChecked){
						Constant.listLevel.add(buttonView.getTag().toString());
					}else{
						Constant.listLevel.remove(buttonView.getTag().toString());
					}
				}
			});
			gl.addView(view);
		}
	}
	/**
	 * 显示服务人员的数据
	 */
	private void showServicePeople(final List<SvrCal> data){
		gl_people.removeAllViews();
		for(int i = 0;i<data.size();i++){
			View view = getLayoutInflater().inflate(R.layout.include_border, null);
			final CheckBox cb  = (CheckBox) view.findViewById(R.id.cb);
			final SvrCal svrCal = data.get(i);
			cb.setText(data.get(i).getName().toString());
			cb.setTag(data.get(i).getId());
			cb.setTextSize(16);
			cb.setTextColor(this.getResources().getColor(R.color.line));
			cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
											 boolean isChecked) {
					if(isChecked){
						Constant.listpople.add(buttonView.getTag().toString());
						serviceList.add(svrCal.getIdx());
						nameData.add(svrCal.getName());
						loadGetPriceData();
					}else{
						Constant.listpople.remove(buttonView.getTag().toString());
						serviceList.remove(svrCal.getIdx());
						nameData.remove(svrCal.getName());
						tv_money.setText(baseMoney+"");
					}
				}
			});
			gl_people.addView(view);
		}
	}
	/**
	 * 动作监听器，可异步加载图片
	 */
	private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
		public static final int SCROLL_STATE_IDLE = 0;
		public static final int SCROLL_STATE_DRAGGING = 1;
		public static final int SCROLL_STATE_SETTLING = 2;
		@Override
		public void onPageScrolled(int i, float v, int i1) {
		}
		@Override
		public void onPageSelected(int position) {
			setFaceCurPage(position);
		}
		@Override
		public void onPageScrollStateChanged(int position) {
			switch (position) {
				case SCROLL_STATE_IDLE:
					break;
				case SCROLL_STATE_DRAGGING:
					break;
				case SCROLL_STATE_SETTLING:
					break;
			}
		}
	}

	/**
	 * 缓存服务内容
	 */
	public  void sharedSerContent(){
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("sercontent", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("level", orderTitle+ " "+levelData);
		StringBuilder sb = new StringBuilder();
		for(String str : nameData){
			sb.append(" ").append(str).toString();
		}
		String result = sb.toString();
		editor.putString("serpeople", result);
		if(orderTime == null){
			editor.putString("sertime", tv_date.getText().toString());
		}else{
			editor.putString("sertime", orderTime);
		}
		editor.putString("note",  et_remark.getText().toString());
		editor.putString("price", tv_money.getText().toString());
		editor.putString("name",  tv_addr_name.getText().toString());
		editor.putString("mobile", tv_addr_phone.getText().toString());
		editor.putString("addr", tv_addr.getText().toString());

		editor.commit();
	}

	@Override
	public void onClick(View view) {
        switch (view.getId()){
			case R.id.btn_back:
				finish();
				break;
			case R.id.btn_next: //快速查找
				sharedSerContent();
				Intent intent = new Intent(HealthNurseActivity.this,NurselistActivity.class);
				startActivity(intent);
				break;
			case R.id.rl_location: //服务地址
				showLocationDialog();
				break;
			case R.id.rl_time:  //选址时间
				addSerTime();
				break;
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiveBroadCast);
	}
}
