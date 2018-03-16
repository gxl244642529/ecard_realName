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
public class InsurancePurchaseSuccessVo implements Serializable,IJsonValueObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public double fee;
	public String id; 
	public String summary;
	public String safeguard_url;
	public String company;
	public String safeguard;
	
	@ViewId(id=R.id.id_name)
	public String userName;//姓名
	@ViewId(id=R.id.id_idcard_no)
	public String IDCard;//身份证
	@ViewId(id=R.id.id_phone)
	public String phone;//电话
	@ViewId(id=R.id.ecard_card_number)
	public String ecard;//e通卡
	@ViewId(id=R.id.name)
	public String title;//产品名称
	public String insurance_id;//产品id

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		MemoryUtil.jsonToObject(json, this);
	}
	
//	{"summary":"<p><span>◆<\/span><span>购买保险必须绑定e通卡卡号<\/span><\/p><p><span>◆<\/span><span>保期为一年(从购买日期开始<\/span><span>计算<\/span><span>)<\/span><\/p>",
//		"fee":1,
//		"id":"15110911500440400",
//		"safeguard_url":"https:\/\/xmjqksoft.oicp.net:1443\/article\/show\/7",
//		"company":"PICC",
//		"safeguard":"保障内容"}
	
//	{"result":{"fee":0.01,"order_id":19},"flag":0}


}
