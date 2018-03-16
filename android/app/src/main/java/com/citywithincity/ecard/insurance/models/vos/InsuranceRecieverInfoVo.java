package com.citywithincity.ecard.insurance.models.vos;

import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.utils.MemoryUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获取收货人信息
 * @author admin
 *
 */
public class InsuranceRecieverInfoVo implements IJsonValueObject{

	public String delivery_addr;
	public String delivery_tel;
	public String express_company;
	public String delivery_no;
	public String name;
	
	@Override
	public void fromJson(JSONObject json) throws JSONException {
		MemoryUtil.jsonToObject(json, this);
	}

}
