package com.citywithincity.ecard.insurance.models;

import android.content.Context;
import android.content.Intent;

import com.citywithincity.ecard.insurance.activities.InsuranceProductDetailActivity;
import com.citywithincity.ecard.ui.activity.MyECardActivity;
import com.citywithincity.ecard.utils.MyECardUtil;
import com.citywithincity.utils.ActivityUtil;
import com.damai.helper.ActivityResult;
import com.damai.webview.urlparser.WebViewUrlParser;

public class SafeTicktetParser implements WebViewUrlParser {

	@Override
	public void parseUrl(Context context, String command, String[] args) {
		if(command.equals("safe_ticket")){
			InsuranceProductDetailActivity.isFromInsuranceActionActivity = true;
			String ticket = args[2];
			ActivityUtil.startActivity(context, InsuranceProductDetailActivity.class,ticket);
		}else if(command.equals("select")){

			MyECardUtil.selectECard(new ActivityResult() {
				@Override
				public void onActivityResult(Intent intent, int i, int i1) {



				}
			},context);

		}
	}

}
