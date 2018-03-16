package com.citywithincity.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

public class CollectionUtil {
	
	
	
	/**
	 * 创建list
	 * @return
	 */
	public static <T> List<T> createListFromArray(T[] array){
		List<T> list = new ArrayList<T>();
		for (T t : array) {
			list.add(t);
		}
		return list;
	}
	

	
	
	/**
	 * 
	 * @param source
	 * @param splitter
	 * @return
	 */
	public static <T> List<String> parse(String source,String splitter){
		String[] values = source.split(splitter);
		List<String> list = new ArrayList<String>();
		for (String string : values) {
			if(!TextUtils.isEmpty(string))
				list.add(string);
		}
		return list;
	}
	public static <T> List<String> parse(String source){
		return parse(source,",");
	}
	
	
	/**
	 * 整合
	 * 
	 */
	public static <T> String join(Collection<T> list,String splitter){
		int count = list.size();
		int i=0;
		StringBuffer sBuffer = new StringBuffer();
		for (T t : list) {
			sBuffer.append(t);
			if(i++<count-1){
				sBuffer.append(splitter);
			}
		}
		return sBuffer.toString();
	}
	
	public static <T> String join(Collection<T> list){
		return join(list,",");
	}

	
	/**
	 * 整合K
	 * @param map
	 * @return
	 */
	public static <K> String Join(Map<K, Boolean> map,String splitter){
		return join(map.keySet(),splitter);
	}
	
	public static <K> String Join(Map<K, Boolean> map){
		return join(map.keySet(),",");
	}
	
}
