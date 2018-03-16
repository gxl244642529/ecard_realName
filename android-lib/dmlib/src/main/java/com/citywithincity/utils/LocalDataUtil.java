package com.citywithincity.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LocalDataUtil {
	private static Context _context;
	public static void setContext(Context context){
		_context = context;
	}
	
	public static boolean getBoolean(String mainKey,String subKey,boolean defaultValue){
		SharedPreferences sp = _context.getSharedPreferences(mainKey,
				Context.MODE_PRIVATE);
		return sp.getBoolean(subKey,defaultValue);
	}
	
	protected static SharedPreferences _sp ;
	protected static Editor _edit;
	
	
	public static final int MODE_READ = 1;
	public static final int MODE_WRITE = 2;
	public static final int MODE_READ_WRITE = 3;
	
	public static void openSession(String mainKey,int mode){
		_sp  =  _context.getSharedPreferences(mainKey,
					Context.MODE_PRIVATE);
		if( (mode & MODE_WRITE) == MODE_WRITE){
			_edit = _sp.edit();
		}
	}
	public static void putBoolean(String key,boolean value){
		_edit.putBoolean(key, value);
	}
	
	public static void putString(String key,String value){
		_edit.putString(key, value);
	}
	public static void putFloat(String mainKey,String subKey,float value){
		SharedPreferences sp = _context.getSharedPreferences(mainKey,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putFloat(subKey, value);
		editor.commit();
	}
	public static void putFloat(String subKey,float value){
		_edit.putFloat(subKey, value);
	}
	public static float getFloot(String subKey,float defaultValue){
		return _sp.getFloat(subKey, defaultValue);
	}
	public static void putInt(String key,int value){
		_edit.putInt(key, value);
	}
	
	public static boolean getBoolean(String key,boolean defaultValue){
		return _sp.getBoolean(key, defaultValue);
	}
	
	public static int getInt(String key,int defaultValue){
		return _sp.getInt(key, defaultValue);
	}

	public static String getString(String subKey,String defaultString){
		return _sp.getString(subKey,defaultString);
	}
	
	public static void endSession(){
		_sp = null;
		if(_edit!=null){
			_edit.commit();
			_edit = null;
		}
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
