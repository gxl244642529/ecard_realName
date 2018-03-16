package com.damai.note;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IViewContainer;

public class SingletonInfoDestroyWhenExit implements ClsInfo {

	
	static class SingletonInfo{
		Object model;
		int count;
	}
	
	/**
	 * 
	 * @param observer
	 */
	private Field field;
	private static Map<Class<?>, SingletonInfo> map = new HashMap<Class<?>, SingletonInfo>();
	
	private ModelFactory factory;
	
	public SingletonInfoDestroyWhenExit(Field field){
		this.field = field;
		field.setAccessible(true);
		factory = new ModelFactory();
		factory.init(field);
		
		
	}
	@Override
	public void setTarget(IViewContainer container) {
		Field field = this.field;
		Class<?> clazz = field.getType();
		SingletonInfo info  = map.get(clazz);
		try{
			if(info==null){
				
				Object model = factory.create(clazz, container);
				
				info = new SingletonInfo();
				info.model = model;
				info.count = 1;
				map.put(clazz, info);
			}else{
				info.count ++;
			}
			field.set(container, info.model);
		}catch(Exception e){
			throw new RuntimeException("Error when init model "+clazz+" field "+field.getName(),e);
		}
		
	}

	@Override
	public void clearObserver() {
		final Field field = this.field;
		Class<?> clazz = field.getType();
		SingletonInfo info  = map.get(clazz);
		if(info==null)return;
		info.count--;
		if(info.count<=0){
			if(info.model instanceof IDestroyable){
				((IDestroyable)info.model).destroy();
			}
			info.model = null;
			map.remove(clazz);
		}
	}

}
