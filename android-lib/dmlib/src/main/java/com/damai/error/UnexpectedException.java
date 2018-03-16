package com.damai.error;

/**
 * 未知的错误，如用户把权限关掉的问题
 * @author renxueliang
 *
 */
public class UnexpectedException extends Exception{

	public UnexpectedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnexpectedException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	public UnexpectedException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public UnexpectedException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3222376819611771170L;

}
