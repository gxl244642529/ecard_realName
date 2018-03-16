package com.citywithincity.interfaces;

import android.view.View;


/**
 * viewpager的数据源
 * @author randy
 *
 */
public interface IViewPagerDataProviderListener<T> {
	void onViewPagerDestroyView(View view,int index);
	void onViewPagerCreateView(View view,int index,T data);
	void onViewPagerPageSelect(View view,int index,T data);
}
