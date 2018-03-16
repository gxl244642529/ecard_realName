package com.citywithincity.models;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.citywithincity.interfaces.ILocalData;
import com.citywithincity.utils.CollectionUtil;
import com.citywithincity.utils.MemoryUtil;
import com.damai.util.JsonUtil;
import com.damai.util.StrKit;


public class LocalData implements ILocalData {

	public enum LocalDataMode{
		MODE_READ,
		MODE_WRITE
	}

	//////////////
	protected static Context _context;

	public static void setContext(Context context){
		_context = context;
	}


	public static ILocalData read(){
		return new LocalData(_context, LocalDataMode.MODE_READ);
	}

	public static ILocalData write(){
		return new LocalData(_context, LocalDataMode.MODE_WRITE);
	}


	public static ILocalData createDefault(LocalDataMode mode){
		return new LocalData(_context, mode);
	}

	public static ILocalData createLocalData(String mainKey,LocalDataMode mode){
		return new LocalData(_context, mainKey,mode);
	}

	protected final SharedPreferences _sp;
	protected final Editor _edit;

	protected LocalData(Context context, String mainKey,LocalDataMode mode) {
		_sp = context.getSharedPreferences(mainKey, Context.MODE_PRIVATE);
		if( (mode==LocalDataMode.MODE_WRITE) ){
			_edit = _sp.edit();
		}else{
			_edit = null;
		}
	}

	protected LocalData(Context context,LocalDataMode mode) {
		this(context, "default", mode);
	}

