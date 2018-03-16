package com.citywithincity.ecard.selling.models.vos;

import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SMyDiyListVo implements IJsonValueObject, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	/**
	 * 正面小图
	 */
	public String thumb;
	/**
	 * 正面
	 */
	public String imageF;
	/**
	 * 反面
	 */
	public String imageZ;
	/**
	 * 上传时间
	 */
	
	public String diyDay;

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		
		id = json.getInt("ID");
		thumb = JsonUtil.getImageUrl(json.getString("THUMB"));
		imageF = JsonUtil.getImageUrl(json.getString("IMG_F"));
		imageZ = JsonUtil.getImageUrl(json.getString("IMG_Z"));
		diyDay = json.getString("DIY_DATE");
	}

}
