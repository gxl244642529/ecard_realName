package com.citywithincity.ecard.models.vos;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Databind
public class PickCardDetailInfo implements IJsonValueObject, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ViewId(id=R.id.card_number)
	public String cardID;					//卡号
	
	
	public String img;					//卡照片
	
	public String addr;					//拾取地址
	public String phone;					//联系电话
	
	@ViewId(id=R.id.pick_card_time)
	public String time;						//时间
	

	@Override
	public void fromJson(JSONObject json) throws JSONException{
		cardID = json.getString("CARD_ID");
		img = json.getString("IMG");
		addr = json.getString("ADDR");
		phone = json.getString("PHONE");
		time = json.getString("TIME");
	}
}
