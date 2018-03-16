package com.citywithincity.ecard.discard.pay;

import android.app.Activity;

import com.citywithincity.paylib.PayType;
import com.citywithincity.utils.Alert;
import com.damai.pay.DMPayModel;
import com.damai.pay.PayActionHandler;

public class DiscardPayActionHandler extends PayActionHandler {

	public DiscardPayActionHandler(Activity activity) {
		super(activity, "book", new int[]{PayType.PAY_ALIPAY.value()/*,PayType.PAY_WEIXIN.value()*/});
	}

	@Override
	public void onPaySuccess(DMPayModel model, Object data) {
		super.onPaySuccess(model, data);
		Alert.alert(context, "支付成功，请耐心等待收货");
	}
}
