package com.citywithincity.interfaces;

import java.util.Map;

import com.citywithincity.models.cache.CachePolicy;

public interface IJsonTask extends IReloadableTask {
	
	
	
	IJsonTask putAll(Map<String, Object> params);
	
	IJsonTask setCachePolicy(CachePolicy cachePolicy);
	
	IJsonTask setErrorListener(IRequestError errorListener);

	
	IJsonTask setCondition(String[] condition);
	
	IJsonTask enableAutoNotify();
	/**
	 * 执行任务
	 */
	IJsonTask execute();
	void clearParam();
	/**
	 * 添加参数
	 * @param paramName
	 * @param paramValue
	 */
	IJsonTask put(String paramName, Object paramValue);
	
	

	IJsonTask setWaitMessage(String waitMessage);
	IJsonTask setCrypt(int crypt);
	void setPadding(boolean padding);
	boolean getPadding();
	boolean isDestroyed();
	void clearCache();
	//void onSuccess();
}
