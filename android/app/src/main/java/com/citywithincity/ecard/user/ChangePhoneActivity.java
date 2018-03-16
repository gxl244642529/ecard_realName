package com.citywithincity.ecard.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.citywithincity.activities.BaseFragmentActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.user.ChangePhoneStep1Fragment.OnVerifyOrgSuccess;
import com.citywithincity.ecard.utils.SystemUtil;

public class ChangePhoneActivity extends BaseFragmentActivity implements OnVerifyOrgSuccess {

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_change_phone);
		setTitle("换绑手机");
		
		ChangePhoneStep1Fragment step1 = new ChangePhoneStep1Fragment();
		step1.setListener(this);
		replaceFragment(step1);
	}
	
	@Override
	public void verifySuccess(Object verifyId) {
		ChangePhoneStep2Fragment step2 = new ChangePhoneStep2Fragment();
		addFragment(step2);
	}
	
	protected void replaceFragment(Fragment fragment){
		SystemUtil.replaceFragment(this, R.id._container, fragment,false,false);
	}
	protected void addFragment(Fragment fragment){
		SystemUtil.addFragment(this, R.id._container, fragment);
	}

}
