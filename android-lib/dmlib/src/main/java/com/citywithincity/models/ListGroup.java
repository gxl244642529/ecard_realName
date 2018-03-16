package com.citywithincity.models;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.interfaces.IListRequsetResult;
import com.citywithincity.libs.LibConfig;

/**
 * 列表，根据不同情况显示加载、失败、没有结果
 * @author Randy
 *
 * @param <T>
 */
public class ListGroup<T> extends ListDataProvider<T>  implements  IListRequsetResult<List<T>>, OnClickListener {
	public interface IListGroupListener<T> extends IListDataProviderListener<T>{
		/**
		 * 加载数据
		 * @param position
		 */
		void onLoadData(int position);
	}
	
	private LoadingState loadingState;
	private IListGroupListener<T> listener;
	
	public void showLoadingState(){
		this.loadingState.initView();
	}
	public ListGroup(Activity container,int itemViewResID,IListGroupListener<T> listener){
		super(container, itemViewResID, listener);
		loadingState = new LoadingState(container);
		loadingState.setListener(this);
		this.listener = listener;
	}
	
	public ListGroup(Context context,View container,int itemViewResID,IListGroupListener<T> listener){
		super(context, itemViewResID, listener);
		loadingState = new LoadingState(container);
		loadingState.setListener(this);
		this.listener = listener;
	}
	
	
	@Override
	public void destroy() {
		listener = null;
		loadingState.destroy();
		loadingState =null;
		super.destroy();
	}



	@Override
	public void onClick(View v) {
		//刷新
		onLoadData(LibConfig.StartPosition);
	}
	
	private int currentPosition;
	protected void onLoadData(int position){
		currentPosition = position;
		//重新加载任务
		listener.onLoadData(position);
	}

	@Override
	public void onRequestError(String errMsg,
			boolean isNetworkError) {
		loadingState.onError(errMsg, isNetworkError);
	}

	@Override
	public void onRequestSuccess( List<T> result,
			boolean isLastPage) {
		loadingState.onSuccess( result.size() > 0);
		setData(result, currentPosition > LibConfig.StartPosition);
		
	}

	
	
	
	
	
}
