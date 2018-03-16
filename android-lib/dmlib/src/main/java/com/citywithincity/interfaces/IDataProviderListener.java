package com.citywithincity.interfaces;

public interface IDataProviderListener<T> {
	void onReceive(T data);
	void onError(String error,boolean isNetworkError);
	
}
