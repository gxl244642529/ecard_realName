package com.damai.dl;

import android.content.Context;
import android.content.Intent;

import com.citywithincity.interfaces.IViewContainer;

public interface IPluginActivity extends IViewContainer,IPluginBase {
	
	/**
	 * activity行为
	 */
	void finish();
	void setContentView(int layoutResID);
	void setResult(int resultCode);
	void setResult(int resultCode, Intent data);
	void onActivityResult(int requestCode, int resultCode, Intent data);
	void onBackPressed();
	void onRestart();
	Context getApplicationContext();
	Context getContext();
	
	/**
	 * host注入
	 * @return
	 */
	void setHost(IHostActivity host);
	IHostActivity getHost();
}
