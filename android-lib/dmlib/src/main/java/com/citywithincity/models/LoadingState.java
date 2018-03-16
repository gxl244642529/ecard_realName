package com.citywithincity.models;



import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.citywithincity.interfaces.IDestroyable;
import com.damai.lib.R;


public class LoadingState implements IDestroyable, OnClickListener {
	private View progress;
	private View noResult;
	private View errorView;
	
	private OnClickListener listener;
	
	public LoadingState(View container){
		progress = container.findViewById(R.id._load_state_loading);
		noResult = container.findViewById(R.id._load_state_no_result);
		errorView = container.findViewById(R.id._load_state_error);
		initView();
	}
	
	public LoadingState(Activity container){
		progress = container.findViewById(R.id._load_state_loading);
		noResult = container.findViewById(R.id._load_state_no_result);
		errorView = container.findViewById(R.id._load_state_error);
		initView();
	}
	
	
	@Override
	public void destroy() {
		listener = null;
		progress = null;
		noResult = null;
		errorView = null;
	}
	
	public void setListener(OnClickListener listener){
		this.listener = listener;
	}

	
	public void initView(){
		if(progress!=null)
		{
			progress.setVisibility(View.VISIBLE);
		}
		if (noResult != null) {
			noResult.setVisibility(View.GONE);
			View refresh = noResult.findViewById(R.id._id_refresh);
			if(refresh!=null){
				refresh.setOnClickListener(this);
			}
		}
		if (errorView != null) {
			errorView.setVisibility(View.GONE);
			View refresh = errorView.findViewById(R.id._id_refresh);
			if(refresh!=null){
				refresh.setOnClickListener(this);
			}
		}
	}
	
	public void onSuccess(boolean hasResult){
		if (progress != null)
			progress.setVisibility(View.GONE);

		if (hasResult) {
			if(noResult!=null)noResult.setVisibility(View.GONE);
		}else{
			if(noResult!=null)noResult.setVisibility(View.VISIBLE);
		}
	}
	
	public void onError(String errorMessage,boolean isNetworkError){
		if (progress != null)
			progress.setVisibility(View.GONE);
		
		
		if(errorView!=null){
			errorView.setVisibility(View.VISIBLE);
			TextView textView = (TextView)errorView.findViewById(R.id._text_view);
			if(textView!=null){
				textView.setText(errorMessage);
			}
		}
		if(noResult!=null)
		{
			noResult.setVisibility(View.GONE);
		}
	}
	@Override
	public void onClick(View v) {
		if(noResult!=null)noResult.setVisibility(View.GONE);
		if(errorView!=null)errorView.setVisibility(View.GONE);
		if(progress!=null)progress.setVisibility(View.VISIBLE);
		listener.onClick(v);
	}
}
