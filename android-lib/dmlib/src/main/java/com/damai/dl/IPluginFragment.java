package com.damai.dl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citywithincity.interfaces.IViewContainer;

public interface IPluginFragment extends IViewContainer,IPluginBase {
	View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState);
	void onViewCreated(View view, Bundle savedInstanceState);
	
	
	/**
	 * 下面的是用于设置一些属性
	 * @param view
	 */
	void setView(View view);
	void setHost(IHostActivity host);
	void setHostFragment(IHostFragment host);
}
