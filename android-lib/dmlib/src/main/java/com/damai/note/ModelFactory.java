package com.damai.note;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import android.content.Context;

import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.utils.ReflectUtil;

public class ModelFactory {


	private boolean initWithContext;
	public void init(Field field){
		Class<?> clazz = field.getType();
		Constructor<?>[] constructors=clazz.getConstructors();
		if(constructors.length==0){
			throw new RuntimeException("Singleton必须有一个构造函数");
		}
		Constructor<?> c = constructors[0];
		Class<?>[] typesClass=c.getParameterTypes();
		if(typesClass.length>1){
			throw new RuntimeException("Singleton的构造函数参数只能为1个或0个");
		}
		if(typesClass.length==1){
			if(!Context.class.isAssignableFrom(typesClass[0])){
				throw new RuntimeException("Singleton的构造函数只能为无参数或者Context的子类");
			}
			initWithContext=true;
		}
	}
	public Object create(Class<?> clazz,IViewContainer container) throws Exception{
		if(initWithContext){
			return ReflectUtil.newInstance(clazz.getConstructors()[0], new Object[]{container.getActivity()});
		}else{
			return clazz.newInstance();
		}
	}
}
