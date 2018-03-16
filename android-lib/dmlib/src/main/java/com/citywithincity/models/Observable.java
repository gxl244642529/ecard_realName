package com.citywithincity.models;

import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IObservable;

/**
 * 被观察者
 * @author Randy
 *
 * @param <T>
 */
public class Observable<T> implements IObservable<T>, IDestroyable {
	protected T listener;
	
	@Override
	public void setListener(T observer){
		this.listener = observer;
	}
	

	@Override
	public void destroy() {
		listener = null;
	}
}
