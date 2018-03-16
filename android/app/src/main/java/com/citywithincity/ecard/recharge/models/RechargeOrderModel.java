package com.citywithincity.ecard.recharge.models;

import android.view.View;
import android.widget.EditText;

import com.citywithincity.ecard.recharge.models.vos.RechargeOrderResult;
import com.citywithincity.ecard.recharge.models.vos.RechargeVo;
import com.citywithincity.models.LocalData;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.core.Crypt;
import com.damai.models.DMModel;
import com.damai.util.StrKit;

public class RechargeOrderModel extends DMModel {
	
	public static final String SUB_KEY = "recharge_ecard";
	
	public static final String submit = "recharge/submit";
	
	public static final String refund = "recharge/refund";
	public static final String invoce = "recharge/invoce";
	public static final String getStatus = "recharge/getStatus";


    public  static final String payResult = "recharge/payResult";

	public static final String otherList = "recharge/otherList";
	
	public RechargeOrderModel(){
		super();
	}
	
	public void load(EditText text){
		text.setText(LocalData.read().getString(SUB_KEY, ""));
	}
	
	public void save(String cardId) {
		LocalData.write().putString(SUB_KEY, cardId).save();
	}

	public void getPayInfo(String orderId,ApiListener listener) {
        ApiJob job = createJob(payResult);
        job.put("id", orderId);
        job.setServer(1);
        job.setApiListener(listener);
        job.setEntity(RechargeOrderResult.class);
        job.setCrypt(Crypt.BOTH);
        job.execute();
	}
	
	
	
	/**
	 * 查询其他人的订单
	 */
	public void otherList(String file05,String file15,String cardRan){
		ApiJob job = createJob(otherList);
		job.put("file05",file05);
		job.put("file15", file15);
		job.put("cardRan",cardRan);
		job.setEntity(RechargeVo.class);
		job.setWaitingMessage("正在查询订单...");
		job.setServer(1);
		job.execute();
	}
	
	
	/**
	 * 提交订单
	 */
	public void submit(int fee,String cardId,ApiListener listener){
		
		ApiJob job = createJob(submit);
		job.put("cardId", cardId);
		job.setWaitingMessage("正在提交订单...");
		job.put("fee", fee);
		job.setServer(1);
		job.setCrypt(Crypt.BOTH);
		job.setApiListener(listener);
		job.execute();
	}

	/**
	 * 退款
	 */
	public void refund(RechargeVo data,View button){
		ApiJob job = createJob(refund);
		job.put("id", data.getTyId());
		job.setWaitingMessage("正在退款...");
		job.setServer(1);
		job.setTimeoutMS(10000);
		job.setCrypt(Crypt.BOTH);
		job.setButton(button);
		job.execute();
	}

	
	/**
	 * 领取发票
	 */
	public void invoce(RechargeVo data,View button){
		ApiJob job = createJob(invoce);
		job.put("id", data.getTyId());
		job.setWaitingMessage("请稍等...");
		job.setServer(1);
		job.setButton(button);
		job.execute();
	}

	public void getStatus(String tyId) {

		ApiJob job = createJob(getStatus);
		job.put("id", tyId);
		job.setWaitingMessage("正在获取订单状态...");
		job.setServer(1);
		job.execute();
	}


}
