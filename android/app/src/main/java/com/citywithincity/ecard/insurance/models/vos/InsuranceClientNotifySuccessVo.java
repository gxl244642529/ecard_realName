package com.citywithincity.ecard.insurance.models.vos;

import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

public class InsuranceClientNotifySuccessVo implements IJsonValueObject{

//	public String orderId;
	public JSONObject json; 
	
	@Override
	public void fromJson(JSONObject json) throws JSONException {
//		MemoryUtil.jsonToObject(json, this);
		this.json = json;
	}

}