	@Override
	public ILocalData clearObject(String subKey, Class<?> clazz) {
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			String name = subKey + field.getName();
			Class<?> fieldClass = field.getType();
			if(fieldClass == Integer.class || fieldClass == int.class){
				putInt(name, 0);
			}else if(fieldClass == String.class){
				putString(name, null);
			}
		}
		return this;
	}




	@Override
	public <T> T getObject(String subKey, Class<T> clazz) {
		T result;
		try {
			result = clazz.newInstance();
		} catch (Exception e1) {
			return null;
		}
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			String name = subKey + field.getName();
			Class<?> type = field.getType();
			try{
				if(type==Integer.class || type== int.class){
					field.setInt(result, getInt(name, 0));
				}else if(type==Boolean.class || type== boolean.class){
					field.setBoolean(result, getBoolean(name, false));
				}else if(type==Float.class || type== float.class){
					field.setFloat(result, getFloot(name, 0));
				}else if(type==Double.class || type== double.class){
					field.setDouble(result, getFloot(name, 0));
				}else if(type == Map.class){
					String string = getString(name, null);
                    if(string!=null){
                        JSONObject jsonObject = new JSONObject(string);
                        field.set(result, MemoryUtil.jsonToMap(jsonObject));
                    }else{
                        field.set(result,null);
                    }

				}else{
					field.set(result, getString(name, null));
				}
			}catch(Exception e){

			}
		}
		for(Method method : clazz.getMethods()){
			if( (method.getModifiers() & Modifier.STATIC) > 0){
				continue;
			}
			String name = method.getName();
			if(name.startsWith("set")){
				name = subKey + StrKit.firstCharToLowerCase(name.substring(3));
				Class<?> type = method.getParameterTypes()[0];
				try{
					if(type==Integer.class || type== int.class){
						method.invoke(result, getInt(name, 0));
					}else if(type==Boolean.class || type== boolean.class){
						method.invoke(result, getBoolean(name, false));
					}else if(type==Float.class || type== float.class){
						method.invoke(result, getFloot(name, 0));
					}else if(type==Double.class || type== double.class){
						method.invoke(result, getFloot(name, 0));
					}else if(type == Map.class){
						String string = getString(name, null);
                        if(string!=null){
                            JSONObject jsonObject = new JSONObject(string);
                            method.invoke(result, MemoryUtil.jsonToMap(jsonObject));
                        }else{
                            method.invoke(result,new Object[]{});
                        }
					}else{
						method.invoke(result, getString(name, null));
					}
				}catch(Exception e){

				}

			}
		}
		return result;
	}
	@Override
	public Map<String, Object> getMap(String key) {
		String value = getString(key, null);
		if(value==null)return null;

		try {
			JSONObject jsonObject = new JSONObject(value);
			return MemoryUtil.jsonToMap(jsonObject);
		} catch (JSONException e) {
			return null;
		}
	}
	@Override
	public ILocalData putMap(String key, Map<String, Object> map) {
		return putString(key, JsonUtil.toJson(map));
	}

	@Override
	public <T> ILocalData putObject(String subKey, T data) {
		Field[] fields = data.getClass().getFields();
		for (Field field : fields) {
			String name = subKey + field.getName();
			Class<?> type = field.getType();
			try{
				Object value = field.get(data);
				if(type==Integer.class || type== int.class){
					putInt(name, (Integer)value);
				}else if(type==Boolean.class || type== boolean.class){
					putBoolean(name, (Boolean)value);
				}else if(type==Float.class || type== float.class){
					putFloat(name, (Float)value);
				}else if(type==Double.class || type== double.class){
					putFloat(name, (float)(double)(Double)value);
				}else if(value instanceof Map){
					putString(name,  MemoryUtil.mapToJson( (Map)value ).toString()  );
				}else{
					putString(name, (String)value);
				}
			}catch(Exception e){

			}
		}
		for(Method method : data.getClass().getMethods()){
			if( (method.getModifiers() & Modifier.STATIC) > 0){
				continue;
			}
			String name = method.getName();
			if(name.startsWith("get")){
				name = subKey + StrKit.firstCharToLowerCase(name.substring(3));
			}else if(name.startsWith("is")){
				name = subKey + StrKit.firstCharToLowerCase(name.substring(2));
			}else{
				continue;
			}

			Class<?> type = method.getReturnType();
			try{
				Object value = method.invoke(data);
				if(type==Integer.class || type== int.class){
					putInt(name, (Integer)value);
				}else if(type==Boolean.class || type== boolean.class){
					putBoolean(name, (Boolean)value);
				}else if(type==Float.class || type== float.class){
					putFloat(name, (Float)value);
				}else if(type==Double.class || type== double.class){
					putFloat(name, (float)(double)(Double)value);
				}else if(value instanceof Map){
					putString(name,  JsonUtil.toJson( (Map)value  )  );
				}else{
					putString(name, (String)value);
				}
			}catch(Exception e){

			}
		}


		return this;
	}
	@Override
	public ILocalData putBoolean(String key,boolean value){
		_edit.putBoolean(key, value);
		return this;
	}
	@Override
	public ILocalData putString(String key,String value){
		_edit.putString(key, value);
		return this;
	}
	@Override
	public ILocalData putFloat(String subKey,float value){
		_edit.putFloat(subKey, value);
		return this;
	}
	@Override
	public ILocalData putInt(String key,int value){
		_edit.putInt(key, value);
		return this;
	}
	@Override
	public float getFloot(String subKey,float defaultValue){
		return _sp.getFloat(subKey, defaultValue);
	}

	@Override
	public boolean getBoolean(String key,boolean defaultValue){
		return _sp.getBoolean(key, defaultValue);
	}
	@Override
	public int getInt(String key,int defaultValue){
		return _sp.getInt(key, defaultValue);
	}
	@Override
	public String getString(String subKey,String defaultString){
		return _sp.getString(subKey,defaultString);
	}
	@Override
	public List<String> getStringList(String subKey){
		String value = _sp.getString(subKey,"");
		return CollectionUtil.parse(value);
	}
	/**
	 * 保存
	 */
	@Override
	public void destroy() {
		if(_edit!=null){
			_edit.commit();
		}
	}

	@Override
	public <T> ILocalData putList(String subKey, List<T> value) {
		return putString(subKey, CollectionUtil.join(value));
	}


	@Override
	public void save() {
		if(_edit!=null){
			_edit.commit();
		}
	}







}
