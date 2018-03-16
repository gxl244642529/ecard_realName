package com.citywithincity.ecard.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.paylib.ECardPayModel;
import com.citywithincity.paylib.IPay;
import com.citywithincity.paylib.WXPay;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ViewUtil;
import com.damai.auto.DMPayEntryActivity;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


@Observer
public class WXPayEntryActivity extends DMPayEntryActivity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		JsonTaskManager.getInstance().onCreate(this);
		super.onCreate(savedInstanceState);
		Notifier.register(this);
		ViewUtil.registerEvent(this);
	}
	
	@Override
	protected void onDestroy() {
		JsonTaskManager.getInstance().onDestroy(this);
		Notifier.unregister(this);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		JsonTaskManager.getInstance().onPause(this);
		super.onPause();
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		JsonTaskManager.getInstance().onResume(this);
	}
    
	@Override
	protected void initPay(Intent intent) {
		api = WXAPIFactory.createWXAPI(this, ECardPayModel.WEIXIN_APPID);
        api.handleIntent(intent, this);
	}
	
	@Override
	protected void handlerPayResult(int errCode) {
		String message = null;
		IPay model = ModelHelper.getModel(ECardPayModel.class);
		final WXPay weixin = model.getPay();
		if(weixin==null){
            Alert.showShortToast("支付渠道信息为空");
            finish();
			return;
		}
		if (errCode==0) {
			weixin.onPaySuccess();
		} else if(errCode==-1) {
			message = "支付失败";
			Alert.showShortToast(message);
			finish();
			
		} else {
			message = "用户取消支付";
			Alert.showShortToast(message);
			finish();
		}
	
	}

	

	@NotificationMethod(IPay.PAY_SUCCESS)
	public void onPaySuccess(Object result){
		Alert.cancelWait();
		finish();
	}
	
	@NotificationMethod(IPay.PAY_ERROR)
	public void onPayError(int status, String result){
		Alert.cancelWait();
		finish();
	}
	
	@NotificationMethod(IPay.PAY_CANCEL)
	public void onPayCancel(Object result){
		Alert.cancelWait();
		finish();
	}
	
	@NotificationMethod(InsuranceModel.CLIENT_NOTIFY_ERROR)
	public void onPiccNotifyFailed(String error,boolean isNetworkError) {
		Alert.cancelWait();
		finish();
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

}