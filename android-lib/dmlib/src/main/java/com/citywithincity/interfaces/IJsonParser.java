package com.citywithincity.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

public interface IJsonParser<T> {
	 T parseResult(JSONObject json)  throws JSONException;
}
