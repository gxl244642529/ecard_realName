package com.citywithincity.ecard.insurance.activities.others;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.ecard.insurance.models.InsuranceUtil;
import com.citywithincity.ecard.insurance.models.vos.InsuranceDetailVo;
import com.citywithincity.ecard.insurance.widgets.InsuranceClickableSpan.IOnTextClickListener;
import com.citywithincity.ecard.ui.activity.WebViewActivity;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.paylib.IPay;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ViewUtil;

@Observer
@EventHandler
public class InsuranceTravelDetailActivity extends BaseActivity implements OnCheckedChangeListener, IOnTextClickListener {
	
	private InsuranceDetailVo detail;
	private String productId,typeId;
	private RadioGroup radioGroup;
	private TextView timeLimit;
	private CheckBox checkBox;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_travel_detail);
		
		Bundle bundle = getIntent().getExtras();
		productId = bundle.getString("insurance_id");
		typeId = bundle.getString("type_id");
		
		radioGroup = (RadioGroup) findViewById(R.id.radio_group1);
		radioGroup.setOnCheckedChangeListener(this);
		if ("5803".equals(productId)) {
			productId = "5803";
			radioGroup.check(R.id.radio_button3);
			getDetail();
		} else if ("5802".equals(productId)) {
			productId = "5802";
			radioGroup.check(R.id.radio_button2);
			getDetail();
		} else {
			productId = "5801";
			radioGroup.check(R.id.radio_button1);
			getDetail();
		}
		
		checkBox = (CheckBox) findViewById(R.id.checkbox);
		checkBox.setChecked(true);
		checkBox.setVisibility(View.VISIBLE);
	}
	
	private void getDetail() {
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
		
		ViewUtil.setBinddataViewValues(result, this);
		
		InsuranceUtil.setTextClick(this, result.protocol_title, R.color.new_btn_normal_bg_color, this);
		
		String[] summaries = detail.summary.split("\\^");
		
		ViewUtil.setTextFieldValue(this, R.id.id_safeguard1, summaries[0]);
		ViewUtil.setTextFieldValue(this, R.id.id_safeguard2, summaries[1]);
//		ViewUtil.setTextFieldValue(this, R.id.id_safeguard2, summaries[3]);
		
		ViewUtil.setTextFieldValue(this, R.id.phone_num, result.service_tel);
		
		TextView price = (TextView) findViewById(R.id.price);
		price.setText("¥" + result.price);
		
//		TextView originalPrice = (TextView) findViewById(R.id.original_price);
//		originalPrice.setVisibility(View.VISIBLE);
//		originalPrice.setText("¥" + result.ori_price);
//		originalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); //中划线
		
		ViewUtil.setTextFieldValue(this, R.id.effective_date, result.getStartTime());
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (null == timeLimit) {
			timeLimit = (TextView) findViewById(R.id.tip);
		}
		if (checkedId == R.id.radio_button1) {
			productId = "5801";
			timeLimit.setText("(7天)");
			getDetail();
		}
		if (checkedId == R.id.radio_button2) {
			productId = "5802";
			timeLimit.setText("(30天)");
			getDetail();
		}
		if (checkedId == R.id.radio_button3) {
			productId = "5803";
			timeLimit.setText("(1年)");
			getDetail();
		}
		
	}
	
	@EventHandlerId(id=R.id.id_detail)
	public void onBtnDetail(){
		WebViewActivity.open(this,detail.safeguard_url, "");
	}
	
	@EventHandlerId(id=R.id._id_ok,checkLogin = true)
	public void onBtnPurchase() {
		
		if (checkBox.isChecked()) {
			if ("2".equals(detail.typeId)) {
				ActivityUtil.startActivity(this, InsuranceDomesticInfoActivity.class,detail);
			} else if ("3".equals(detail.typeId)) {
				ActivityUtil.startActivity(this, InsuranceTravelInfoActivity.class,detail);
			}
		} else {
			Alert.alert(this, "提示", "请您认真阅读《保险条款》与《重要须知》", new DialogListener() {
				@Override
				public void onDialogButton(int id) {
					// TODO
				}
		});
		}
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
	
	@NotificationMethod(InsuranceModel.CLIENT_NOTIFY)
	public void onPiccNotifySuccess(){
		finish();
	}
	
	@EventHandlerId(id=R.id.make_call)
	public void onBtnMakeCall() {
		ActivityUtil.makeCall(this, detail.service_tel);
	}

}
