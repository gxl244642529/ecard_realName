package com.citywithincity.ecard.selling.models.vos;

import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单详情
 *
 */
public class SOrderDetailVo implements IJsonValueObject {
			/**
		* 交易流水号
		*/
		public String sn;
		/**
		* 收货地址
		*/
		public String addr;
		/**
		* 收货人姓名
		*/
		public String name;
		/**
		* 收货电话
		*/
		public String phone;
		/**
		* 邮编
		*/
		public String pcode;
		/**
		* 应付价
		*/
		public float realPrice;
		/**
		* 状态
		*/
		public int state;
		
		public List<Card> cards;
		
		public String time;
		public String payTime;
		public String deliverCode;
		public String deliver;
		public int count;
		public float totalPrice;
		public String id;
		public String code;
		public int attach;
		

	
	@Override
	public void fromJson(JSONObject json) throws JSONException{
//		sn=json.getString("SN");
		addr=json.getString("ADDR");
		name=json.getString("NAME");
		phone=json.getString("PHONE");
		pcode=json.getString("PCODE");
		realPrice=((float)json.getInt("REAL_PRICE"))/100;
		state=json.getInt("STATE");
		time=json.getString("TIME");
		payTime=json.getString("PAY_TIME");
		deliverCode=json.getString("DELIVER_CODE");
		deliver=json.getString("DELIVER");
		count=json.getInt("COUNT");
		totalPrice=((float)json.getInt("TOTAL_PRICE"))/100;
		id=json.getString("ID");
		code=json.getString("CODE");
		attach=json.getInt("ATTACH");
		
		cards = new ArrayList<SOrderDetailVo.Card>();
		JSONArray jsonArray = json.getJSONArray("cards");
		for (int i = 0,len = jsonArray.length(); i < len; i++) {
			Card card = Card.fromJson(jsonArray.getJSONObject(i));
			cards.add(card);
		}
		
	}
	
	public static class Card implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String id;
		public float price;
		public float total;
		public String thumb;
		public String title;
		public int count;
		public float preCharge;
		
		public static Card fromJson(JSONObject json) throws JSONException {
			Card data = new Card();
			data.count = json.getInt("COUNT");
			data.id = json.getString("ID");
			data.preCharge = ((float)json.getInt("RECHARGE"))/100;
			data.price = ((float)json.getInt("PRICE"))/100;
//			data.thumb = JsonUtil.getImageUrl(json.getString("THUMB"));
			data.thumb = json.getString("THUMB");
			data.title = json.getString("TITLE");
			data.total = ((float)json.getInt("DISPRICE"))/100;
			
			return data;
		}
		
	}
}
