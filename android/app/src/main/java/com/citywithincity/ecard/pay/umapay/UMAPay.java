package com.citywithincity.ecard.pay.umapay;

import android.app.Activity;
import android.content.Intent;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.pay.PayTypes;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.damai.auto.LifeManager;
import com.damai.core.ApiJob;
import com.damai.helper.ActivityResult;
import com.damai.pay.AbsDMPay;

import org.json.JSONObject;

public class UMAPay extends AbsDMPay implements ActivityResult {

	@Override
	public void onJobSuccess(ApiJob job) {
		//prepaysuccess
		Intent intent = new Intent(LifeManager.getActivity(),UMAPayActivity.class);
		JSONObject json = job.getData();
		try{
			intent.putExtra("orderId", json.getString("orderId"));
			intent.putExtra("fee", json.getInt("fee"));
			intent.putExtra("businessId", json.getInt("businessId"));
			intent.putExtra("type",  json.getInt("type"));
			intent.putExtra("title",  json.getString("title"));
			LifeManager.getCurrent().startActivityForResult(this, intent, 1000);
		}catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onJobError(ApiJob job) {
		return false;
	}

	@Override
	public boolean onApiMessage(ApiJob job) {
		return false;
	}

	@Override
	public void onActivityResult(Intent intent, int resultCode, int requestCode) {
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == 1000){
				MessageUtil.sendMessage(new IMessageListener(){
					@Override
					public void onMessage(int message) {
				//		getModel().getOrderInfo(null, true);
						getModel().notifyPaySuccess(getPayType(),null,true);
					}
				});
			}
		}
	}

	@Override
	public boolean checkInstalled() {
		return true;
	}

	@Override
	public int getPayType() {
		return PayTypes.PayType_UMA;
	}

	@Override
	public String getTitle() {
		return "移动话费支付";
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_pay_uma;
	}

}
