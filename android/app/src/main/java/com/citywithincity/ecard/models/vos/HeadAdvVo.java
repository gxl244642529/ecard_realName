package com.citywithincity.ecard.models.vos;

import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.utils.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class HeadAdvVo implements IJsonValueObject,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int AD_TYPE_HTML = 1;
	public static final int AD_TYPE_URL = 2;
	
	public int id;
	public String img;
	public String img2;
	public String title;
	/**
	 * 是否是活动  
	 * //多媒体类型
		define("AD_TYPE_HTML", 1);
		//url类型
		define("AD_TYPE_URL", 2);
	 */
	public int type;
	public String url;
	public int isEvent;//是否是活动   1活动

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		id = json.getInt("ID");
		
		float property =  (float)ViewUtil.screenWidth/(float)ViewUtil.screenHeight;
		
		if (property > 0.75) {
			img = JsonUtil.getImageUrl(json.getString("IMG"));
		} else {
			img = JsonUtil.getImageUrl(json.getString("IMG2"));
		}
		title = json.getString("TITLE");
		type = json.getInt("TYPE");
		isEvent = json.getInt("IS_EVENT");
		url = json.getString("URL");
		
//		{"ID":"11",
//			"IS_EVENT":"0",
//			"URL":null,
//			"TYPE":"1",
//			"IMG":"\/uploads\/2015_04_29\/f2b2d50d91016132bb2f539b13a9f5b1.png",
//			"TITLE":"新妆上线"}
		
	}
}