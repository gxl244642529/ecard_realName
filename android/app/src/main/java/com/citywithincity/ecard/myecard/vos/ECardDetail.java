package com.citywithincity.ecard.myecard.vos;

import android.annotation.SuppressLint;

import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.utils.DateTimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ECardDetail implements IJsonValueObject {

	private String lastUpdate;// 最后更新时间

	private String left;// 剩余

	private List<ECardHistroyInfo> history; // 历史记录列表

	public String getInfo(){
		return new StringBuilder("查询3个月内最多20条记录，目前")
				.append( getHistory().size() ).append( "条" ).toString();
	}


	@SuppressLint("SimpleDateFormat")
	@Override
	public void fromJson(JSONObject json) throws JSONException {
		left = String.format("%.2f", Double.valueOf(json.getString("left")));
		history = (parseECardHistory(json.getJSONArray("history")));
		if(history!=null){
			if (history.size() > 0) {
				lastUpdate = getHistory().get(0).getTime();

			} else {
				lastUpdate = json.getString("time");
			}
		}else{
			lastUpdate = DateTimeUtil.getDateTime();
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
			String time = obj.getString("time");
			info.setTime(parseData(time));
			info.setLeft(obj.getDouble("left"));
			info.setShpName(obj.getString("shpName"));
			info.setType(obj.getInt("type"));
			info.setValue(obj.getDouble("value"));
			result.add(info);
		}
		return result;
	}

	private static final String parseData(String time){
		return new StringBuilder()
				.append(time.substring(0,4)).append('-')
				.append(time.substring(4, 6)).append('-')
				.append(time.substring(6, 8)).append(' ')
				.append(time.substring(8,10)).append(':')
				.append(time.substring(10, 12)).append(':')
				.append(time.substring(12,14)).toString();
	}

	public String getLastUpdate() {
		return lastUpdate;
	}


	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}


	public String getLeft() {
		return left;
	}


	public void setLeft(String left) {
		this.left = left;
	}


	public List<ECardHistroyInfo> getHistory() {
		return history;
	}


	public void setHistory(List<ECardHistroyInfo> history) {
		this.history = history;
	}


}
