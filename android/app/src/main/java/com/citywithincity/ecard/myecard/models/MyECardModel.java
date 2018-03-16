package com.citywithincity.ecard.myecard.models;

import com.damai.core.ApiJob;
import com.damai.models.DMModel;

public class MyECardModel extends DMModel {
	
	
	public static final String unbind = "ecard/unbind";
	public static final String bindBarcode = "ecard/bindBarcode";
	public static final String bind = "ecard/bind";
	public static final String is = "real/is";
	/**
	 * 解绑
	 * @param cardId
	 */
	public void unbind(String cardId){
		
		ApiJob job = createJob(unbind);
		job.setServer(1);
		job.setWaitingMessage("请稍等...");
		job.put("cardId", cardId);
		
		job.execute();
		
	}

	public void bind(String barcode) {
		ApiJob job = createJob(bindBarcode);
		job.setServer(1);
		job.setWaitingMessage("请稍等...");
		job.put("barcode", barcode);
		
		job.execute();
	}


	public void bindCard(String cardId,String name) {
		ApiJob job = createJob(bind);
		job.setServer(1);
		job.setWaitingMessage("请稍等...");
		job.put("cardId", cardId);
		job.put("name", name);
		job.execute();
	}

	public void isReal(){
		ApiJob job = createJob(is);
		job.setServer(1);
		job.setWaitingMessage("请稍等...");

		job.execute();
	}

}
