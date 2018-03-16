package com.citywithincity.ecard.models.vos;

import com.citywithincity.ecard.ECardConfig;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * exam_adv_list
 * 
 */
public class ExamAdvListVo implements IJsonValueObject {
	/**
		* 
		*/
	public int id;
	/**
		* 
		*/
	public String title;
	/**
		* 
		*/
	public String img;

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		id = json.getInt("ID");
		title = json.getString("TITLE");
		img =ECardConfig.BASE_URL + json.getString("IMG");

	}
}
