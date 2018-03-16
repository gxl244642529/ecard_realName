package com.damai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.damai.interfaces.ITab;
import com.damai.widget.proxy.ITabView;
import com.damai.widget.proxy.WidgetProxy;

/**
 * 滑动显示的tabView
 * @author Randy
 *
 */
public class TabView extends FrameLayout implements ITab {
	
	private ITabView proxy;
	
	
	public TabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(isInEditMode())
			return;
		
		proxy = (ITabView) WidgetProxy.getFactory().createTabView(context, attrs, this);
	}
	
	public void setOnTabChangeListener(OnTabChangeListener listener){
		proxy.setOnTabChangeListener(listener);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		proxy.destroy();
		proxy=null;
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if(isInEditMode())
			return;
		proxy.onFinishInflate();
	}
	
	
	@Override
	public void setCurrentIndex(int index, boolean animated) {
		proxy.setCurrentIndex(index, animated);
	}

	
	

}
