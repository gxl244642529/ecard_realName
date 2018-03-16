package com.citywithincity.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class ValueUtil {
	/**
	 * 检查是否为空
	 * @param json
	 * @param name
	 * @return
	 * @throws JSONException
	 */
	public static String checkAndGetString(JSONObject json,String name) throws JSONException{
		String str = json.getString(name);
		if(str==null)return "";
		if(str.equals("null"))return "";
		return str;
	}
	
	
	public static int checkAndGetInt(JSONObject json, String name) {
		int ret = 0;
		try {
			ret = json.getInt(name);
		} catch (JSONException e) {
			
		}
		return ret;
	}
}
