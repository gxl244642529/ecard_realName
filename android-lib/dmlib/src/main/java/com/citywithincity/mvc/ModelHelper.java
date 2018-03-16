package com.citywithincity.mvc;

import android.app.Activity;

import com.citywithincity.interfaces.IJsonTaskManager;

public class ModelHelper {

	
	protected static IJsonTaskManager instance;
	
	public static void setJsonTaskManager(IJsonTaskManager i){
		instance = i;
	}
	
	public static <T> T getModel(Class<T> clazz){
		return (T) instance.getModel(clazz);
	}
	
	public static Activity getActivity(){
		return (Activity)instance.getCurrentContext();
	}

	private static Object dataObject;
	
	public static void setListData(Object data) {
		dataObject = data;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getListData(){
		return (T)dataObject;
	}
	
	public static void unregisterModel(Class<?> clazz){
		instance.unregisterModel(clazz);
	}
}
