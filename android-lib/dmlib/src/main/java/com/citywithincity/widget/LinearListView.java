package com.citywithincity.widget;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.citywithincity.interfaces.IOnItemClickListener;


public class LinearListView<T> extends LinearLayout implements View.OnClickListener {

	private BaseAdapter mAdapter;
	private IOnItemClickListener<T> onItemClickListener;
	public LinearListView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public LinearListView(Context context) {
		super(context);
	}

	public void setAdapter(BaseAdapter adapter) {
		if(mAdapter!=null){
			mAdapter.unregisterDataSetObserver(mObserver);
		}
		mAdapter = adapter;
		mAdapter.registerDataSetObserver(mObserver);
		mAdapter.notifyDataSetChanged();
	}

	protected DataSetObserver mObserver = new DataSetObserver() {
		public void onChanged() {
			// Do nothing
			int currentCount = LinearListView.this.getChildCount();
			int count = mAdapter.getCount();
			if(currentCount <= count){
				//再创建
				for(int i=currentCount; i < count; ++i){
					View view = mAdapter.getView(i, null, LinearListView.this);
					LinearListView.this.addView(view);
					view.setTag(i);
					view.setOnClickListener(LinearListView.this);
				}
			}else if(currentCount > count){
				//删除后面的
				for(int i=count; i < currentCount; ++i){
					LinearListView.this.removeViewAt(i);
				}
			}else{
				for(int i=0; i < currentCount; ++i){
					View view = LinearListView.this.getChildAt(i);
					mAdapter.getView(i, view, LinearListView.this);
				}
			}
		}
	};
	
	public void setOnItemClickListener(IOnItemClickListener<T> listener){
		this.onItemClickListener = listener;
	}


	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mAdapter.unregisterDataSetObserver(mObserver);
		mAdapter=null;
		mObserver=null;
		onItemClickListener = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		Integer index = (Integer)v.getTag();
		onItemClickListener.onItemClick((Activity)getContext(),
				(T)mAdapter.getItem(index), index);
	}

}
