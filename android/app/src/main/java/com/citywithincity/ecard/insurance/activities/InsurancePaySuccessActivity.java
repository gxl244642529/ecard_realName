package com.citywithincity.ecard.insurance.activities;

import android.os.Bundle;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceUtil;
import com.citywithincity.ecard.insurance.models.vos.InsurancePaySuccessNotifyVo;
import com.citywithincity.ecard.insurance.widgets.InsuranceClickableSpan.IOnTextClickListener;
import com.citywithincity.ecard.ui.activity.WebViewActivity;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.ViewUtil;

@Observer
@EventHandler
public class InsurancePaySuccessActivity extends BaseActivity implements IOnTextClickListener {

	private InsurancePaySuccessNotifyVo data;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_pay_success);
		setTitle("支付成功");
		
		data = ModelHelper.getListData(); 
		ViewUtil.setBinddataViewValues(data, this);
		
		ViewUtil.setTextFieldValue(this, R.id.phone_num, data.serviceTel);
		
		InsuranceUtil.setTextClick2(this, data.protocol_title, R.color.new_btn_normal_bg_color, this);

	}
	
	@EventHandlerId(id=R.id.make_call)
	public void onBtnCall() {
		ActivityUtil.makeCall(this, data.serviceTel);
	}
	
	@EventHandlerId(id=R.id.id_btn)
	public void onBtnMyOrder() {
		ActivityUtil.startActivity(this, InsuranceMyPolicyActivity.class);
		finish();
	}
	
	@Override
	public void onTextClick(Object tag) {
		if (null != tag) {
			if (tag.equals(data.protocol_title)) {
				WebViewActivity.open(this,data.protocolUrl, data.protocol_title);
			} else {
				WebViewActivity.open(this,data.noticeUrl, "重要告知");
			}
		} else {
			WebViewActivity.open(this,data.noticeUrl, "重要告知");
		}
	}

}
