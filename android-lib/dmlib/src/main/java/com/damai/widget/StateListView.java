package com.damai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import com.citywithincity.interfaces.IOnItemClickListener;
import com.damai.core.ApiJob;
import com.damai.interfaces.IRefreshable;
import com.damai.widget.proxy.IStateListView;
import com.damai.widget.proxy.WidgetProxy;

/**
 * 一个带状态的listView
 * @author Randy
 *
 */
@SuppressWarnings("rawtypes")
public class StateListView<T> extends FrameLayout implements IRefreshable  {
	
	private IStateListView<T> stateListView;
	
	
	@SuppressWarnings("unchecked")
	public StateListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(isInEditMode()){
			return;
		}
		stateListView = (IStateListView)WidgetProxy.getFactory().createStateListView(context, attrs, this);
	}
	
	
	@SuppressWarnings("unchecked")
	public StateListView(Context context){
		super(context);
		stateListView = (IStateListView)WidgetProxy.getFactory().createStateListView(context, this);
	}
	
	
	public void refreshWithState(){
		stateListView.refreshWithState();
	}

	public void setOnItemClickListener(IOnItemClickListener<T> listener){
		stateListView.setOnItemClickListener(listener);
	}
	public View getHeaderView(){
		return stateListView.getHeaderView();
	}
	public ApiJob getJob(){
		return stateListView.getJob();
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		stateListView.destroy();
	}

	@SuppressWarnings("hiding")
	public <T extends AbsListView> T getAdapterView(){
		return stateListView.getAdapterView();
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if(isInEditMode()){
			return;
		}
		stateListView.layout();
	}
}
