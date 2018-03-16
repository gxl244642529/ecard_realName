package com.citywithincity.ecard.recharge.models;

import android.app.Activity;
import android.content.Intent;

import com.citywithincity.ecard.ECardConfig;
import com.citywithincity.ecard.pay.PayTypes;
import com.citywithincity.ecard.recharge.activities.RechargePaySuccessActivity;
import com.citywithincity.ecard.recharge.models.vos.RechargeSuccessVo;
import com.damai.auto.LifeManager;
import com.damai.core.DMLib;
import com.damai.helper.DataHolder;
import com.damai.pay.DMPayModel;
import com.damai.pay.PayActionHandler;

public class RechargePayActionHandler extends PayActionHandler {

	public RechargePayActionHandler(Activity activity) {
		super(activity, "recharge", ECardConfig.RECHARGE_PAY,
				RechargeSuccessVo.class,
				RechargePaySuccessActivity.class);
		setPaySuccessAction(PaySuccessAction.PaySuccessAction_PopupToCurrent);
	}


    @Override
    public void onPayError(int type, String error, boolean isNetworkError) {
        if(type == PayTypes.PayType_ECard){
            LifeManager.popupTo(context);
        }
    }

    @Override
	public boolean onClientPaySuccess(DMPayModel model, int type, Object data) {
		//这里并不去拉取订单，而是跳转到第二个activity进行解析
        //释放收银台
        LifeManager.popupTo(context);
        Intent intent = new Intent(context,RechargePaySuccessActivity.class);
        intent.putExtra("orderId", DMLib.getPayModel().getOrderId());
        context.startActivity(intent);


		return true;
	}
}
