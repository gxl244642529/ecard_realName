package com.damai.note;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.citywithincity.interfaces.IViewContainer;

public class SingletonInfoAll implements ClsInfo  {
	/**
	 * 
	 * @param observer
	 */
	private Field field;
	private ModelFactory factory;
	public SingletonInfoAll(Field field){
		this.field = field;
		field.setAccessible(true);
		factory = new ModelFactory();
		factory.init(field);
	}

	
	private static Map<Class<?>,  Object> map = new HashMap<Class<?>, Object>();
	
	@Override
	public void setTarget(IViewContainer container) {
		Field field = this.field;
		Class<?> clazz = field.getType();
		Object model  = map.get(clazz);
		try{
			if(model==null){
				model = factory.create(clazz, container);
				map.put(clazz, model);
			}
			field.set(container, model);
		}catch(Exception e){
			throw new RuntimeException("Error when init model "+clazz+" field "+field.getName(),e);
		}
		
	}

	@Override
	public void clearObserver() {
		
	}
}
