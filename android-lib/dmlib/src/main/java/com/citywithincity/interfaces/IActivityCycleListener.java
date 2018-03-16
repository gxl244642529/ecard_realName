package com.citywithincity.interfaces;

import android.app.Activity;

public interface IActivityCycleListener {
	
	void onCreate(Activity context);

	void onDestroy(Activity context);

	void onPause(Activity context);

	void onResume(Activity context);
}
