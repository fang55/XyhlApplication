package com.szxyyd.mpxyhls.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.lidroid.xutils.exception.DbException;
import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.activity.ArchivesActivity;
import com.szxyyd.mpxyhls.activity.BaseApplication;
import com.szxyyd.mpxyhls.activity.Constant;
import com.szxyyd.mpxyhls.http.BitmapCache;
import com.szxyyd.mpxyhls.modle.Nurse;
import com.szxyyd.mpxyhls.view.RoundImageView;
import java.util.List;

/**
 * 个人中心
 * Created by jq on 2016/7/13.
 */
public class MyFragment extends Fragment implements View.OnClickListener{
    private View rootView;
    private RoundImageView iv_people;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    private Nurse nurse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_my,container,false);
        mQueue = BaseApplication.getRequestQueue();
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
        initView();
        initData();
        return rootView;
    }

    private void initView(){
        iv_people = (RoundImageView) rootView.findViewById(R.id.iv_people);
        RelativeLayout rl_file = (RelativeLayout) rootView.findViewById(R.id.rl_file);
        rl_file.setOnClickListener(this);
    }
    private void initData(){
        try {
            List<Nurse> nurseList = BaseApplication.getdbUtils().findAll(Nurse.class);
            if(nurseList.size() != 0 && nurseList != null) {
                nurse = nurseList.get(0);
                Constant.nurId= nurse.getNursvrid();
                ((TextView) rootView.findViewById(R.id.tv_myName)).setText(nurse.getName());
                ((TextView) rootView.findViewById(R.id.tv_myId)).setText(nurse.getNursvrid() + "");
                ((TextView) rootView.findViewById(R.id.tv_approve)).setText("未认证");
                ((TextView) rootView.findViewById(R.id.tv_myGrade)).setText("护理师等级"+nurse.getSvrid() + nurse.getLvl());
                ((TextView) rootView.findViewById(R.id.tv_good)).setText(nurse.getSrvscore());
                ((TextView) rootView.findViewById(R.id.tv_service)).setText(nurse.getSrvnum());
                ((TextView) rootView.findViewById(R.id.tv_collet)).setText(nurse.getFvrnur());
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv_people, 0, R.mipmap.people);
                mImageLoader.get(Constant.nurseImage + nurse.getIcon(), listener);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_set: //设置

                break;
            case R.id.rl_file: //我的档案
                Intent intentFile = new Intent(getActivity(),ArchivesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("nurse",nurse);
                intentFile.putExtras(bundle);
                startActivity(intentFile);
                break;
        }
    }
}
