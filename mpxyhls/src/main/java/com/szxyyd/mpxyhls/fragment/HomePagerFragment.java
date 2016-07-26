package com.szxyyd.mpxyhls.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.lidroid.xutils.exception.DbException;
import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.activity.BaseApplication;
import com.szxyyd.mpxyhls.activity.Constant;
import com.szxyyd.mpxyhls.http.BitmapCache;
import com.szxyyd.mpxyhls.modle.Nurse;
import com.szxyyd.mpxyhls.view.RoundImageView;

import java.util.List;

/**
 * Created by jq on 2016/7/14.
 */
public class HomePagerFragment extends Fragment{
    private View rootView;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home,container,false);
        mQueue = BaseApplication.getRequestQueue();
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
        initData();
        return rootView;
    }
    private void initData(){
        try {
            List<Nurse> nurseList = BaseApplication.getdbUtils().findAll(Nurse.class);
            if(nurseList.size() != 0 && nurseList != null) {
                Nurse nurse = nurseList.get(0);
                Constant.nurId= nurse.getNursvrid();
                ((TextView) rootView.findViewById(R.id.tv_myName)).setText(nurse.getName());
                ((TextView) rootView.findViewById(R.id.tv_myId)).setText(nurse.getNursvrid() + "");
                ((TextView) rootView.findViewById(R.id.tv_approve)).setText("未认证");
                ((TextView) rootView.findViewById(R.id.tv_myGrade)).setText("护理师等级"+nurse.getSvrid() + nurse.getLvl());
                ((TextView) rootView.findViewById(R.id.tv_good)).setText(nurse.getSrvscore());
                ((TextView) rootView.findViewById(R.id.tv_service)).setText(nurse.getSrvnum());
                ((TextView) rootView.findViewById(R.id.tv_collet)).setText(nurse.getFvrnur());
                RoundImageView iv_people = (RoundImageView) rootView.findViewById(R.id.iv_people);
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv_people, 0, R.mipmap.people);
                mImageLoader.get(Constant.nurseImage + nurse.getIcon(), listener);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
