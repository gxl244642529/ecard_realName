package com.damai.util;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.citywithincity.utils.MD5Util;


public class SignUtil {
	
	private static Map<String, Object> files = new LinkedHashMap<String, Object>();

	public static String sign(Map<String, Object> json,String key){
		files.clear();
		StringBuilder sb = new StringBuilder();
		toMap(json, sb,null);
		sb.append(key);
		return MD5Util.md5Appkey(sb.toString());
	}

	public static Map<String, Object> getFiles() {
		return files;
	}
	
	private static void toMap(Map<String, Object> json,StringBuilder sb,String path){
		//这里开始签名
    	Map<String,Object> map = new TreeMap<String,Object>();
    	for (Entry<String, Object> entry : json.entrySet()) {
			Object value = entry.getValue();
			String key = entry.getKey();
			if(value instanceof File){
				files.put(getPath(path, key), value);
				map.put(key, null);
				continue;
			}else if(value instanceof byte[]){
				files.put(getPath(path, key), value);
				map.put(key, null);
				continue;
			}
			map.put(key, value);
		}
    	mapToString(map, sb,path);
	}
	
	@SuppressWarnings("unchecked")
	private static void mapToString(Map<String,Object> map ,StringBuilder sb,String path){
		for (Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if(value instanceof List){
				String key =entry.getKey();
				sb.append(key);
				toList((List<Object>)value,sb,getPath(path,key));
			}else{
				sb.append(entry.getKey()).append(value);
			}
		}
	}
	
	private static String getPath(String path,String key){
		if(path==null){
			return key;
		}
		return path+"."+key;
	}
	
	private static String getPath(String path,int index){
		if(path==null){
			return "["+index+"]";
		}
		return path+"["+index+"]";
	}
	
	@SuppressWarnings("unchecked")
	private static void toList(List<Object> json,StringBuilder sb,String path){
		for(int i=0, count = json.size(); i < count; ++i){
			Object value = json.get(i);
			if(value instanceof Map){
				toMap((Map<String, Object>)value, sb,getPath(path,i));
			}else {
				sb.append(value);
			}
		}
	}



}
