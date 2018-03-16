package com.citywithincity.utils;

import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IDetailJsonTask;
import com.citywithincity.interfaces.IJsonTaskManager;
import com.citywithincity.interfaces.ImageRequestLisener;
import com.citywithincity.models.cache.CachePolicy;

public class Http {
	
	protected static IJsonTaskManager jsonTaskManager;
	
	public static void setJsonTaskManager(IJsonTaskManager value){
		jsonTaskManager = value;
	}
	
	/**
	 * 
	 * @param name
	 * @param cachePolicy
	 * @param isPrivate
	 * @return
	 */
	public static <T> IArrayJsonTask<T> list(String name,Class<T> clazz,CachePolicy cachePolicy,boolean isPrivate){
		return jsonTaskManager.createArrayJsonTask(name, cachePolicy, isPrivate,clazz,0);
	}
	public static <T> IArrayJsonTask<T> list(String name,Class<T> clazz,CachePolicy cachePolicy,boolean isPrivate,int factory){
		return jsonTaskManager.createArrayJsonTask(name, cachePolicy, isPrivate,clazz,factory);
	}
	
	public static <T> IDetailJsonTask<T> detail(String api,CachePolicy cachePolicy,Class<T> clazz,int factory){
		return jsonTaskManager.createDetailJsonTask(api, cachePolicy, clazz,factory);
	}
	

	
	public static IDestroyable loadImage(String url,ImageRequestLisener listener){
		
		return jsonTaskManager.loadImage(url,listener);
	}
}
