package com.szxyyd.xyhl.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.pay.PayTools;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {

	}

	@Override
	public void onResp(BaseResp resp) {
		//Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
         System.out.println( "onPayFinish, errCode = " + resp.errCode+":"+resp.errStr+";"+resp.transaction);
		PayTools.OnPayResultLinstener onPayResultLinstener = PayTools.getInstance(this).getOnPayResultLinstener();
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode == BaseResp.ErrCode.ERR_OK){
				if(onPayResultLinstener!=null)
					onPayResultLinstener.paySuccessfullResult(PayTools.WX_WAY,"支付成功!");
			}else if(resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL){
				if(onPayResultLinstener!=null)
					onPayResultLinstener.faildPayResult(PayTools.WX_WAY,"支付已取消!");
			}else{
				if(onPayResultLinstener!=null)
					onPayResultLinstener.faildPayResult(PayTools.WX_WAY,"支付失败!");
			}
			finish();

		}else{
			finish();
		}

	}
}