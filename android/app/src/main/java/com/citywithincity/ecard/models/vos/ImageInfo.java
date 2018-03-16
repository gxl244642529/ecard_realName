package com.citywithincity.ecard.models.vos;

import com.citywithincity.ecard.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片信息
 * @author Randy
 *
 */
public class ImageInfo{
	public String big;//大图
	public String small;//小图
	
	

	public static List<ImageInfo> parseImages(JSONArray jimgs)
			throws JSONException {

		int count = jimgs.length();
		List<ImageInfo> shops = new ArrayList<ImageInfo>();
		for (int i = 0; i < count; i++) {
			JSONObject jimg = jimgs.getJSONObject(i);
			ImageInfo info = new ImageInfo();
			info.big = JsonUtil.getImageUrl(jimg.getString("BIG"));
			info.small = JsonUtil.getImageUrl(jimg.getString("SMALL"));
			shops.add(info);
		}
		return shops;

	}
}
