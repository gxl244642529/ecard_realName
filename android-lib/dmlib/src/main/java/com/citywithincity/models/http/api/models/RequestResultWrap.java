package com.citywithincity.models.http.api.models;

import java.lang.ref.WeakReference;

import com.citywithincity.interfaces.IRequestResult;

public class RequestResultWrap<T> implements IRequestResult<T> {
	protected WeakReference<IRequestResult<T>> listener;
	
	public RequestResultWrap(IRequestResult<T> listener){
		this.listener = new WeakReference<IRequestResult<T>>(listener);
	}

	
	@Override
	public void onRequestError(String errMsg,
			boolean isNetworkError) {
		IRequestResult<T> l = listener.get();
		if(l!=null)
		{
			l.onRequestError(errMsg, isNetworkError);
		}
	}

	@Override
	public void onRequestSuccess(T result) {
		IRequestResult<T> l = listener.get();
		if(l!=null)
		{
			l.onRequestSuccess(result);
		}
	}
	
	
	
	
}
