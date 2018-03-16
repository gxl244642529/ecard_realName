package com.citywithincity.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class MemoryUtil {
	/**
	 * 清空array
	 * @param array
	 */
	public static <T> void clearArray(T[] array){
		for(int i=0 , count = array.length; i < count; ++i){
			array[i] = null;
		}
	}


	@SuppressWarnings("unchecked")
	public static <T> T clone(Serializable object){
		ObjectOutputStream out = null;
		ByteArrayOutputStream byteOut = null;
		ByteArrayInputStream byteIn = null;
		ObjectInputStream in = null;
		try {
			byteOut = new ByteArrayOutputStream();
			out = new ObjectOutputStream(byteOut);
			out.writeObject(object);
			byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			in=new ObjectInputStream(byteIn);
			return (T)in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
					out= null;
				}
				if (byteOut != null) {
					byteOut.close();
					byteOut= null;
				}
				if (byteIn != null) {
					byteIn.close();
					byteIn= null;
				}
				if (in != null) {
					in.close();
					in= null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
		return null;
	}

    public static <T> T getObject(byte[] bytes) throws Exception{
        ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (T)in.readObject();
    }
	public static byte[] getBytes(Serializable object) throws IOException{
		ObjectOutputStream out = null;
		ByteArrayOutputStream byteOut = null;
		try {
			byteOut = new ByteArrayOutputStream();
			out = new ObjectOutputStream(byteOut);
			out.writeObject(object);
			return byteOut.toByteArray();
		} finally {
			IoUtil.close(byteOut);
			IoUtil.close(out);
		}
	}



	public static JSONObject mapToJson(Map<String, Object> map) throws JSONException{
		JSONObject json = new JSONObject();
		for (Map.Entry<String, Object> iterable_element : map.entrySet()) {
			json.put(iterable_element.getKey(), iterable_element.getValue());
		}
		return json;
	}


	/**
	 *
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException{
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray names = json.names();
		if(names==null){
			return map;
		}
		int len = names.length();

		for(int i=0; i < len; ++i){
			String name = names.getString(i);
			Object value = json.get(name);
			if(value instanceof  JSONObject){
				map.put(name,jsonToMap(  (JSONObject)value ) );
			}else{
				map.put(name,value );
			}

		}

		return map;

	}

	/**
	 *
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, Object> bundleToMap(Bundle bundle){
		Map<String, Object> map = new HashMap<String, Object>();
		for (String key : bundle.keySet()) {
			map.put(key, bundle.get(key));
		}
		return map;
	}

	/**
	 *
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Bundle jsonToBundle(JSONObject json) throws JSONException{
		Bundle map = new Bundle();
		JSONArray names = json.names();
		int len = names.length();

		for(int i=0; i < len; ++i){
			String name = names.getString(i);
			Object value = json.get(name);
			if(value instanceof String){
				map.putString(name, (String)value);
			}else if(value instanceof Integer){
				map.putInt(name, (Integer)value);
			}else if(value instanceof Float){
				map.putFloat(name, (Float)value);
			}else if(value instanceof Double){
				map.putDouble(name, (Double)value);
			}else if(value instanceof Boolean){
				map.putBoolean(name, (Boolean)value);
			}else{
				throw new Error("不支持这种类型"+value.getClass());
			}

		}

		return map;

	}


	public static void jsonToObject(JSONObject json,Object object){
		Field[] fields = object.getClass().getFields();
		for (Field field : fields) {
			String name = field.getName();
			if(json.has(name)){
				try{
					field.set(object, json.get(name));
				}catch(Exception e){

				}

			}
		}
	}

}
