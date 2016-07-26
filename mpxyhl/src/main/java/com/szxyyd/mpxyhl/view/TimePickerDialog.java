package com.szxyyd.mpxyhl.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.szxyyd.mpxyhl.R;

/**
 * Created by jq on 2016/7/19.
 */
public class TimePickerDialog extends AlertDialog implements View.OnClickListener, DatePicker.OnDateChangedListener,TimePicker.OnTimeChangedListener {
    private final DatePicker mDatePicker_start;
    private TimePicker timePicker;
    private OnDateSetListener mCallBack = null;
    private LayoutInflater inflater;
    private View view;
    private TextView tv_select;
    private TextView tv_dialog_cancle;
    private TextView tv_dialog_sure;
    private int year = 0;
    private int monthOfYear = 0;
    private int dayOfMonth = 0;

    public interface OnDateSetListener{
        void onDateSet(int startYear, int startMonthOfYear, int startDayOfMonth,
                       int hourOfDay,int minute);
    }
    public TimePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
                 this(context, 0, callBack, year, monthOfYear, dayOfMonth);
             }
    public TimePickerDialog(Context context, int theme,OnDateSetListener callBack, int year, int monthOfYear,
                                  int dayOfMonth) {
                this(context, 0, callBack, year, monthOfYear, dayOfMonth, true);
             }

    public TimePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
                                  int dayOfMonth, boolean isDayVisible) {
                 super(context, theme);
        mCallBack = callBack;
        Context themeContext = getContext();
        inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.date_picker_dialog, null);
        setView(view);
        tv_select = (TextView) view.findViewById(R.id.tv_select);
        tv_dialog_cancle = (TextView) view.findViewById(R.id.tv_dialog_cancle);
        tv_dialog_sure = (TextView) view.findViewById(R.id.tv_dialog_sure);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        mDatePicker_start = (DatePicker) view.findViewById(R.id.datePickerStart);
        tv_select.setText("请选择日期");
        timePicker.setIs24HourView(true);
        mDatePicker_start.init(year, monthOfYear, dayOfMonth, this);
        timePicker.setOnTimeChangedListener(this);
        tv_dialog_cancle.setOnClickListener(this);
        tv_dialog_sure.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_dialog_cancle:
                dismiss();
                break;
            case R.id.tv_dialog_sure:
               if(tv_select.getText().toString().equals("请选择日期")){
                   mDatePicker_start.setVisibility(View.GONE);
                   timePicker.setVisibility(View.VISIBLE);
                   tv_select.setText("请选择时间");
               }else{
                   tryNotifyDateSet();
                   dismiss();
               }
                break;
        }
    }
    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
            mDatePicker_start.init(year, month, day, this);
          this.year = year;
        monthOfYear = month;
        dayOfMonth = day;
    }

    /**
           * 获得开始日期的DatePicker
           *
         * @return The calendar view.
     1      */
         public DatePicker getDatePickerStart() {
                return mDatePicker_start;
             }

    public void updateStartDate(int year, int monthOfYear, int dayOfMonth) {
        mDatePicker_start.updateDate(year, monthOfYear, dayOfMonth);
    }
    private void tryNotifyDateSet() {
                 if (mCallBack != null) {
                         mDatePicker_start.clearFocus();
                        mCallBack.onDateSet(year, monthOfYear,
                                dayOfMonth,timePicker.getCurrentHour(),timePicker.getCurrentMinute());
                     }
            }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        System.out.println("h:"+ hourOfDay +" m:"+minute);
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

}
