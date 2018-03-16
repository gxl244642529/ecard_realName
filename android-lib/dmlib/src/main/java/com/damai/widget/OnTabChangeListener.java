package com.damai.widget;

import android.view.View;

public interface OnTabChangeListener {
	/**
	 * 当切换改变了
	 * @param tab
	 * @param index
	 * @param isFromUser
	 */
	void onTabChanged(View tab,int index,boolean isFromUser);
}
