package com.szxyyd.mpxyhl.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.adapter.GridAdapter;
import com.szxyyd.mpxyhl.fragment.MyOrderFragment;
import com.szxyyd.mpxyhl.http.BitmapCache;
import com.szxyyd.mpxyhl.http.VolleyRequestUtil;
import com.szxyyd.mpxyhl.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhl.modle.ImageItem;
import com.szxyyd.mpxyhl.modle.Order;
import com.szxyyd.mpxyhl.utils.Bimp;
import com.szxyyd.mpxyhl.utils.FileUtils;
import com.szxyyd.mpxyhl.utils.PublicWay;

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
        parentView = getLayoutInflater().inflate(R.layout.activity_comment, null);
        setContentView(parentView);
        bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused);
        order = (Order) getIntent().getSerializableExtra("order");
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
    private void submitData(){
        //nur?a=nurseCmt	cstid (客户id) nurseid (护理师id)  content (评论内容)   star (星级)
        String url = Constant.nurseCmtUrl + "&cstid="+Constant.cstId
                +"&nurseid="+order.getNurseid()
                +"&content="+et_commcontent.getText().toString().trim()
                +"&id="+order.getId()
                +"&star="+starNum;
        VolleyRequestUtil.newInstance().RequestGet(this, url, "nurseCmt",
                new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("CommentDialog", "CommentDialog---result=="+result);
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
            case R.id.btn_comm_sunmit:
                submitData();
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
                Intent intent = new Intent(OrderCommentActivity.this,
                        AlbumActivity.class);
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
