package com.citywithincity.ecard.discard.models;

import android.view.View;

import com.citywithincity.ecard.discard.vos.BookInfo;
import com.damai.core.ApiJob;
import com.damai.models.DMModel;
import com.damai.widget.vos.AddressVo;

public class DiscardModel extends DMModel {
	
	public static final String SUBMIT = "book/submit";
	
	/**
	 * 提交订单
	 */
	public void submit(boolean recharge,BookInfo data,AddressVo address,View button){
		ApiJob job = createJob(SUBMIT);
		job.setButton(button);
		/**
		 * 提交哪一单
		 */
		job.put("phone", address.getPhone());
		job.put("name", address.getName());
		job.put("address", AddressVo.formatAddress(address));
		job.put("cardId", data.getCardId());
		job.put("idCard", data.getIdCard());
		job.put("savType", data.getSavType());
		job.put("recharge", recharge);
		job.put("gdId", 0);
		job.put("type", data.getType());
		job.setTimeoutMS(30000);
		job.setServer(1);
		job.setWaitingMessage("正在提交订单...");
		
		job.execute();
	}

}
