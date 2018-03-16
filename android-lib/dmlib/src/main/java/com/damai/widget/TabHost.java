package com.damai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.damai.interfaces.ITab;
import com.damai.widget.proxy.WidgetProxy;

public class TabHost extends FrameLayout implements ITab {
	
	private WidgetProxy proxy;

	
	
	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	public TabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(isInEditMode())
			return;
		proxy = WidgetProxy.getFactory().createTabHost(context, attrs, this);
	}
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if(isInEditMode())
			return;
		proxy.onFinishInflate();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		proxy.destroy();
	}


	@Override
	public void setCurrentIndex(int index, boolean animated) {
		((ITab)proxy).setCurrentIndex(index, animated);
	}
	

	
}
