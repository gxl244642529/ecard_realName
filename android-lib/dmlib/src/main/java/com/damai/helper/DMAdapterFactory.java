package com.damai.helper;

import com.citywithincity.interfaces.IViewContainer;

public class DMAdapterFactory {
	
	public static interface IDMAdapterFactory{
		<T> DMAdapter<T> create(IViewContainer context,int itemResId);
	}
	
	private static IDMAdapterFactory gFactory;
	
	public static void setFactory(IDMAdapterFactory factory){
		gFactory = factory;
	}
	
	public static <T> DMAdapter<T> create(IViewContainer context,int itemResId){
		return gFactory.create(context, itemResId);
	}
}
