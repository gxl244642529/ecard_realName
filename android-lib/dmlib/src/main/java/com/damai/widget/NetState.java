package com.damai.widget;

import android.view.ViewGroup;

import com.citywithincity.interfaces.IDestroyable;

public class NetState implements IDestroyable {
	private ViewGroup parent;
	
	public NetState(ViewGroup parent){
		this.parent = parent;
	}
	

	@Override
	public void destroy() {
		parent = null;
	}
	
	
}
