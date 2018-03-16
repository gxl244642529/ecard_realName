package com.citywithincity.paylib;

import android.app.Activity;
import android.content.Intent;

import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.paylib.IPay.OrderInfo;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.CryptAES;

import org.json.JSONException;
import org.json.JSONObject;

public class ECardPay extends AbsPay implements IRequestResult<Object> {
	private String key;
	private String platOrderId;

	public ECardPay(Activity context, ECardPayModel payListener) {
		super(context, payListener);
		payType = PayType.PAY_ETONGKA;
	}

	@Override
	public void onPrePaySuccess(Object serverInfo) {
		// 解密
		String content = CryptAES.decrypt(JsonTaskManager.getInstance()
				.getDeviceID().substring(0, 24), (String)serverInfo);
		try {
			JSONObject json = new JSONObject(content);

			OrderInfo orderInfo = new OrderInfo();
			orderInfo.title = "";
			orderInfo.outId = "";
			orderInfo.fee = json.getDouble("fee");
			orderInfo.platId = json.getString("order_id");
			listener.setOrderInfo(orderInfo);
			platOrderId = orderInfo.platId;
			key = json.getString("sign").substring(0, 24);
			mContext.startActivity(new Intent(mContext,
					EAccountPayActivity.class));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 这里进行解析
	}

	@Override
	public void onNotifyServerSuccess(Object result) {
		// 这里不做处理
	}

	public void pay(String account, String pwd) {
		String cryptPwd = CryptAES.encrypt(key, pwd);

		if (LibConfig.DEBUG) {
			System.out.println("加密开始,密钥:" + key + "=明文:" + pwd + "=密文:"
					+ cryptPwd);
		}

		JsonTaskManager.getInstance().createValueJsonTask("ecard_pay/pay",0)
				.setListener(this).put("account", account)
				.put("password", cryptPwd).put("plat_order_id", platOrderId)
				.setWaitMessage("请稍候...").execute();
	}

	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
		// 支付失败
		listener.onPayError(0, "后台账户支付失败:" + errMsg);
	}

	@Override
	public void onRequestSuccess(final Object result) {
		Alert.alert(ModelHelper.getActivity(), "温馨提示", "支付成功",
				new DialogListener() {
					@Override
					public void onDialogButton(int id) {

						if (ModelHelper.getActivity() instanceof EAccountPayActivity) {
							ModelHelper.getActivity().finish();
						}
						listener.onPaySuccess(payType, result);
					}
				});
	}

	@Override
	public void onNotityServerError(String error) {
		// TODO Auto-generated method stub
		listener.onPayError(0,error);
	}

}
