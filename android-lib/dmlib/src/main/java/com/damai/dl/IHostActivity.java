package com.damai.dl;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.damai.helper.ActivityResultContainer;

public interface IHostActivity extends ActivityResultContainer,IHostBase {
	
	View inflate(int resId);
	void setContentView(View view);
	void setOverrideResources(boolean override);
	PluginInfo getPluginInfo();
	

	
	/**
	 * 下面的是activity的方法，原封不动
	 * @return
	 */
	Context getApplicationContext();
	/**
	 * 获取的是host的packageName
	 * @return
	 */
	String getPackageName();
	
	void finish();
	void setResult(int resultCode);
	void setResult(int resultCode,Intent data);
}
