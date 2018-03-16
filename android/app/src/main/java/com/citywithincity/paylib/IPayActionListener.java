package com.citywithincity.paylib;

public interface IPayActionListener {
	void onPrePaySuccess(Object serverInfo) throws Exception;
	void onNotifyServerSuccess(Object result);
	void onNotityServerError(String error);
}
