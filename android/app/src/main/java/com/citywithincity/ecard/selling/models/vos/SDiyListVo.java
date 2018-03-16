package com.citywithincity.ecard.selling.models.vos;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * diy列表
 *
 */
@Databind
public class SDiyListVo implements IJsonValueObject, Serializable {
			/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			/**
		* 标题
		*/
		public String date;
		/**
		* 编号
		*/
		public int id;
		/**
		* 缩略图
		*/
		@ViewId(id=R.id.card_diy_onlineimage)
		public String thumb;
		/**
		* 正面大图
		*/
		public String img;
		/**
		* 图片反面
		*/
		public String imgF;
		
		/**
		 * 
		 */
		@ViewId(id=R.id.diy_online_title)
		public String title;

	
	@Override
	public void fromJson(JSONObject json) throws JSONException{
				date=json.getString("DIY_DATE");
		id=json.getInt("ID");
		thumb=JsonUtil.getImageUrl(json.getString("THUMB"));
		img=JsonUtil.getImageUrl(json.getString("IMG_Z"));
		imgF=JsonUtil.getImageUrl(json.getString("IMG_F"));
		title = json.getString("TITLE");

	}
}
