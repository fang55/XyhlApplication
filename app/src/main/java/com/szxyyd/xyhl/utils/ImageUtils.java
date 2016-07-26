package com.szxyyd.xyhl.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.szxyyd.xyhl.activity.Constant;

/**
 * Created by jq on 2016/6/16.
 */
public class ImageUtils {
    private Context mContext;
    private ImageLoader mImageLoader;
    private RequestQueue mQueue;
    public ImageUtils(Context context){
        mContext = context;
        mQueue = Volley.newRequestQueue(mContext);
    }

}
