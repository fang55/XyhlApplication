package com.szxyyd.xyhl.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.szxyyd.xyhl.R;

/**
 * Created by jq on 2016/6/17.
 */
public class EarningsFragment extends Fragment{
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_earnings,container,false);
        return rootView;
    }
}
