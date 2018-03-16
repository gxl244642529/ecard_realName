package com.damai.helper;


import java.util.HashMap;
import java.util.Map;

import android.app.Activity;

/**
 * 传递数据使用
 * @author Randy
 *
 */
public class DataHolder {
	public static Map<Class<? extends Activity>,Object> map = new HashMap<Class<? extends Activity>,Object>();
	@SuppressWarnings("unchecked")
	public static <T> T get(Class<? extends Activity> clazz){
		return (T) map.get(clazz);
	}
	public static void set(Class<? extends Activity> targetClass,Object data){
		if(data == null){
			map.remove(targetClass);
		}else{
			map.put(targetClass,data);
		}

	}
}
