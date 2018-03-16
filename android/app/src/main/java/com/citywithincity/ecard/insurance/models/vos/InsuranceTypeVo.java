package com.citywithincity.ecard.insurance.models.vos;

import com.citywithincity.ecard.insurance.models.InsuranceUtil;
import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.utils.MemoryUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class InsuranceTypeVo implements IJsonValueObject,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String type_id;
	public String title;
	public String icon_url;
	
	@Override
	public void fromJson(JSONObject json) throws JSONException {
		MemoryUtil.jsonToObject(json, this);
		icon_url = InsuranceUtil.setUrl(icon_url);
	}
	
}
