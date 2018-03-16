package com.damai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.damai.core.ApiJob;
import com.damai.widget.proxy.ISubmitButton;
import com.damai.widget.proxy.WidgetProxy;

public class SubmitButton extends Button implements FormSubmit{
	public SubmitButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SubmitButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private ISubmitButton widgetProxy;
	public SubmitButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(isInEditMode())return;
		widgetProxy = (ISubmitButton)WidgetProxy.getFactory().createSubmitButton(context, attrs, this);
		
	}
	
	public void setOnSubmitListener(OnSubmitListener listener){
		((FormSubmit)widgetProxy).setOnSubmitListener(listener);
	}
	
	public ApiJob getJob(){
		return widgetProxy.getJob();
	}
	
	@Override
	protected void onFinishInflate() {
		if(isInEditMode())return;
		widgetProxy.onFinishInflate();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		widgetProxy.destroy();
		widgetProxy=null;
	}

	@Override
	public void submit() {
		widgetProxy.submit();
	}

	@Override
	public void setForm(Form form) {
		widgetProxy.setForm(form);
	}

	@Override
	public void submit(int submitPolicy) {
		widgetProxy.submit(submitPolicy);
	}

	

}
