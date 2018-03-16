package com.damai.core;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.damai.dmlib.BusinessLogic;

@SuppressWarnings("rawtypes")
@BusinessLogic
public class ApiPageParser implements ApiParser {

	@SuppressWarnings("unchecked")
	@Override
	public Object parse(JSONObject json, Class<?> clazz)
			throws JSONException {
		//分页下标最小为1
		DMPage result = new DMPage();
		result.page = json.getInt("page");
		result.total = json.getInt("total");
		result.list = (List) EntityUtil.parseArray(json.getJSONArray("result"), clazz);

		
		return result;
	}






}
