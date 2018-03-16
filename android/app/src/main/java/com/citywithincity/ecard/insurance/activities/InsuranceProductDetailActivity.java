package com.citywithincity.ecard.insurance.activities;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.ecard.insurance.models.InsuranceUtil;
import com.citywithincity.ecard.insurance.models.MyInsuranceModel;
import com.citywithincity.ecard.insurance.models.vos.InsuranceDetailVo;
import com.citywithincity.ecard.insurance.models.vos.InsurancePaySuccessNotifyVo;
import com.citywithincity.ecard.insurance.widgets.InsuranceClickableSpan.IOnTextClickListener;
import com.citywithincity.ecard.ui.activity.WebViewActivity;
import com.citywithincity.ecard.utils.DateTimeUtil_M;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.paylib.IPay;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ViewUtil;

import java.text.ParseException;

@Observer
@EventHandler
public class InsuranceProductDetailActivity extends BaseActivity implements IOnTextClickListener{

	private InsuranceDetailVo detail;
//	private InsuranceListVo listVo;
	private CheckBox checkBox;
	private String productId = "5701";
	private String ticket;
	
	public static boolean isFromInsuranceActionActivity = false;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_product_detail);
		
		if(isFromInsuranceActionActivity) {
			ticket = (String) getIntent().getExtras().get(LibConfig.DATA_NAME);
		} else {
			productId = (String) getIntent().getExtras().get(LibConfig.DATA_NAME);
		}
		ModelHelper.getModel(InsuranceModel.class).getDetail(productId);
		Alert.wait(this, "正在加载数据……");
		Alert.waitCanceledOnTouchOutside(false);
		
		checkBox = (CheckBox) findViewById(R.id.checkbox);
		checkBox.setChecked(true);
		checkBox.setVisibility(View.VISIBLE);
		
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
		
		if(isFromInsuranceActionActivity) {
			result.ticket = ticket;
		}
		
		setTitle(result.title);
		
		findViewById(R.id._id_ok).setEnabled(true);
		
		detail = result;
		detail.insurance_id = productId;
		ViewUtil.setBinddataViewValues(result, this);
		
		InsuranceUtil.setTextClick(this, result.protocol_title, R.color.new_btn_normal_bg_color, this);
		
		ViewUtil.setTextFieldValue(this, R.id.id_safeguard1, result.summaries[0]);
		ViewUtil.setTextFieldValue(this, R.id.id_safeguard2, result.summaries[1]);
		
		ViewUtil.setTextFieldValue(this, R.id.phone_num, result.service_tel);
		
		TextView price = (TextView) findViewById(R.id.price);
		if(isFromInsuranceActionActivity) {
			price.setText("免费");
			price.setTextSize(14);
		} else {
			price.setText("¥" + result.price);
		}
		
		TextView originalPrice = (TextView) findViewById(R.id.original_price);
		originalPrice.setVisibility(View.VISIBLE);
		originalPrice.setText("¥" + result.ori_price);
		originalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); //中划线
		
		try {
			ViewUtil.setTextFieldValue(this, R.id.effective_date, DateTimeUtil_M.convertFormat(result.start_time, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@EventHandlerId(id=R.id.id_detail)
	public void onBtnDetail(){
		WebViewActivity.open(this,detail.safeguard_url, "");
	}
	
	@EventHandlerId(id=R.id._id_ok,checkLogin = true)
	public void onBtnPurchase() {
		if (checkBox.isChecked()) {
			ActivityUtil.startActivity(this, InsurancePersonalInfoActivity.class,detail);
		} else {
			Alert.alert(this, "提示", "请您认真阅读《保险条款》与《重要须知》", new DialogListener() {
				
				@Override
				public void onDialogButton(int id) {
					// TODO
				}
			});
		}
		
		
	}
	
	@EventHandlerId(id=R.id.make_call)
	public void onBtnMakeCall() {
		ActivityUtil.makeCall(this, detail.service_tel);
	}


	@Override
	public void onTextClick(Object tag) {
		if (null != tag) {
			if (tag.equals(detail.protocol_title)) {
				WebViewActivity.open(this,detail.protocol_url, detail.protocol_title);
			} else {
				WebViewActivity.open(this,detail.notice_url, "重要告知");
			}
		} else {
			WebViewActivity.open(this,detail.notice_url, "重要告知");
		}
	}
	
	@NotificationMethod(IPay.PAY_SUCCESS)
	public void onPaySuccess(Object result) {
		finish();
	}
	
	@NotificationMethod(IPay.PAY_ERROR)
	public void onPayError(Object result) {
		finish();
	}
	
	@NotificationMethod(IPay.PAY_CANCEL)
	public void onPayCancel(Object result) {
		finish();
	}
	
	@NotificationMethod(InsuranceModel.CLIENT_NOTIFY_ERROR)
	public void onPiccNotifyFailed(String error,boolean isNetworkError) {
		Alert.cancelWait();
		finish();
	}
	
	@NotificationMethod(MyInsuranceModel.SUBMIT)
	public void onPurchaseSuccess(InsurancePaySuccessNotifyVo result) {
		finish();
	}
	
	@Override
	public void onBackPressed() {
		InsuranceProductDetailActivity.isFromInsuranceActionActivity = false;
		finish();
		super.onBackPressed();
	}
	
	@Override
	protected void backToPrevious() {
		InsuranceProductDetailActivity.isFromInsuranceActionActivity = false;
		finish();
		super.backToPrevious();
	}

}
