package com.damai.widget.proxy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


public interface WidgetFactory {
	WidgetProxy createDetailView(Context context, AttributeSet attrs, ViewGroup detailView);
	WidgetProxy createSubmitButton(Context context, AttributeSet attrs, Button submitButton);
	WidgetProxy createStateListView(Context context, AttributeSet attrs, FrameLayout stateListView);
	WidgetProxy createStateListView(Context context,FrameLayout stateListView);
	
	WidgetProxy createFormView(Context context, AttributeSet attrs, ViewGroup formView);
	WidgetProxy createTabView(Context context, AttributeSet attrs, FrameLayout tabView);
	WidgetProxy createTabGroup(Context context, AttributeSet attrs, ViewGroup tabGroup);
	WidgetProxy createTabHost(Context context, AttributeSet attrs, ViewGroup tabGroup);
	WidgetProxy createAdvView(Context context, AttributeSet attrs, RelativeLayout advView);
	WidgetProxy createTabListView(Context context, AttributeSet attrs, RelativeLayout tabListView);
	
}
