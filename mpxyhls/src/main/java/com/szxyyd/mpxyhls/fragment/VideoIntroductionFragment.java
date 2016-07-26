package com.szxyyd.mpxyhls.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.szxyyd.mpxyhls.R;

/**
 * 视频介绍
 * Created by jq on 2016/7/13.
 */
public class VideoIntroductionFragment extends Fragment{
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_video,container,false);
        return rootView;
    }
}
