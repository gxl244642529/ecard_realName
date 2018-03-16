package com.citywithincity.paylib;


import android.app.Activity;

import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.paylib.IPay.OrderInfo;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.CryptAES;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class WXPay extends AbsPay {
	final IWXAPI msgApi;
	IPayAction payAction;
	private Object serverInfo;
	
	public WXPay(Activity context, ECardPayModel payListener,String appID,IPayAction payAction) {
		super(context, payListener);
		payType = PayType.PAY_WEIXIN;
		msgApi = WXAPIFactory.createWXAPI(context, null);
		msgApi.registerApp(appID);
		this.payAction = payAction;
	}

	@Override
	public void onPrePaySuccess(Object serverInfo) throws Exception {
		this.serverInfo = serverInfo;
		JSONObject json;
		if(serverInfo instanceof String){
			String content = CryptAES.decrypt(JsonTaskManager.getInstance().getDeviceID(), 
					(String)serverInfo);
			json = new JSONObject(content);
		}else{
			json = (JSONObject)serverInfo;
		}
		
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
			
			OrderInfo order = new OrderInfo();
			order.fee = json.getDouble("fee");
			order.outId = json.getString("out_id");
			order.platId = req.prepayId;
			order.title = json.getString("title");
			listener.setOrderInfo(order);
			
			msgApi.registerApp(req.appId);
			msgApi.sendReq(req);
		} catch (JSONException e) {
			e.printStackTrace();
			Alert.showShortToast("调用微信支付模块失败");
		}
	}

	@Override
	public void onNotifyServerSuccess(Object result) {
		listener.onPaySuccess(payType, result);
	}

	/**
	 * 微信支付成功
	 */
	public void onPaySuccess(){
		payAction.notifyServer(payType,serverInfo , this);
	}
	
	public void onPayError(int code){
		
		listener.onPayError(code, "微信支付失败");
	}
	
	public void onPayCancel(){
		listener.onPayCancel();
	}

	@Override
	public void onNotityServerError(String error) {
		// TODO Auto-generated method stub
		listener.onPayError(0,error);
	}

}
