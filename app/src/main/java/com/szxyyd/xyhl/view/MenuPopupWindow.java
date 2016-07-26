package com.szxyyd.xyhl.view;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.activity.CommunityActivity;
import com.szxyyd.xyhl.activity.HomePageActivity;
import com.szxyyd.xyhl.activity.MessageActivity;

/**
 * 导航弹出框
 * Created by fq on 2016/6/6.
 */
public class MenuPopupWindow extends PopupWindow implements View.OnClickListener{
    private Context mContext;
    private LayoutInflater inflater;
    private LinearLayout ll_1;
    private LinearLayout ll_2;
    private LinearLayout ll_3;
       public MenuPopupWindow(Context context){
           mContext = context;
           inflater = LayoutInflater.from(mContext);
       }
    public void initPopupWindow(View rootView, int width, int height){
        View view = inflater.inflate(R.layout.view_menu,null,false);
        ll_1 = (LinearLayout) view.findViewById(R.id.ll_1);
        ll_2 = (LinearLayout) view.findViewById(R.id.ll_2);
        ll_3= (LinearLayout) view.findViewById(R.id.ll_3);
        ll_1.setOnClickListener(this);
        ll_2.setOnClickListener(this);
        ll_3.setOnClickListener(this);
        this.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.def_navbg));
        this.setWidth(width);
        this.setHeight(height);
        setContentView(view);
        showAsDropDown(rootView);
    }
    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.ll_1:  //首页
                 Intent intent = new Intent(mContext, HomePageActivity.class);
                 mContext.startActivity(intent);
                 break;
             case R.id.ll_2: //社区
                 Intent intent2 = new Intent(mContext, CommunityActivity.class);
                 mContext.startActivity(intent2);
                 break;
             case R.id.ll_3:
                 Intent intent3 = new Intent(mContext, MessageActivity.class);
                 mContext.startActivity(intent3);
                 break;
         }
        this.dismiss();
    }
}
