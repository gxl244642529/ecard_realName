package com.citywithincity.ecard.insurance.models.vos;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.utils.MemoryUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Databind
public class InsuranceOtherPurchaseSuccessVo implements Serializable, IJsonValueObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	public JSONObject json;
	
	public String id;
	@ViewId(id=R.id.id_name)
	public String name;
	@ViewId(id=R.id.id_idcard_no)
	public String idCard;
	@ViewId(id=R.id.id_phone)
	public String phone;
	@ViewId(id=R.id.name)
	public String title;
	public String inId;
	public String typeId;
	public String summary;
	public double fee;
	public int count;
	public String[] summaries;

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		MemoryUtil.jsonToObject(json, this);
//		this.json = json;
		
		summaries = summary.split("\\^");
		
//		{"id":"16012605274510400",
//			"platId":null,
//			"payStatus":0,
//			"payType":0,
//			"fee":1,
//			"createTime":"2016-01-26 17:27:45",
//			"payTime":null,
//			"refundTime":null,
//			"cmId":95,
//			"cmAcc":"18659210057",
//			"insured":null,
//			"carNo":null,
//			"carFrame":null,
//			"userError":null,
//			"name":"休年假",
//			"addr":null,
//			"idCard":"358979199108087946",
//			"phone":"15375872588",
//			"pc":null,
//			"idImg":null,
//			"cardId":null,
//			"ticket":null,
//			"inId":"5434",
//			"duration":"11",
//			"typeId":"3",
//			"title":"国内十日游-低保障",
//			"piccOrderId":null,
//			"guardUrl":"\/article\/show\/11",
//			"guard":"产品详情",
//			"com":"PICC",
//			"comIcon":"PICC",
//			"protocolUrl":"\/article\/show\/14",
//			"startTime":null,
//			"endTime":null,
//			"noticeUrl":"\/article\/show\/17",
//			"backgroundUrl":null,
//			"summary":"短期出行，自由选投^成本超低，安心出门",
//			"serviceTel":"0592-95518",
//			"count":0,
//			"dynamicFields":[]}
		
	}

}
