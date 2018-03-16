package com.damai.core;

import org.json.JSONException;
import org.json.JSONObject;

import com.damai.dmlib.DMParseException;

@SuppressWarnings("rawtypes")
public class JsonDataParser implements DataParser {

	@Override
	public Object parseData(HttpJobImpl api, byte[] data) throws DMParseException {
		try{
			JSONObject json = new JSONObject(new String(data));
			return json;
		}catch(JSONException e){
			throw new DMParseException(e);
		}
	}

}
