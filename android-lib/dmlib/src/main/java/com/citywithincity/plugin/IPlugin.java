package com.citywithincity.plugin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public interface IPlugin{
	
	
	void onResume();
	void onStop();
	void onDestroy();
	
	void onRestart();
	void onNewIntent(Intent intent);
	/**
	 * 
	 * @param outState
	 */
	void onSaveInstanceState(Bundle outState);
	void onActivityResult(int requestCode, int resultCode, Intent data);
	
	View inflate(int layoutId);
	
	/**
	 * 启动
	 * @param clazz
	 */
	void startActivity(Class<? extends IPlugin> clazz);
	
	
}
