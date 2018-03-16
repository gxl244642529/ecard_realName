package com.damai.core;

import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.damai.dmlib.BusinessLogic;


/**
 * 这里涉及具体服务器的逻辑
 * @author Randy
 *
 * @param 
 */
@BusinessLogic
public class ApiArrayParser implements ApiParser {

	@SuppressWarnings("rawtypes")
	@Override
	public List parse(JSONObject json, Class<?> clazz) throws JSONException {
		if(json.isNull("result")){
			return Collections.emptyList();
		}
		return EntityUtil.parseArray(json.getJSONArray("result"), clazz);
	}
	
}
