package com.damai.widget.proxy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


public class WidgetFactoryImpl implements WidgetFactory {

	@Override
	public WidgetProxy createDetailView(Context context, AttributeSet attrs,
			ViewGroup detailView) {
		return new DetailViewProxy(context, attrs, detailView);
	}

	@Override
	public WidgetProxy createSubmitButton(Context context, AttributeSet attrs,
			Button submitButton) {
		return new SubmitButtonProxy(context, attrs, submitButton);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public WidgetProxy createStateListView(Context context, AttributeSet attrs,
			FrameLayout stateListView) {
		
		return new StateListViewProxy(context, attrs, stateListView);
	}

	@Override
	public WidgetProxy createFormView(Context context, AttributeSet attrs,
			ViewGroup formView) {
	
		return new FormViewProxy(context, attrs, formView);
	}

	@Override
	public WidgetProxy createTabView(Context context, AttributeSet attrs,
			FrameLayout tabView) {
		
		
		return new TabViewProxy(context, attrs, tabView);
	}

	@Override
	public WidgetProxy createTabGroup(Context context, AttributeSet attrs,
			ViewGroup tabGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WidgetProxy createAdvView(Context context, AttributeSet attrs,
			RelativeLayout advView) {
		return new AdvViewProxy(context, attrs, advView);
	}

	@Override
	public WidgetProxy createTabListView(Context context, AttributeSet attrs,
			RelativeLayout tabListView) {
		return new TabListViewProxy(context, attrs, tabListView);
	}

	@Override
	public WidgetProxy createTabHost(Context context, AttributeSet attrs,
			ViewGroup tabGroup) {
		
		return new TabHostProxy(context, attrs, tabGroup);
	}

	@Override
	public WidgetProxy createStateListView(Context context,
			FrameLayout stateListView) {
		// TODO Auto-generated method stub
		return null;
	}


}
