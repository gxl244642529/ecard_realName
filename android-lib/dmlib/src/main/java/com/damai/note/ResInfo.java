package com.damai.note;

import java.lang.reflect.Field;

import android.view.View;

import com.citywithincity.interfaces.IViewContainer;

public class ResInfo implements ClsInfo {

	/**
	 * 
	 * @param observer
	 */
	private Field field;
	
	public ResInfo(Field field){
		this.field = field;
		field.setAccessible(true);
	}
	

	public void setTarget(IViewContainer observer) {
		View view = observer.findViewByName(field.getName());
		try {
			field.set(observer, view);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


	@Override
	public void clearObserver() {
		// TODO Auto-generated method stub
		
	}

}
