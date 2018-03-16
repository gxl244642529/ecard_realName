package com.citywithincity.interfaces;

import android.view.View;

public interface IListDataProviderListener<T> {
	
	/**
	 * 初始化view
	 * @param view
	 * @param data
	 * @param position
	 */
	void onInitializeView(View view,T data,int position);
}
