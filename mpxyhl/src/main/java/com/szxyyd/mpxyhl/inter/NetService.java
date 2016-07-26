package com.szxyyd.mpxyhl.inter;

import com.szxyyd.mpxyhl.modle.Cst;
import com.szxyyd.mpxyhl.modle.JsonBean;
import com.szxyyd.mpxyhl.modle.NurseType;
import com.szxyyd.mpxyhl.modle.User;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 用于网络请求的url
 * Created by jq on 2016/7/25.
 */
public interface NetService {
    //验证用户是否存在
    @GET("cst")
    Observable<User> RxFindUser(@Query("a") String cst, @Query("usr") String usr);
    //用户登录
    @GET("cst")
    Observable<Cst> RxLogin(@Query("a") String cst, @Query("usr") String usr, @Query("pwd") String pwd);
    //首页
    @GET("svr")
    Observable<NurseType> RxHomePager(@Query("a") String svrlist);
    //全部订单
    @GET("ord")
    Observable<JsonBean> RxOrderList(@Query("a") String orderList, @Query("cstid") String cstid);
    //状态订单
    @GET("ord")
    Observable<JsonBean> RxStatusOrderList(@Query("a") String orderList,@Query("cstid") String cstid,@Query("status") int states);
    //提交订单
    @GET("ord")
    Observable<JsonBean> RxOdrCstUpd(@Query("a") String odrCstUpd,@Query("id") String id,@Query("status") String status);
    //护理师列表
    @GET("nur")
    Observable<JsonBean> RxNurList(@Query("a") String nurList,@QueryMap Map<String, String> map);
    //获取籍贯
    @GET("code")
    Observable<JsonBean> RxOrigoList(@Query("a") String origo);
    //提交订单
    @GET("ord")
    Observable<String> RxOrderAdd(@Query("a") String add,@QueryMap Map<String, String> map);
    //获取收货地址列表
    @GET("addr")
    Observable<JsonBean> RxAddressList(@Query("a") String addressList,@Query("cstid") String cstid);
    //删除服务地址
    @GET("addr")
    Observable<String> RxDelAddres(@Query("a") String delAddres,String id);
    //修改默认地址
    @GET("addr")
    Observable<String> RxSaveDefAddress(@Query("a") String saveAddressById,String id,String cstid);
}
