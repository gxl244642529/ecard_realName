package com.citywithincity.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.citywithincity.interfaces.IDetailJsonTask;
import com.citywithincity.interfaces.IReloadableTask;
import com.citywithincity.interfaces.IRequestResult;
import com.damai.lib.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class StateScrollView<T> extends PullToRefreshScrollView implements IRequestResult<T>, com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener<ScrollView> {
	
	
	private StateHandler handler;
	private int loadingRes;
	private int errorRes;
	
	private IReloadableTask task;

	
	public StateScrollView(Context context) {
		super(context);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		task = null;
		if(handler!=null){
			handler.destroy();
			handler = null;
		}
		super.onDetachedFromWindow();
	}

	/**
	 * 
	 * @param task
	 */
	public void setTask(IReloadableTask task){
		this.task = task;
	}
	
	public void setTask(IDetailJsonTask<T> task){
		task.setListener(this);
		this.task = task;
	}
	
	public StateScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setOnRefreshListener(this);
	}
	
	@Override
	protected void handleStyledAttributes(TypedArray a) {
		loadingRes = a.getResourceId(R.styleable._PullToRefresh_loading, 0);
		errorRes = a.getResourceId(R.styleable._PullToRefresh_error, 0);
		handler.setIds(loadingRes,errorRes,0);
		handler.setListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				reloadWithState();
			}
		});
	}
	
	@Override
	protected void addRefreshableView(Context context,ScrollView refreshableView) {
		super.addRefreshableView(context, refreshableView);
		handler = new StateHandler(context, mRefreshableViewWrapper);
		
	}

	public void reloadWithState(){
		handler.setStateLoading();
		task.reload();
	}
	
	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
		handler.onError(errMsg, isNetworkError);
		onComplete();
	}

	@Override
	public void onRequestSuccess(T result) {
		handler.onSuccess(true);
		onComplete();
	}
	public void onComplete() {
		onRefreshComplete();
		setLabel();
	}
	private void setLabel() {
		String label = DateUtils.formatDateTime(getContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		getLoadingLayoutProxy().setLastUpdatedLabel(label);
	}
	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		
		task.reload();
		
	}
	
	
	


}
