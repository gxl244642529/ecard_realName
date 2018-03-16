package com.citywithincity.ecard.pay;

import android.view.View;

import com.citywithincity.auto.Crypt;
import com.citywithincity.utils.CryptAES;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.models.DMModel;

public class ECardAccountPayModel extends DMModel {

	public static final String PAY = "ecard_pay/pay";

    public static final int RESULT_ERROR = 999;
	
	public void pay(String account, String pwd, String key, String platId, View button, ApiListener listener){
		ApiJob job = createJob(PAY);
		
		job.setButton(button);
		String cryptPwd = CryptAES.encrypt(key, pwd);
		job.put("cardId", account);
		job.put("pwd", cryptPwd);
		job.put("platId", platId);
		job.setTimeoutMS(10000);
		job.setApiListener(listener);
		job.setCrypt(Crypt.BOTH);
		job.setWaitingMessage("请稍等...");
		job.setServer(1);
		job.execute();
	}
	
}
