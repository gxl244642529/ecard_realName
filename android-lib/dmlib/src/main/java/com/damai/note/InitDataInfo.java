package com.damai.note;

import java.lang.reflect.Field;

import com.citywithincity.interfaces.IViewContainer;
import com.damai.helper.DataHolder;

public class InitDataInfo implements ClsInfo  {
	/**
	 * 
	 * @param observer
	 */
	private Field field;
	
	public InitDataInfo(Field field){
		this.field = field;
		field.setAccessible(true);
	}

	@Override
	public void setTarget(IViewContainer container) {
		try {
			field.set(container,DataHolder.get(container.getActivity().getClass()));
		} catch (Exception e) {
			throw new RuntimeException("Error when set init data "+container.getClass() + " field " + field.getName(),e);
		}
	}

	@Override
	public void clearObserver() {
		// TODO Auto-generated method stub
		
	}
}
