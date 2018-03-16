package com.citywithincity.auto.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;

public class Notifier {

	public static String makeError(String api){
		return api + "_error";
	}


	private static Map<String, List<Object[]>> messageMap = new HashMap<String, List<Object[]>>();

	public static void register(Object obj) {
		Class<?> clazz = obj.getClass();
		if (clazz.isAnnotationPresent(Observer.class)) {
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(NotificationMethod.class)) {
					NotificationMethod m = (NotificationMethod) method
							.getAnnotation(NotificationMethod.class);
					register(m.value(), obj,
							method);
				}
			}
		}
	}
	

	//哪个note，归哪个管 object[] 保存 {commandClass,Method}[]
	private static Map<String,List<Object[]>> map =
			new LinkedHashMap<String, List<Object[]>>();
	private static Set<Class<? extends Command>> set = new HashSet<Class<? extends Command>>();
	
	
	public static boolean onNotify(String note,Object...args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		List<Object[]> list = map.get(note);
		if(list!=null){
			for (Object[] objects : list) {
				Class<? extends Command> clazz = (Class<? extends Command>)objects[0];
				Method method = (Method)objects[1];
				Command command = clazz.newInstance();
				method.invoke(command, args);
			}
			return true;
		}
		return false;
		
	}

	/**
	 * 注册command
	 * 
	 * @param notification
	 * @param clazz
	 */
	public static void register(Class<? extends Command> clazz) {
		//执行方法
		//这个command是否被分析过了
		if(set.contains(clazz))return;
		set.add(clazz);
		
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if(method.isAnnotationPresent(NotificationMethod.class)){
				NotificationMethod m = method.getAnnotation(NotificationMethod.class);
				String note = m.value();
				List<Object[]> list = map.get(note);
				if(list==null){
					list = new ArrayList<Object[]>();
					map.put(note, list);
				}
				list.add(new Object[]{
						clazz,
						method
				});
			}
		}
	}

	
	
	public static void unregister(Object obj){
		Class<?> clazz = obj.getClass();
		if (clazz.isAnnotationPresent(Observer.class)) {
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(NotificationMethod.class)) {
					NotificationMethod m = (NotificationMethod) method
							.getAnnotation(NotificationMethod.class);
					remove(m.value(), obj);
				}
			}
		}
	}

	public static void register(String notification, Object object, Method method) {

		List<Object[]> objects = messageMap.get(notification);
		if (objects == null) {
			objects = new ArrayList<Object[]>();
			messageMap.put(notification, objects);
		}
		objects.add(new Object[] { object, method });
	}

	public static void remove(String notification, Object object) {
		List<Object[]> objects = messageMap.get(notification);
		if (objects != null) {
			Iterator<Object[]> it = objects.iterator();
			while (it.hasNext()) {
				Object[] objs = it.next();
				if (objs[0] == object) {
					it.remove();
				}
			}
		}

	}

	public static boolean notifyObservers(String notification, Object... data) {
		List<Object[]> objects = messageMap.get(notification);
		boolean notified = false;
		if (objects != null) {

			for (Object[] obj : objects) {

				Object object = obj[0];
				Method method = (Method) obj[1];

				try {
					method.invoke(object, data);
					notified = true;
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}

			}
		}
		
		try {
			return notified || onNotify(notification, data);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return notified;

	}

}
