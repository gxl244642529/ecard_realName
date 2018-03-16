package com.citywithincity.ecard.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.citywithincity.activities.BaseFragmentActivity;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.enums.Actions;
import com.citywithincity.ecard.models.MyECardModel;
import com.citywithincity.ecard.ui.fragment.BindECardStep1;
import com.citywithincity.ecard.ui.fragment.BindECardStep1.IBindECardStep1;
import com.citywithincity.ecard.ui.fragment.BindECardStep2_1;
import com.citywithincity.ecard.ui.fragment.BindECardStep2_2;
import com.citywithincity.ecard.ui.fragment.BindECardStep2_2.IBindStep2;
import com.citywithincity.ecard.ui.fragment.BindECardStep3_1;
import com.citywithincity.ecard.ui.fragment.BindECardStep3_2;
import com.citywithincity.ecard.utils.SystemUtil;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.KeyboardUtil;

@Observer
public class BindECardActivity extends BaseFragmentActivity implements  IBindECardStep1, IBindStep2 {
	
	
	public static void requestBindECard(Activity activity) {
		ActivityUtil.startActivityForResult(activity, BindECardActivity.class,
				Actions.REQUEST_CODE_BIND_ECARD);
	}
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_bind_ecard);
		setTitle("绑定e通卡");
		BindECardStep1 step1 = new BindECardStep1();
		step1.setListener(this);
		replaceFragment( step1);
	}
	

	@Override
	public void onBindNormal() {
		BindECardStep2_1 fragment = new BindECardStep2_1();
		fragment.setLayout(R.layout.fragment_bind_step2_1);
		fragment.setListener(this);
		addFragment( fragment);
	}
	
	@Override
	public void onBindExt() {
		BindECardStep2_2 fragment = new BindECardStep2_2();
		fragment.setLayout(R.layout.fragment_bind_step2_2);
		fragment.setListener(this);
		addFragment( fragment);
	}
	
	protected void replaceFragment(Fragment fragment){
		SystemUtil.replaceFragment(this, R.id._container, fragment,false,false);
	}
	protected void addFragment(Fragment fragment){
		SystemUtil.addFragment(this, R.id._container, fragment);
	}

	@Override
	public void onStep2(int resID) {
		if(resID == R.layout.fragment_bind_step2_1)
		{
			BindECardStep3_1 fragment = new BindECardStep3_1();
			addFragment(fragment);
		}else{
			BindECardStep3_2 fragment = new BindECardStep3_2();
			addFragment(fragment);
		}
	}


	@Override
	public void finish() {
		if(getCurrentFocus()!=null)
		{
			KeyboardUtil.hideSoftKeyboard(this,getCurrentFocus());
		}
		super.finish();
	}
	
	@NotificationMethod(MyECardModel.ECARD_BIND)
	public void onEcardBindSuccess(Object result) {
		finish();
	}


}
