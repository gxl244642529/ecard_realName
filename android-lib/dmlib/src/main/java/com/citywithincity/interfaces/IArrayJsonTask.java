package com.citywithincity.interfaces;

import java.util.List;

public interface IArrayJsonTask<T> extends IJsonTask,IListTask {
	/**
	 * 监听
	 * @param listener
	 */
	IArrayJsonTask<T> setListener(IListRequsetResult<List<T>> listener);
	/**
	 * 数据解析
	 * @param parser
	 */
	IArrayJsonTask<T> setParser(IJsonParser<T> parser);
	/**
	 * 分页位置
	 * @param position
	 */
	IArrayJsonTask<T> setPosition(int position);
	
	
	
	IArrayJsonTask<T> setOnParseListener(IJsonTaskListener<T> listener);
	
	void loadMore(int position);
	
	void add(int index,T data);
	
	void remove(T data);
	
	void update(T data);
	
	List<T> array();
}
