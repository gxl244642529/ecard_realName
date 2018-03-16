package com.citywithincity.ecard.models.vos;

import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

public class MyLostCardDetailInfo implements IJsonValueObject{
	public int ret;//结果
	public LostCardDetailInfo info;
	@Override
	public void fromJson(JSONObject json) throws JSONException {
		ret = json.getInt("ret");
		if (json.has("data")) {
			info = new LostCardDetailInfo();
			info.fromJson(json.getJSONObject("data"));
		}
	}
}
