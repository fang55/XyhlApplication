package com.szxyyd.xyhl.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szxyyd.xyhl.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jq on 2016/6/3.
 */
public class SelectTimeView extends LinearLayout implements View.OnClickListener{
   private Context mContext;
    private TextView tv_title;
    private TextView tv_date1;
    private TextView tv_date2;
    private TextView tv_date3;
    private TextView tv_date4;
    private TextView tv_date5;
    private TextView tv_date6;
    private TextView tv_date7;
    private LinearLayout ll_date1;
    private LinearLayout ll_date2;
    private LinearLayout ll_date3;
    private LinearLayout ll_date4;
    private LinearLayout ll_date5;
    private LinearLayout ll_date6;
    private LinearLayout ll_date7;
    private LinearLayout ll_select_time;
    private Button btn_dialog_back;
    private onClick click;
    private LayoutInflater inflater;
    private String seleDay;
    private int mMonth;
    private int mYear;
    private List<String> data;
    private String[] timeData = new String[]{"08:30","10:30","12:30","14:30","16:30"};
    public SelectTimeView(Context context) {
        super(context);
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        initData();
    }
    public void init(final onClick click){
        View view = inflater.inflate(
                R.layout.view_selecttime, null, false);
        this.addView(view);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(mContext.getString(R.string.select_get_time));
        btn_dialog_back = (Button) view.findViewById(R.id.btn_back);
        tv_date1 = (TextView) view.findViewById(R.id.tv_date1);
        tv_date2 = (TextView) view.findViewById(R.id.tv_date2);
        tv_date3 = (TextView) view.findViewById(R.id.tv_date3);
        tv_date4 = (TextView) view.findViewById(R.id.tv_date4);
        tv_date5 = (TextView) view.findViewById(R.id.tv_date5);
        tv_date6 = (TextView) view.findViewById(R.id.tv_date6);
        tv_date7 = (TextView) view.findViewById(R.id.tv_date7);
        ll_date1 = (LinearLayout) view.findViewById(R.id.ll_date1);
        ll_date2 = (LinearLayout) view.findViewById(R.id.ll_date2);
        ll_date3 = (LinearLayout) view.findViewById(R.id.ll_date3);
        ll_date4 = (LinearLayout) view.findViewById(R.id.ll_date4);
        ll_date5 = (LinearLayout) view.findViewById(R.id.ll_date5);
        ll_date6 = (LinearLayout) view.findViewById(R.id.ll_date6);
        ll_date7 = (LinearLayout) view.findViewById(R.id.ll_date7);
        ll_select_time = (LinearLayout) view.findViewById(R.id.ll_select_time);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        showDate();
        showTime();
        btn_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                click.dwonFinsh(mYear,mMonth,seleDay);
            }
        });
        initEvent();
    }
    private void initEvent(){
        ll_date1.setOnClickListener(this);
        ll_date2.setOnClickListener(this);
        ll_date3.setOnClickListener(this);
        ll_date4.setOnClickListener(this);
        ll_date5.setOnClickListener(this);
        ll_date6.setOnClickListener(this);
        ll_date7.setOnClickListener(this);
        btn_dialog_back.setOnClickListener(this);
    }
    private void initData(){
      data = new ArrayList<>();
        for(int i = 0;i<timeData.length;i++){
            data.add(timeData[i].toString());
        }
    }
    private void showDate(){
         Calendar c = Calendar.getInstance();
       /* int mYear = c.get(Calendar.YEAR); //获取当前年份
        int mMonth = c.get(Calendar.MONTH)+1;//获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
        int mHour = c.get(Calendar.HOUR_OF_DAY);//获取当前的小时数
        int mMinute = c.get(Calendar.MINUTE);//获取当前的分钟数
        String week = String.valueOf(c.get(Calendar.DAY_OF_WEEK));//获取当前周几 数字*/
         mYear = c.get(Calendar.YEAR); //获取当前年份
         mMonth = c.get(Calendar.MONTH)+1;//获取当前月份
        tv_date1.setText("今天"+mMonth + "/" + c.get(Calendar.DAY_OF_MONTH));
        tv_date1.setTag(c.get(Calendar.DAY_OF_MONTH));
        tv_date2.setText("明天"+mMonth + "/" + (c.get(Calendar.DAY_OF_MONTH)+1));
        tv_date2.setTag((c.get(Calendar.DAY_OF_MONTH)+1));
        tv_date3.setText("后天"+mMonth + "/" + (c.get(Calendar.DAY_OF_MONTH)+2));
        tv_date3.setTag((c.get(Calendar.DAY_OF_MONTH)+2));
        tv_date4.setText("周四"+mMonth + "/" + (c.get(Calendar.DAY_OF_MONTH)+3));
        tv_date4.setTag((c.get(Calendar.DAY_OF_MONTH)+3));
        tv_date5.setText("周五"+mMonth + "/" + (c.get(Calendar.DAY_OF_MONTH)+4));
        tv_date5.setTag((c.get(Calendar.DAY_OF_MONTH)+4));
        tv_date6.setText("周六"+mMonth + "/" + (c.get(Calendar.DAY_OF_MONTH)+5));
        tv_date6.setTag((c.get(Calendar.DAY_OF_MONTH)+5));
        tv_date7.setText("周日"+mMonth + "/" + (c.get(Calendar.DAY_OF_MONTH)+6));
        tv_date7.setTag((c.get(Calendar.DAY_OF_MONTH)+6));
    }

    private void showTime(){
       /* ll_select_time.removeAllViews();
        for(int i = 0;i<data.size();i++){
            View view = inflater.inflate(R.layout.view_showtime,null);
            TextView tv = (TextView) view.findViewById(R.id.tv_show_time);
            tv.setText(data.get(i));
            ll_select_time.addView(view);
        }*/
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_date1:
                seleDay = String.valueOf(tv_date1.getTag());
                break;
            case R.id.ll_date2:
                seleDay =  String.valueOf(tv_date2.getTag());
                break;
            case R.id.ll_date3:
                seleDay = String.valueOf(tv_date3.getTag());
                break;
            case R.id.ll_date4:
                seleDay =  String.valueOf(tv_date4.getTag());
                break;
            case R.id.ll_date5:
                seleDay = String.valueOf(tv_date5.getTag());
                break;
            case R.id.ll_date6:
                seleDay = String.valueOf(tv_date6.getTag());
                break;
            case R.id.ll_date7:
                seleDay = String.valueOf(tv_date7.getTag());
                break;
        }
    }

    public interface onClick{
        void dwonFinsh(int year,int month,String day);
    }
}
