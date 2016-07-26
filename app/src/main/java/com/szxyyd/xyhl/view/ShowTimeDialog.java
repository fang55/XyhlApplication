package com.szxyyd.xyhl.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szxyyd.xyhl.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jq on 2016/6/11.
 */
public class ShowTimeDialog extends AlertDialog implements View.OnClickListener{
    private Context mContext;
    private TextView tv_date1;
    private TextView tv_date2;
    private TextView tv_date3;
    private TextView tv_date4;
    private TextView tv_date5;
    private TextView tv_date6;
    private TextView tv_date7;
    private TextView tv_title;
    private TextView tv_state1;
    private TextView tv_state2;
    private TextView tv_state3;
    private TextView tv_state4;
    private TextView tv_state5;
    private TextView tv_state6;
    private TextView tv_state7;
    private LinearLayout ll_date1;
    private LinearLayout ll_date2;
    private LinearLayout ll_date3;
    private LinearLayout ll_date4;
    private LinearLayout ll_date5;
    private LinearLayout ll_date6;
    private LinearLayout ll_date7;
    private LinearLayout ll_select_time;
    private Button btn_dialog_back;
    private Button btn_time1;
    private Button btn_time2;
    private Button btn_time3;
    private Button btn_time4;
    private Button btn_time5;
    private onClick mClick;
    private String seleDay;
    private int mMonth;
    private int mYear;
    private List<String> data;
  //  private String[] timeData = new String[]{"08:30","10:30","12:30","14:30","16:30"};
    private AlertDialog alertDialog;
    private LayoutInflater inflater;
    private boolean isDwonDate = false;
    private String resultTime;
    public ShowTimeDialog(Context context) {
        super(context);
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }
    public void init(){
        alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.show();
        Window view = alertDialog.getWindow();
        view.setContentView(R.layout.view_selecttime);
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
        tv_state1 = (TextView) view.findViewById(R.id.tv_state1);
        tv_state2 = (TextView) view.findViewById(R.id.tv_state2);
        tv_state3 = (TextView) view.findViewById(R.id.tv_state3);
        tv_state4 = (TextView) view.findViewById(R.id.tv_state4);
        tv_state5 = (TextView) view.findViewById(R.id.tv_state5);
        tv_state6 = (TextView) view.findViewById(R.id.tv_state6);
        tv_state7 = (TextView) view.findViewById(R.id.tv_state7);
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
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mClick.dwonDate(mYear,mMonth,seleDay,resultTime);
                alertDialog.cancel();
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
        ll_select_time.removeAllViews();
        View view = inflater.inflate(R.layout.view_showtime,null);
        btn_time1 = (Button) view.findViewById(R.id.btn_time1);
        btn_time2 = (Button) view.findViewById(R.id.btn_time2);
        btn_time3 = (Button) view.findViewById(R.id.btn_time3);
        btn_time4 = (Button) view.findViewById(R.id.btn_time4);
        btn_time5 = (Button) view.findViewById(R.id.btn_time5);
        btn_time1.setOnClickListener(this);
        btn_time2.setOnClickListener(this);
        btn_time3.setOnClickListener(this);
        btn_time4.setOnClickListener(this);
        btn_time5.setOnClickListener(this);
       ll_select_time.addView(view);
    }
    private void changeBtnBg(Button btn1,Button btn2,Button btn3,Button btn4,Button btn5){
        btn1.setBackgroundResource(R.drawable.btn_blue_bg);
        btn1.setTextColor(mContext.getResources().getColor(R.color.white));
        btn2.setBackgroundResource(R.drawable.shape_seltime_border);
        btn2.setTextColor(mContext.getResources().getColor(R.color.tv_login));
        btn3.setBackgroundResource(R.drawable.shape_seltime_border);
        btn3.setTextColor(mContext.getResources().getColor(R.color.tv_login));
        btn4.setBackgroundResource(R.drawable.shape_seltime_border);
        btn4.setTextColor(mContext.getResources().getColor(R.color.tv_login));
        btn5.setBackgroundResource(R.drawable.shape_seltime_border);
        btn5.setTextColor(mContext.getResources().getColor(R.color.tv_login));
    }

private void dwonTextColor(TextView tv_data,TextView tv_states){
        tv_data.setTextColor(mContext.getResources().getColor(R.color.tv_login));
        tv_states.setTextColor(mContext.getResources().getColor(R.color.tv_login));
}
    private void notDwonTextColor(TextView tv2,TextView tv_states2,TextView tv3,TextView tv_states3, TextView tv4,TextView tv_states4,
                                  TextView tv5, TextView tv_states5,TextView tv6,TextView tv_states6,TextView tv7,TextView tv_states7){
        tv2.setTextColor(mContext.getResources().getColor(R.color.login_top));
        tv_states2.setTextColor(mContext.getResources().getColor(R.color.order_state));
        tv3.setTextColor(mContext.getResources().getColor(R.color.login_top));
        tv_states3.setTextColor(mContext.getResources().getColor(R.color.order_state));
        tv4.setTextColor(mContext.getResources().getColor(R.color.login_top));
        tv_states4.setTextColor(mContext.getResources().getColor(R.color.order_state));
        tv5.setTextColor(mContext.getResources().getColor(R.color.login_top));
        tv_states5.setTextColor(mContext.getResources().getColor(R.color.order_state));
        tv6.setTextColor(mContext.getResources().getColor(R.color.login_top));
        tv_states6.setTextColor(mContext.getResources().getColor(R.color.order_state));
        tv7.setTextColor(mContext.getResources().getColor(R.color.login_top));
        tv_states7.setTextColor(mContext.getResources().getColor(R.color.order_state));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                alertDialog.cancel();
                break;
            case R.id.ll_date1:
                seleDay = String.valueOf(tv_date1.getTag());
                dwonTextColor(tv_date1,tv_state1);
                notDwonTextColor(tv_date2,tv_state2,tv_date3,tv_state3,tv_date4,tv_state4,
                        tv_date5,tv_state5,tv_date6,tv_state6,tv_date7,tv_state7);
                break;
            case R.id.ll_date2:
                seleDay =  String.valueOf(tv_date2.getTag());
                dwonTextColor(tv_date2,tv_state2);
                notDwonTextColor(tv_date1,tv_state1,tv_date3,tv_state3,tv_date4,tv_state4,
                        tv_date5,tv_state5,tv_date6,tv_state6,tv_date7,tv_state7);
                break;
            case R.id.ll_date3:
                seleDay = String.valueOf(tv_date3.getTag());
                dwonTextColor(tv_date3,tv_state3);
                notDwonTextColor(tv_date2,tv_state2,tv_date1,tv_state1,tv_date4,tv_state4,
                        tv_date5,tv_state5,tv_date6,tv_state6,tv_date7,tv_state7);
                break;
            case R.id.ll_date4:
                seleDay =  String.valueOf(tv_date4.getTag());
                dwonTextColor(tv_date4,tv_state4);
                notDwonTextColor(tv_date2,tv_state2,tv_date3,tv_state3,tv_date1,tv_state1,
                        tv_date5,tv_state5,tv_date6,tv_state6,tv_date7,tv_state7);
                break;
            case R.id.ll_date5:
                seleDay = String.valueOf(tv_date5.getTag());
                dwonTextColor(tv_date5,tv_state5);
                notDwonTextColor(tv_date2,tv_state2,tv_date3,tv_state3,tv_date4,tv_state4,
                        tv_date1,tv_state1,tv_date6,tv_state6,tv_date7,tv_state7);
                break;
            case R.id.ll_date6:
                seleDay = String.valueOf(tv_date6.getTag());
                dwonTextColor(tv_date6,tv_state6);
                notDwonTextColor(tv_date2,tv_state2,tv_date3,tv_state3,tv_date4,tv_state4,
                        tv_date5,tv_state5,tv_date1,tv_state1,tv_date7,tv_state7);
                break;
            case R.id.ll_date7:
                seleDay = String.valueOf(tv_date7.getTag());
                dwonTextColor(tv_date7,tv_state7);
                notDwonTextColor(tv_date2,tv_state2,tv_date3,tv_state3,tv_date4,tv_state4,
                        tv_date5,tv_state5,tv_date6,tv_state6,tv_date1,tv_state1);
                break;
            case R.id.btn_time1:
                changeBtnBg(btn_time1,btn_time2,btn_time3,btn_time4,btn_time5);
                resultTime = btn_time1.getText().toString();
                break;
            case R.id.btn_time2:
                changeBtnBg(btn_time2,btn_time1,btn_time3,btn_time4,btn_time5);
                resultTime = btn_time2.getText().toString();
                break;
            case R.id.btn_time3:
                changeBtnBg(btn_time3,btn_time2,btn_time1,btn_time4,btn_time5);
                resultTime = btn_time3.getText().toString();
                break;
            case R.id.btn_time4:
                changeBtnBg(btn_time4,btn_time2,btn_time3,btn_time1,btn_time5);
                resultTime = btn_time4.getText().toString();
                break;
            case R.id.btn_time5:
                changeBtnBg(btn_time5,btn_time2,btn_time3,btn_time4,btn_time1);
                resultTime = btn_time5.getText().toString();
                break;
        }
    }
    public void setSelectOnclick(onClick click){
        mClick = click;
    }
    public interface onClick{
        void dwonDate(int year,int month,String day,String time);
    }
}
