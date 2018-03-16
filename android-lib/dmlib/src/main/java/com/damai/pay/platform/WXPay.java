package com.damai.pay.platform;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import com.citywithincity.paylib.PayType;
import com.citywithincity.utils.Alert;
import com.damai.auto.LifeManager;
import com.damai.core.ApiJob;
import com.damai.lib.R;
import com.damai.pay.AbsDMPay;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPay extends AbsDMPay {
	private Object serverInfo;
	final IWXAPI msgApi;
	public WXPay(Context context,String appID){
		msgApi = WXAPIFactory.createWXAPI(context, null);
		msgApi.registerApp(appID);
	}
	
	
	
	@Override
	public void onJobSuccess(ApiJob job) {
		
		this.serverInfo = job.getData();
		JSONObject json= (JSONObject)serverInfo;

		try {
			PayReq req = new PayReq();
//			req.appId = ECardPayModel.WEIXIN_APPID;
			req.appId = json.getString("appid");
			req.partnerId = json.getString("partnerid"); //Constants.MCH_ID;
			req.prepayId = json.getString("prepayid");
			req.packageValue = json.getString("package");
			req.nonceStr = json.getString("noncestr"); //genNonceStr();
			req.timeStamp = json.getString("timestamp");//String.valueOf(genTimeStamp());
			req.sign =  json.getString("sign");
			
			/*
			OrderInfo order = new OrderInfo();
			order.fee = json.getDouble("fee");
			order.outId = json.getString("out_id");
			order.platId = req.prepayId;
			order.title = json.getString("title");
			listener.setOrderInfo(order);*/
			
			msgApi.registerApp(req.appId);
			msgApi.sendReq(req);
		} catch (JSONException e) {
			e.printStackTrace();
			Alert.showShortToast("调用微信支付模块失败");
		}
		
	}

	@Override
	public boolean onJobError(ApiJob job) {
		return getModel().notifyError(job.getError());
	}

	@Override
	public boolean onApiMessage(ApiJob job) {
		return getModel().notifyMessage(job.getMessage());
	}
	
	public void onPaySuccess(){
		getModel().notifyPaySuccess(getPayType(),null,false);

		//getModel().getOrderInfo(null,false);
	}
	
	public void onPayCancel(){
		//用户取消
		Alert.showShortToast("用户取消支付");
		getModel().notifyUserCancel(getPayType());
	}
	
	public void onPayError(String error){
		Alert.showShortToast(error);
	}


	@Override
	public String getTitle() {
		return "微信";
	}


	public void handleIntent(Intent intent,IWXAPIEventHandler handler) {
		msgApi.handleIntent(intent, handler);
	}

	@Override
	public int getPayType() {
		return PayType.PAY_WEIXIN.value();
	}

	@Override
	public int getIcon() {
		return R.drawable._pay_wx;
	}



	@Override
	public boolean checkInstalled() {
		if(msgApi.isWXAppInstalled()){
			return true;
		}
		Alert.alert(LifeManager.getActivity(), "请安装微信");
		
		return false;
		
	}

}
