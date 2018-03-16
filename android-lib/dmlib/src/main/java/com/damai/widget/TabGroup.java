package com.damai.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.damai.interfaces.ITab;
import com.damai.interfaces.ITabGroup;
import com.damai.interfaces.ITabIndicator;
import com.damai.lib.R;

/**
 * 选项组
 * @author Randy
 *
 */
public class TabGroup extends LinearLayout implements ITabGroup, IMessageListener {

	
	public TabGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	public void setOnTabChangeListener(OnTabChangeListener listener){
		this.tabListener = listener;
	}
	
	public int getTabCount(){
		return buttons.size();
	}
	
	private OnTabChangeListener tabListener;
	private ITab tab;
	private int lastIndex = -1;
	private List<View> buttons;
	//指示器
	private ITabIndicator indicator;
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			Integer index = (Integer)view.getTag();
			setCurrentIndex(index);
		}
	};
	
	
	
	@Override
	protected void onDetachedFromWindow() {
		buttons.clear();
		buttons = null;
		tabListener = null;
		tab = null;
		indicator = null;
		super.onDetachedFromWindow();
		
	}

	@Override
	protected void onFinishInflate() {
		buttons = new ArrayList<View>(getChildCount());
		for(int i=0 , index = 0,count = getChildCount(); i < count; ++i){
			View view = getChildAt(i);
			if(shoundAddToButtonList(view)){
				view.setTag(index++);
				view.setOnClickListener(listener);
				buttons.add(view);
			}
		}
		
		if(buttons.size()>0){
			lastIndex = 0;
			buttons.get(0).setSelected(true);
		}
		
		MessageUtil.sendMessage(this);
	}
	
	protected boolean shoundAddToButtonList(View view ){
		return view instanceof Button;
	}

	public void setCurrentIndex(int index) {
		if(index!=lastIndex){
			if(tabListener!=null){
				tabListener.onTabChanged(TabGroup.this, index, true);
			}
			if(tab!=null){
				tab.setCurrentIndex(index, true);
			}
			if(indicator!=null){
				indicator.setCurrentIndex(index, true);
			}
			buttons.get(index).setSelected(true);
			if(lastIndex>=0){
				buttons.get(lastIndex).setSelected(false);
			}
			lastIndex = index;
			
		}
	}

	@Override
	public void setCurrentIndex(int index, boolean animated) {
		setCurrentIndex(index);
	}

	@Override
	public void onMessage(int message) {
		ViewGroup viewGroup =  (ViewGroup) getParent();
		for(int i=0; i < 3; ++i){
			if(viewGroup==null){
				break;
			}
			tab = (ITab) viewGroup.findViewById(R.id._tab_container);
			if(tab!=null){
				break;
			}
			viewGroup =  (ViewGroup) viewGroup.getParent();
		}
		
		indicator = (ITabIndicator)viewGroup.findViewById(R.id._tab_indicator);
		
	}
}
