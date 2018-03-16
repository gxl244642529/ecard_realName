package com.citywithincity.ecard.insurance.activities.others;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceUtil;
import com.citywithincity.ecard.insurance.models.vos.InsurancePolicyVo;
import com.citywithincity.ecard.insurance.widgets.InsuranceClickableSpan.IOnTextClickListener;
import com.citywithincity.ecard.ui.activity.WebViewActivity;
import com.citywithincity.ecard.utils.DateTimeUtil_M;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.ViewUtil;

import java.text.ParseException;

@Observer
@EventHandler
public class InsurancePolicyTravelActivity extends BaseActivity implements IOnTextClickListener {

	private InsurancePolicyVo data;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_policy_travel);
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
		ViewUtil.setTextFieldValue(this, R.id.id_safeguard1, data.summaries[0]);
		ViewUtil.setTextFieldValue(this, R.id.id_safeguard2, data.summaries[1]);
		ViewUtil.setTextFieldValue(this, R.id.phone_num, data.service_tel);
		
		ViewUtil.setTextFieldValue(this, R.id.insured, data.applicants.BUYER_NAME);
		ViewUtil.setTextFieldValue(this, R.id.insured_id, data.applicants.BUYER_ID);
		ViewUtil.setTextFieldValue(this, R.id.count, data.qty + "份");
		
		if (data.insurants != null && data.insurants.size() > 0) {
			View view = findViewById(R.id.info_container);
			view.setVisibility(View.VISIBLE);
			ViewUtil.setTextFieldValue(this, R.id.insured1, data.insurants.get(0).insurant_name);
			ViewUtil.setTextFieldValue(this, R.id.insured_id1, data.insurants.get(0).insurant_pid);
		}
		
		if (TextUtils.isEmpty(data.picc_policy_no)) {
			findViewById(R.id.picc_id_container).setVisibility(View.GONE);
		} else {
			findViewById(R.id.picc_id_container).setVisibility(View.VISIBLE);
			TextView piccId = (TextView) findViewById(R.id.picc_id); 
			piccId.setText(data.picc_policy_no);
		}
		
		InsuranceUtil.setTextClick2(this, data.protocol_title,
				R.color.new_btn_normal_bg_color, this);
	}

	private void initStatusView(int status) {
		ImageView statusView = (ImageView) findViewById(R.id._id_arrow);

		if (status == 0) {
			// 保障中
			findViewById(R.id._id_ok).setVisibility(View.VISIBLE);
			statusView.setVisibility(View.VISIBLE);
			statusView
					.setBackgroundResource(R.drawable.ic_insurance_in_security);
		}

		if (status == 4) {
			// 已过期
			statusView.setVisibility(View.VISIBLE);
			statusView.setBackgroundResource(R.drawable.ic_insurance_expires);
		}

		if (status == -1) {
			// 信息错误
		}

		if (status == 6) {
			// 未生效保单
		}

	}
	
	@EventHandlerId(id = R.id.id_detail)
	public void onBtnDetail() {
		WebViewActivity.open(this, data.safeguard_url, "");
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
	
	@EventHandlerId(id = R.id.make_call)
	public void onBtnMakeCall() {
		ActivityUtil.makeCall(this, data.service_tel);
	}
	
	@EventHandlerId(id = R.id._id_ok)
	public void onBtnSettleClaim() {
		ActivityUtil.startActivity(this, InsuranceNoticeClaimActivity.class);
	}

}
