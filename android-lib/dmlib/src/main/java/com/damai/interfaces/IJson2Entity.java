package com.damai.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

public interface IJson2Entity<T> {
	 T parseEntity(JSONObject json)  throws JSONException;
}
