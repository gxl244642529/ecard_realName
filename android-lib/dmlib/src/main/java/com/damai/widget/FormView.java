package com.damai.widget;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.damai.widget.proxy.WidgetProxy;

/**
 * 表单
 * 
 * @author Randy
 * 
 */
public class FormView extends LinearLayout implements Form {
	
	private WidgetProxy proxy;
	private Form form;
	public FormView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(isInEditMode())return;
		proxy = WidgetProxy.getFactory().createFormView(context, attrs, this);
		form = (Form) proxy;
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		if(isInEditMode())return;
		proxy.onFinishInflate();
	}


	@Override
	protected void onDetachedFromWindow() {
		proxy.destroy();
		proxy=null;
		super.onDetachedFromWindow();
	}



	@Override
	public List<FormElement> getElements() {
		return form.getElements();
	}

	@Override
	public FormElement getElement(String name) {
		return form.getElement(name);
	}

	@Override
	public boolean isPersistent() {
		return form.isPersistent();
	}

	@Override
	public void persistent() {
		form.persistent();
	}

}
