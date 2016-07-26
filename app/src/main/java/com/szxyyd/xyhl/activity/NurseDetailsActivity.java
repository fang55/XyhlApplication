package com.szxyyd.xyhl.activity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.http.BitmapCache;
import com.szxyyd.xyhl.http.VolleyRequestUtil;
import com.szxyyd.xyhl.inf.VolleyListenerInterface;
import com.szxyyd.xyhl.modle.DetailFile;
import com.szxyyd.xyhl.modle.JsonBean;
import com.szxyyd.xyhl.modle.Nurse;
import com.szxyyd.xyhl.modle.NurseList;
import com.szxyyd.xyhl.modle.NurseTrain;
import com.szxyyd.xyhl.utils.CommUtils;
import com.szxyyd.xyhl.view.MenuPopupWindow;
import com.szxyyd.xyhl.view.RoundImageView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 护理师详情
 * @author fq
 *
 */
public class NurseDetailsActivity extends Activity implements OnClickListener{
	private RoundImageView iv_people;
	private Button btn_order;
	private Button btn_collect;  //收藏
	private Button btn_navigation;
	private LinearLayout ll_train;
	private LinearLayout ll_evaluate;
	private LinearLayout ll_image;
	private TextView tv_live;
	private LinearLayout ll_certificate;//存储证书
	private LinearLayout ll_star;//评分数
	private LinearLayout ll_video; //视频
	private WebView wv_video;
	private RelativeLayout rl_start;
	private String nurseId;
	private NurseList nurse;
	private RequestQueue mQueue;
	private ImageLoader mImageLoader;
	private List<String> cerList = new ArrayList<>(); //存储证书
	private List<String> cerImage = new ArrayList<>(); //存储生活照
	private int fvrId; //收藏id
	private String videoUrl = null; //视频地址
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case Constant.LIST:
					List<DetailFile> fileList = (List<DetailFile>) msg.obj;
					Log.e("NurseDetailsActivity","fileList.size()=="+fileList.size());
					if(fileList.size() != 0){
						for(DetailFile detailFile : fileList){
							if(detailFile.getType().equals("6000")){
								cerImage.add(detailFile.getFiles());
							}
						}
						showImageLive(cerImage);
					}
					break;
				case Constant.SUCCEED:
					List<Nurse>  nurseList = (List<Nurse>) msg.obj;
					for(Nurse nurses: nurseList){
						fvrId = nurses.getFvrid();
						if(fvrId == 0){
							btn_collect.setBackgroundResource(R.drawable.iv_nocollect);
						}else{
							Constant.collectId = String.valueOf(fvrId);
							btn_collect.setBackgroundResource(R.drawable.iv_collect);
						}
					//	videoUrl = nurses.getWorkvideo();
						try {
							videoUrl = Constant.workvideo + nurses.getWorkvideo();
							Log.e("handler","handler--videoUrl==="+videoUrl);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				case Constant.SERVICE_LEVEL:
					List<NurseTrain> trainList = (List<NurseTrain>) msg.obj;
					showTrainData(trainList);
					break;
				case Constant.SUBITM:
					List<NurseList> nurseLists = (List<NurseList>) msg.obj;
					showEvaluateData(nurseLists);
					break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		nurse = (NurseList) getIntent().getSerializableExtra("nurse");
		initView();
		initEvent();
		showData();
		mQueue = BaseApplication.getRequestQueue();
		mImageLoader = new ImageLoader(mQueue, new BitmapCache());
		showStar();
		showImae();
		lodeDetailsData();
		lodeEvaluateData();
		lodeTrainData();
		showVideoData();
		BaseApplication.getInstance().addActivity(this);
	}

	private void initView(){
	   ((TextView)findViewById(R.id.tv_title)).setText("护理师详情");
	   iv_people = (RoundImageView) findViewById(R.id.iv_people);
	   Button btn_back = (Button) findViewById(R.id.btn_back);
	   btn_back.setOnClickListener(this);
	   btn_collect = (Button) findViewById(R.id.btn_collect);
	   btn_order = (Button) findViewById(R.id.btn_order);
	   ll_train = (LinearLayout) findViewById(R.id.ll_train);
		ll_evaluate = (LinearLayout) findViewById(R.id.ll_evaluate);
	   btn_navigation = (Button) findViewById(R.id.btn_navigation);
	   ll_image = (LinearLayout) findViewById(R.id.ll_image);
	   ll_certificate = (LinearLayout) findViewById(R.id.ll_certificate);
	   ll_star = (LinearLayout) findViewById(R.id.ll_star);
	   ll_video = (LinearLayout) findViewById(R.id.ll_video);
	   tv_live = (TextView) findViewById(R.id.tv_live);
   }
	private void initEvent(){
		btn_order.setOnClickListener(this);
		btn_collect.setOnClickListener(this);
		btn_navigation.setOnClickListener(this);
	}
	private void showStar(){
		ll_star.removeAllViews();
		int leng = nurse.getSrvscore() > 5 ? 5 : nurse.getSrvscore();
		for(int i = 0; i < leng;i++){
			ImageView imageView = new ImageView(this);
			imageView.setBackground(this.getResources().getDrawable(R.drawable.star));
			ll_star.addView(imageView);
		}
	}
	private void showImae() {
			ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv_people, 0, R.drawable.teach);
			mImageLoader.get(Constant.nurseImage + nurse.getIcon(), listener);
	}
	private void showImageLive(List<String> cerImage){
		ll_image.removeAllViews();
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(20,10,10,10);
		for(int i = 0;i<cerImage.size();i++){
			ImageView imageView = new ImageView(this);
			ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, 0, R.drawable.teach);
			Log.e("getDataSource","Constant.lifePic=="+Constant.lifePic + cerImage.get(i));
			mImageLoader.get(Constant.lifePic + cerImage.get(i), listener,200,200);
			ll_image.addView(imageView,layoutParams);
		}
	}

	/**
	 * 显示个人信息
	 */
	private void showData(){
		nurseId = nurse.getId();
		int nurseAge = CommUtils.showAge(nurse.getBirth());
		((TextView)findViewById(R.id.tv_name)).setText(nurse.getName());
		((TextView)findViewById(R.id.tv_citys)).setText(nurse.getCity());
		((TextView)findViewById(R.id.tv_age)).setText(nurseAge+"岁");
		((TextView)findViewById(R.id.tv_experience)).setText("服务次数"+" " + nurse.getSrvnum());
		((TextView)findViewById(R.id.tv_health)).setText(nurse.getSvrid());
		((TextView)findViewById(R.id.tv_detail_age)).setText(nurseAge+"岁");
		((TextView)findViewById(R.id.tv_detail_city)).setText(nurse.getCity());
		((TextView)findViewById(R.id.tv_detail_marriage)).setText(nurse.getMarried());
		((TextView)findViewById(R.id.tv_detail_nation)).setText(nurse.getNation());
		((TextView)findViewById(R.id.tv_detail_type)).setText(nurse.getSvrid());
		((TextView)findViewById(R.id.tv_detail_grade)).setText(nurse.getLvl());
	}

	/**
	 * 显示视频
	 */
	private void showVideoData(){
		ll_video.removeAllViews();
		View view = LayoutInflater.from(this).inflate(R.layout.view_video,null,false);
		wv_video = (WebView) view.findViewById(R.id.wv_video);
		rl_start = (RelativeLayout) view.findViewById(R.id.rl_start);
		Button btn_start = (Button) view.findViewById(R.id.btn_start);
		btn_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				rl_start.setVisibility(View.INVISIBLE);
				playVideo(videoUrl);
			}
		});
		ll_video.addView(view);

	}
	private void playVideo(String strPath) {
		wv_video.getSettings().setJavaScriptEnabled(true) ;
		wv_video.loadUrl(strPath) ;
		//获取WebSettings对象,设置缩放
		wv_video.getSettings().setUseWideViewPort(true) ;
		wv_video.getSettings().setLoadWithOverviewMode(true) ;
		wv_video.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.d("访问网址：",url) ;
				view.loadUrl(url) ;
				return true ;
			}
		}) ;
	}

	/**
	 * 获取培训经历
	 */
   private void lodeTrainData(){
	   Map<String, String> map = new HashMap<String, String>();
	   map.put("nurseid",nurse.getNursvrid());
	   VolleyRequestUtil.newInstance().GsonPostRequest(this,Constant.nurseTrainListUrl,"train" ,map,new TypeToken<JsonBean>(){},
			   new Response.Listener<JsonBean>() {
				   @Override
				   public void onResponse(JsonBean jsonBean) {
					   List<NurseTrain> trainList = jsonBean.getNurseTrain();
					   if(trainList.size() == 0){
						   return;
					   }
					   Message message = new Message();
					   message.what = Constant.SERVICE_LEVEL;
					   message.obj = trainList;
					   handler.sendMessage(message);
				   }
			   }, new Response.ErrorListener() {
				   @Override
				   public void onErrorResponse(VolleyError error) {
				   }
			   });
   }
	/**
	 * 显示培训经历
	 */
		private void showTrainData(List<NurseTrain> trainList){
		ll_train.removeAllViews();
		for(int i = 0;i<trainList.size();i++){
			View view = LayoutInflater.from(this).inflate(R.layout.listview_item_train, null, false);
			String time1 = CommUtils.showData(trainList.get(i).getAtrec1());
			String time2 = CommUtils.showData(trainList.get(i).getAtrec2());
			TextView tv_time = (TextView) view.findViewById(R.id.tv_train_time);
			tv_time.setText(time1 + "-" + time2);
			TextView tv_content = (TextView) view.findViewById(R.id.tv_train_content);
			tv_content.setText(trainList.get(i).getTitle());
			ll_train.addView(view);
		}
	}
	/**
	 * 获取护理师详情
	 */
   private void lodeDetailsData(){
	   Map<String, String> map = new HashMap<String, String>();
	   map.put("nurid",nurse.getNursvrid());
	  VolleyRequestUtil.newInstance().GsonPostRequest(this,Constant.nurseDetailUrl,"detail" ,map,new TypeToken<JsonBean>(){},
			   new Response.Listener<JsonBean>() {
				   @Override
				   public void onResponse(JsonBean jsonBean) {
					   List<DetailFile> levels = jsonBean.getFiles();
					   List<Nurse> nurses = jsonBean.getNurse();
					   if(null != levels) {
						   Message message = new Message();
						   message.what = Constant.LIST;
						   message.obj = levels;
						   handler.sendMessage(message);
					   }
					   if(null != nurses){
						   Message message = new Message();
						   message.what = Constant.SUCCEED;
						   message.obj = nurses;
						   handler.sendMessage(message);
					   }
				   }
			   }, new Response.ErrorListener() {
				   @Override
				   public void onErrorResponse(VolleyError error) {
				   }
			   });
   }

	/**
	 * 获取护理师评论列表
	 */
	private void lodeEvaluateData(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("nurseid",nurse.getNursvrid());
		VolleyRequestUtil volley = new VolleyRequestUtil();
		volley.RequestPost(this,Constant.nurseCmtAllUrl,"cmt",map,new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
			@Override
			public void onSuccess(String result) {
				Log.e("NurseDetailsActivity", "addCollect()=="+result);
				parserData(result,"cmt");
			}
			@Override
			public void onError(VolleyError error) {

			}
		});
	}
	/**
	 * 解析护理师评论
	 */
	private void parserData(String  result,String type){
		try {
			JSONObject object = new JSONObject(result);
			if(type.equals("cmt")){
				String jsonData = object.getString("nurse");
				Type listType = new TypeToken<LinkedList<NurseList>>() {}.getType();
				Gson gson = new Gson();
				List<NurseList> list = gson.fromJson(jsonData, listType);
				Message message = new Message();
				message.what = Constant.SUBITM;
				message.obj = list;
				handler.sendMessage(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void showEvaluateData(List<NurseList> list){
		ll_evaluate.removeAllViews();
		for(int i = 0;i<list.size();i++){
			View view = getLayoutInflater().inflate(R.layout.listview_item_evaluate,null,false);
			TextView tv_evcontent = (TextView) view.findViewById(R.id.tv_evcontent);
			tv_evcontent.setText(list.get(i).getContent());
			ll_evaluate.addView(view);
		}
	}
	/**
	 * 添加收藏
	 */
	private void addCollect(String type){
		// cstid  （客户id）  svrid   （服务id） nurseid   （护理师id） 80001953 1000 1294016
		Log.e("NurseDetailsActivity", "addCollect()--cstid=="+Constant.cstId);
		Log.e("NurseDetailsActivity", "addCollect()--svrid=="+Constant.svrId);
		Log.e("NurseDetailsActivity", "addCollect()--nurseid=="+Constant.nurseId);
		String url = null;
		Map<String, String> map = new HashMap<String, String>();
        if(type.equals("add")){
			url = Constant.cstFvrnurAddUrl;
			map.put("cstid",Constant.cstId);
			map.put("svrid",String.valueOf(Constant.svrId));
			map.put("nurseid",nurse.getNursvrid());
		}else if(type.equals("delect")){
			url = Constant.cstFvrnurDelUrl;
			map.put("id",Constant.collectId);
		}
		VolleyRequestUtil volley = new VolleyRequestUtil();
		volley.RequestPost(this,url,"add",map,new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
			@Override
			public void onSuccess(String result) {
				Log.e("NurseDetailsActivity", "addCollect()=="+result);
				Constant.collectId = result;//4
			}
			@Override
			public void onError(VolleyError error) {

			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_back:
				finish();
				break;
			case R.id.btn_order:
				Intent intent = new Intent(this,OrderNurseActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("nurse", nurse);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.btn_collect:
				if(Constant.isCollect){
					btn_collect.setBackgroundResource(R.drawable.iv_nocollect);
					Constant.isCollect = false;
					addCollect("delect");
					Toast.makeText(this,"已取消",Toast.LENGTH_SHORT).show();
				}else{
					btn_collect.setBackgroundResource(R.drawable.iv_collect);
					Constant.isCollect = true;
					addCollect("add");
					Toast.makeText(this,"已收藏",Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btn_navigation:
				MenuPopupWindow popupWindow = new MenuPopupWindow(NurseDetailsActivity.this);
				popupWindow.initPopupWindow(btn_navigation,124,372);
				popupWindow.setOutsideTouchable(true);
				break;
			default:
				break;
		}
	}

}
