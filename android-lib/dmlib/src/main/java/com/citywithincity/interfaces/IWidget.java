package com.citywithincity.interfaces;

import com.citywithincity.auto.tools.FormError;


/**
 * 表单元素
 * @author Randy
 *
 * @param <T>
 */
public interface IWidget<T> {
	
	public interface OnWidgetValueChangeListener<T>{
		void onWidgetValueChange(IWidget<T> widget);
	}
	
	
	void setOnWidgetValueChangeListener(OnWidgetValueChangeListener<T> listener);
	
	/**
	 * 获取表单元素的值（基本类型或者map)
	 * @return
	 */
	T getValue() throws FormError;
	
	/**
	 * 设置表单元素的值(基本类型，或者map)
	 */
	void setValue(T value);
	
	/**
	 * 如多属性，则必须返回，否则返回null
	 * @return
	 */
	String[] getPropertyNames();
	
}
