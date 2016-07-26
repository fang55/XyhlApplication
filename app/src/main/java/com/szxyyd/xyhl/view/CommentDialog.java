package com.szxyyd.xyhl.view;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.activity.BaseApplication;
import com.szxyyd.xyhl.activity.Constant;
import com.szxyyd.xyhl.http.BitmapCache;
import com.szxyyd.xyhl.http.VolleyRequestUtil;
import com.szxyyd.xyhl.inf.VolleyListenerInterface;
import com.szxyyd.xyhl.modle.Order;

/**
 * 用户评论
 * Created by jq on 2016/6/11.
 */
public class CommentDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private Dialog alertDialog;
    private RatingBar rb_skill;
    private TextView tv_title;
    private EditText et_commcontent;
    private Button btn_back;
    private Button btn_comm_sunmit;
    private ImageView tv_commpeople;
    private Order order;
    private Handler handler;
    private int starNum = 0;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    public CommentDialog(Context context, Order order, Handler handler) {
        super(context);
        mContext = context;
        this.order = order;
        this.handler = handler;
        mQueue = BaseApplication.getRequestQueue();
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
    }
    public void init(){
        alertDialog = new Dialog(mContext,R.style.dialog);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.view_comment);
        tv_title = (TextView) window.findViewById(R.id.tv_title);
        tv_title.setText("评论");
        tv_commpeople = (ImageView) window.findViewById(R.id.tv_commpeople);
        et_commcontent = (EditText) window.findViewById(R.id.et_commcontent);
        btn_comm_sunmit = (Button) window.findViewById(R.id.btn_comm_sunmit);
        btn_back = (Button) window.findViewById(R.id.btn_back);
        rb_skill = (RatingBar) window.findViewById(R.id.rb_skill);
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
        btn_comm_sunmit.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(tv_commpeople, 0, R.drawable.teach);
        mImageLoader.get(Constant.nurseImage + order.getIcon(), listener);
    }
    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_back:
                    alertDialog.cancel();
                    break;
                case R.id.btn_comm_sunmit:
                    submitData();
                    break;
            }
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
        VolleyRequestUtil volley = new VolleyRequestUtil();
        volley.RequestGet(mContext, url, "nurseCmt",
                new VolleyListenerInterface(mContext,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("CommentDialog", "CommentDialog---result=="+result);
                        Toast.makeText(BaseApplication.getInstance(),"已评论",Toast.LENGTH_SHORT).show();
                        Message message = new Message();
                        message.what = Constant.SUBITM;
                        handler.sendMessage(message);
                        alertDialog.cancel();
                     //   parserData(result);
                    }
                    @Override
                    public void onError(VolleyError error) {

                    }
                });

    }
    private void parserData(String result){

    }
}
