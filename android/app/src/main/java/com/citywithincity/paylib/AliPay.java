package com.citywithincity.paylib;

import android.app.Activity;
import android.util.Base64;

import com.alipay.sdk.app.PayTask;
import com.citywithincity.paylib.IPay.OrderInfo;
import com.citywithincity.utils.ThreadUtil;

public class AliPay extends AbsPay implements Runnable {
	
	private int mState;
	private String mResult;
	private IPayAction payAction;
	
	public AliPay(Activity context,ECardPayModel payListener,IPayAction payAction) {
		super(context,payListener);
		this.payAction = payAction;
		payType = PayType.PAY_ALIPAY;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		mResult = null;
		payAction = null;
	}
	
	private OrderInfo mOrderInfo;
	
	@Override
	public void onPrePaySuccess(final Object serverInfo) {
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				PayTask alipay = new PayTask(mContext);
				String result = alipay.pay((String)serverInfo,true);
				mState = AliUtil.checkPayState(result);
				mResult = AliUtil.getResult(result);
				OrderInfo orderInfo = new OrderInfo();
				if (mState == 9000) {
					orderInfo.fee = AliUtil.getTotalFee(result);
					mOrderInfo = orderInfo;
				}
				ThreadUtil.post(AliPay.this);
			}
		};
		
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	@Override
	public void onNotifyServerSuccess(Object result) {
		listener.onPaySuccess(payType, result);
	}

	@Override
	public void run() {
		switch(mState){
		case 9000:
		{
			//通知服务器成功
			listener.setOrderInfo(mOrderInfo);
			String str64 = Base64.encodeToString(mResult.getBytes(), Base64.NO_WRAP);
			payAction.notifyServer(payType, str64, this);
		}
			
			break;
		case 8000://
			listener.onPayError(mState, "正在处理中");
			break;
		case 6001:
			listener.onPayCancel();
			break;
		case 6002:
			listener.onPayError(mState, "网络连接出错");
			break;
		case 4000:
			listener.onPayError(mState, "订单支付失败");
			break;

		default:
			break;
		}
	}

	@Override
	public void onNotityServerError(String error) {
		listener.onPayError(0,error);
	}
}
