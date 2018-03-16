package com.damai.core;

public class DeliverTypes {
	/**
	 * 普通投递
	 * 只是调用success、errorlistener
	 */
	public static final int DeliverType_Normal = 0;
	
	/**
	 * 图片成功以后的投递
	 */
	public static final int DeliverType_Image = 1;
	
	/**
	 * api调用，广义上面的api，指的是服务器端调用，不管是什么服务器
	 */
	public static final int DeliverType_Api = 2;
	
	
}
