package com.szxyyd.xyhl.pay;


import com.szxyyd.xyhl.wxapi.Constants;
import com.szxyyd.xyhl.wxapi.MD5;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.app.Activity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

public class WX {
	private Activity mContext;
	private IWXAPI msgApi;
	private PayReq req;
	
	protected WX(Activity context){
		init(context);	
	}
	
	private void init(Activity context) {
		mContext = context;
		msgApi = WXAPIFactory.createWXAPI(mContext, null);
		msgApi.registerApp(Constants.APP_ID);
		req = new PayReq();
	}
	
	/**
	 * 使用服务器得到的prepay_id和最终签名
	*/
	private void nGenPayReq(String APP_ID,String MCH_ID,String prepayId,String packageValue,
			String nonceStr,String timeStamp,String sign){
//		nDialog = ProgressDialog.show(ConfirmCashOrderActivity.this, getString(R.string.reminder), getString(R.string.getting_pay_order));
//		Message msg = new Message();
//		msg.what = WX_PAY_RESULT;
//		msg.obj = "time_out";
//		mHandler.sendMessageDelayed(msg, 5000);
		req.appId = Constants.APP_ID;
		req.partnerId = MCH_ID;
		req.prepayId = prepayId;
		req.packageValue = packageValue;
		req.nonceStr = nonceStr;
		req.timeStamp = timeStamp;
		req.sign = sign;
		//req.transaction="1";
   System.out.println(sign+";"+req.appId+","+req.nonceStr+","+req.packageValue+","+req.prepayId+","+req.timeStamp+","+req.partnerId);
	List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));


	//String mySing = genAppSign(signParams);
   //String mgin= MD5.getMessageDigest(("stringA&"+signParams.toString()).getBytes()).toUpperCase();
	     //req.sign=mySing;
	 //	System.out.println( "第二次签名后打包"+",sign="+mySing+",mySing="+signParams.toString());

	}
	
	/** 发起微信支付,请确保req已生成，即已完成含prepayid的签名*/
	protected void sendPayReq(String APP_ID,String MCH_ID,String prepayId,String packageValue,
			String nonceStr,String timeStamp,String sign) {
		nGenPayReq(APP_ID, MCH_ID, prepayId, packageValue, nonceStr, timeStamp, sign);
	//	msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);

	}
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		//sb.append("sign str\n"+sb.toString()+"\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		//Log.e("orion",appSign);
		return appSign;
	}
}
