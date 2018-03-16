package com.citywithincity.libs;


import com.damai.dmlib.LibBuildConfig;

public class LibConfig {
	
	//调试
	public static boolean DEBUG = LibBuildConfig.DEBUG;
	
	//分页位置
	public static int StartPosition = 1;
	
	public static final String DATA_NAME = "data";
	
	
	/**
	 * 站点url
	 */
	protected static String mUrl;
	/**
	 * 设置网站的网址 http://www.ecard.com
	 * @param url
	 */
	public static void setSiteUrl(String url){
		mUrl = url;
	}
	
	/**
	 * 给图片加上网址
	 * @param url
	 * @return
	 */
	public static String getImageUrl(String url){
		return mUrl + url;
	}
}
