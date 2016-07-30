package com.szxyyd.mpxyhl.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.adapter.GridAdapter;
import com.szxyyd.mpxyhl.fragment.MyOrderFragment;
import com.szxyyd.mpxyhl.http.BitmapCache;
import com.szxyyd.mpxyhl.modle.ImageItem;
import com.szxyyd.mpxyhl.modle.Order;
import com.szxyyd.mpxyhl.utils.Bimp;
import com.szxyyd.mpxyhl.utils.FileUtils;
import com.szxyyd.mpxyhl.utils.PublicWay;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 评价
 * Created by jq on 2016/7/7.
 */
public class OrderCommentActivity extends Activity implements View.OnClickListener{
    private RatingBar rb_skill;
    private EditText et_commcontent;
    private Button btn_comm_sunmit;
    private ImageView iv_teach;
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private int starNum = 0;
    private Order order;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    public static Bitmap bimap ;
    private static final int TAKE_PICTURE = 0x000001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order = (Order) getIntent().getSerializableExtra("order");
        parentView = getLayoutInflater().inflate(R.layout.activity_comment, null);
        setContentView(parentView);
        bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused);
        mQueue = BaseApplication.getRequestQueue();
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
        PublicWay.activityList.add(this);
        initView();
        initPopupWindow();
    }
    private void initView(){
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("评价");
        iv_teach = (ImageView) findViewById(R.id.iv_teach);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        et_commcontent = (EditText) findViewById(R.id.et_commcontent);
        btn_comm_sunmit = (Button)findViewById(R.id.btn_comm_sunmit);
        rb_skill = (RatingBar)findViewById(R.id.rb_skill);
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv_teach, 0, R.mipmap.teach);
        mImageLoader.get(Constant.nurseImage + order.getIcon(), listener);
        rb_skill.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                if(b){
                    String result = String.valueOf(rating);
                    String str = result.substring(0,result.indexOf("."));
                    starNum = Integer.valueOf(str);
                }
            }
        });
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    Log.i("ddddddd", "----------");
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(OrderCommentActivity.this,R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                  /*  Intent intent = new Intent(MainActivity.this,
                            GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);*/
                }
            }
        });
    btn_comm_sunmit.setOnClickListener(this);
    btn_back.setOnClickListener(this);
}
    public void initPopupWindow() {
        pop = new PopupWindow(OrderCommentActivity.this);
        View view = getLayoutInflater().inflate(R.layout.item_comm_popupwindows, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        pop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(this);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
    }
    /**
     * 提交评论
     */
    private void submitCommentData(){
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        Map<String,String> map = new HashMap<>();
        map.put("bycstid",Constant.cstId);
        map.put("nurseid",order.getNurseid());
        map.put("content",et_commcontent.getText().toString().trim());
        map.put("id",order.getId());
        map.put("star",String.valueOf(starNum));
        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //遍历map中所有参数到builder
        for (String key : map.keySet()) {
            multipartBody.addFormDataPart(key, map.get(key));
        }
        //遍历paths中所有图片绝对路径到builder，并约定key如“file”作为后台接受多张图片的key
        if(Bimp.tempSelectBitmap.size()>0){
            for(int i = 0;i<Bimp.tempSelectBitmap.size();i++){
                File file = new File(Bimp.tempSelectBitmap.get(i).getImagePath());
                multipartBody.addFormDataPart("file",file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
            //    Log.e("tempSelectBitmap","file==="+file);
            }
        }
        RequestBody requestBody = multipartBody.build();
        Request request = new Request.Builder()
                .url(Constant.nurseCmtUrl)
                .post(requestBody)
                .build();
       //设置超时
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS);//设置写入超时时间;
        builder.build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               Log.e("OrderCommentActivity","call.toString()=="+call.toString());
            }
            @Override
            public void onResponse(Call call, final  Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BaseApplication.getInstance(),"已评论",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OrderCommentActivity.this,MyOrderFragment.class);
                        setResult(2, intent);
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_comm_sunmit:
                submitCommentData();
                break;
            case R.id.parent:
                pop.dismiss();
                ll_popup.clearAnimation();
                break;
            case R.id.item_popupwindows_camera: //照相
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
                break;
            case R.id.item_popupwindows_Photo: //选择照片
                Intent intent = new Intent(OrderCommentActivity.this, AlbumActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                pop.dismiss();
                ll_popup.clearAnimation();
                break;
            case R.id.item_popupwindows_cancel: //取消
                pop.dismiss();
                ll_popup.clearAnimation();
                break;
        }
    }
    /**
     * 拍照
     */
    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }
    @Override
    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    FileUtils.saveBitmap(bm, fileName);
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
        }
    }

}
