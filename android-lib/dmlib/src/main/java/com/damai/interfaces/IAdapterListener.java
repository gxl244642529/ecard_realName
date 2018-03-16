package com.damai.interfaces;

import android.view.View;

public interface IAdapterListener<T> {
	/**
	 * 初始化view
	 * @param view
	 * @param data
	 * @param position
	 */
	void onInitializeView(View view,T data,int position);
}
