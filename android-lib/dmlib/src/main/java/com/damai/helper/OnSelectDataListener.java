package com.damai.helper;

/**
 * 选中事件，必须一个listview单击拿到一个数据
 * @author renxueliang
 *
 */
public interface OnSelectDataListener<T> {

	void onSelectData(T data);
	
}
