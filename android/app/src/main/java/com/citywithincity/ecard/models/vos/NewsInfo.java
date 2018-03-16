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
public class NewsInfo implements Serializable, IJsonValueObject {
	private static final long serialVersionUID = 1L;
	public int id;		//id	
	@ViewId(id=R.id._image_view)
	public String img;		//图片
	@ViewId(id=R.id._text_view)
	public String title;	//标题
	@ViewId(id=R.id._sub_text_view)
	public String content; //内容
	
	
	@Override
	public void fromJson(JSONObject json) throws JSONException {
		id = json.getInt("ID");
		title = json.getString("TITLE");
		content = json.getString("CONTENT");
		img = JsonUtil.getImageUrl(json.getString("IMG"));
	}
}
