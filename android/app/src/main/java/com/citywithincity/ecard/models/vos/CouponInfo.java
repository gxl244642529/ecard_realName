package com.citywithincity.ecard.models.vos;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券信息
 */

@Databind
public class CouponInfo implements IJsonValueObject,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	public int id;//id
	
	
	@ViewId(id=R.id.coupon_intro)
	public String title;//标题
	
	@ViewId(id=R.id.coupon_img)
	public String img;//优惠券图片
	
	public CouponType type;//优惠券类型
	
	@ViewId(id=R.id.coupon_title)
	public String bsname;//商家名字
	
	public int fetched;//获取次数
	
	public int used;//使用次数
	public int total;//总张数,在限量的优惠券有效
	//用户id
	@Override
	public void fromJson(JSONObject obj) throws JSONException {
		id = obj.getInt("ID");
		title = obj.getString("TITLE");
		img = JsonUtil.getImageUrl(obj.getString("IMG"));
		used = obj.getInt("USED");
		total = obj.getInt("TOTAL");
		type = getCouponType(obj);
		if (obj.has("BS_NAME")) {
			bsname = obj.getString("BS_NAME");
		}
	}
	
	
	
	@ViewId(id=R.id.coupon_purch)
	public String getCouponType(){
		switch (type) {
		case CouponType_Limit:
			return "剩余: "+(total-used);
		case CouponType_Qr:
			return "验证有效，不限次数";
		case CouponType_Show:
			return "出示即可使用";
		case CouponType_ECard:
			return "刷e通卡享受优惠";
		default:
			break;
		}
		return "";
	}
	
	
	public static CouponType getCouponType(JSONObject json)
			throws JSONException {
		int nType = json.getInt("TYPE");
		return CouponType.values()[nType];
	}
	
	public static List<CouponInfo> parseArray(JSONArray json) throws JSONException{
		int count = json.length();
		List<CouponInfo> result = new ArrayList<CouponInfo>();
		for (int i = 0; i < count; i++) {
			JSONObject obj = json.getJSONObject(i);
			CouponInfo info = new CouponInfo();
			info.fromJson(obj);
			result.add(info);
		}
		return result;
	}

}
