package com.citywithincity.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * json解析出的实体类
 * @author Randy
 *
 */
public interface IJsonValueObject {
	void fromJson(JSONObject json) throws JSONException;
}
