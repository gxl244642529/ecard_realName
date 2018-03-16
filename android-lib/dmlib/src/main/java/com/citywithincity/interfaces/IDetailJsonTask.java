package com.citywithincity.interfaces;

public interface IDetailJsonTask<T> extends IObjectJsonTask<T> {
	/**
	 * 详情id 
	 * @param id
	 */
	IObjectJsonTask<T> setId(Object id);
	
	/**
	 * 详情id
	 * @param name
	 * @param id
	 */
	IObjectJsonTask<T> setId(String name,Object id);
}
