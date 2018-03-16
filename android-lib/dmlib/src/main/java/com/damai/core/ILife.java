package com.damai.core;

import android.content.Intent;

import com.citywithincity.interfaces.IViewContainer;

public interface ILife {
	void onResume(IViewContainer container);
	void onPause(IViewContainer container);
	void onNewIntent(Intent intent,IViewContainer container);
	void onDestroy(IViewContainer container);
}
