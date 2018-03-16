package com.damai.widget.proxy;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;

import com.damai.interfaces.ITab;
import com.damai.lib.R;

public class TabHostProxy extends WidgetProxy implements ITab {
	private Fragment[] fragments;
	private int lastIndex = -1;
	private String[] fragmentClasses;
	
	public TabHostProxy(Context context, AttributeSet attrs, View view) {
		super(context, attrs, view);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable._tab_host);
		String fragments = a.getString(R.styleable._tab_host_tab_fragments);
		if(fragments!=null){
			fragmentClasses = fragments.split(",");
		}else{
			throw new RuntimeException("请使用tab_fragments定义所需fragment的class类名称，中间以/逗号/隔开");
		}
		a.recycle();
	}

	@Override
	public void onFinishInflate() {
		fragments = new Fragment[fragmentClasses.length];
		setCurrentIndex(0, false);
	}
	/**
	 * 创建fragment
	 * @param index
	 * @return
	 */
	private Fragment createFragment(int index) {
		try {
			Class<?> clazz = Class.forName(fragmentClasses[index]);
			return (Fragment) clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}


	@Override
	public void setCurrentIndex(int index, boolean animated) {
		Fragment fragment;
		FragmentActivity activity = (FragmentActivity) getContext();
		FragmentManager fm = activity.getSupportFragmentManager();  
        FragmentTransaction transaction = fm.beginTransaction();
        if(lastIndex>=0){
        	fragment = fragments[lastIndex];
        	transaction.hide(fragment);
        }
        fragment = fragments[index];
		if(fragment==null){
			fragment = createFragment(index);
			fragments[index] = fragment;
			transaction.add(view.getId(),fragment);
		}else{
			transaction.show( fragment );
		}
		
		lastIndex = index;
		transaction.commit();  
	}

	
	
	@Override
	public void destroy() {
		for(int i=0 ,count = fragments.length; i < count; ++i){
			fragments[i] = null;
		}
		fragments = null;
		fragmentClasses = null;
		super.destroy();
	}
}
