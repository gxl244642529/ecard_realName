package com.damai.dl;

import android.content.Intent;
import android.os.Bundle;

public interface IPluginBase {
	/**
	 * 启动activity
	 * @param intent
	 * @param requestCode
	 */
	void startActivityForResult(Intent intent, int requestCode);

	void startActivity(Intent intent);
	
	
	/**
	 * 生命周期
	 */
	void onStop();

	void onStart();

	void onResume();
	
	void onPause();

	void onDestroy();

	void onCreate(Bundle savedInstanceState);

	void onNewIntent(Intent intent);
}
