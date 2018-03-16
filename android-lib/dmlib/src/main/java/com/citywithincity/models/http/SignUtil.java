package com.citywithincity.models.http;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.citywithincity.utils.MD5Util;

public class SignUtil {
	
	//
	public static JSONObject getInitObject(String token) throws JSONException{
		JSONObject jsonObject = new JSONObject();
		return getInitObject(token, jsonObject);
	}
	
	public static JSONObject getInitObject(String token,JSONObject jsonObject) throws JSONException{
		jsonObject.put("rand_str", MD5Util.md5Appkey(String.valueOf(System.currentTimeMillis()*Math.random())));
		jsonObject.put("time", System.currentTimeMillis()/1000);
		jsonObject.put("token", token);
		
		return jsonObject;
	}
	
	public static JSONObject getSigned(JSONObject jsonObject,String key) throws JSONException{
		jsonObject.put("sign", sign(jsonObject, key));
		return jsonObject;
	}

	public static String sign(JSONObject json,String key) throws JSONException{
		json.remove("sign");
		StringBuilder sb = new StringBuilder();
		getString(json, sb);
		sb.append(key);
		return MD5Util.md5Appkey(sb.toString());
	}
	
	private static void getString(JSONObject json,StringBuilder sb) throws JSONException{
		//这里开始签名
    	Map<String,Object> map = new TreeMap<String, Object>();
    	JSONArray names = json.names();
    	for(int i=0 , count = names.length(); i < count; ++i){
    		String name = names.getString(i);
    		Object object = json.get(name);
    		map.put(name, object);
    		
    	}
    	mapToString(map, sb);
	}
	
	private static void mapToString(Map<String,Object> map ,StringBuilder sb) throws JSONException{
		for (Entry<String, Object> entry : map.entrySet()) {
			Object object = entry.getValue();
			if(object instanceof JSONArray){
				sb.append(entry.getKey());
    			getString((JSONArray)object,sb);
    		}else{
    			sb.append(entry.getKey()).append(object);
    		}
			
			
		}
	}
	
	private static void getString(JSONArray json,StringBuilder sb) throws JSONException{
		for(int i=0, count = json.length(); i < count; ++i){
			Object object = json.get(i);
			if(object instanceof JSONObject){
				getString(json.getJSONObject(i), sb);
			}else{
				sb.append(object);
			}
		}
	}
}
