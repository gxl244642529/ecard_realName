package com.citywithincity.ecard.pay.umapay;

import android.widget.Button;

import com.citywithincity.auto.Crypt;
import com.damai.core.ApiJob;
import com.damai.models.DMModel;

public class UmaModel extends DMModel {
	
	public static final String pay = "uma/pay";
	public static final String verify = "uma/verify";
	public void pay(String verifyId,String phone,String code,String orderId,int businessId,int type,int fee,Button button){
		ApiJob job  = createJob(pay);
		job.put("phone", phone);
		job.put("orderId", orderId);
		job.put("businessId", businessId);
		job.put("type", type);
		job.put("fee", fee);
		job.put("code", code);
		job.put("verifyId", verifyId);
		job.setButton(button);
		job.setServer(1);
		
		job.setWaitingMessage("正在支付...");
		
		job.setCrypt(Crypt.BOTH);
		job.execute();
		
	}
	
	
}
