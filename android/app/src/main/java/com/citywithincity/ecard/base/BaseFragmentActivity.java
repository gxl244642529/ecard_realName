package com.citywithincity.ecard.base;

import com.damai.auto.DMFragmentActivity;

public abstract class BaseFragmentActivity extends DMFragmentActivity {

	/*
	 * 友盟统计
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if(isStatisticsEnabled()){
		}
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(isStatisticsEnabled()){
		}
		
	}
	
	
	
	/**
	 * 是否支持统计
	 * @param enable
	 */
	protected boolean isStatisticsEnabled(){
		return true;
	}
}
