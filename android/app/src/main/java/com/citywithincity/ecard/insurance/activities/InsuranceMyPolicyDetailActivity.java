package com.citywithincity.ecard.insurance.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.ecard.insurance.models.InsuranceUtil;
import com.citywithincity.ecard.insurance.models.vos.InsurancePolicyVo;
import com.citywithincity.ecard.insurance.widgets.InsuranceClickableSpan.IOnTextClickListener;
import com.citywithincity.ecard.ui.activity.WebViewActivity;
import com.citywithincity.ecard.utils.DateTimeUtil_M;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ViewUtil;

import java.text.ParseException;

@Observer
@EventHandler
public class InsuranceMyPolicyDetailActivity extends BaseActivity implements
		IOnTextClickListener {

	private InsurancePolicyVo data;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_my_policy_detail);
		setTitle("保单详情");

		data = (InsurancePolicyVo) getIntent().getExtras().get(
				LibConfig.DATA_NAME);

		ViewUtil.setBinddataViewValues(data, this);

		initStatusView(data.status);
		initView();

		try {
			ViewUtil.setTextFieldValue(this, R.id.effective_date,
					DateTimeUtil_M.convertFormat(data.start_time,
							"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initView() {
		findViewById(R.id.company_container).setVisibility(View.GONE);
		ViewUtil.setTextFieldValue(this, R.id.id_safeguard1, data.summaries[0]);
		ViewUtil.setTextFieldValue(this, R.id.id_safeguard2, data.summaries[1]);
		ViewUtil.setTextFieldValue(this, R.id.phone_num, data.service_tel);
		findViewById(R.id.order_container).setVisibility(View.VISIBLE);
		findViewById(R.id.ecard_info_card).setVisibility(View.VISIBLE);
		InsuranceUtil.setTextClick2(this, data.protocol_title,
				R.color.new_btn_normal_bg_color, this);

		findViewById(R.id.id_container).setVisibility(View.VISIBLE);
		
		if (TextUtils.isEmpty(data.picc_policy_no)) {
			findViewById(R.id.picc_id_container).setVisibility(View.GONE);
		} else {
			findViewById(R.id.picc_id_container).setVisibility(View.VISIBLE);
			TextView piccId = (TextView) findViewById(R.id.picc_id); 
			piccId.setText(data.picc_policy_no);
		}
	}

	private void initStatusView(int status) {
		ImageView statusView = (ImageView) findViewById(R.id._id_arrow);

		if (status == 0) {
			// 保障中
			findViewById(R.id._id_ok).setVisibility(View.VISIBLE);
			statusView.setVisibility(View.VISIBLE);
			statusView.setBackgroundResource(R.drawable.ic_insurance_in_security);
		}
		
		if(status==8){
			statusView.setVisibility(View.VISIBLE);
			statusView.setBackgroundResource(R.drawable.ic_insurance_handled);
			findViewById(R.id.id_handled).setVisibility(View.VISIBLE);
		}

		if (status == 4) {
			// 已过期
			findViewById(R.id.id_btn).setVisibility(View.VISIBLE);
			statusView.setVisibility(View.VISIBLE);
			statusView.setBackgroundResource(R.drawable.ic_insurance_expires);
		}

		if (status == -1) {
			// 信息错误
		}

		if (status == 6) {
			// 未生效保单
		}

		// if (status == 1 || status == 2) {
		// //理赔中
		// }
		//
		// if (status == 3) {
		// //已理赔
		// }

	}

	// 理赔
	@EventHandlerId(id = R.id._id_ok)
	public void onBtnSettleClaim() {
		// Alert.confirm(
		// this,
		// "温馨提示",
		// "1、报失成功后您的e通卡将无法再次使用\n2、保险公司有权根据您的报失记录调整承保条件，同时保留对恶意骗保等违法行为的追责权利。\n确认报失吗？",
		// new DialogListener() {
		//
		// @Override
		// public void onDialogButton(int id) {
		// if (id == R.id._id_ok) {
		// ActivityUtil.startActivity(
		// InsuranceMyPolicyDetailActivity.this,
		// InsuranceSettleClaimActivity.class, data);
		// }
		// }
		// });
		Alert.dialog(this, Alert.OK_CANCEL).setTitle("温馨提示")
				.setCancelOnTouchOutside(true)
				.setContent(R.layout.view_insurance_alert)
				.setDialogListener(new DialogListener() {

					@Override
					public void onDialogButton(int id) {
						if (id == R.id._id_ok) {
							ActivityUtil.startActivity(
									InsuranceMyPolicyDetailActivity.this,
									InsuranceSettleClaimActivity.class, data);
						}
					}
				}).show();
	}

	// 重新购买
	@EventHandlerId(id = R.id.id_btn)
	public void onBtnPurchase() {
		ActivityUtil.startActivity(this, InsuranceProductDetailActivity.class,
				data.insurance_id);
	}

	@EventHandlerId(id = R.id.make_call)
	public void onBtnMakeCall() {
		ActivityUtil.makeCall(this, data.service_tel);
	}

	@EventHandlerId(id = R.id.id_detail)
	public void onBtnDetail() {
		WebViewActivity.open(this, data.safeguard_url, "");
	}

	@EventHandlerId(id = R.id.ecard_info_card)
	public void onBtnShowCardModel() {
		WebViewActivity.open(this, data.sample_url, "");
	}

	@Override
	public void onTextClick(Object tag) {
		if (null != tag) {
			if (tag.equals(data.protocol_title)) {
				WebViewActivity.open(this, data.protocol_url,
						data.protocol_title);
			} else {
				WebViewActivity.open(this, data.notice_url, "重要告知");
			}
		} else {
			WebViewActivity.open(this, data.notice_url, "重要告知");
		}
	}

	@NotificationMethod(InsuranceModel.LOST)
	public void onLostSuccess(boolean result) {
		finish();
	}

}
