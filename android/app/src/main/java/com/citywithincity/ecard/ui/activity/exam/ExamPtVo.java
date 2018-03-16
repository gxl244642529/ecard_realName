package com.citywithincity.ecard.ui.activity.exam;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.models.vos.AddressVo;

import org.json.JSONException;
import org.json.JSONObject;

@Databind
public class ExamPtVo extends AddressVo implements IJsonValueObject{
	/**
	* 编号
	*/
	public int id;

	
	@Override
	public void fromJson(JSONObject json) throws JSONException{
		id=json.getInt("ID");
		title=json.getString("TITLE");
		address=json.getString("ADDRESS");
		lat=json.getDouble("LAT");
		lng=json.getDouble("LNG");
	}
	
	/**
	* 标题
	*/
	@ViewId(id=R.id.id_title)
	public String getTitle(){
		return title;
	}
	/**
	* 地址
	*/
	@ViewId(id=R.id.id_address)
	public String getAddress(){
		return address;
	}
	
	@ViewId(id=R.id.distance)
	public String getDistance(){
		return distance;
	}
}
