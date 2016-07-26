package com.szxyyd.mpxyhl.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.szxyyd.mpxyhl.modle.WxPayResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * */
public class PayTools {
	/** 支付宝支付方式*/
	public static final int ZFB_WAY = 0;
	/** 微信支付方式*/
	public static final int WX_WAY = 1;

	public static final int ZFB_PAY_FLAG = 0x1010;
	private static PayTools mInstance = null;
	private Handler mHandler = new Handler(Looper.getMainLooper(),new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if(msg.what == ZFB_PAY_FLAG){
				String result = (String) msg.obj;
				PayResult payResult = new PayResult(result);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				//				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();
		//	Logger.e("zfb_pay_result", "code="+resultStatus+";msg="+payResult.getMemo());
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					if(onPayResultLinstener!=null){
						onPayResultLinstener.paySuccessfullResult(ZFB_WAY, "支付成功!");
					}

				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						if(onPayResultLinstener!=null){
							onPayResultLinstener.faildPayResult(ZFB_WAY, "支付结果确认中...");
						}

					} else if(TextUtils.equals(resultStatus, "6001")){
						if(onPayResultLinstener!=null){
							onPayResultLinstener.faildPayResult(ZFB_WAY, "支付已取消");
						}
					} else if(TextUtils.equals(resultStatus, "6002")){
						if(onPayResultLinstener!=null){
							onPayResultLinstener.faildPayResult(ZFB_WAY, "网络连接异常");
						}
					}else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						if(onPayResultLinstener!=null){
							onPayResultLinstener.faildPayResult(ZFB_WAY, "支付失败");
						}
					}
				}
			}
			return false;
		}
	});
	private OnPayResultLinstener onPayResultLinstener;
	protected ZFB zfb;
	private WX wx;
	private static Activity mContext;

	private PayTools(Activity context){
		mContext = context;
		if(zfb == null){
			zfb = new ZFB(context);
		}
		if(wx == null){
			wx = new WX(context);
		}
	}

	public static PayTools getInstance(Activity context){
		if(mContext!=null&&mContext.isFinishing()){
			mInstance = null;
			mContext = null;
		}
		if(mInstance == null||mContext==null){
			mInstance = new PayTools(context);
		}
		return mInstance;
	}

	/**
	 * 支付结果回调接口
	 *
	 */
	public interface OnPayResultLinstener{
		/**
		 * 返回支付成功结果的回调
		 * @param payType  支付的类型，如支付宝为0、微信为1
		 * @param message  返回的结果消息
		 */
		void paySuccessfullResult(int payType, String message);
		/**
		 * 返回支付失败结果的回调
		 * @param payType 支付的类型，如支付宝为0、微信为1
		 * @param message 返回的结果消息
		 */
		void faildPayResult(int payType, String message);
	}

	/** 设置支付结果回调监听*/
	public void setOnPayResultLinstener(OnPayResultLinstener onPayResultLinstener){
		synchronized (this) {
			if(this.onPayResultLinstener == null){
				this.onPayResultLinstener = onPayResultLinstener;
			}
		}
	}

	public OnPayResultLinstener getOnPayResultLinstener(){
		return onPayResultLinstener;
	}

	/** <pre>
	 * 使用支付宝支付
	 * @param sign 从服务器获得的签名数据
	 */
	public void payByZFB(final String sign){
		if(!TextUtils.isEmpty(sign)){
			Runnable payRunnable = new Runnable() {

				@Override
				public void run() {
					String result = zfb.pay(sign);
					Message msg = new Message();
					msg.what = ZFB_PAY_FLAG;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}

			};
			Thread payThread = new Thread(payRunnable);
			payThread.start();
		}else{
			if(onPayResultLinstener!=null){
				onPayResultLinstener.faildPayResult(ZFB_WAY, "支付失败");
			}
		}
	}
	/**
	 * <pre>
	 * 调起微信支付
	 * @param APP_ID       微信签约的ID 
	 * @param MCH_ID	       微信收款账号 
	 * @param prepayId	       预支付ID微信
	 * @param packageValue 微信包名值
	 * @param nonceStr	       防重复随机值
	 * @param timeStamp    时间戳
	 */
	public void payByWX(String APP_ID,String MCH_ID,String prepayId,String packageValue,
			String nonceStr,String timeStamp,String sign){
		//		Constants.MCH_ID = MCH_ID;
		wx.sendPayReq(APP_ID, MCH_ID, prepayId, packageValue, nonceStr, timeStamp, sign);
	}

	/**
	 * <pre>
	 * 使用微信支付
	 * @param resp  支付所需的必要信息
	 */
	public void payByWx(WxPayResponse resp){
		if(resp!=null&&resp.getPrepayid()!=null){
			payByWX(resp.getAppid(),resp.getPartnerid(), resp.getPrepayid(),
					resp.getPackageX(), resp.getNoncestr(), resp.getTimestamp(),resp.getSign());
		}else{
			if(onPayResultLinstener!=null)
				onPayResultLinstener.faildPayResult(PayTools.WX_WAY,"支付失败!");
		}
	}
	/**
	 * 获取支付宝sdk版本号
	 * @return
	 */
	public String getZfbVersion(){
		return zfb.getSDKVersion();
	}

	public String getSign(String subject, String body, String price,String orderNo)
	{
		String orderinfo=zfb.getOrderInfo(subject,body,price,orderNo);
		System.out.println("oderinfo="+orderinfo);
		String sign=zfb.sign(orderinfo);
		System.out.println("sign="+orderinfo);
		try {
			sign= URLEncoder.encode(sign,"UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return orderinfo+"&sign=\""+sign+"\"&"+zfb.getSignType();
	}
}
