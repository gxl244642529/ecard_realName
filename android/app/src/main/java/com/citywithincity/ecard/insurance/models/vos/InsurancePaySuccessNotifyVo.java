package com.citywithincity.ecard.insurance.models.vos;

import android.annotation.SuppressLint;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceUtil;
import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.utils.MemoryUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Databind
public class InsurancePaySuccessNotifyVo implements IJsonValueObject,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public String id;
	//平台订单号
	public String platId;
	public int payStatus;
	public int payType;
	public int fee;
	
	public String createTime;
	@ViewId(id=R.id.id_date)
	public String payTime;
	public String refundTime;
	
	public long cmId;
	public String cmAcc;
	
	//由于用户提交信息不符合导致的错误
	public String userError;
	/**
	 * 这里比较关心的是这些
	 */
	@ViewId(id=R.id.id_name)
	public String name;
	public String addr;
	@ViewId(id=R.id.id_idcard_no)
	public String idCard;
	@ViewId(id=R.id.id_phone)
	public String phone;
	public String pc;
	public String idImg;
	@ViewId(id=R.id.ecard_card_number)
	public String cardId;
	
	

	//picc服务器上面
	public String inId;
	public String duration;
	public String typeId;
	@ViewId(id=R.id.name)
	public String title;
	@ViewId(id=R.id.id_1)
	public String piccOrderId;
	public String guardUrl;
	public String guard;
	public String com;
	public String comIcon;
	public String protocolUrl;
	public String startTime;
	public String endTime;
	public String noticeUrl;
	public String backgroundUrl;
	public String summary;
	public String serviceTel;
	public String protocol_title;
	
	@Override
	public void fromJson(JSONObject json) throws JSONException {
		MemoryUtil.jsonToObject(json, this);
		protocol_title = "《保险条款》";
		guardUrl = InsuranceUtil.setUrl(guardUrl);
		protocolUrl = InsuranceUtil.setUrl(protocolUrl);
		noticeUrl = InsuranceUtil.setUrl(noticeUrl);
		if (!json.isNull("backgroundUrl")) {
			backgroundUrl = InsuranceUtil.setUrl(backgroundUrl);
		}
		if (!json.isNull("idImg")) {
			idImg = JsonUtil.getJavaImageUrl(idImg);
		}
	}
	
	@SuppressLint("DefaultLocale")
	@ViewId(id=R.id.price)
	public String setFee(){
		return String.format("%.2f", ((float)fee)/100);
	}
	
//	{"id":"15120904105454600",
//		"platId":"1003400509201512091981582591",
//		"payStatus":3,
//		"payType":2,
//		"fee":100,
//		"createTime":"16:10:54",
//		"payTime":"2015-12-09 16:12:15",
//		"refundTime":null,
//		"cmId":48024,
//		"cmAcc":"15375872587",
//		"userError":null,
//		"name":"夏季",
//		"addr":null,
//		"idCard":"350555199511125555",
//		"phone":"15375872587",
//		"pc":null,
//		"idImg":"http://www.xm95518.com:8989/uploads/2015-12-09/2403d995d2b4ebb90bd0ba159409cc77.jpg",
//		"cardId":"9012601017394908",
//		"inId":"5432",
//		"duration":"4",
//		"typeId":"1",
//		"title":"卡保",
//		"piccOrderId":"20151209161248282",
//		"guardUrl":"http://www.xm95518.com:8989/article/show/4",
//		"guard":"投保详情",
//		"com":"PICC",
//		"comIcon":"PICC",
//		"protocolUrl":"\/article\/show\/8",
//		"startTime":"2015-12-10 00:00:00",
//		"endTime":"2016-06-10 00:00:00",
//		"noticeUrl":"\/article\/show\/6",
//		"backgroundUrl":null,
//		"summary":"小小1元，放心用卡^手续简便，轻松补卡","set":[],"dynamicFields":[]}
	
	
}
