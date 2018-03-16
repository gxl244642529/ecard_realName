package com.citywithincity.ecard.insurance.activities.others;

import android.os.Bundle;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.ecard.insurance.models.vos.InsuranceDetailVo;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.Alert;

@Observer
@EventHandler
public class InsuranceLifeDetialActivity extends BaseActivity {

	private String productId,typeId;
	private InsuranceDetailVo detail;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_life_detial);

		Bundle bundle = getIntent().getExtras();
		productId = bundle.getString("insurance_id");
		typeId = bundle.getString("type_id");
		
		ModelHelper.getModel(InsuranceModel.class).getDetail(productId);
		Alert.wait(this, "正在加载数据……");
		Alert.waitCanceledOnTouchOutside(false);
	}
	
	@NotificationMethod(InsuranceModel.DETAIL_ERROR)
	public void onGetDetailError(String err, boolean isNetworkError) {
		if (!isNetworkError) {
			Alert.showShortToast("数据获取失败");
			finish();
		}
	}
	
	@NotificationMethod(InsuranceModel.DETAIL)
	public void onGetDetailSuccess(InsuranceDetailVo result) {
		Alert.cancelWait();
		
		setTitle(result.title);
		
		findViewById(R.id._id_ok).setEnabled(true);
		
		detail = result;
		detail.insurance_id = productId;
		detail.typeId = typeId;
	}

}
