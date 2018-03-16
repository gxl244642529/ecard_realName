package com.damai.core;

import java.util.Map;

import com.damai.dmlib.LibBuildConfig;

public class DMServers {
	private static String[] serverUrls = new String[LibBuildConfig.MAX_SERVERS];
	
	public static String getUrl(int index){
		return serverUrls[index];
	}

	public static void setUrl(int index,String url){
		serverUrls[index] = url;
	}
	

	/**
	 * 相对链接转绝对链接
	 * @param index
	 * @param url
	 * @return
	 */
	public static String getImageUrl(int index,String url){
		return new StringBuilder(LibBuildConfig.MAX_URL_LEN).append(serverUrls[index]).append(url).toString();
	}

	/**
	 * 从这个地址加载session
	 * @param url
	 */
	public static Map<String, String> querySession(String url) {
		
		return null;
	}


	



	
}
