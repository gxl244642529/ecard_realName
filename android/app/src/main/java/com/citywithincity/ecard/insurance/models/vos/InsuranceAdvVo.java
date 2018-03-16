package com.citywithincity.ecard.insurance.models.vos;

import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

public class InsuranceAdvVo implements IJsonValueObject{

	public String img;
	public String title;
	public int id;
	public int type;
	public String url;
	/**
	 * 1可以点 0不行
	 */
	public int flag;
	
	@Override
	public void fromJson(JSONObject json) throws JSONException {
		img = JsonUtil.getImageUrl(json.getString("img"));
		title = json.getString("title");
		id = json.getInt("id");
		type = json.getInt("type");
		if (!json.isNull("url")) {
			url = json.getString("url");
		}
		flag = json.getInt("flag");
	}

}
