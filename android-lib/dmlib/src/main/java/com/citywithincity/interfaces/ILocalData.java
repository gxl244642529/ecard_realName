package com.citywithincity.interfaces;

import java.util.List;
import java.util.Map;

public interface ILocalData extends IDestroyable {
	<T> ILocalData putObject(String subKey, T data);
	ILocalData putBoolean(String key,boolean value);
	<T> ILocalData putList(String subKey,List<T> value);
	ILocalData putInt(String key,int value);
	ILocalData putFloat(String subKey,float value);
	ILocalData putString(String key,String value);
	
	ILocalData putMap(String key,Map<String, Object> value);
	/**
	 * 获取对象
	 * @param subKey
	 * @param clazz
	 * @return
	 */
	<T> T getObject(String subKey, Class<T> clazz);
	/**
	 * 清除对象
	 * @param subKey
	 * @param clazz
	 */
	ILocalData clearObject(String subKey,Class<?> clazz);
	
	Map<String, Object> getMap(String key);
	List<String> getStringList(String subKey);
	int getInt(String key,int defaultValue);
	String getString(String subKey,String defaultString);
	boolean getBoolean(String key,boolean defaultValue);
	float getFloot(String subKey,float defaultValue);
	
	void save();
	
}
