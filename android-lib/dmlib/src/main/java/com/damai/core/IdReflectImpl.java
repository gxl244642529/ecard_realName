package com.damai.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.res.Resources;

public class IdReflectImpl implements IdReflect {
	@SuppressLint("UseSparseArrays")
	private Map<Integer, String> idMap = new HashMap<Integer, String>();
	
	
	private Resources resources;
	private String packageName;
	
	public String idToString(int id) {
		return idMap.get(id);
	}
	
	public int toId(String name){
		return resources.getIdentifier(name, "id", packageName);
	}

	@SuppressWarnings("rawtypes")
	public void init(Resources resources,String packageName,ClassLoader classLoader){
		this.resources = resources;
		this.packageName = packageName;
		try {
			Class rClass = Class.forName(packageName+".R$id",false,classLoader);
			Field[] fIDs = rClass.getFields();
			for (Field field : fIDs) {
	             String name = field.getName();
                if(name.startsWith("$")){
                    continue;
                }
                try{
                    int id = field.getInt(null);
                    idMap.put(id, name);
                }catch (Exception e){

                }

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

}
