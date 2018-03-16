package com.damai.widget.proxy;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.damai.interfaces.ITab;
import com.damai.lib.R;
import com.damai.widget.OnTabChangeListener;
import com.damai.widget.TabFragmentAdapter;

public class TabViewProxy extends WidgetProxy implements IMessageListener,ITabView{

	private ViewPager viewPager;
	private String[] fragmentClasses;
	private OnTabChangeListener tabChangeListener;
	private ITab tab;
	
	
	public TabViewProxy(Context context, AttributeSet attrs, FrameLayout view) {
		super(context, attrs, view);
		viewPager = new ViewPager(context);
		viewPager.setId(R.id._view_pager);
		view.addView(viewPager);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable._tab_host);
		String strings = a.getString(R.styleable._tab_host_tab_fragments);
		if(strings!=null){
			fragmentClasses = strings.split(",");
		}else{
			throw new RuntimeException("Cannot find string with R.styleable._tab_host_tab_fragments,please define it!");
		}
		a.recycle();
	}
	
	private void onPageSelected(int index){
		if(tabChangeListener!=null){
			tabChangeListener.onTabChanged(viewPager, index, true);
		}
		if(tab!=null){
			tab.setCurrentIndex(index, false);
		}
	}

	OnPageChangeListener listener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int index) {
			TabViewProxy.this.onPageSelected(index);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
	};
	public void setOnTabChangeListener(OnTabChangeListener listener){
		tabChangeListener = listener;
	}
	@Override
	public void onFinishInflate() {
		if(view.getId()==View.NO_ID){
			view.setId(R.id._tab_container);
		}
		FragmentActivity activity = (FragmentActivity) getContext();
		TabFragmentAdapter adapter = new TabFragmentAdapter(activity.getSupportFragmentManager(), getContext().getClassLoader(), fragmentClasses);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(listener);
		MessageUtil.sendMessage(this);
	}
	
	public void setCurrentIndex(int index, boolean animated) {
		ViewPager viewPager =this.viewPager;
		viewPager.setOnPageChangeListener(null);
		viewPager.setCurrentItem(index, animated);
		viewPager.setOnPageChangeListener(listener);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		tabChangeListener = null;
		fragmentClasses = null;
		tab = null;
	}


	@Override
	public void onMessage(int message) {
		ViewGroup viewGroup = (ViewGroup) view.getParent();
		tab = (ITab) viewGroup.findViewById(R.id._tab_group);
	}
	

}
