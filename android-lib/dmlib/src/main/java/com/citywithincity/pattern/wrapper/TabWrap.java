package com.citywithincity.pattern.wrapper;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.citywithincity.interfaces.IOnTabChangeListener;
import com.damai.lib.R;

public class TabWrap implements OnClickListener {
	
	private IOnTabChangeListener onTabChangeListener;
	private List<View> stateViews = new ArrayList<View>();
	private List<View> stateViews2 = new ArrayList<View>();
	private int lastIndex = -1;
	
	public TabWrap(ViewGroup viewGroup,IOnTabChangeListener onTabChangeListener){
		for(int i=0 , count = viewGroup.getChildCount(); i < count; ++i){
			View view = viewGroup.getChildAt(i);
			view.setTag(i);
			view.setOnClickListener(this);
			stateViews.add(view);
			stateViews2.add(view.findViewById(R.id._text_view));
		}
		this.onTabChangeListener  = onTabChangeListener;
	}

	@Override
	public void onClick(View v) {
		int index = (Integer) v.getTag();
		setSelectedIndex(index);
		onTabChangeListener.onTabChange(index);
	}
	
	/**
	 * 设置选中
	 * @param index
	 */
	public void setSelectedIndex(int index){
		if(lastIndex == index)return;
		if(lastIndex >= 0){
			stateViews.get(lastIndex).setSelected(false);
			stateViews2.get(lastIndex).setSelected(false);
		}
		
		lastIndex = index;
		stateViews.get(lastIndex).setSelected(true);
		stateViews2.get(lastIndex).setSelected(true);
	}
	
	public int getCurrentIndex(){
		return lastIndex;
	}

	public void destroy() {
		stateViews.clear();
		stateViews2.clear();
		stateViews = null;
		stateViews2 = null;
		onTabChangeListener = null;
	}
}
