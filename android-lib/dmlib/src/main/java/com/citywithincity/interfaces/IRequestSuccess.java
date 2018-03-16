package com.citywithincity.interfaces;

public interface IRequestSuccess<T> {
	/**
	 * 成功
	 */
	void onRequestSuccess(T result);
}
