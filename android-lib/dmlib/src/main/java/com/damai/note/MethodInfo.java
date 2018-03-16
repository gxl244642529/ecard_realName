package com.damai.note;

import java.lang.reflect.Method;

import com.citywithincity.interfaces.IViewContainer;
import com.damai.dmlib.ExceptionHandler;

public abstract class MethodInfo implements ClsInfo {
	/**
	 * 被观察者
	 */
	protected IViewContainer observer;
	/**
	 * 方法
	 */
	protected Method method;
	
	public MethodInfo(Method method){
		this.method = method;
		
	}
	

	public void invoke(Object data){
		try {
			method.invoke(observer, data);
		} catch (Exception e) {
			ExceptionHandler.handleException(e);
		} 
	}
	public void invoke(){
		try {
			method.invoke(observer);
		} catch (Exception e) {
			ExceptionHandler.handleException(e);
		} 
	}
	public void setTarget(IViewContainer observer) {
		this.observer = observer;
	}



	public abstract void clearObserver();
	
	public String getName(){
		return method.getName();
	}
}
