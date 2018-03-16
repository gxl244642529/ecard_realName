package com.damai.core;

public class DMMessage {
	
	
	public static final int TOAST = 0;
	
	
	public static final int ALERT = 1;
	
	
	/**
	 * 服务器的消息
	 */
	private String message;
	/**
	 * 消息标题
	 */
	private String title;

	/**
	 * 消息类型
	 */
	private int type;
	
	/**
	 * 服务器通知
	 * @param message
	 * @param title
	 * @param type
	 */
	public DMMessage(String message,String title,int type){
		this.title = title;
		this.type = type;
		this.message = message;
	}
	
	

	
	/**
	 * 
	 */
	public void doAlert(){
		
	}
	
	
	public int getType(){
		return type;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getMessage(){
		return message;
	}
}
