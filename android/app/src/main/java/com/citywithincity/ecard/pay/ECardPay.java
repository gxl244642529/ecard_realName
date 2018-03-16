package com.citywithincity.ecard.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.paylib.EAccountPayActivity;
import com.damai.auto.LifeManager;
import com.damai.core.ApiJob;
import com.damai.helper.ActivityResult;
import com.damai.pay.AbsDMPay;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ECardPay extends AbsDMPay implements ActivityResult {

	private String platId;
	@Override
	public void onJobSuccess(ApiJob job) {
		
		JSONObject jsonObject = job.getData();
		
		try {
			double fee = jsonObject.getDouble("fee");
			platId = jsonObject.getString("order_id");
			//进入支付界面
			IViewContainer container= LifeManager.getCurrent();
			Intent intent = new Intent(container.getActivity(),EAccountPayActivity.class);
			intent.putExtra("fee", String.format("%.02f",fee ));
			intent.putExtra("sign",  jsonObject.getString("sign"));
			intent.putExtra("platId", platId);
			container.startActivityForResult(this, intent, 200);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(Intent intent, int resultCode, int requestCode) {
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == 200){
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("platId", platId);
				getModel().notifyPaySuccess(getPayType(),data,true);
				//getModel().getOrderInfo(data, true);
			}
		}else if(resultCode == Activity.RESULT_CANCELED){
            if(requestCode == 200){
                //取消
                getModel().notifyUserCancel(getPayType());
            }
        }else if(resultCode == ECardAccountPayModel.RESULT_ERROR){
            Bundle bundle = intent.getExtras();
            getModel().notifyPayError(getPayType(),bundle.getString("error"),bundle.getBoolean("isNetworkError"));
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
		return PayTypes.PayType_ECard;
	}

	@Override
	public String getTitle() {
		return "e通卡账户支付";
	}
	
	@Override
	public int getIcon() {
		return R.drawable.ecard_pay;
	}

	

}
