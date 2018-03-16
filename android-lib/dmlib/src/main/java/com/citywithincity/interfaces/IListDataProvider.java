package com.citywithincity.interfaces;

import java.util.List;

public interface IListDataProvider<T> extends IDestroyable {
	void setListener(IListDataProviderListener<T> listener);
	void setItemEventHandlerTarget(Object target);
	void remove(T data);
	void setData(T[] dataSource, boolean addToEnd);
	List<T> getData();
	void setData(List<T> dataSource, boolean addToEnd);
	void add(T data);
	void insert(int index,T data);
	int getCount();
	T getItem(int index) ;
	void setItemRes(int res);
}
