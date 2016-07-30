package com.szxyyd.mpxyhl.http;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.szxyyd.mpxyhl.activity.BaseApplication;
import com.szxyyd.mpxyhl.activity.Constant;
import com.szxyyd.mpxyhl.inter.NetService;
import com.szxyyd.mpxyhl.modle.CityData;
import com.szxyyd.mpxyhl.modle.Cst;
import com.szxyyd.mpxyhl.modle.JsonBean;
import com.szxyyd.mpxyhl.modle.NurseType;
import com.szxyyd.mpxyhl.modle.Order;
import com.szxyyd.mpxyhl.modle.User;
import com.szxyyd.mpxyhl.utils.OkHttp3Utils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class HttpMethods {
    public static final String TAG = "HttpMethods";
  //  public static final String BASE_URL = "http://183.232.35.71:8080/xyhl/";
    public static final String BASE_URL = Constant.baseUrl;
    private static final int DEFAULT_TIMEOUT = 5;
    private  Retrofit retrofit;
    private NetService netService;
    private static OkHttpClient mOkHttpClient;
    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
       /* int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(BaseApplication.getInstance().getCacheDir(), cacheSize);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间;
                .cache(cache)
                .addNetworkInterceptor(new StethoInterceptor());*/
        if (null == mOkHttpClient) {
            mOkHttpClient = OkHttp3Utils.getOkHttpClient();
        }
        retrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        netService = retrofit.create(NetService.class);
    }
    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }
    //获取单例
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 用于验证用户是否存在
     * @param subscriber  由调用者传过来的观察者对象
     */
    public void getFindUsrData(String type, String lvl,Subscriber<User> subscriber ){
        Observable observable = netService.RxFindUser(type,lvl);
        toSubscribe(observable, subscriber);
    }
    /**
     * 登录接口
     */
    public void geLoginData(String type,String usr, String pwd,Subscriber<Cst> subscriber ){
        Observable observable = netService.RxLogin(type,usr,pwd);
        toSubscribe(observable, subscriber);
    }
    /**
     * 首页
     */
    public void getHomePagerData(String type,Subscriber<NurseType> subscriber){
        Observable observable = netService.RxHomePager(type);
        toSubscribe(observable, subscriber);
    }
    /**
     * 获取全部订单列表
     */
    public void getOrderListData(String type,String cstid,Subscriber<JsonBean> subscriber){
        Observable observable = netService.RxOrderList(type,cstid);
        toSubscribe(observable, subscriber);
    }
    /**
     * 根据状态获取订单列表
     */
    public void getOrderListData(String type,String cstid,int states,Subscriber<JsonBean> subscriber){
        Observable observable = netService.RxStatusOrderList(type,cstid,states);
        toSubscribe(observable, subscriber);
    }

    /**
     * 提交订单
     */
    public void submitOrderData(String type,String id,String states,Subscriber<String> subscriber){
        Observable observable = netService.RxOdrCstUpd(type,id,states);
        toSubscribe(observable, subscriber);
    }

    /**
     * 护理师列表
     */
    public void getNurseListData(String type,Map<String, String> map,Subscriber<JsonBean> subscriber){
        Observable observable = netService.RxNurList(type,map);
        toSubscribe(observable, subscriber);
    }
    /**
     * 获取籍贯
     */
    public void getOrigoData(String type,Subscriber<JsonBean> subscriber){
        Observable observable = netService.RxOrigoList(type);
        toSubscribe(observable, subscriber);
    }
    /**
     * 提交订单
     */
    public void submitAddOrderData(String type,Map<String, String> map,Subscriber<String> subscriber){
        Observable observable = netService.RxOrderAdd(type,map);
        toSubscribe(observable, subscriber);
    }
    /**
     * 获取收货地址列表
     */
    public void getAddressListData(String type,String cstid,Subscriber<JsonBean> subscriber){
        Observable observable = netService.RxAddressList(type,cstid);
        toSubscribe(observable, subscriber);
    }
    /**
     * 删除收货地址
     */
    public void submitDelAddrData(String type,String id,Subscriber<String> subscriber){
        Observable observable = netService.RxDelAddres(type,id);
        toSubscribe(observable, subscriber);
    }
    /**
     * 修改默认收货地址
     */
    public void submitSaveAddrData(String type,String id,String cstid,Subscriber<CityData> subscriber){
        Observable observable = netService.RxSaveDefAddress(type,id,cstid);
        toSubscribe(observable, subscriber);
    }
    /**
     * 添加收货地址
     */
    public void submitAddAddresData(String type,String cstid,Map<String, String> map,Subscriber<String> subscriber){
        Observable observable = netService.RxAddAddres(type,cstid,map);
        toSubscribe(observable, subscriber);
    }
    /**
     * 修改收货地址
     */
    public void submitEditAddresData(String type,String id,Map<String, String> map,Subscriber<String> subscriber){
        Observable observable = netService.RxEditAddres(type,id,map);
        toSubscribe(observable, subscriber);
    }
    /**
     * 一级 城市
     */
    public void getFindCityData(String type,Subscriber<JsonBean> subscriber){
        Observable observable = netService.RxFindCity(type);
        toSubscribe(observable, subscriber);
    }
    /**
     * 二级 区县
     */
    public void getFindCountyData(String type,String iid,Subscriber<JsonBean> subscriber){

        Observable observable = netService.RxFindCounty(type,iid);
        toSubscribe(observable, subscriber);
    }
    /**
     * 三级 街道
     */
    public void getFindStreetData(String type,String iid,Subscriber<JsonBean> subscriber){
        Observable observable = netService.RxFindStreet(type,iid);
        toSubscribe(observable, subscriber);
    }

    //获取收藏列表
    public void getFvrnurListData(String type,String cstid,Subscriber<JsonBean> subscriber){
          Observable observable = netService.RxFvnList(type,cstid);
          toSubscribe(observable, subscriber);
    }
    //删除收藏
    public void submitFvrnurDelData(String type,String id,Subscriber<String> subscriber){
        Observable observable = netService.RxFvrDel(type,id);
        toSubscribe(observable, subscriber);
    }
    //护理师评论
//    public void submitNurseCmtData(String type,Map<String, RequestBody> map,Subscriber<String> subscriber){
//        Observable observable = netService.RxNurseCmt(map);
//        toSubscribe(observable, subscriber);
//    }
    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
         o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
   /* private class HttpResultFunc<T> implements Func1<HttpResult<T>, T>{

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getCount() == 0) {
                throw new ApiException(100);
            }
            return httpResult.getSubjects();
        }
    }
*/
}
