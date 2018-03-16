package com.damai.widget.vos;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.citywithincity.interfaces.IJsonValueObject;


public class Ssq implements IJsonValueObject{
	public String name;
	public int id;
	public List<Ssq> list;
	@Override
	public void fromJson(JSONObject json) throws JSONException {
		name = json.getString("pName");
		id = json.getInt("id");
		if(!json.isNull("list")){
			list = parseArray(json.getJSONArray("list"));
		}
	}
	
	public static List<Ssq> parseArray(JSONArray arr) throws JSONException{
		List<Ssq> list = new ArrayList<Ssq>(arr.length());
		for(int i=0,c=arr.length(); i <c; ++i){
			Ssq ssq = new Ssq();
			ssq.fromJson(arr.getJSONObject(i));
			list.add(ssq);
		}
		return list;
	}
}
