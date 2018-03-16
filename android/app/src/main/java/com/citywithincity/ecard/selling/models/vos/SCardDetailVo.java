package com.citywithincity.ecard.selling.models.vos;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 售卡详情
 * 
 */
@Databind
public class SCardDetailVo implements IJsonValueObject {
	
	/**
	 * 是否包含图文介绍
	 */
	public int hasInfo;
	/**
	 * 销售数量
	 */
	@ViewId(id=R.id.card_buy_member)
	public int saled;
	/**
	 * 库存
	 */
	
	public int stock;
	
	
	@ViewId(id=R.id.id_stock)
	public String getStock(){
		return String.valueOf(stock);
	}
	
	
	/**
	 * 购卡须知
	 */
	@ViewId(id=R.id.card_user_to_know)
	public String tip;
	
	public float rawPrice;
	
	public String info;
	
	public List<String> list;

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		
//		hasInfo = json.getInt("HAS_INFO");
		saled = json.getInt("SALED");
		stock = json.getInt("STOCK");
		tip = json.getString("TIP");
		rawPrice =((float)json.getInt("RAW_PRICE"))/100;
		info = json.getString("INFO");
		
		list = new ArrayList<String>();
		JSONArray array = new JSONArray();
		array = json.getJSONArray("list");
		for (int i = 0, count = array.length(); i < count; ++i) {
			String path = (String) array.get(i);
			list.add(path);
		}
		
	}
}
