package com.citywithincity.ecard.selling.models.vos;

import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 卡面
 *
 */
public class SDiyPagesVo implements IJsonValueObject,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		/**
		* 正面图片
		*/
		public String img;
		/**
		* 编号
		*/
		public int id;
		/**
		* 正面小图
		*/
		public String thumb;
		/**
		* 价格
		*/
		public float price;
		/**
		* 
		*/
		public String title;
		
		public int stock;

	@Override
	public void fromJson(JSONObject json) throws JSONException{
//				img=JsonUtil.getImageUrl(json.getString("IMG"));
		img=json.getString("IMG");
		id=json.getInt("ID");
		thumb=JsonUtil.getImageUrl(json.getString("THUMB"));
		price=((float)json.getInt("PRICE"))/100;
		title=json.getString("TITLE");
		stock= json.getInt("STORK");
		
	}
}
