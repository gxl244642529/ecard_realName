package com.citywithincity.ecard.models.vos;

import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BusinessDetail implements IJsonValueObject {
	public ArrayList<ShopInfo> shops; // 联盟商家——店铺列表
	public List<ImageInfo> images; // 商家相册
	public List<CouponInfo> coupons; // 优惠劵
	public String detail; // 商家详情
	public boolean isCollected; // 登录以后可以收藏

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		// 扥点
		shops = JsonUtil.parseShops(json.getJSONArray("shops"));
		// 图片
		images = ImageInfo.parseImages(json.getJSONArray("images"));
		// CouponInfo
		coupons = CouponInfo.parseArray(json.getJSONArray("coupons"));
		detail = json.getString("DETAIL");
		isCollected = JsonUtil.getBooean(json, "COLLECTED");
		
//		{"LAST_MODIFIED":"1431069432",
//			"DETAIL":"     厦门灵玲国际马戏城，是全球最大的国际马戏旅游综合体，福建省重点文化产业，是以马戏为主题，融国际马戏演出、马戏博览，公司二期项目落成后将成为集观光、娱乐、教育于一体的一站式大型马戏乐园旅游度假区。",
//			"coupons":[],
//			"images":[{"SMALL":"\/uploads\/2015_04_30\/6887ea308f44eb1d658adc3087580020_thumb.jpg","BIG":"\/uploads\/2015_04_30\/6887ea308f44eb1d658adc3087580020.jpg"},
//			          {"SMALL":"\/uploads\/2015_04_30\/9a4c2c647490bca5fa9ff0b83eee341f_thumb.jpg","BIG":"\/uploads\/2015_04_30\/9a4c2c647490bca5fa9ff0b83eee341f.jpg"},
//			          {"SMALL":"\/uploads\/2015_04_30\/bde09a6d1a0011933c1ae221fadde4e1_thumb.jpg","BIG":"\/uploads\/2015_04_30\/bde09a6d1a0011933c1ae221fadde4e1.jpg"},
//			          {"SMALL":"\/uploads\/2015_04_30\/99fb22e1c7786ea5be6bc1c8c12b0337_thumb.jpg","BIG":"\/uploads\/2015_04_30\/99fb22e1c7786ea5be6bc1c8c12b0337.jpg"},
//			          {"SMALL":"\/uploads\/2015_04_30\/ac5e4ae78f7d9d1e54969c12da7d64d8_thumb.jpg","BIG":"\/uploads\/2015_04_30\/ac5e4ae78f7d9d1e54969c12da7d64d8.jpg"},
//			          {"SMALL":"\/uploads\/2015_04_30\/f38438903d78263e064aa79c173af5de_thumb.jpg","BIG":"\/uploads\/2015_04_30\/f38438903d78263e064aa79c173af5de.jpg"},
//			          {"SMALL":"\/uploads\/2015_04_30\/c23f517a23fee16f7e7e2b83e53b37ff_thumb.jpg","BIG":"\/uploads\/2015_04_30\/c23f517a23fee16f7e7e2b83e53b37ff.jpg"},
//			          {"SMALL":"\/uploads\/2015_04_30\/ab1c24459779c833a379a109bc2d1f90_thumb.jpg","BIG":"\/uploads\/2015_04_30\/ab1c24459779c833a379a109bc2d1f90.jpg"},
//			          {"SMALL":"\/uploads\/2015_04_30\/3597f7763dac0f75258f7898dcb7b799_thumb.jpg","BIG":"\/uploads\/2015_04_30\/3597f7763dac0f75258f7898dcb7b799.jpg"},
//			          {"SMALL":"\/uploads\/2015_04_30\/6db26cb03f99532560c0b47bec2afb28_thumb.jpg","BIG":"\/uploads\/2015_04_30\/6db26cb03f99532560c0b47bec2afb28.jpg"},
//			          {"SMALL":"\/uploads\/2015_04_30\/61b96a4c5b6519b52ad38046f923b751_thumb.jpg","BIG":"\/uploads\/2015_04_30\/61b96a4c5b6519b52ad38046f923b751.jpg"}],
//			          "COLLECTED":0,
//			          "shops":[
//			                   {"LNG":"118.07159792375",
//			                	   "PHONE":"05923753999",
//			                	   "LAT":"24.578088635878",
//			                	   "ADDRESS":"集美杏锦路366号",
//			"TITLE":"厦门灵玲国际马戏城"}]}
		
	}

}
