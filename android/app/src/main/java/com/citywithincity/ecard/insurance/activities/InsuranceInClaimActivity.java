package com.citywithincity.ecard.insurance.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.ecard.insurance.models.vos.InsurancePolicyVo;
import com.citywithincity.ecard.insurance.models.vos.InsuranceRecieverInfoVo;
import com.citywithincity.ecard.insurance.widgets.InsuranceClickableSpan.IOnTextClickListener;
import com.citywithincity.ecard.ui.activity.WebViewActivity;
import com.citywithincity.ecard.utils.DateTimeUtil_M;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.ViewUtil;

import java.text.ParseException;

@Observer
@EventHandler
public class InsuranceInClaimActivity extends BaseActivity implements IOnTextClickListener {

	private InsurancePolicyVo data;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_in_claim);
		setTitle("理赔进程");

		data = (InsurancePolicyVo) getIntent().getExtras().get(LibConfig.DATA_NAME);
		
		//获取收货人信息
		ModelHelper.getModel(InsuranceModel.class).getRecieverInfo(data.order_id);
		
		ViewUtil.setBinddataViewValues(data, this);
		
		initStatusView(data.status);
		initView();
		
		findViewById(R.id.company_container).setVisibility(View.GONE);
		
	}
	
	
	@NotificationMethod(InsuranceModel.RECIEVER_INFO)
	public void onGetRecieverInfoSuccess(InsuranceRecieverInfoVo result) {
		ViewUtil.setTextFieldValue(this, R.id.id_name, result.name);
		ViewUtil.setTextFieldValue(this, R.id.id_phone, result.delivery_tel);
		ViewUtil.setTextFieldValue(this, R.id.id_address, result.delivery_addr);
		String tip = ""; 
		if (data.status == 1) {
			tip = "未审核，请耐心等待";
		}
		if (data.status == 2) {
			tip = "审核通过，还未发货";
		} 
		
		if (TextUtils.isEmpty(result.delivery_no)) {
			ViewUtil.setTextFieldValue(this, R.id.post_code, tip);
		} else {
			ViewUtil.setTextFieldValue(this, R.id.post_code, result.delivery_no);
		}
		if (TextUtils.isEmpty(result.express_company)) {
			ViewUtil.setTextFieldValue(this, R.id.express, tip);
		} else {
			ViewUtil.setTextFieldValue(this, R.id.express, result.express_company);
		}
	}
	
	private void initView() {
		ViewUtil.setTextFieldValue(this, R.id.id_safeguard1, data.summaries[0]);
		ViewUtil.setTextFieldValue(this, R.id.id_safeguard2, data.summaries[1]);
		ViewUtil.setTextFieldValue(this, R.id.phone_num, data.service_tel);
		
		try {
			ViewUtil.setTextFieldValue(this, R.id.effective_date, DateTimeUtil_M.convertFormat(data.start_time, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (TextUtils.isEmpty(data.picc_policy_no)) {
			findViewById(R.id.picc_id_container).setVisibility(View.GONE);
		} else {
			findViewById(R.id.picc_id_container).setVisibility(View.VISIBLE);
			TextView piccId = (TextView) findViewById(R.id.picc_id); 
			piccId.setText(data.picc_policy_no);
		}
		
//		InsuranceUtil.setTextClick(this, data.protocol_title, R.color.new_btn_normal_bg_color, this);
	}
	
	private void initStatusView(int status) {
		ImageView statusView;

		if (status == 3) {
			//已理赔
			statusView = (ImageView) findViewById(R.id.insurance_step3);
			statusView.setBackgroundResource(R.drawable.ic_insurance_claims_step3_3);
			
			View view = findViewById(R.id._container1);
			view.setVisibility(View.VISIBLE);
			showClaimInfo();
			
			findViewById(R.id._id_arrow).setVisibility(View.VISIBLE);
			
			TextView tips = (TextView) findViewById(R.id.tip2);
			tips.setVisibility(View.VISIBLE);
			
		} else {
			//理赔中
			statusView = (ImageView) findViewById(R.id.insurance_step2);
			statusView.setBackgroundResource(R.drawable.ic_insurance_claims_step2_2);
			if (status == 2) {
				showClaimInfo();
			}
			if (status == 1) {
				TextView tips = (TextView) findViewById(R.id.tip1);
				tips.setVisibility(View.VISIBLE);
			}
		}
	}
	
	private void showClaimInfo() {
		if (!TextUtils.isEmpty(data.claim_result)) {
			RelativeLayout ecardInfo = (RelativeLayout) findViewById(R.id.ecard_info_card);
			ecardInfo.setVisibility(View.VISIBLE);
			
			RelativeLayout claimView = (RelativeLayout) findViewById(R.id._container2);
			claimView.setVisibility(View.VISIBLE);
			
			ViewUtil.setTextFieldValue(this, R.id.id_price, data.claim_amt);
			
		}
	}
	
	@EventHandlerId(id=R.id.make_call)
	public void onBtnMakeCall() {
		ActivityUtil.makeCall(this, data.service_tel);
	}
	
	@EventHandlerId(id=R.id.id_detail)
	public void onBtnDetail() {
		WebViewActivity.open(this,data.safeguard_url, "");
	}
	
	@EventHandlerId(id=R.id.ecard_info_card)
	public void onBtnShowClaimCard() {
		WebViewActivity.open(this,data.sample_url, "");
	}
	
	@Override
	public void onTextClick(Object tag) {
		if (null != tag) {
			if (tag.equals(data.protocol_title)) {
				WebViewActivity.open(this,data.protocol_url, data.protocol_title);
			} else {
				WebViewActivity.open(this,data.notice_url, "重要告知");
			}
		} else {
			WebViewActivity.open(this,data.notice_url, "重要告知");
		}
	}

}
