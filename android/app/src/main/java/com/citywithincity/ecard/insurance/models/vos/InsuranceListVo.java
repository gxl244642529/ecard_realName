package com.citywithincity.ecard.insurance.models.vos;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceUtil;
import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.utils.MemoryUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Databind
public class InsuranceListVo implements IJsonValueObject,Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*@ViewId(id=R.id.price)
	public String price;
	public String img;
	*/

	@ViewId(id=R.id.name)
	public String title;
	public String insurance_id;
	@ViewId(id=R.id.intro)
	public String compensate;
	public String info;
	public String type_id;
	@ViewId(id=R.id.img)
	public String icon_url;
	public String promotion_type;// 1 - 爆款 2- NEW   0 - 无
	public String ori_price;
	public String on_sale;//1:app购买
	
	@Override
	public void fromJson(JSONObject json) throws JSONException {
		MemoryUtil.jsonToObject(json, this);
		
		icon_url = InsuranceUtil.setUrl(icon_url);
	}
	
	@ViewId(id=R.id.price)
	public String getPrice() {
		return "¥" + info;
	}
	
}
