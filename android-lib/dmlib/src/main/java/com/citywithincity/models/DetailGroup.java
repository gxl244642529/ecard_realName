package com.citywithincity.models;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IRequestResult;

public class DetailGroup<T> implements IDestroyable,IRequestResult<T>, OnClickListener{
	public interface IDetailGroupListener<T>{
		/**
		 * 数据加载成功
		 * @param data
		 */
		void onDataLoadComplete(T data);
		/**
		 * 刷新
		 */
		void onRefresh();
	}
	
	private LoadingState loadingState;
	private IDetailGroupListener<T> listener;
	
	public DetailGroup(Activity activity,IDetailGroupListener<T> listener){
		loadingState = new LoadingState(activity);
		loadingState.setListener(this);
		this.listener = listener;
	}
	public DetailGroup(View view,IDetailGroupListener<T> listener){
		loadingState = new LoadingState(view);
		loadingState.setListener(this);
		this.listener = listener;
	}
	@Override
	public void destroy() {
		listener = null;
		loadingState.destroy();
		loadingState = null;
	}

	

	@Override
	public void onClick(View v) {
		//刷新页面，重新加载任务
		refreshData();
	}

	public void refreshData(){
		listener.onRefresh();
	}
	@Override
	public void onRequestError(String errMsg,
			boolean isNetworkError) {
		loadingState.onError(errMsg, isNetworkError);
	}
	@Override
	public void onRequestSuccess( T result) {
		listener.onDataLoadComplete(result);
		loadingState.onSuccess(true);
	}
	
}
