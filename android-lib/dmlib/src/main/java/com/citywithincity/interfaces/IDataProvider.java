package com.citywithincity.interfaces;

public interface IDataProvider<T> {
	void load();
	
	void setListener(IDataProviderListener<T> listener);
}
