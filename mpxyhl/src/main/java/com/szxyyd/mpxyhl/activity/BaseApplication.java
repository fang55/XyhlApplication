package com.szxyyd.mpxyhl.activity;

import android.app.Activity;
import android.app.Application;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.Stetho;
import com.lidroid.xutils.DbUtils;
import java.util.LinkedList;
import java.util.List;
import cn.jpush.android.api.JPushInterface;

public class BaseApplication extends Application {
	private List<Activity> mList = new LinkedList<Activity>();
	private static BaseApplication instance;
	// 建立请求队列
    public static RequestQueue queue;
	//数据库
	public static DbUtils dbUtils;
	public static BaseApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		Stetho.initializeWithDefaults(this);
		queue = Volley.newRequestQueue(getApplicationContext());
		dbUtils = DbUtils.create(getApplicationContext());
		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);     		// 初始化 JPush

	}
	 public static RequestQueue getRequestQueue() {
	        return queue;
	    }

	public <T> void addRequest(Request<T> req, String tag) {
		req.setTag(tag);
		getRequestQueue().add(req);
	}
	public <T> void addRequest(Request<T> req) {
	//	req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelRequests(Object tag) {
		if (queue != null) {
			queue.cancelAll(tag);
		}
	}
	public static DbUtils getdbUtils() {
		return dbUtils;
	}
	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}
	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		//	System.exit(0);
		}
	}
}
