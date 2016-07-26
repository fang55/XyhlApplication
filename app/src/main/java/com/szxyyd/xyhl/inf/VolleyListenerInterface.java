package com.szxyyd.xyhl.inf;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import android.content.Context;

public abstract class VolleyListenerInterface {
	  public Context mContext;
	    public static Response.Listener<String> mListener;
	    public static Response.ErrorListener mErrorListener;

	    public VolleyListenerInterface(Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
	        this.mContext = context;
	        this.mErrorListener = errorListener;
	        this.mListener = listener;
	    }
	 // 请求成功时的回调函数
	    public abstract void onSuccess(String result);
	    // 请求失败时的回调函数
	    public abstract void onError(VolleyError error);
	    // 创建请求的事件监听
	    public Response.Listener<String> responseListener() {
	        mListener = new Response.Listener<String>() {
	            @Override
	            public void onResponse(String result) {
	                onSuccess(result);
	            }
	        };
	        return mListener;
	    }

	    // 创建请求失败的事件监听
	    public Response.ErrorListener errorListener() {
	        mErrorListener = new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					onError(error);
				}
	        };
	        return mErrorListener;
	    }
}
