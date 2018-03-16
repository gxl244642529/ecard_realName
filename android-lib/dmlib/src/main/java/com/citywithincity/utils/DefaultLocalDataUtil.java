package com.citywithincity.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DefaultLocalDataUtil {
	private static Context _context;
	public static void setContext(Context context){
		_context = context;
	}
	
	public static boolean getBoolean(String mainKey,String subKey,boolean defaultValue){
		SharedPreferences sp = _context.getSharedPreferences(mainKey,
				Context.MODE_PRIVATE);
		return sp.getBoolean(subKey,defaultValue);
	}
	
	public static int getInt(String mainKey,String subKey,int defaultValue){
		SharedPreferences sp = _context.getSharedPreferences(mainKey,
				Context.MODE_PRIVATE);
		return sp.getInt(subKey,defaultValue);
	}
	
	public static void putBoolean(String mainKey,String subKey,boolean value){
		SharedPreferences sp = _context.getSharedPreferences(mainKey,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(subKey, value);
		editor.commit();
	}
	public static void putInt(String mainKey,String subKey,int value){
		SharedPreferences sp = _context.getSharedPreferences(mainKey,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt(subKey, value);
		editor.commit();
	}
	
	
	
	public static String getString(String mainKey,String subKey,String defaultValue){
		SharedPreferences sp = _context.getSharedPreferences(mainKey,
				Context.MODE_PRIVATE);
		return sp.getString(subKey,defaultValue);
	}
	
	
	public static void putString(String mainKey,String subKey,String value){
		SharedPreferences sp = _context.getSharedPreferences(mainKey,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(subKey, value);
		editor.commit();
	}
	

	public static void clear(String mainKey) {
		SharedPreferences sp = _context.getSharedPreferences(mainKey,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}
}
