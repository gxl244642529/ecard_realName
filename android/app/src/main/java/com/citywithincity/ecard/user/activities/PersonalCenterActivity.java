package com.citywithincity.ecard.user.activities;

import android.content.Intent;
import android.os.Bundle;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.activities.InsuranceMyPolicyActivity;
import com.citywithincity.ecard.myecard.activities.MyECardActivity;
import com.citywithincity.ecard.recharge.activities.RechargeOrderListActivity;
import com.citywithincity.ecard.selling.activities.SAllMyOrderActivity;
import com.damai.auto.DMActivity;
import com.damai.helper.a.Event;

public class PersonalCenterActivity extends DMActivity {

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_user_center);
		setTitle("我的");

	}
	
	@Event
	public void onSetting(){
		startActivity(new Intent(this,SettingActivity.class));
	}

	@Event(requreLogin=true)
	public void my_recharge(){
		startActivity(new Intent(this,RechargeOrderListActivity.class));
	}
	
	@Event(requreLogin=true)
	public void onMyEcard(){
		
		startActivity(new Intent(this,MyECardActivity.class));
	}
	
	@Event(requreLogin=true)
	public void id_insurance(){
		startActivity(new Intent(this, InsuranceMyPolicyActivity.class));
	}
	
	@Event(requreLogin=true)
	public void my_order(){
		startActivity(new Intent(this,SAllMyOrderActivity.class));
	}
}
