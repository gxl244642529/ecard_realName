package com.citywithincity.ecard.pay.cmbpay;

import android.app.Activity;
import android.content.Intent;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.pay.PayTypes;
import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.damai.auto.LifeManager;
import com.damai.core.ApiJob;
import com.damai.helper.ActivityResult;
import com.damai.pay.AbsDMPay;

import java.io.IOException;

public class CMBPay extends AbsDMPay implements ActivityResult, IMessageListener {


	@Override
	public void onJobSuccess(ApiJob job) {
		String url = job.getData();
		IViewContainer activity = LifeManager.getCurrent();
		
		Intent intent = new Intent(activity.getActivity(), CMBPayWebActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("title", getTitle());
		
		activity.startActivityForResult(this,intent , 1000);
	}
	
	private boolean isCanceled = false;
	@Override
	public void onActivityResult(Intent intent, int resultCode, int requestCode) {
		if(resultCode == Activity.RESULT_OK){
            isCanceled = false;
			if(requestCode == 1000){
				MessageUtil.sendMessage(this);
			}
		}else if(resultCode == Activity.RESULT_CANCELED){
            if(requestCode == 1000){
               getModel().notifyUserCancel(getPayType());
            }
        }
	}

	@Override
	public boolean onJobError(ApiJob job) {

		return getModel().notifyError( job.getError());
	}

	@Override
	public boolean onApiMessage(ApiJob job) {
		
		return getModel().notifyMessage( job.getMessage() );
	}

	@Override
	public boolean checkInstalled() {
		return true;
	}

	@Override
	public int getPayType() {
		return PayTypes.PayType_CMB;
	}

	@Override
	public String getTitle() {
		return "一网通银行卡支付";
	}

	@Override
	public int getIcon() {
		return R.drawable.cmb_pay;
	}


	@Override
	public void onMessage(int message) {
		getModel().notifyPaySuccess(getPayType(),null,true);
		//getModel().getOrderInfo(null, true);
	}



}
