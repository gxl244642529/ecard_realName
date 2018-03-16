package com.citywithincity.ecard.models;

import android.content.Context;

import com.citywithincity.ecard.interfaces.IDoudouBus;
import com.citywithincity.ecard.models.ECardModel.RealBusData;
import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.models.MyTimer;
import com.citywithincity.models.MyTimer.ITimerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DoudouBusModel implements IDoudouBus, ITimerListener,
		IRequestResult<String> {

	private int id;

	private int lineId1;
	private int lineId2;
	private MyTimer myTimer;
	boolean findStart = false;
	boolean findEnd = false;
	private String start;
	private String end;
	private int startStationIndex;
	private int endStationIndex;
	private IDoudouBusData listener;
	List<LineData> ret;

	public static class LineData {

		public static final int TYPE_END = 2;
		public static final int TYPE_START = 1;
		public static final int TYPE_NONE = 0;

		public String name;
		public int comeCount;
		public int ariveCount;
		public int type;
	}

	public interface IDoudouBusData {
		void onReceiveLiveData(String time, List<LineData> list);

		void onReceiveData(RealBusData data);

		void onRefresStart();
	}

	public DoudouBusModel(Context context) {
		myTimer = new MyTimer(0, 30000);
		myTimer.setListener(this);

	}

	public void setListener(IDoudouBusData listener) {
		this.listener = listener;
	}

	public void getRealData(RealBusData data) {
		listener.onReceiveData(data);
		int line = data.line1;
		if (this.id != line) {
			this.ret = null;
		}
		this.lineId1 = line;
		this.id = line;
		/*
		List<Cookie> list = JsonTaskManager.getInstance().getCookieStore().getCookies();
		int count = 0;
		for (Cookie cookie : list) {
			if (cookie.getDomain().equals("m.doudou360.com")) {
				count++;
			}
		}

		if (count == 0) {
			requestCookie();
		} else {
			request();
		}*/
	}

	protected void requestCookie() {
	/*	Http.get(
				"http://m.doudou360.com/bus/Index.aspx?area=xiamen&partner=etongka.com",
				CachePolicy.CachePolity_NoCache,
				new IRequestResult<String>() {

					@Override
					public void onRequestError(String errMsg,
							boolean isNetworkError) {

					}

					@Override
					public void onRequestSuccess(String result) {
						request();
					}
				});*/
	}

	private String time;

	protected void request() {
	//	Http.get("http://m.doudou360.com/bus/i/live.ashx?lid=" + id, CachePolicy.CachePolity_NoCache,this);
	}

	private void notifySuccess() {
		if (onlyshowMyBus) {
			List<LineData> mineDatas = filterMine();
			listener.onReceiveLiveData(time, mineDatas);
		} else {
			listener.onReceiveLiveData(time, ret);
		}
	}

	private List<LineData> filterMine() {
		List<LineData> mine = new ArrayList<LineData>();
		int minIndex = 0;
		for (int i = startStationIndex; i >= 0; --i) {
			LineData data = ret.get(i);
			if (data.ariveCount > 0 || data.comeCount > 0) {
				minIndex = i;
				break;
			}
		}
		for (int i = minIndex, count = ret.size(); i < count; ++i) {
			LineData data = ret.get(i);
			mine.add(data);
			if (i == endStationIndex) {
				break;
			}
		}

		return mine;
	}

	private void onSearch(LineData lineData, int index) {
		if (!findStart) {
			if (lineData.name.indexOf(start) >= 0) {
				lineData.type = LineData.TYPE_START;
				findStart = true;
				startStationIndex = index;
			}
		}

		if (!findEnd) {
			if (lineData.name.indexOf(end) >= 0) {
				lineData.type = LineData.TYPE_END;
				findEnd = true;
				endStationIndex = index;
			}
		}
	}

	protected void research(List<LineData> result) {

		if (!findStart) {
			String s = start;
			while (s.length() > 2) {
				s = s.substring(0, s.length() - 1);
				if (searchStart(s, result)) {
					break;
				}
			}

		}

		if (!findEnd) {
			String s = end;
			while (s.length() > 2) {
				s = s.substring(0, s.length() - 1);
				if (searchEnd(s, result)) {
					break;
				}
			}
		}
	}

	private boolean searchStart(String start, List<LineData> result) {
		int index = 0;
		for (LineData lineData : result) {
			if (lineData.name.indexOf(start) >= 0) {
				lineData.type = LineData.TYPE_START;
				startStationIndex = index;
				return true;
			}
			index++;
		}
		return false;
	}

	private boolean searchEnd(String end, List<LineData> result) {
		int index = 0;
		for (LineData lineData : result) {
			if (lineData.name.indexOf(end) >= 0) {
				lineData.type = LineData.TYPE_END;
				endStationIndex = index;
				return true;
			}
			++index;
		}
		return false;
	}

	@Override
	public void refresh() {
		listener.onRefresStart();
		myTimer.stop();
		request();
	}

	@Override
	public void reserve() {
		if (id == lineId1) {
			id = lineId2;
		} else {
			id = lineId1;
		}
		this.ret = null;
		request();
	}

	@Override
	public void setRefreshInterval(int seconds) {

	}

	@Override
	public void stop() {
		myTimer.stop();
	}

	@Override
	public void onTimer(int id) {
		refresh();
	}

	public void setStartEnd(String start, String end) {
		this.start = start;
		this.end = end;
	}

	private boolean onlyshowMyBus;

	@Override
	public void setOnlyShowMyBus(boolean isChecked) {
		onlyshowMyBus = isChecked;
		notifySuccess();
	}

	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {

	}

	@Override
	public void onRequestSuccess(String str) {
		try {
			JSONObject json = new JSONObject(str);
			if (1 == json.getInt("success")) {
				JSONObject result = json.getJSONObject("result");
				JSONArray cur = result.getJSONObject("cur")
						.getJSONArray("time");
				time = cur.getString(0) + "-" + cur.getString(1);
				int otherid = result.getJSONObject("oppo").getInt("lid");

				findStart = false;
				findEnd = false;
				if (ret == null) {
					ret = new ArrayList<LineData>();
					JSONArray stations = result.getJSONArray("stas");
					for (int i = 0, count = stations.length(); i < count; ++i) {
						LineData lineData = new LineData();
						lineData.name = stations.getString(i);
						onSearch(lineData, i);
						ret.add(lineData);
					}
					research(ret);

					if (findStart && findEnd) {

						if (startStationIndex > endStationIndex) {
							// 倒着
							this.reserve();
							return;
						}

					}
				}

				JSONArray live = result.getJSONArray("live");
				for (int i = 0, count = live.length(); i < count; ++i) {
					JSONArray obj = live.getJSONArray(i);

					int index = obj.getInt(0) - 1;

					LineData lineData = ret.get(index);
					if (obj.getInt(1) == 0) {
						lineData.comeCount = obj.getInt(2);
					} else {
						lineData.ariveCount = obj.getInt(2);
					}
				}
				this.lineId2 = otherid;

				// 记录下来
				notifySuccess();

				myTimer.start();
			} else {

				// 解析
				requestCookie();

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
