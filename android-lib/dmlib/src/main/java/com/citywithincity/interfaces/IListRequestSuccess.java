package com.citywithincity.interfaces;

public interface IListRequestSuccess<T> {
	/**
	 * 成功,是否最后一页
	 */
	void onRequestSuccess(T result,boolean isLastPage);
}
