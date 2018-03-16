package com.citywithincity.ecard.insurance.models;

import android.widget.Button;

import com.citywithincity.auto.Crypt;
import com.citywithincity.ecard.insurance.models.vos.InsuranceOtherPurchaseSuccessVo;
import com.citywithincity.ecard.insurance.models.vos.InsurancePaySuccessNotifyVo;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.interfaces.IObjectJsonTask;
import com.citywithincity.models.cache.CachePolicy;
import com.damai.core.ApiJob;
import com.damai.core.DMLib;

import java.io.File;
import java.util.List;
import java.util.Map;

public class MyInsuranceModel {

	private static MyInsuranceModel instance;

	public static MyInsuranceModel getInstance() {
		if (instance == null) {
			instance = new MyInsuranceModel();
		}

		return instance;
	}

	// 保险下单
	public static final String SUBMIT = "i_safe/submitV2";
	
	
	//购买多人保险
	public static final String SUBMIT_INSURED = "i_safe/submitInsured";
	
	public static final String SUBMIT_INSURED_ERROR = "i_safe/submitInsured_error";

	public void submit(Button button,String inId, String idCard, File addFile, String cardId,
			String name, String phone, String ticket) {
		
		ApiJob task = DMLib.getJobManager().createObjectApi(SUBMIT);
		task.setEntity(InsurancePaySuccessNotifyVo.class);
		/*
		
		IObjectJsonTask<InsurancePaySuccessNotifyVo> task = ECardJsonManager
				.getInstance().createObjectJsonTask(SUBMIT,
						CachePolicy.CachePolity_NoCache,
						InsurancePaySuccessNotifyVo.class,1);
		task.clearParam();*/
		task.setServer(1);
		task.setButton(button);
		task.put("inId", inId);// 验证码id
		task.put("idCard", idCard);// 用户输入的验证码
		task.put("cardId", cardId);
		task.put("name", name);// 推送id
		task.put("phone", phone);
		task.put("ticket", ticket);
		task.put("idImg",addFile);
		task.setCrypt(Crypt.BOTH);
		task.setWaitingMessage("正在提交资料……");
		
		task.execute();

	}
	
	/**
	 * 购买多人保险
	 * @param customer
	 * @param json
	 * @param insured
	 * @return
	 */
	@Crypt(Crypt.BOTH)
	public void submitInsured(String insuranceId, String typeId, String phone, String name,
							  String ID, int count, List<Map<String,Object>> array, Map<String, Object> data) {
		IObjectJsonTask<InsuranceOtherPurchaseSuccessVo> task = ECardJsonManager
				.getInstance().createObjectJsonTask(SUBMIT_INSURED,
						CachePolicy.CachePolity_NoCache,
						InsuranceOtherPurchaseSuccessVo.class,1);
		task.clearParam();
		task.put("inId", insuranceId);//保险id
		task.put("typeId", typeId);//类型号
		task.put("phone", phone);//电话
		task.put("name", name);//姓名
		task.put("idCard", ID);//身份证号
		if(data!=null){
			task.putAll(data);
		}
		
		//task.put("addr", addr);//家庭地址
		task.put("count", count);//购买份数
		if (array != null) {
			task.put("insured", array);//被投保人
		}
		task.setCrypt(Crypt.BOTH);
		task.setWaitMessage("正在提交资料……");
		task.enableAutoNotify();
		task.execute();
	}
	
}
