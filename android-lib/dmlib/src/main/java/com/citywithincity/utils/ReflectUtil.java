package com.citywithincity.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.damai.util.StrKit;

public class ReflectUtil {

	public static final Object[] NULL_PARAM = new Object[] { null };

	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String className, ClassLoader classLoader)
			throws Exception {
		Class<?> clazz = Class.forName(className, false, classLoader);
		return (T) clazz.newInstance();
	}

	/**
	 * 
	 * @param constructor
	 * @param list
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Constructor<?> constructor, Object[] list)
			throws Exception {
		Method cMethod = constructor.getClass().getDeclaredMethod(
				"newInstance", Array.newInstance(Object.class, 0).getClass());
		Object objs = Array.newInstance(Object.class, list.length);
		int index = 0;
		for (Object object : list) {
			Array.set(objs, index++, object);
		}
		return (T) cMethod.invoke(constructor, objs);
	}

	public static Class<?> getFieldClass(Field field) {
		ParameterizedType pt = (ParameterizedType) field.getGenericType();
		return (Class<?>) pt.getActualTypeArguments()[0];
	}

	/**
	 * 获取List类型的属性的类型
	 * 
	 * @return
	 */
	public static Class<?> getListClass(Field field) {
		ParameterizedType pt = (ParameterizedType) field.getGenericType();
		return (Class<?>) pt.getActualTypeArguments()[0];
	}

	/**
	 * 取出所有属性
	 * 
	 * @param fromClazz
	 * @param endClass
	 * @return
	 */
	public static Field[] getDeclareFields(Class<?> fromClazz, Class<?> endClass) {

		List<Field> result = new ArrayList<Field>();
		Class<?> tmp = fromClazz;
		do {
			getFields(result, tmp);
			tmp = tmp.getSuperclass();
			if (tmp == endClass) {
				break;
			}
		} while (true);
		Field[] list = new Field[result.size()];
		result.toArray(list);
		return list;
	}

	// 获取所有

	private static void getFields(List<Field> result, Class<?> clazz) {
		Field[] list = clazz.getDeclaredFields();
		for (Field field : list) {
			if ((field.getModifiers() & java.lang.reflect.Modifier.STATIC) > 0) {
				continue;
			}
			if(field.getType()==Class.class){
				continue;
			}
			result.add(field);
		}
	}

	/**
	 * map转成实体类
	 * 
	 * @param map
	 * @param object
	 */
	public static void mapToObject(Map<String, ?> map, Object object) {
		Class<?> clazz = object.getClass();
		for (Entry<String, ?> enterEntry : map.entrySet()) {
			String key = enterEntry.getKey();
			try {
				Field field = clazz.getField(key);
				field.set(object, enterEntry.getValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 包含
	 * 
	 * @param clazz
	 * @param interfaceClazz
	 * @return
	 */
	public static boolean containsInterface(Class<?> clazz,
			Class<?> interfaceClazz) {
		Class<?>[] classess = clazz.getInterfaces();
		for (Class<?> inter : classess) {
			if (inter == interfaceClazz) {
				return true;
			}
		}
		return false;
	}

	public static Map<String, Object> beanToMap(Object bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		Method[] methods = bean.getClass().getMethods();
		for (Method method : methods) {
			if (method.getParameterTypes().length > 0)
				continue;
			String name = method.getName();
			Object value = null;

			if (name.startsWith("get") && name.length() > 3) {
				name = StrKit.firstCharToLowerCase(name.substring(3));
				try {
					value = method.invoke(bean);

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			} else if (name.startsWith("is") && name.length() > 2) {
				name = StrKit.firstCharToLowerCase(name.substring(2));
				try {
					value = method.invoke(bean);

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			map.put(name, value);
		}
		return map;
	}

}
