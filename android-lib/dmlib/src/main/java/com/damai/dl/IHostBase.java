package com.damai.dl;

import android.content.Intent;

public interface IHostBase {
	/**
	 * 控制startActivity
	 * @param intent
	 */
	void startActivity(Intent intent);
	
	IPluginBase getPlugin();
	
	void startActivityForResult(Intent intent, int requestCode);
}
