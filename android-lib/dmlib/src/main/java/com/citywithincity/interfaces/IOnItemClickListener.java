package com.citywithincity.interfaces;

import android.app.Activity;

public interface IOnItemClickListener<T> {
	/**
	 * 要进入activity之前
	 * @param parent
	 * @param intent
	 */
	void onItemClick(Activity activity,T data,int position);

}
