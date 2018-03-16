package com.citywithincity.ecard.models.vos;

import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CouponDetail implements IJsonValueObject {
	public int id;//id
	public int bsid;//商家id
	public String tip;//优惠券使用提示
	public String stime;//开始时间
	public String etime;//结束时间
	//二维码
	public String code;
	public CouponType type;//优惠券类型
	public ArrayList<ShopInfo> shops;//支持分店		
	
	public String leftTime;//剩余时间
	public boolean isCollected;//是否收藏
	public int fetch;//抢票张数
	public int leftCount;//剩余张数
	public int total;//总张数,在限量的优惠券有效
	public int used;//二维码使用次数
	
	
	
	//图片
	public List<ImageInfo> images;



	@Override
	public void fromJson(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub

		if (json.has("detail")) {
			JSONObject couponJsonObject = json
					.getJSONObject("detail");
			id = couponJsonObject.getInt("ID");
			bsid = couponJsonObject.getInt("BSID");
			tip = couponJsonObject.getString("TIP");
			stime = couponJsonObject.getString("STIME");
			etime = couponJsonObject.getString("ETIME");
			type = CouponInfo.getCouponType(couponJsonObject);// .get("TYPE");
			code = couponJsonObject.getString("CODE");
			shops = JsonUtil.parseShops(json.getJSONArray("shops"));

			// 剩余天数
			leftTime = couponJsonObject.getString("LEFT");
			if (type == CouponType.CouponType_Limit) {
				// 限量版
				leftCount = couponJsonObject
						.getInt("LEFT_COUNT");
				total = couponJsonObject.getInt("TOTAL");
				fetch = couponJsonObject
						.getInt("COU_FETCH");
			}
		}

		if (json.has("images")) {
			images = ImageInfo.parseImages(json.getJSONArray("images"));
		}

		isCollected = true;
		JSONObject part = json.getJSONObject("part");
		used = part.getInt("USED");

	}

}
