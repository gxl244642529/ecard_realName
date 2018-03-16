package com.damai.core;

import org.json.JSONException;
import org.json.JSONObject;

import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.utils.ReflectUtil;
import com.damai.dmlib.BusinessLogic;
@BusinessLogic
public class ApiObjectParser implements ApiParser {

	@Override
	public Object parse(JSONObject json, Class<?> clazz) throws JSONException {
		
		if(json.isNull("result")){
			return null;
		}
		
		if(clazz==null){
			return json.get("result");
		}
		
		try {
			json = json.getJSONObject("result");
			if(ReflectUtil.containsInterface(clazz, IJsonValueObject.class)){
				Object result = clazz.newInstance();
				((IJsonValueObject)result).fromJson(json);
				return result;
			}else{
				return EntityUtil.jsonToObject(json, clazz);
			}
		} catch (Exception e) {
			throw new JSONException(e.getMessage());
		} 
	}

}
