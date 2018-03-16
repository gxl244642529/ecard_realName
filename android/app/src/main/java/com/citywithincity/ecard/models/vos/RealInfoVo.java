package com.citywithincity.ecard.models.vos;

import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.utils.MemoryUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RealInfoVo implements IJsonValueObject, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int cmId;
	public String name;
	public String addr;
	public String idCard;
	public String phone;
	public String pc;
	//是否已经验证过合法性
	public boolean isValid;

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		MemoryUtil.jsonToObject(json, this);
	}

//	"cmId":95,
//	"name":"任雪亮",
//	"addr":null,
//	"idCard":"320483195612100514",
//	"phone":"18659210057",
//	"pc":null

}
