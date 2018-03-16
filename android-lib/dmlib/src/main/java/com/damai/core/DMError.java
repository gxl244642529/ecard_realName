package com.damai.core;

import java.io.IOException;

/**
 * 错误，这个错误可能是错误，也有可能是服务器的通知
 * 
 * 
 * @author renxueliang
 *
 */
public class DMError {
	
	private final Throwable error;

	/**
	 * 异常错误
	 * @param error
	 */
	public DMError(Throwable error){
		this.error = error;
	}
	
	public String getReason(){
		return error.getMessage();
	}
	
	public Throwable getError(){
		return error;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isNetworkError(){
		return error!=null && (error instanceof IOException);
	}

}
