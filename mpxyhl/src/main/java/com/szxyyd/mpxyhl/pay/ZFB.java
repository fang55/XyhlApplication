package com.szxyyd.mpxyhl.pay;

import android.app.Activity;

import com.alipay.sdk.app.PayTask;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class ZFB {
	//商户PID
	protected static final String PARTNER = "2088021343718597";
	//商户收款账号
	protected static final String SELLER = "zmbapp@163.com";
	//商户私钥，pkcs8格式
	protected static final String RSA_PRIVATE ="un3I7v0XT55H7OaDJoLsT%2Fbf3cXjmGleNC53%2BlJrtPCxamxBh1aX1OGxtd6Gcct51Qdm%2Bj8T8pCPcrOrCBRJgdDZiZZi5MQGGeTssT%2FruswACPzPfjMtdWrDdc02gKOjfbtbRbQ1YCsZ%2B4P%2BAXC4s9g3wIc13suY8ZytKiJTATA%3D";

	//支付宝公钥
	protected static final String RSA_PUBLIC = "";
//	private static final int SDK_PAY_FLAG = 1;
//	private static final int SDK_CHECK_FLAG = 2;
	private static final String ALGORITHM = "RSA";
	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
	private static final String DEFAULT_CHARSET = "UTF-8";
	private Activity mContext;

	protected ZFB(Activity context){
		init(context);
	}

	/**<pre>
	 * 此操作为耗时操作，必须异步调用
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	protected  String pay(String sign) {
		PayTask alipay = new PayTask(mContext);
		// 调用支付接口，获取支付结果
		return alipay.pay(sign);
	}

	/**<pre>
	 * 此操作为耗时操作，必须异步调用
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	protected boolean check() {

		// 构造PayTask 对象
		PayTask payTask = new PayTask(mContext);
		// 调用查询接口，获取查询结果

		return payTask.checkAccountIfExist();
	}

	/**
	 * get the sdk version. 获取支付宝SDK版本号
	 * 
	 */
	protected String getSDKVersion() {
		PayTask payTask = new PayTask(mContext);
		return payTask.getVersion();
	}

	/**
	 * create the order info. 创建订单信息
	 * @deprecated
	 */
	public String getOrderInfo(String subject, String body, String price,String orderno) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderno+ "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://xfn.skyline.gs/api/pay/alinotice/"+orderno
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * @deprecated
	 */
	protected String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * @deprecated
	 * @param content
	 *            待签名订单信息
	 */
	protected String sign(String content) {
		return sign(content, RSA_PRIVATE);
	}

	/**@deprecated
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	protected String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	/**
	 * @deprecated
	 * @param content
	 * @param privateKey
	 * @return
	 */
	private String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			//KeyFactory keyf = KeyFactory.getInstance(ALGORITHM,"BC");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private void init(Activity context) {
		mContext = context;
	}
}
