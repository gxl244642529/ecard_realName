package com.citywithincity.ecard.myecard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.base.BaseFragmentActivity;
import com.citywithincity.ecard.myecard.fragment.BindECardStep2_1;
import com.citywithincity.ecard.myecard.fragment.BindECardStep2_2;
import com.citywithincity.ecard.myecard.fragment.BindECardStep2_2.IBindStep2;
import com.citywithincity.ecard.myecard.fragment.BindECardStep3_1;
import com.citywithincity.ecard.myecard.fragment.BindECardStep3_2;
import com.citywithincity.ecard.ui.activity.MyECardScanActivity;
import com.citywithincity.utils.FragmentUtil;
import com.citywithincity.utils.KeyboardUtil;
import com.damai.helper.a.Event;
import com.damai.http.api.a.JobSuccess;

/**
 * 绑定e通卡
 * @author renxueliang
 *
 */
public class BindECardActivity extends BaseFragmentActivity implements  IBindStep2 {

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_bind_ecard);
		setTitle("绑定e通卡");
	}
	

	@Event
	public void bind_by_scan(){
		startActivity(new Intent(this,MyECardScanActivity.class));
		finish();
	}
	
	@Event
	public void bind_normal_card(){
		BindECardStep2_1 fragment = new BindECardStep2_1();
		fragment.setLayout(R.layout.fragment_bind_step2_1);
		fragment.setListener(this);
		addFragment( fragment);
	}
	
	@Event
	public void bind_ext_card(){
		BindECardStep2_2 fragment = new BindECardStep2_2();
		
		fragment.setLayout(R.layout.fragment_bind_step2_2);
		fragment.setListener(this);
		addFragment( fragment);
	}
	
	


	protected void addFragment(Fragment fragment){
		FragmentUtil.addFragment(this, R.id._container, fragment);
	}

	@Override
	public void onStep2(int resID) {
		Fragment newFragment = ((resID == R.layout.fragment_bind_step2_1 )?  
				new BindECardStep3_1() :  new BindECardStep3_2());
		FragmentUtil.addFragment(this, R.id._container, newFragment);
	}

	@Override
	public void finish() {
		if(getCurrentFocus()!=null)
		{
			KeyboardUtil.hideSoftKeyboard(this,getCurrentFocus());
		}
		super.finish();
	}
	
	@JobSuccess({"ecard/bind","ecard_bind2"})
	public void onEcardBindSuccess(Object result) {
		finish();
	}


}
