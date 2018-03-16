package com.citywithincity.interfaces;

public interface IRequestError {
	/**
	 * 请求错误原因
	 * @param errMsg
	 */
	void onRequestError(String errMsg,boolean isNetworkError);
}
