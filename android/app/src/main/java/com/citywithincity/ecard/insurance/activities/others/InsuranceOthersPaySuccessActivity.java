package com.citywithincity.ecard.insurance.activities.others;

import android.os.Bundle;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.activities.InsuranceMyPolicyActivity;
import com.citywithincity.ecard.insurance.models.vos.InsurancePaySuccessNotifyVo;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.ViewUtil;

@Observer
@EventHandler
public class InsuranceOthersPaySuccessActivity extends BaseActivity {

	private InsurancePaySuccessNotifyVo data;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_others_pay_success);
		setTitle("支付成功");

		init();
	}
	
	private void init() {
		data = ModelHelper.getListData(); 
		ViewUtil.setBinddataViewValues(data, this);
	}
	
	@EventHandlerId(id=R.id.id_btn)
	public void onBtnMyOrder() {
		ActivityUtil.startActivity(this, InsuranceMyPolicyActivity.class);
		finish();
	}

}
