package com.citywithincity.auto.tools;

import android.app.Activity;
import android.view.View;

import com.citywithincity.interfaces.IItemEventHandlerGroup;

public class AutoCreator {
	
	
	public static interface IItemEventHandlerCreator{
		IItemEventHandlerGroup createItemEventHandlerGroup(Object target);
	}
	
	
	
	public static interface IEventHandlerCreator{
		void registerEvent(Object target);
	}
	
	/**
	 * 设置子item的数据
	 * @author randy
	 *
	 */
	public static interface IBinddataViewSetterCreator{
		void setBinddataViewValues(Object data, View view,int layoutID);
		void setBinddataViewValues(Object data, Activity view);
	}
	
	protected static IItemEventHandlerCreator itemEventHandlerCreator;
	
	
	public static void setItemEventHandlerCreator(IItemEventHandlerCreator value){
		itemEventHandlerCreator  = value;
	}
	
	/**
	 * 为dataprovider提供子item的itemhandler
	 * @param target
	 * @return
	 */
	public static IItemEventHandlerGroup createItemEventHandler(Object target){
		return itemEventHandlerCreator.createItemEventHandlerGroup(target);
	}
	
	
	
	
	
}
