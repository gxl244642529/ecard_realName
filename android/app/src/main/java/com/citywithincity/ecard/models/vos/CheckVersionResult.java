package com.citywithincity.ecard.models.vos;

import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.models.vos.VersionInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckVersionResult implements IJsonValueObject{
	public int ret;
	public VersionInfo versionInfo;
	@Override
	public void fromJson(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		ret = json.getInt("ret");
		if (json.has("version")) {
			json = json.getJSONObject("version");

			VersionInfo info = new VersionInfo();
			info.versionName = json.getString("name");
			info.versionCode = json.getInt("code");
			String urlString = (json.getString("url"));

			if (!urlString.startsWith("http")) {
				if (urlString.startsWith("/")) {
					urlString = JsonUtil.getImageUrl(urlString);
				} else {
					urlString = JsonUtil.getImageUrl("/"
							+ urlString);
				}
			}
			info.url = urlString;
			info.info = json.getString("info");
			versionInfo = info;
		}
	}
}