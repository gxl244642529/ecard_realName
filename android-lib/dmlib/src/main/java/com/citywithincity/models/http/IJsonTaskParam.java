package com.citywithincity.models.http;

import org.json.JSONObject;

public interface IJsonTaskParam {
	Object getParams(JSONObject json, Object target,AbsJsonTask<?> task) throws Exception ;
	
	JSONObject parseData(byte[] content,AbsJsonTask<?> task) throws Exception;
}
