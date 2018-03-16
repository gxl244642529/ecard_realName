package com.citywithincity.ecard.insurance.activities.others;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.ecard.insurance.models.vos.InsuranceDetailVo;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;

@Observer
@EventHandler
public class InsuranceOffLineActivity extends BaseActivity {

	private InsuranceDetailVo detail;
	private String productId,typeId;
	private ImageView imageView;
	private String title;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_off_line);

		Bundle bundle = getIntent().getExtras();
		productId = bundle.getString("insurance_id");
		typeId = bundle.getString("type_id");
//		String imageUrl = bundle.getString("icon_url");
		title = bundle.getString("title");
		setTitle(title);
		
		ModelHelper.getModel(InsuranceModel.class).getDetail(productId);
		Alert.wait(this, "正在加载数据……");
		Alert.waitCanceledOnTouchOutside(false);
		
		((Button)findViewById(R.id._id_ok)).setText("投保热线：" + getResources().getString(R.string.safe_piccc_off_line_insure_phone));
		
	}
	
	@NotificationMethod(InsuranceModel.DETAIL)
	public void onGetDetailSuccess(InsuranceDetailVo result) {
		
		Alert.cancelWait();
		
		setTitle(result.title);
		
		findViewById(R.id._id_ok).setEnabled(true);
		
		detail = result;
		detail.insurance_id = productId;
		detail.typeId = typeId;
		
		imageView = (ImageView) findViewById(R.id.id_image);
		ECardJsonManager.getInstance().setImageSrc(detail.detail_url, imageView);
		
	}
	
	@EventHandlerId(id=R.id._id_ok)
	public void onBtnCall() {
		ActivityUtil.makeCall(this, getResources().getString(R.string.safe_piccc_off_line_insure_phone));
	}
	
	@NotificationMethod(InsuranceModel.DETAIL_ERROR)
	public void onGetDetailError(String err, boolean isNetworkError) {
		if (!isNetworkError) {
			Alert.showShortToast("数据获取失败");
			finish();
		}
	}

}
