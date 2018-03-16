package com.citywithincity.models.http;

import org.json.JSONException;
import org.json.JSONObject;

import com.citywithincity.interfaces.IJsonParser;
import com.citywithincity.interfaces.IJsonValueObject;

class DefaultJsonParser<T> implements IJsonParser<T> {
	
	
	
	private Class<T> clazz;
	public DefaultJsonParser(Class<T> clazz){
		this.clazz = clazz;
	}

	@Override
	public T parseResult(JSONObject json) throws JSONException {
		try {
			IJsonValueObject jsonValueObject = (IJsonValueObject)clazz.newInstance();
			jsonValueObject.fromJson(json);
			return (T)jsonValueObject;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
