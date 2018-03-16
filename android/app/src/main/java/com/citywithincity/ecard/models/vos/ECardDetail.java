package com.citywithincity.ecard.models.vos;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Databind
public class ECardDetail implements IJsonValueObject {

	@ViewId(id=R.id.ecard_last_date)
	public String lastUpdate;// 最后更新时间
	
	@ViewId(id=R.id.ecard_card_left)
	public String left;// 剩余
	
	
	public List<ECardHistroyInfo> histroy; // 历史记录列表
	
	@ViewId(id=R.id.ecard_info_title)
	public String getInfo(){
		return new StringBuilder("查询3个月内最多20条记录，目前")
				.append( histroy.size() ).append( "条" ).toString();
	}
	
	

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		left = String.format("%.2f", Double.valueOf(json.getString("left")));
		if (histroy != null) {
			//昨天晚上
			lastUpdate = new Date().toString();
		} else {
			histroy = parseECardHistory(json.getJSONArray("history"));
			if (histroy.size() > 0) {
				lastUpdate = histroy.get(0).hisTime;
			} else {
				lastUpdate = json.getString("time");
			}
		}
		lastUpdate = "截止: " + lastUpdate;
	}

	private List<ECardHistroyInfo> parseECardHistory(JSONArray json)
			throws JSONException {
		int count = json.length();
		List<ECardHistroyInfo> result = new ArrayList<ECardHistroyInfo>();

		for (int i = 0; i < count; i++) {
			JSONObject obj = json.getJSONObject(i);
			ECardHistroyInfo info = new ECardHistroyInfo();
			info.fromJson(obj);
			result.add(info);
		}
		return result;
	}
}
