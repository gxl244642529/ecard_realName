package com.damai.core;


public class LoginListenerWrap extends ListenerWrap<LoginListener> {
	
	private static LoginListenerWrap instance = new LoginListenerWrap();
	
	public static LoginListenerWrap getInstance(){
		return instance;
	}
	
	
	public LoginListenerWrap(){
		
	}
	
	
	
	
}
