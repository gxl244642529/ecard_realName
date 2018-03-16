package com.citywithincity.ecard.models.vos;

import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 商家列表数据
 * @author Randy
 *
 */
public class BusinessInfo implements IJsonValueObject, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int id;		//id	
	
	public String title;	//标题
	
	public String img;		//图片
	
	
	//分店id
	public int shopId;

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		id = json.getInt("ID");
		title = json.getString("TITLE");
		img = JsonUtil.getImageUrl(json.getString("IMG"));
		if(json.has("SHP_ID")){
			shopId = json.getInt("SHP_ID");
		}
	}
}
