package com.damai.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;

@SuppressLint("UseSparseArrays")
class ArrayContainer<T> {
	private T[] array;
	private Map<Integer, Object> list;
	private int size;
	public ArrayContainer(){
		size = 0;
		list = new HashMap<Integer, Object>();
	}
	
	public void register(int index,Object data){
		list.put(index, data);
		if(size < index+1){
			size = index+1;
		}
	}
	
	public int size(){
		return size;
	}
	
	public T[] getArray(){
		return array;
	}
	
	@SuppressWarnings("unchecked")
	public T[] toArray(T[] array){
		if(list==null){
			throw new RuntimeException("List is null!");
		}
		for (Entry<Integer, Object> entry : list.entrySet()) {
			array[entry.getKey()] = (T) entry.getValue();
		}
		list.clear();
		list = null;
		this.array = array;
		return array;
	}
}
