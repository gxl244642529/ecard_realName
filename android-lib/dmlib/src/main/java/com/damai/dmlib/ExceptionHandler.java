package com.damai.dmlib;


public class ExceptionHandler {
	
	/**
	 * 异常处理
	 * @author Randy
	 *
	 */
	public static interface IExceptionHandler{
		void handleException(Exception e);
	}
	
	protected static class DefaultExceptionHandler implements IExceptionHandler{

		@Override
		public void handleException(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static IExceptionHandler defaultHandler = new DefaultExceptionHandler();
	
	public static void setExceptionHandler(IExceptionHandler handler){
		defaultHandler = handler;
	}
	
	public static void handleException(Exception e){
		defaultHandler.handleException(e);
	}
}
