package com.szxyyd.mpxyhl.inter;

import com.android.volley.VolleyError;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by jq on 2016/7/30.
 */
public interface CallOnResponsetListener {
    void onSuccess(Call call, Response response);
}
