package com.szxyyd.mpxyhls.http;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.mpxyhls.activity.BaseApplication;
import com.szxyyd.mpxyhls.inter.VolleyListenerInterface;

import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class VolleyRequestUtil {
	public static StringRequest stringRequest;
	public static Context context;



	private static class VolleyManagerHolder {
		private static final VolleyRequestUtil INSTANCE = new VolleyRequestUtil();
	}
	/**
	 * 单例模式（静态内部类）
	 *
	 * @return VolleyManager instance
	 */
	public static VolleyRequestUtil newInstance() {
		return VolleyManagerHolder.INSTANCE;

	}
	/*
       * 获取GET请求内容
       * 参数：
       * context：当前上下文；
       * url：请求的url地址；
       * tag：当前请求的标签；
       * volleyListenerInterface：VolleyListenerInterface接口；
       * */
	public void RequestGet(Context context, String url, String tag, VolleyListenerInterface volleyListenerInterface) {
		// 清除请求队列中的tag标记请求
		BaseApplication.getRequestQueue().cancelAll(tag);
		// 创建当前的请求，获取字符串内容
		stringRequest = new StringRequest(Request.Method.GET, url, volleyListenerInterface.responseListener(), volleyListenerInterface.errorListener());
		// 为当前请求添加标记
		stringRequest.setTag(tag);
		// 将当前请求添加到请求队列中
		BaseApplication.getRequestQueue().add(stringRequest);
		// 重启当前请求队列
		BaseApplication.getRequestQueue().start();
	}

	/*
     * 获取POST请求内容（请求的代码为Map）
     * 参数：
     * context：当前上下文；
     * url：请求的url地址；
     * tag：当前请求的标签；
     * params：POST请求内容；
     * volleyListenerInterface：VolleyListenerInterface接口；
     * */
	public static void RequestPost(Context context, String url, String tag, final Map<String, String> params, VolleyListenerInterface volleyListenerInterface) {
		// 清除请求队列中的tag标记请求
		BaseApplication.getRequestQueue().cancelAll(tag);
		// 创建当前的POST请求，并将请求内容写入Map中
		stringRequest = new StringRequest(Request.Method.POST, url, volleyListenerInterface.responseListener(), volleyListenerInterface.errorListener()) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return params;
			}
		};
		// 为当前请求添加标记
		stringRequest.setTag(tag);
		// 将当前请求添加到请求队列中
		BaseApplication.getRequestQueue().add(stringRequest);
		// 重启当前请求队列
		BaseApplication.getRequestQueue().start();
	}

	public <T> GsonRequest<T> GsonPostRequest(Context context, String url, String tag, Map<String, String> params,
											  TypeToken<T> typeToken, Response.Listener<T> listener, Response.ErrorListener errorListener) {
		// 清除请求队列中的tag标记请求
		BaseApplication.getRequestQueue().cancelAll(tag);
		GsonRequest<T> gsonRequest = new GsonRequest<T>(Request.Method.POST, params, url, typeToken, listener, errorListener);
		//添加请求到队列
		BaseApplication.getInstance().addRequest(gsonRequest, tag);
		return gsonRequest;
	}

	public <T> List<T> GsonParserData(Context context, String result,String type){
		try {
			JSONObject json = new JSONObject(result);
			String jsonData = json.getString(type);
			Type listType = new TypeToken<LinkedList<T>>() {}.getType();
			Gson gson = new Gson();
			List<T> listData = gson.fromJson(jsonData, listType);
			return  listData;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return  null;
	}

}
