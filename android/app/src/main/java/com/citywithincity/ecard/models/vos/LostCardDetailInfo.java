package com.citywithincity.ecard.models.vos;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Databind
public class LostCardDetailInfo implements IJsonValueObject, Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;					//卡号
	
	@ViewId(id=R.id.id_detail)
	public String message;					//信息
	@ViewId(id=R.id.id_phone)
	public String phone;					//电话
	@ViewId(id=R.id.id_sex)
	public int sex;					//性别
	public int vry_time;
	public int vry_id;
	public String vry_error;			//审核错误
	public String img;					//图片
	
	public String cardID;				//卡号
	@ViewId(id=R.id.id_time)
	public String time;					//时间
	
	
	public int status;					//状态
	public String vry_result;
	

	@Override
	public void fromJson(JSONObject json) throws JSONException{
		
		//id = json.getString("ID");
		message = json.getString("INFO");
		if(json.has("LAST_MODIFIED")){
			time = json.getString("LAST_MODIFIED");
		}
		
		
		if(json.has("PHONE")){
			phone = json.getString("PHONE");
			sex = json.getInt("SEX");
			cardID=json.getString("CARD_ID");
			if(cardID.equals("null")){
				cardID = null;
			}
		}
		
		if(json.has("STATUS")){
			status=json.getInt("STATUS");
			vry_time = json.getInt("VRY_TIME");
			vry_id = json.getInt("VRY_ID");
			vry_result=json.getString("VRY_RESULT");
			vry_error=json.getString("VRY_ERROR");
		}
		
	}
}
