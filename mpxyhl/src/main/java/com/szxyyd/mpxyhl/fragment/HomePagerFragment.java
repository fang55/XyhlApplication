package com.szxyyd.mpxyhl.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.activity.Constant;
import com.szxyyd.mpxyhl.activity.HealthNurseActivity;
import com.szxyyd.mpxyhl.activity.HomePagerActivity;
import com.szxyyd.mpxyhl.adapter.HomeAdapter;
import com.szxyyd.mpxyhl.http.HttpMethods;
import com.szxyyd.mpxyhl.http.VolleyRequestUtil;
import com.szxyyd.mpxyhl.inter.SubscriberOnNextListener;
import com.szxyyd.mpxyhl.modle.JsonBean;
import com.szxyyd.mpxyhl.modle.NurseType;
import com.szxyyd.mpxyhl.modle.ProgressSubscriber;

import java.util.List;

/**
 * Created by jq on 2016/7/6.
 */
public class HomePagerFragment extends Fragment {
    private View rootView;
    private GridView gv;
    private HomeAdapter adapter;
    private  List<NurseType.SvrBean> nurseList;
    private HomePagerActivity mActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (HomePagerActivity) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_homepager,container,false);
        lodeData();
        initView();
        return rootView;
    }
    private void initView(){
        gv = (GridView) rootView.findViewById(R.id.gv_home);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positon, long l) {
                NurseType.SvrBean svrBean = nurseList.get(positon);
                int svrid = svrBean.getId();
                Constant.svrId = svrid;
                Intent intent = new Intent(getActivity(), HealthNurseActivity.class);
                intent.putExtra("svrid", svrid);
                intent.putExtra("title", svrBean.getName());
                startActivity(intent);
            }
        });
    }
    private SubscriberOnNextListener getHomeOnNext =  new SubscriberOnNextListener<NurseType>() {
        @Override
        public void onNext(NurseType nurseType) {
            nurseList = nurseType.getSvr();
            adapter = new HomeAdapter(getActivity(),nurseList);
            gv.setAdapter(adapter);
        }
    };
    /**
     * 加载数据
     */
    private void lodeData(){
        HttpMethods.getInstance().getHomePagerData("svrlist",new ProgressSubscriber<NurseType>(getHomeOnNext,getActivity()));
    }
}
