package com.szxyyd.mpxyhls.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.http.VolleyRequestUtil;
import com.szxyyd.mpxyhls.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhls.modle.NurseTrain;
import com.szxyyd.mpxyhls.utils.ExampleUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 添加或者编辑培训
 * Created by jq on 2016/7/14.
 */
public class AddTrainActivity extends Activity implements View.OnClickListener{
    private EditText et_school;
    private EditText et_content;
    private TextView tv_startTime;
    private TextView tv_endTime;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int isStart = 0;
    private String type;
    private NurseTrain train;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtrain);
        type = getIntent().getStringExtra("type");
        train = (NurseTrain) getIntent().getSerializableExtra("train");
        initView();
    }

    private void initView(){
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH)+1;
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        Button btn_save = (Button) findViewById(R.id.btn_save);
        et_school = (EditText) findViewById(R.id.et_school);
        et_content = (EditText) findViewById(R.id.et_content);
        tv_startTime = (TextView) findViewById(R.id.tv_startTime);
        tv_endTime = (TextView) findViewById(R.id.tv_endTime);
        if(type.equals("add")){
            tv_title.setText(getString(R.string.text_addtrain));
            tv_startTime.setText(new StringBuffer().append(mYear).append("-").append(mMonth).append("-").append(mDay).toString());
            tv_endTime.setText(new StringBuffer().append(mYear).append("-").append(mMonth).append("-").append(mDay).toString());
        }else{
            tv_title.setText(getString(R.string.text_edittrain));
            et_school.setText(train.getTitle());
            tv_startTime.setText(ExampleUtil.showData(train.getAtrec1()));
            tv_endTime.setText(ExampleUtil.showData(train.getAtrec2()));
        }
        RelativeLayout ll_startTime = (RelativeLayout) findViewById(R.id.ll_startTime);
        RelativeLayout ll_endTime = (RelativeLayout) findViewById(R.id.ll_endTime);
        btn_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        ll_startTime.setOnClickListener(this);
        ll_endTime.setOnClickListener(this);

    }
    private void showTimeDialog(){
        // 或许当前的年月日，小时，分钟
        Dialog datePicker = new DatePickerDialog(this,listener,mYear,mMonth,mDay);
        datePicker.show();
    }
    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int mother, int day) {
            mYear = year;
            mMonth = mother;
            mDay = day;
            updateDisplay();
        }
    };
    private void updateDisplay(){
        String result ;
        StringBuffer sb = new StringBuffer();
        result = sb.append(mYear).append("-").append(mMonth).append("-").append(mDay).toString();
        if(isStart == 1){
            tv_startTime.setText(result);
        }else{
            tv_endTime.setText(result);
        }
    }
    /**
     * 上传培训经历
     */
    private void submitTrainData(String type){
        //80001967
        Log.e("ArchivesTrainFragment", "submitTrainData---Constant.nurId=="+Constant.nurId);
        String nurseid = Constant.nurId;
        String score = "10";
        String url = null;
        Map<String,String> map = new HashMap<>();
        map.put("nurseid","1294110");
        map.put("title",et_school.getText().toString());
        map.put("atrec1",tv_startTime.getText().toString());
        map.put("atrec2",tv_endTime.getText().toString());
        map.put("score",score);
        if(type.equals("add")){  //新增
            url = Constant.nurseTrainAddUrl;
        }else if(type.equals("edit")){  //编辑
            url = Constant.nurseTrainUpdUrl ;
        }
        VolleyRequestUtil.newInstance().RequestPost(this, url, type,map,
                new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("ArchivesTrainFragment", "submitTrainData---result=="+result);
                        Toast.makeText(AddTrainActivity.this,"已保存",Toast.LENGTH_SHORT).show();
                        finish();
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
            case R.id.btn_save:
                submitTrainData(type);
                break;
            case R.id.ll_startTime:
                isStart = 1;
                showTimeDialog();
                break;
            case R.id.ll_endTime:
                isStart = 2;
                showTimeDialog();
                break;
        }
    }
}
