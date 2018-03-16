package com.damai.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.citywithincity.interfaces.IDestroyable;
import com.damai.lib.R;
public class StateHandler implements IDestroyable, OnClickListener {
	private View progress;
	private View noResult;
	private View errorView;

	private int loading;
	private int error;
	private int no_result;
	
	private boolean showHeader;
	
	private OnClickListener listener;

	private Context context;
	private FrameLayout parent;
	private View headerView;
	
	public StateHandler(Context context,FrameLayout parent) {
		this.context = context;
		this.parent = parent;
	}
	
	public void setHeaderView(View headerView){
		this.headerView = headerView;
	}
	
	public void layout(){
		if(headerView!=null && showHeader){
			if(progress!=null){
				FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) progress.getLayoutParams();
				lp.topMargin = headerView.getMeasuredHeight();
				progress.setLayoutParams(lp);
			}
		}
	}
	
	public void setIds(int loading,int error,int no_result){
		this.loading = loading;
		this.error = error;
		this.no_result = no_result;
		createLoading();
	}


	@Override
	public void destroy() {
		listener = null;
		progress = null;
		noResult = null;
		errorView = null;
		
		context = null;
		parent = null;
	}

	public void setListener(OnClickListener listener) {
		this.listener = listener;
	}
	
	private void createLoading(){
		if(loading>0 && progress==null){
			progress = LayoutInflater.from(context).inflate(loading, null);
			parent.addView(progress,new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}

	}
	private void destroyLoading(){
		if(progress!=null){
			parent.removeView(progress);
			progress = null;
		}
	}
	
	private void createError(){
		if(error>0 && errorView==null){
			errorView = LayoutInflater.from(context).inflate(error, null);
			parent.addView(errorView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			
			View refresh = errorView.findViewById(R.id._id_refresh);
			if (refresh != null) {
				refresh.setOnClickListener(this);
			}
		}
	}
	
	private void destroyError(){
		if(errorView!=null){
			parent.removeView(errorView);
			errorView = null;
		}
	}
	
	private void createNoResult(){
		if(no_result>0 && noResult==null){
			noResult = LayoutInflater.from(context).inflate(no_result, null);
			parent.addView(noResult, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			
			View refresh = noResult.findViewById(R.id._id_refresh);
			if (refresh != null) {
				refresh.setOnClickListener(this);
			}
		}
	}
	
	private void destroyNoResult(){
		if(noResult!=null){
			parent.removeView(noResult);
			noResult = null;
		}
	}


	public void onSuccess(boolean hasResult) {
		destroyLoading();
		destroyError();
		if (hasResult) {
			destroyNoResult();
		} else {
			createNoResult();
		}
	}

	public void onError(String errorMessage, boolean isNetworkError) {
		destroyLoading();
		createError();
		destroyNoResult();
	}

	@Override
	public void onClick(View v) {
		setStateLoading();
		listener.onClick(v);
	}

	/**
	 * 
	 */
	public void setStateLoading() {
		destroyError();
		destroyNoResult();
		createLoading();
	}
}
