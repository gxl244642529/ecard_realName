package com.damai.widget.proxy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public abstract class WidgetProxy implements IWidgetProxy {
	
	
	private static WidgetFactory gFactory;
	public static WidgetFactory getFactory(){
		return gFactory;
	}
	public static void setFactory(WidgetFactory factory){
		gFactory = factory;
	}
	
	protected Context context;
	protected View view;
	public WidgetProxy(Context context, AttributeSet attrs,View view){
		this.context = context;
		this.view = view;
	}
	

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	
	public void destroy(){
		context = null;
		view = null;
	}
	
}
