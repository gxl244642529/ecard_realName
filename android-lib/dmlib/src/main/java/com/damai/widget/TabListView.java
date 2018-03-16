package com.damai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.damai.core.ApiJob;
import com.damai.interfaces.ITab;
import com.damai.widget.proxy.IStateListView;
import com.damai.widget.proxy.WidgetProxy;

public class TabListView<T> extends RelativeLayout implements ITab {

	
	private IStateListView<T> stateListView;
	
	
	public TabListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}
	public void refreshWithState(){
		stateListView.refreshWithState();
	}
	
	public ApiJob getJob(){
		return stateListView.getJob();
	}
	
	@SuppressWarnings("unchecked")
	private void init(Context context, AttributeSet attrs){
		if(isInEditMode())
			return;
		
		stateListView =(IStateListView<T>) WidgetProxy.getFactory().createTabListView(context, attrs, this);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if(isInEditMode())
			return;
		stateListView.onFinishInflate();
	}
	@Override
	public void setCurrentIndex(int index, boolean animated) {
		((ITab)stateListView).setCurrentIndex(index, animated);
	}
	public TabListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public TabListView(Context context) {
		super(context);
	}

	
	@Override
	protected void onDetachedFromWindow() {
		stateListView.destroy();
		stateListView=null;
		super.onDetachedFromWindow();
	}

	
}
