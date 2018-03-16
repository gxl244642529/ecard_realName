package com.citywithincity.ecard.selling.models;

import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.interfaces.IJsonParser;
import com.citywithincity.interfaces.IJsonTaskListener;
import com.citywithincity.interfaces.IListRequsetResult;
import com.citywithincity.models.AbsDataProvider;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.widget.data.MenuData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShopMenuDataProvider extends AbsDataProvider<List<List<MenuData>>> implements IJsonParser<MenuData>, IListRequsetResult<List<MenuData>>, IJsonTaskListener<MenuData> {
	//low-height
	public static final String ORDER_PRICE_LH = "12";
	public static final String ORDER_PRICE_HL = "2";
	
	public static final String ORDER_UPDATE_LH = "11";
	public static final String ORDER_UPDATE_HL = "1";
	
	public static final String ORDER_SELL_LH = "13";
	public static final String ORDER_SELL_HL = "3";

	@Override
	public void load() {
		IArrayJsonTask<MenuData> task = JsonTaskManager
		.getInstance()
		.createArrayJsonTask("s_card_type2",CachePolicy.CachePolicy_TimeLimit,false);
		task.setParser(this).setListener(this).setOnParseListener(this)
		.execute();
	}

	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
		
	}

	@Override
	public void onRequestSuccess(List<MenuData> result, boolean isLastPage) {
		List<List<MenuData>> list = new ArrayList<List<MenuData>>();
		list.add(result);
		list.add(getOrderData(ORDER_UPDATE_LH));
		listener.onReceive(list);
	}

	@Override
	public MenuData parseResult(JSONObject json) throws JSONException {
		MenuData menuData = new MenuData();
		menuData.data = json.getInt("ID");
		menuData.label = json.getString("TITLE");
		return menuData;
	}
	
	@Override
	public Object beforeParseData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void afterParseData(List<MenuData> data, Object result) {
		MenuData menuData = new MenuData();
		menuData.data = -1;
		menuData.selected = true;
		menuData.label = "全部";
		data.add(0, menuData);
	}

	public static List<MenuData> getOrderData(String defaultOrder){
		List<MenuData> list = new ArrayList<MenuData>();
		//全城
		MenuData data = new MenuData();
		data.label = "价格从高到低";
		data.data = ORDER_PRICE_LH;
		list.add(data);
		
		
		data = new MenuData();
		data.label = "价格从低到高";
		data.data = ORDER_PRICE_HL;
		list.add(data);
		
		data = new MenuData();
		data.label = "更新日期从早到晚";
		data.data = ORDER_UPDATE_LH;
		list.add(data);
		
		data = new MenuData();
		data.label = "更新日期从晚到早";
		data.data = ORDER_UPDATE_HL;
		list.add(data);
		
		data = new MenuData();
		data.label = "按销量升序";
		data.data = ORDER_SELL_HL;
		list.add(data);
		
		data = new MenuData();
		data.label = "按销量降序";
		data.data = ORDER_SELL_LH;
		list.add(data);
		
//		data = new MenuData();
//		data.text = "推荐";
//		data.data = ORDER_SERVER;
//		list.add(data);
		
		for(int i=0, count = list.size(); i < count; ++i){
			data  = list.get(i);
			if(data.data.equals(defaultOrder)){
				data.selected = true;
				break;
			}
		}
		
		return list;
	}
	

}
