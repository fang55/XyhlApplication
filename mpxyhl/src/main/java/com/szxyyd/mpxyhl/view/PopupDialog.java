package com.szxyyd.mpxyhl.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.szxyyd.mpxyhl.R;

/**
 * Created by jq on 2016/7/7.
 */
public class PopupDialog extends Dialog implements View.OnClickListener{
    private Dialog alertDialog;
    private Context mContext;
    private onSelectClickListener mListener;
    private String mType;
    public PopupDialog(Context context, String type) {
        super(context);
        mContext = context;
        mType = type;
    }
    public void initView(){
        alertDialog = new Dialog(mContext, R.style.dialog);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.view_dialog);
        TextView tv_dialog_title = (TextView) window.findViewById(R.id.tv_dialog_title);
        TextView tv_cancle = (TextView) window.findViewById(R.id.tv_cancle);
        TextView tv_sure = (TextView)window.findViewById(R.id.tv_sure);
        if(mType.equals("addr")){
            tv_dialog_title.setText("确定删除地址吗？");
        }else  if(mType.equals("collect")){
            tv_dialog_title.setText("确定删除收藏吗？");
        }
        else{
            tv_dialog_title.setText("确定删除订单吗？");
        }
        tv_cancle.setOnClickListener(this);
        tv_sure.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.tv_cancle:
               alertDialog.cancel();
               break;
           case R.id.tv_sure:
              if(mListener != null){
                  mListener.onSure();
              }
               alertDialog.cancel();
               break;
       }
    }
    public void setonSelectListener(onSelectClickListener listener){
        mListener = listener;
    }
    public interface onSelectClickListener{
        void onSure();
    }
}
