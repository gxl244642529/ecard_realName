package com.damai.core;

import java.lang.ref.WeakReference;

public class ListenerWrap<T> {
	
	private WeakReference<T> listener;
	
	public void setListener(T listener){
		if(listener==null){
			listener =null;
		}else{
			this.listener = new WeakReference<T>(listener);
		}
	}
	
	public T getListener(){
		if(listener!=null){
			return listener.get();
		}
		return null;
	}
}
