package com.damai.note;

import java.lang.reflect.Field;

import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IViewContainer;
import com.damai.dmlib.ExceptionHandler;

public class ModelInfo implements ClsInfo  {
	/**
	 * 
	 * @param observer
	 */
	private Field field;
	private ModelFactory factory;
	public ModelInfo(Field field){
		this.field = field;
		field.setAccessible(true);
		factory = new ModelFactory();
		factory.init(field);
	}
	private IDestroyable destroyable;
	@Override
	public void setTarget(IViewContainer container) {
		
		try {
			Object model = factory.create(field.getType(), container);
			if(model instanceof IDestroyable){
				destroyable = (IDestroyable) model;
			}
			field.set(container, model);
		} catch (Exception e) {
			ExceptionHandler.handleException(e);
		}
		
	}

	@Override
	public void clearObserver() {
		if(destroyable!=null){
			destroyable.destroy();
			destroyable = null;
		}
	}

}
