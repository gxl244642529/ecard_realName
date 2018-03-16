package com.citywithincity.ecard.models.vos;

import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterVerify implements IJsonValueObject{
	
	public String phone;
	public String password;
	public String userid;

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		phone = json.getString("phone");
		if (json.has("userid")) {
			userid = json.getString("userid");
		}
	}

}
