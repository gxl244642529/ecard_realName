package com.damai.core;

import com.citywithincity.models.cache.CachePolicy;

public interface HttpJob extends Job {

	String getUrl();

	String getCacheKey();

	int getCurrentBytes();
	void setRetryTimes(int times);
	int getTotalBytes();

	DMError getError();

	<T> T getData();
	
	CachePolicy getCachePolicy();
	void setCachePolicy(CachePolicy cachePolicy);
}
