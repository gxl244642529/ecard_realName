package com.citywithincity.ecard.insurance.models.vos;

import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.utils.MemoryUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SafeAddressVos implements Serializable,IJsonValueObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2011452169646170225L;

	public long id;
	public long cmId;
	public String sheng;
	public String shi;
	public String qu;
	public int shengId;
	public int shiId;
	public int quId;
	public String addr;
	
	@Override
	public void fromJson(JSONObject json) throws JSONException {
		MemoryUtil.jsonToObject(json, this);
		
	}
	
	
}
