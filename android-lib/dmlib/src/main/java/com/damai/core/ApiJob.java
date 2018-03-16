package com.damai.core;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.view.View;

public interface ApiJob extends HttpJob {
	
	DMMessage getMessage();
	void setServer(int server);
	void setTimeoutMS(int timeout);
	void setOnSuccessListener(OnJobSuccessListener<ApiJob> listener);
	void setApiListener(ApiListener listener);
	/**
	 * 是否可以取消
	 * @param cancelable
	 */
	void setCancelable(boolean cancelable);
	Map<String, Object> getParams();
	Class<?> getEntity();
	<T> ApiJob putBean(String key,T obj);
	<T> ApiJob putList(String key,List<T> array);
	<T> ApiJob putObject(String key,T data);
	ApiJob put(String key,String data);
	ApiJob put(String key,Integer data);
	ApiJob put(String key,Boolean data);
	ApiJob put(String key,Double data);
	ApiJob put(String key, Long value);
	ApiJob put(String key,File data);
	void setEntity(Class<?> entity);
	void removeParam(String key);
	String getApi();
	void setApi(String api);
	int getCrypt();
	void setCrypt(int crypt);
	
	void execute() ;
	void reload() ;
	void setButton(View button);
	boolean is(String api);
	View getButton();
	void setPosition(int position);
	void putAll(Map<String, Object> data) ;
	void setWaitingMessage(String waiting);
	String getWaitingMessage();

	void setRelated(Object related);
}
