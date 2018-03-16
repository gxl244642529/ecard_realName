package com.damai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.damai.widget.proxy.WidgetProxy;


/**
 * 广告页面
 * @author Randy
 *
 */
public class AdvView extends RelativeLayout  {

	private WidgetProxy proxy;
	
	public AdvView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		proxy = WidgetProxy.getFactory().createAdvView(context, attrs, this);
	}

	/**
	 * life
	 * @param context
	 * @param attrs
	 */
	public AdvView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(isInEditMode()){
			return;
		}
		proxy = WidgetProxy.getFactory().createAdvView(context, attrs, this);
	}
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if(isInEditMode()){
			return;
		}
		proxy.onFinishInflate();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		proxy.destroy();
		proxy=null;
	}
	


	
	

}
