package com.citywithincity.ecard.insurance.activities.others;

import android.os.Bundle;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;

public class InsuranceNoticeClaimActivity extends BaseActivity {

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.insurance_notice_claim);
		setTitle("理赔须知");
	}

}
