package com.citywithincity.ecard.base;

import android.app.Activity;
import android.content.Intent;

import com.damai.auto.DMActivity;

public abstract class BaseActivity extends DMActivity {

	
	
	protected void startActivity(Class<? extends Activity> clazz){
		startActivity(new Intent(this,clazz));
	}

	
	/**
	 * 是否支持统计
	 */
	protected boolean isStatisticsEnabled(){
		return needsStatistics();
	}
}
