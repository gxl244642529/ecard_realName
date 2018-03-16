package com.citywithincity.plugin;

import java.io.Serializable;

import android.app.Activity;
import android.view.View;

public interface IPluginContainer {
	void onEventStartActivity(Class<? extends IPlugin> clazz);
	void onEventStartActivity(Class<? extends IPlugin> clazz,Serializable data);
	void onEventStartActivity(Class<? extends IPlugin> clazz,Object data);
	void onSetContentView(View view);
	View inflate(int layoutId);
	Activity getActivity();
}
