package com.citywithincity.ecard.insurance.activities;

import android.os.Bundle;
import android.widget.ImageView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.ecard.insurance.models.vos.InsuranceClientNotifySuccessVo;
import com.citywithincity.ecard.insurance.models.vos.InsurancePolicyVo;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ViewUtil;
/**
 * 资料提交失败，用户重新提交
 * @author admin
 *
 */
@Observer
@EventHandler
public class InsuranceValidateSubmitInfoActivity extends BaseActivity {

	private InsurancePolicyVo data;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_valitate_submit_info);
		setTitle("保单详情");

		data = (InsurancePolicyVo) getIntent().getExtras().get(LibConfig.DATA_NAME);
		
		initView();
	}
	
	private void initView() {
		
		ECardJsonManager.getInstance().setImageSrc(data.background_url, (ImageView) findViewById(R.id.id_image));
		
		ViewUtil.setTextFieldValue(this, R.id.id_safeguard1, data.summaries[0]);
		ViewUtil.setTextFieldValue(this, R.id.id_safeguard2, data.summaries[1]);
		
		ViewUtil.setTextFieldValue(this, R.id.id_name, data.name);
		ViewUtil.setTextFieldValue(this, R.id.id_idcard_no, data.id_card);
		ViewUtil.setTextFieldValue(this, R.id.id_phone, data.phone);
		ViewUtil.setTextFieldValue(this, R.id.name, data.product_name);
		
		ViewUtil.setTextFieldValue(this, R.id.total_pay_price, data.product_price);
	}
	
	@EventHandlerId(id=R.id._id_ok)
	public void onBtnSubmit() {
		ModelHelper.getModel(InsuranceModel.class).clientNotify(data.id, data.id_card, data.phone, data.addr, data.name);
	}
	
	@NotificationMethod(InsuranceModel.CLIENT_NOTIFY)
	public void onBtnNotifySuccess(InsuranceClientNotifySuccessVo result) {
		Alert.alert(this, "提示", "资料提交成功", new DialogListener() {
			
			@Override
			public void onDialogButton(int id) {
				ActivityUtil.startActivity(InsuranceValidateSubmitInfoActivity.this, InsuranceMyPolicyActivity.class);
				finish();
			}
		});
	}
	
	

}
