package com.citywithincity.ecard.selling.models.vos;

import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获取售卡分类
 * 
 */
public class SCardTypeVo implements IJsonValueObject {
	/**
	 * 编号
	 */
	public int id;
	/**
	 * 标题
	 */
	public String title;

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		id = json.getInt("ID");
		title = json.getString("TITLE");
		

	}
}
