package com.damai.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.utils.ReflectUtil;
import com.damai.util.StrKit;


/**
 * 数据转化
 * @author Randy
 *
 */
public class EntityUtil {
	
	/**
	 * 获取数据
	 * @param data
	 * @param fieldName
	 * @return
	 */
	public static Object get(Object data,String fieldName){
		if(data instanceof JSONObject){
			try {
				return ((JSONObject)data).get(fieldName);
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}
		try {
			Field field = data.getClass().getField(fieldName);
			try{
				return field.get(data);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		} catch (NoSuchFieldException e) {
			String getterName = StrKit.getterName(fieldName);
			try {
				Method method = data.getClass().getMethod(getterName);
				return method.invoke(data);
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
		}
		
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List parseArray(JSONArray arr, Class<?> clazz) throws JSONException{
		
		if(arr==null){
			return Collections.EMPTY_LIST;
		}
		if(clazz==null){
			List<JSONObject> list = new ArrayList<JSONObject>();
			for(int i=0, count = arr.length(); i < count ; ++i){
				list.add(arr.getJSONObject(i));
			}
			return list;
		}
		
		
		if(ReflectUtil.containsInterface(clazz,IJsonValueObject.class)){
			int count = arr.length();
			List list = new ArrayList(count);
			for(int i=0; i < count; ++i){
				JSONObject json = arr.getJSONObject(i);
				IJsonValueObject data = null;
				try{
					data = (IJsonValueObject)clazz.newInstance();
				}catch(Exception e){
					e.printStackTrace();
				}
				data.fromJson(json);
				list.add(data);
			}
			return list;
		}else{
			
			try {
				return listToList(arr,clazz);
			} catch (Exception e) {
				throw new JSONException(e.getMessage());
			}
			
		}
		
		
		
	}
	
	
	private static final Map<Class<?>, Field[]> map = new HashMap<Class<?>, Field[]>();
	
	private static final Field[] getFields(Class<?> clazz){
		Field[] fields = map.get(clazz);
		if(fields==null){
			fields = ReflectUtil.getDeclareFields(clazz, Object.class);
			for (Field field : fields) {
				field.setAccessible(true);
			}
			map.put(clazz, fields);
		}
		return fields;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List listToList(JSONArray arr,Class<?> clazz) throws InstantiationException, IllegalAccessException, JSONException{
		int count = arr.length();
		List list = new ArrayList(count);
		Field[] fields =getFields(clazz); 
		for(int i=0; i < count; ++i){
			JSONObject json = arr.getJSONObject(i);
			list.add(jsonToObject(json,clazz,fields));
		}
		return list;
	}
	public static Object jsonToObject(JSONObject json,Class<?> clazz) throws InstantiationException, IllegalAccessException, JSONException{
		return jsonToObject(json, clazz,getFields(clazz));
	}
	
	
	private static String[] toStringArray(JSONArray arr) throws JSONException{
		int c = arr.length();
		String[] result = new String[c];
		for(int i=0 ; i < c; ++i){
			result[i]=arr.getString(i);
		}
		return result;
	}
	
	public static Object jsonToObject(JSONObject json,Class<?> clazz,Field[] fields) throws InstantiationException, IllegalAccessException, JSONException{
		Object data = clazz.newInstance();
		for (Field field : fields) {
			String name = field.getName();
			if(json.has(name) && !json.isNull(name)){
				Object value = json.get(name);
				if(value instanceof JSONArray){
					Class<?>  type = field.getType();
					if(type == List.class){
						type = ReflectUtil.getFieldClass(field);
						field.set(data,listToList( (JSONArray)value , type));
					}else{
						//其他直接set
						String typeName = type.getName();
						if(typeName.startsWith("[L")){
							//[Ljava.lang.String;
							value=toStringArray((JSONArray)value);
						}
						
						field.set(data, value);
					}
				}else if(value instanceof JSONObject){
					field.set(data,jsonToObject((JSONObject)value, field.getType(), getFields(field.getType())));
				}else{
					field.set(data, value);
				}
			}
		}
		return data;
	}
	
	public static Method[] findSetters(Class<?> clazz){
		Method[] methods = clazz.getMethods();
		List<Method> list = new ArrayList<Method>(methods.length);
		for (Method method : methods) {
			String name = method.getName();
			if(method.getParameterTypes().length==1 && name.startsWith("set") && name.length() > 3){
				list.add(method);
			}
		}
		methods = new Method[list.size()];
		list.toArray(methods);
		return methods;
	}
	
	
	
}
