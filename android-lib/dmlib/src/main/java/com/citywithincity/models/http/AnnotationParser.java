package com.citywithincity.models.http;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.citywithincity.auto.JsonField;
import com.citywithincity.interfaces.IJsonParser;
import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.libs.LibConfig;
class AnnotationParser<T> implements IJsonParser<T> {

	protected static abstract class ToValueBase{
		protected String name;
		protected Field field;
		protected boolean check;
		public ToValueBase(String name,Field field,boolean check){
			this.field = field;
			this.name = name;
			this.check = check;
		}
		public abstract void getValue(JSONObject json,Object object) throws IllegalAccessException, IllegalArgumentException, JSONException;
	}
	
	protected static class ToValueString extends ToValueBase{

		public ToValueString(String name, Field field,boolean check) {
			super(name, field,check);
		}

		@Override
		public void getValue(JSONObject json, Object object)
				throws IllegalAccessException, IllegalArgumentException,
				JSONException {
			if(!check || json.has(name)){
				field.set(object, json.getString(name));
			}
			
		}
	}
	
	protected static class ToValueList<T> extends ToValueBase{

		protected Class<T> clazz;
		
		public ToValueList(String name, Field field, boolean check,Class<T> cls) {
			super(name, field, check);
			this.clazz = cls;
		}

		@Override
		public void getValue(JSONObject json, Object object)
				throws IllegalAccessException, IllegalArgumentException,
				JSONException {
			if(!check || json.has(name)){
				JSONArray arr = json.getJSONArray(name);
				@SuppressWarnings("unchecked")
				List<T> list = (List<T>)field.get(object);
				if(list==null){
					list = new ArrayList<T>();
					field.set(object, list);
				}
				IJsonParser<T> parser = JsonTaskManager.getInstance().createParser(clazz);
				int count = arr.length();
				for(int i=0; i < count; ++i){
					JSONObject obj = arr.getJSONObject(i);
					T result = (T)parser.parseResult(obj);
					list.add(result);
				}
			}
		}
		
	}
	
	
	protected static class ToValueImage extends ToValueBase{

		public ToValueImage(String name, Field field,boolean check) {
			super(name, field,check);
		}

		@Override
		public void getValue(JSONObject json, Object object)
				throws IllegalAccessException, IllegalArgumentException,
				JSONException {
			if(!check || json.has(name)){
				field.set(object, LibConfig.getImageUrl(json.getString(name)));
			}
			
		}
	}
	
	
	protected static class ToValueInt extends ToValueBase{

		public ToValueInt(String name, Field field,boolean check) {
			super(name, field,check);
		}

		@Override
		public void getValue(JSONObject json, Object object)
				throws IllegalAccessException, IllegalArgumentException,
				JSONException {
			if(!check || json.has(name)){
				field.setInt(object, json.getInt(name));
			}
		}
	}
	
	protected static class ToValueObject extends ToValueBase{

		public ToValueObject(String name, Field field, boolean check) {
			super(name, field, check);
		}

		@Override
		public void getValue(JSONObject json, Object object)
				throws IllegalAccessException, IllegalArgumentException,
				JSONException {
			if(!check || json.has(name)){
				@SuppressWarnings("rawtypes")
				IJsonParser parser = JsonTaskManager.getInstance().createParser(field.getClass());
				Object value = parser.parseResult(json.getJSONObject(name));
				field.set(object, value);
			}
		}
		
	}
	
	
	protected static class ToValueDouble extends ToValueBase{

		public ToValueDouble(String name, Field field,boolean check) {
			super(name, field,check);
		}

		@Override
		public void getValue(JSONObject json, Object object)
				throws IllegalAccessException, IllegalArgumentException,
				JSONException {
			if(!check || json.has(name)){
				field.setDouble(object, json.getDouble(name));
			}
			
		}
	}

	
	protected static class ToValueBoolean extends ToValueBase{

		public ToValueBoolean(String name, Field field,boolean check) {
			super(name, field,check);
		}

		@Override
		public void getValue(JSONObject json, Object object)
				throws IllegalAccessException, IllegalArgumentException,
				JSONException {
			if(!check || json.has(name)){
				field.setBoolean(object, json.getBoolean(name));
			}
		}
	}



	
	private Class<T> clazz;
	private List<ToValueBase> list;
	
	@SuppressLint("DefaultLocale")
	public AnnotationParser(Class<T> clazz){
		this.list = new ArrayList<ToValueBase>();
		this.clazz = clazz;
		Field[] fields = clazz.getFields();
	    for (Field field : fields) {
	         if(field.isAnnotationPresent(JsonField.class))
	         {
	        	 JsonField des = (JsonField) field.getAnnotation(JsonField.class);
	        	 String name;
	        	
	        	 boolean check = des.check();
	        	 if(!TextUtils.isEmpty(des.name())){
	        		 name =des.name();
	        	 }else{
	        		 name = field.getName();
	        		 if(des.upper()){
		        		 name = name.toUpperCase();
		        	 }
	        	 }
	        	 
	        	 if(des.img()){
	        		 list.add(new ToValueImage(name, field,check));
	        	 }else{
	        		 
	        		 Class<?> typeClass = field.getType();
	        		 if(typeClass.isPrimitive()){
	        			 if(typeClass.equals(Integer.class)){
			        		 list.add(new ToValueInt(name, field,check));
			        	 }else if(typeClass.equals(Double.class)){
			        		 list.add(new ToValueDouble(name, field,check));
			        	 }else if(typeClass.equals(Boolean.class)){
			        		 list.add(new ToValueBoolean(name, field,check));
			        	 }
	        		 }else if(typeClass.equals(String.class)){
		        		 list.add(new ToValueString(name, field,check));
		        	 }else if(typeClass.isAssignableFrom(List.class)){
		        		 ParameterizedType pt = (ParameterizedType) field.getGenericType();
		        		 Class<?> cls = (Class<?>)pt.getActualTypeArguments()[0];
		        		 list.add(new ToValueList(name, field, check, cls));
		        	 }else{
		        		list.add(new ToValueObject(name, field, check));
		        	 }
	        	 }
	        	
	         }
		}
	}
	
	@Override
	public T parseResult(JSONObject json) throws JSONException {
		T object = null;
		try {
			object = this.clazz.newInstance();
			for (ToValueBase value : list) {
				value.getValue(json, object);
			}
			if(object instanceof IJsonValueObject){
				((IJsonValueObject)object).fromJson(json);
			}
			return object;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
