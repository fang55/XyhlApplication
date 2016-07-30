package com.szxyyd.mpxyhl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.adapter.EvaluateAdapter;
import com.szxyyd.mpxyhl.http.BitmapCache;
import com.szxyyd.mpxyhl.http.VolleyRequestUtil;
import com.szxyyd.mpxyhl.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhl.modle.DetailFile;
import com.szxyyd.mpxyhl.modle.JsonBean;
import com.szxyyd.mpxyhl.modle.NurseList;
import com.szxyyd.mpxyhl.modle.NurseTrain;
import com.szxyyd.mpxyhl.utils.CommUtils;
import com.szxyyd.mpxyhl.view.RoundImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by jq on 2016/7/5.
 */
public class NurseDetailsActivity extends Activity implements View.OnClickListener{
    private RoundImageView image_theach;
    private LinearLayout ll_certificate;//存储证书
    private LinearLayout ll_star;//评分数
    private LinearLayout ll_image; //生活照
    private LinearLayout ll_train; //培训经历
    private LinearLayout ll_video; //视频
    private WebView wv_video;  //播放视频
    private Button btn_start;
    private Button btn_collect; //收藏
    private ListView lv_evaluate;
    private RelativeLayout ll_evaMore;
    private NurseList nurse;
    private List<String> cerImage = new ArrayList<>(); //存储生活照
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    private String videoUrl;
    private boolean isCollect = false;
    private  String fvrid;
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
                    List<NurseList> nurseLists = (List<NurseList>) msg.obj;
                    if(nurseLists.size() != 0){
                        if(nurseLists.size() > 3){
                            ll_evaMore.setVisibility(View.VISIBLE);
                        }
                        lv_evaluate.setVisibility(View.VISIBLE);
                        showEvaluateData(nurseLists);
                    }else{
                        ll_evaMore.setVisibility(View.GONE);
                        lv_evaluate.setVisibility(View.GONE);
                    }
                    break;
                case Constant.SERVICE_LEVEL: ////1294110
                    List<NurseTrain> trainList = (List<NurseTrain>) msg.obj;
                    showTrainData(trainList);
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nurse = (NurseList) getIntent().getSerializableExtra("nurse");
        setContentView(R.layout.activity_details);
        mQueue = BaseApplication.getRequestQueue();
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
        initView();
        showData();
        showStar();
        lodeDetailsData();
        lodeEvaluateData();
        lodeTrainData();
        showVideoData();
        BaseApplication.getInstance().addActivity(this);
    }
    private void initView(){
        ((TextView)findViewById(R.id.tv_title)).setText("护理师详情");
        image_theach = (RoundImageView) findViewById(R.id.image_theach);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        Button btn_order = (Button) findViewById(R.id.btn_order);
        btn_collect = (Button) findViewById(R.id.btn_collect);
        ll_certificate = (LinearLayout) findViewById(R.id.ll_certificate);
        ll_star = (LinearLayout) findViewById(R.id.ll_star);
        ll_train = (LinearLayout) findViewById(R.id.ll_train);
        ll_image = (LinearLayout) findViewById(R.id.ll_image);
        ll_evaMore = (RelativeLayout) findViewById(R.id.ll_evaMore);
        lv_evaluate = (ListView) findViewById(R.id.lv_evaluate);
        ll_video = (LinearLayout) findViewById(R.id.ll_video);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(image_theach, 0, R.mipmap.teach);
        mImageLoader.get(Constant.nurseImage + nurse.getIcon(), listener);
        int fvlnur = nurse.getFvrnur();
        btn_collect.setBackgroundResource(fvlnur > 0 ? R.mipmap.iv_collect : R.mipmap.iv_collectnot);
        isCollect = fvlnur > 0 ? true : false;
        btn_back.setOnClickListener(this);
        btn_order.setOnClickListener(this);
        btn_collect.setOnClickListener(this);
        ll_evaMore.setOnClickListener(this);
    }

    /**
     * 显示个人信息
     */
    private void showData(){
      //  nurseId = nurse.getId();
        videoUrl = Constant.workvideo + nurse.getWorkvideo();
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
    private void showStar(){
        ll_star.removeAllViews();
        int leng = nurse.getSrvscore() > 5 ? 5: nurse.getSrvscore();
        for(int i = 0; i <leng;i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackground(this.getResources().getDrawable(R.mipmap.star));
            ll_star.addView(imageView);
        }
    }
    /**
     * 显示视频
     */
    private void showVideoData(){
        ll_video.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.view_video,null,false);
        wv_video = (WebView) view.findViewById(R.id.wv_video);
        btn_start = (Button) view.findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_start.setVisibility(View.INVISIBLE);
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
     * 显示生活照
     * @param cerImage
     */
    private void showImageLive(List<String> cerImage){
        ll_image.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20,10,10,10);
        for(int i = 0;i<cerImage.size();i++){
            ImageView imageView = new ImageView(this);
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, 0, R.mipmap.teach);
            mImageLoader.get(Constant.lifePic + cerImage.get(i), listener,200,200);
            ll_image.addView(imageView,layoutParams);
        }
    }
    /**
     * 获取护理师详情
     */
    private void lodeDetailsData(){
        //1294016
        Map<String, String> map = new HashMap<String, String>();
        map.put("nurid",nurse.getNursvrid());
        map.put("cstid",Constant.cstId);
        VolleyRequestUtil.newInstance().RequestPost(this,Constant.nurseDetailUrl,"detail",map,new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
            @Override
            public void onSuccess(String result) {
                parserData(result);
            }
            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    private void parserData(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String jsonCst = json.getString("files");
            Type cstType = new TypeToken<LinkedList<DetailFile>>() {}.getType();
            Gson gsonCst = new Gson();
            List<DetailFile> listFile = gsonCst.fromJson(jsonCst, cstType);

            JSONArray jsonArray = json.getJSONArray("nurse");
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                fvrid = jsonObject.getString("fvrid");
            }
            Message message = new Message();
            message.what = Constant.LIST;
            message.obj = listFile;
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取护理师评论列表
     */
    private void lodeEvaluateData(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("nurseid",nurse.getNursvrid());
        VolleyRequestUtil.newInstance().GsonPostRequest(this,Constant.nurseCmtAllUrl,"cmt" ,map,new TypeToken<JsonBean>(){},
                new Response.Listener<JsonBean>() {
                    @Override
                    public void onResponse(JsonBean jsonBean) {
                        List<NurseList> nurses = jsonBean.getNurse();
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
    private void showEvaluateData(List<NurseList> list){
        EvaluateAdapter adapter = new EvaluateAdapter(this,list);
        lv_evaluate.setAdapter(adapter);

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
            map.put("id",fvrid);
        }
        VolleyRequestUtil.newInstance().RequestPost(this,url,"add",map,new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
            @Override
            public void onSuccess(String result) {

            }
            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.btn_back:
               finish();
               break;
           case R.id.btn_order:
               Intent intent = new Intent(NurseDetailsActivity.this,OrderNurseActivity.class);
               Bundle bundle = new Bundle();
               bundle.putSerializable("nurse", nurse);
               intent.putExtras(bundle);
               startActivity(intent);
               break;
           case R.id.btn_collect:
               if(isCollect){
                   btn_collect.setBackgroundResource(R.mipmap.iv_collectnot);
                   isCollect = false;
                   addCollect("delect");
                   Toast.makeText(this,"已取消",Toast.LENGTH_SHORT).show();
               }else{
                   btn_collect.setBackgroundResource(R.mipmap.iv_collect);
                   isCollect = true;
                   addCollect("add");
                   Toast.makeText(this,"已收藏",Toast.LENGTH_SHORT).show();
               }
               break;
           case R.id.ll_evaMore:
               Intent intentEva = new Intent(NurseDetailsActivity.this,EvaluateActivity.class);
               Bundle bundleEva = new Bundle();
               bundleEva.putSerializable("nurse", nurse);
               intentEva.putExtras(bundleEva);
               startActivity(intentEva);
               break;
       }
    }
}
