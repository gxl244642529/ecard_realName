package com.citywithincity.interfaces;

public interface IModelManager {

	/**
	 * 删除模型
	 * @param clazz
	 */
	void unregisterModel(Class<?> clazz);
	
	/**
	 * 获取模型,如没有则创建,有则直接获取
	 * @param modelClass
	 * @return
	 */
	<T> T getModel(Class<T> modelClass);
}
