package com.szxyyd.mpxyhl.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.szxyyd.mpxyhl.activity.BaseApplication;
import com.szxyyd.mpxyhl.inter.CallOnResponsetListener;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 类名称：OkHttp3Utils
 * 类描述：封装一个OkHttp3的获取类
 *
 */
public class OkHttp3Utils {
    private static OkHttpClient mOkHttpClient;
    //设置缓存目录
    private static File cacheDirectory = new File(BaseApplication.getInstance().getApplicationContext().getCacheDir().getAbsolutePath(), "MyCache");
    private static Cache cache = new Cache(cacheDirectory, 10 * 1024 * 1024);
    private static CallOnResponsetListener mListener;
    private static OkHttp3Utils instance;

    /**
     * 获取OkHttpClient对象
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient() {
        if (null == mOkHttpClient) {
            //同样okhttp3后也使用build设计模式
            mOkHttpClient = new OkHttpClient.Builder()
                    //设置一个自动管理cookies的管理器
                    //.cookieJar(new CookiesManager())
                    //添加拦截器
                    //.addInterceptor(new MyIntercepter())
                    //添加网络连接器
                    //.addNetworkInterceptor(new CookiesInterceptor(MyApplication.getInstance().getApplicationContext()))
                    //设置请求读写的超时时间
                    .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                    .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间;
                    .cache(cache)
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();
        }
        return mOkHttpClient;
    }
    public static OkHttp3Utils getInstance() {
        if (instance == null) {
            synchronized (OkHttp3Utils.class) {
                if (instance == null) {
                    instance = new OkHttp3Utils();
                }
            }
        }
        return instance;
    }
    /**
     * 拦截器
     */
    private static class MyIntercepter implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!isNetworkReachable(BaseApplication.getInstance().getApplicationContext())) {
                Toast.makeText(BaseApplication.getInstance().getApplicationContext(), "暂无网络", Toast.LENGTH_SHORT).show();
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)//无网络时只从缓存中读取
                        .build();
            }

            Response response = chain.proceed(request);
            if (isNetworkReachable(BaseApplication.getInstance().getApplicationContext())) {
                int maxAge = 60 * 60; // 有网络时 设置缓存超时时间1个小时
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }
    /**
     * 自动管理Cookies
     *//*
    private static class CookiesManager implements CookieJar {
        private final PersistentCookieStore cookieStore = new PersistentCookieStore(BaseApplication.getInstance().getApplicationContext());
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                for (Cookie item : cookies) {
                    cookieStore.add(url, item);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies;
        }
    }*/

    /**
     * 判断网络是否可用
     *
     * @param context Context对象
     */
    public static Boolean isNetworkReachable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = cm.getActiveNetworkInfo();
        if (current == null) {
            return false;
        }
        return (current.isAvailable());
    }
    public void setOnResultListener(CallOnResponsetListener listener){
        mListener = listener;
    }
    /**
     * 一般的get请求 对于一般的请求，我们希望给个url，然后取的返回的String。
     */
    public void requestPost(String url,Map<String,String> params){
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if(params != null && params.size() > 0){
            Set<Map.Entry<String,String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder().url(url).post(formBodyBuilder.build()).build();
        getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(BaseApplication.getInstance(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
                    Toast.makeText(BaseApplication.getInstance(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                 //   Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("ProgressSubscriber","error:==="+e.getMessage());
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (mListener != null) {
                    mListener.onSuccess(call,response);
                }
            }
        });
    }
}
