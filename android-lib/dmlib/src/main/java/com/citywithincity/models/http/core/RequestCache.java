package com.citywithincity.models.http.core;


/**
 * 请求的缓存，对于解析出来的数据做直接缓存
 * @author Randy
 *
 */
interface RequestCache<T> {
	T loadFromCache(AbsRequest<?> request);
	boolean writeToCache(T data);
}
