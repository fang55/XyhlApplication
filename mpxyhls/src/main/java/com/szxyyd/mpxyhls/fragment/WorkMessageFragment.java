package com.szxyyd.mpxyhls.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.activity.BaseApplication;
import com.szxyyd.mpxyhls.activity.Constant;
import com.szxyyd.mpxyhls.http.VolleyRequestUtil;
import com.szxyyd.mpxyhls.inter.VolleyListenerInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作信息
 * Created by jq on 2016/7/13. train video
 */
public class WorkMessageFragment extends Fragment{
    private View rootView;
    private Button btn_save;
    private GridLayout gl_speciality;
    private List<String> data ;
    private String[]  specialityData = new String[]{"月子餐","心理疏导","宝宝护理","宝宝早教","疾病预防"
            ,"产妇护理","无痛通乳","瑜伽"};
    private List<String> vulesList = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_work,container,false);
        initData();
        initView();
        showServicePeople();
        return rootView;
    }
    private void initView(){
        gl_speciality = (GridLayout) rootView.findViewById(R.id.gl_speciality);
        btn_save = (Button) rootView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
    }
    private void initData(){
        data = new ArrayList<String>();
        for(int i = 0;i<specialityData.length;i++){
            data.add(specialityData[i].toString());
        }
    }
    /**
     * 显示业务特长的数据
     */
    private void showServicePeople(){
        gl_speciality.removeAllViews();
        for(int i = 0;i<data.size();i++){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.include_border, null);
            final CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
            cb.setTag(data.get(i));
            cb.setText(data.get(i));
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if(isChecked){
                        vulesList.add(cb.getTag().toString());
                    }else{
                        vulesList.remove(cb.getTag().toString());
                    }
                }
            });
            gl_speciality.addView(view);
        }
    }
    private String returnResult(){
        String result = "";
        StringBuffer sb = new StringBuffer();
        for(String str : vulesList){
            sb.append("&spcty=").append(str);
        }
        result = sb.toString();
        return  result;
    }
    /**
     * 提交修改的信息
     */
    private void submitData(){
        String spcty = returnResult();
        Log.e("ArchivesWorkFragment","spcty=="+spcty);
        String id = Constant.nurId;
        String url = Constant.yxNurseUpdWorkUrl + "&id=" + id +returnResult();
        VolleyRequestUtil.newInstance().RequestGet(getActivity(), url, "upd",
                new VolleyListenerInterface(getActivity(),VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(BaseApplication.getInstance(),"已提交",Toast.LENGTH_SHORT).show();
                        Log.e("LoginActivity", "submitLoginData---result=="+result);
                    }
                    @Override
                    public void onError(VolleyError error) {

                    }
                });

    }
}
