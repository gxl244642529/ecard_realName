package com.damai.core;

import java.util.Map;


public interface ServerHandler {

	/**
	 * 获取地址
	 * @return
	 */
	String getUrl();
	
	int getServer();
	void setServer(int server);
	
	void setUrl(String url);
	
	
	/**
	 * 从列表参数到分页参数
	 * 
	 * @param position
	 * @return
	 */
	int positionToPageParam(int position);
	
	void setPosition(int postion,Map<String,Object> param);
	
	
	ApiParser getListParser();
	
	ApiParser getPageParser();
	
	ApiParser getObjectParser();
	
	/**
	 * 创建方法
	 * @return
	 */
	DMPage<?> createPage();
	
}
