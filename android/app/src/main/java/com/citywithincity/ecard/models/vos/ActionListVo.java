package com.citywithincity.ecard.models.vos;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Databind
public class ActionListVo implements IJsonValueObject,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 编号
	 */
	public int id;
	/**
	 * 图片
	 */
	@ViewId(id=R.id._image_view)
	public String img;
	/**
	 * 标题
	 */
	@ViewId(id=R.id._text_view)
	public String title;
	/**
	 * 链接
	 */
	public String url;
	
	@Override
	public void fromJson(JSONObject json) throws JSONException {
		id = json.getInt("ID");
		img = JsonUtil.getImageUrl(json.getString("IMG"));
		title = json.getString("TITLE");
		url = json.getString("URL");
		
	}

}
